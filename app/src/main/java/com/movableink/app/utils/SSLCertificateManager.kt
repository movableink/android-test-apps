package com.movableink.app.utils

import android.content.Context
import android.util.Log
import com.movableink.app.R
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class SSLCertificateManager {
    fun setupCustomSSLContext(context: Context) {
        try {
            // Load certificate from raw resources
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val inputStream = context.resources.openRawResource(R.raw.fanatics)
            val certificate = certificateFactory.generateCertificate(inputStream) as X509Certificate
            inputStream.close()

            Log.d("SSL_CERT", "Certificate loaded: ${certificate.subjectDN}")
            Log.d("SSL_CERT", "Certificate issuer: ${certificate.issuerDN}")
            Log.d("SSL_CERT", "Certificate valid until: ${certificate.notAfter}")

            // Create KeyStore with the custom certificate
            val customKeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            customKeyStore.load(null, null)
            customKeyStore.setCertificateEntry("xtremepush", certificate)

            // Create TrustManager for custom certificate
            val customTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            customTrustManagerFactory.init(customKeyStore)
            val customTrustManagers = customTrustManagerFactory.trustManagers

            // Get default system TrustManager
            val defaultTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            defaultTrustManagerFactory.init(null as KeyStore?) // null loads default system certificates
            val defaultTrustManagers = defaultTrustManagerFactory.trustManagers

            // Create composite trust manager
            val compositeTrustManager =
                CompositeTrustManager(
                    customTrustManagers[0] as X509TrustManager,
                    defaultTrustManagers[0] as X509TrustManager,
                )

            // Create SSL context with composite trust manager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(compositeTrustManager), null)

            // Set as default
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true } // Be careful with this in production

            Log.d("SSL_CERT", "Custom SSL context with system defaults configured successfully")
        } catch (e: Exception) {
            Log.e("SSL_CERT", "Failed to setup custom SSL context", e)
            throw e // Re-throw to handle in calling code
        }
    }

    /**
     * Composite TrustManager that tries custom certificate first, then falls back to system defaults
     */
    private class CompositeTrustManager(
        private val customTrustManager: X509TrustManager,
        private val defaultTrustManager: X509TrustManager,
    ) : X509TrustManager {
        override fun checkClientTrusted(
            chain: Array<X509Certificate>,
            authType: String,
        ) {
            try {
                customTrustManager.checkClientTrusted(chain, authType)
            } catch (e: CertificateException) {
                defaultTrustManager.checkClientTrusted(chain, authType)
            }
        }

        override fun checkServerTrusted(
            chain: Array<X509Certificate>,
            authType: String,
        ) {
            try {
                customTrustManager.checkServerTrusted(chain, authType)
                Log.d("SSL_CERT", "Certificate validated using custom trust manager")
            } catch (e: CertificateException) {
                try {
                    defaultTrustManager.checkServerTrusted(chain, authType)
                    Log.d("SSL_CERT", "Certificate validated using default system trust manager")
                } catch (defaultException: CertificateException) {
                    Log.e("SSL_CERT", "Certificate validation failed in both custom and default trust managers")
                    throw defaultException
                }
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = customTrustManager.acceptedIssuers + defaultTrustManager.acceptedIssuers
    }
}
