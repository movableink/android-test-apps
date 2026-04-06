# android-test-apps

## Setup

### Prerequisites
- Android Studio
- JDK 17+
- Firebase CLI (`brew install firebase-cli`)

### local.properties

This file is git-ignored and must be created manually at the project root. It holds all secrets and local paths:

```properties
sdk.dir=/path/to/your/Android/sdk

# Movable Ink
MOVABLE_INK_SDK_API_KEY=your_value

# Salesforce Marketing Cloud
MC_ACCESS_TOKEN=your_value
MC_APP_ID=your_value
FCM_SENDER_ID=your_value
MARKETING_CLOUD_URL=your_value

# Release signing
STORE_FILE=movableink-release.jks
STORE_PASSWORD=your_password
KEY_ALIAS=movableink
KEY_PASSWORD=your_password
```

---

## Firebase App Distribution Release

### First-time setup

**1. Generate a keystore** (only needed once):
```bash
keytool -genkeypair -v \
  -keystore app/movableink-release.jks \
  -alias movableink \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -storepass YOUR_PASSWORD \
  -keypass YOUR_PASSWORD \
  -dname "CN=Movable Ink, OU=Engineering, O=Movable Ink, L=New York, ST=NY, C=US"
```

Add the credentials to `local.properties` as shown above. Never commit the `.jks` file or `local.properties`.

**2. Get a Firebase CI token** (only needed once):
```bash
firebase login:ci
```

Sign in with your Google account in the browser. Copy the token printed in the terminal and export it:
```bash
export FIREBASE_TOKEN=your_token_here
```

You can add this to your shell profile (`~/.zshrc` or `~/.bashrc`) to avoid setting it each time.

---

### Releasing a build

**1. Update the release notes** at `app/release-notes.txt` with what's changed.

**2. Update `versionCode`** in `gradle.properties` (increment by 1 each release):
```properties
versionCode=5
```

**3. Build and upload** from the project root:
```bash
./gradlew assembleRelease appDistributionUploadRelease
```

The APK will be built, signed, and uploaded to Firebase App Distribution automatically. Testers with access via the distribution link will be notified.
