package com.movableink.app.messaging

import com.moengage.core.DataCenter
import org.junit.Assert.assertEquals
import org.junit.Test

class MoEngageAccountTest {

    @Test
    fun `partner sandbox has correct app id and data center`() {
        assertEquals("CF6VET3G5MRFCA7CML6D37ND", MoEngageAccount.PARTNER_SANDBOX.appId)
        assertEquals(DataCenter.DATA_CENTER_1, MoEngageAccount.PARTNER_SANDBOX.dataCenter)
    }

    @Test
    fun `demo ecommerce has correct app id and data center`() {
        assertEquals("TAQGW6TG2CFSQMH5P0NHXBIH", MoEngageAccount.DEMO_ECOMMERCE.appId)
        assertEquals(DataCenter.DATA_CENTER_4, MoEngageAccount.DEMO_ECOMMERCE.dataCenter)
    }

    @Test
    fun `default account is partner sandbox`() {
        assertEquals(MoEngageAccount.PARTNER_SANDBOX, MoEngageAccount.DEFAULT)
    }

    @Test
    fun `fromName falls back to default for unknown name`() {
        assertEquals(MoEngageAccount.DEFAULT, MoEngageAccount.fromName("nope"))
        assertEquals(MoEngageAccount.DEFAULT, MoEngageAccount.fromName(null))
        assertEquals(MoEngageAccount.DEMO_ECOMMERCE, MoEngageAccount.fromName("DEMO_ECOMMERCE"))
    }
}
