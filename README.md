#  Architecture Overview


## Project Structure

| Module | Purpose |
|--------|---------|
| **app** | The main application with a settings screen where users select their preferred MSP vendor. |
| **msp-api** | A shared interface that defines what any MSP integration must do (initialize, send events, handle push, etc.). |
| **msp-braze**, **msp-moEngage**, **msp-airShip**, etc. | Individual implementations for each vendor. Each wraps that vendor's SDK and conforms to the `msp-api` interface. |

## How It Works

1. **All vendors bundled** — Every MSP module is included in the app at build time. Nothing is initialized yet.

2. **User selects vendor** — On the settings page, the user picks which MSP they want to use (e.g., Braze, MoEngage, Airship).

3. **Lazy initialization** — Only when a vendor is selected does the app initialize that specific SDK. This avoids conflicts between vendors and keeps resource usage minimal until needed.

4. **Common interface** — The app interacts with the selected vendor through the `msp-api` interface. The app code doesn't need to know *which* vendor is active—it just calls the same methods regardless.

API Keys

[!NOTE]
API keys are stored in /msp-api/src/main/assets/secrets.properties and is ignored by version control.

Create a secrets.properties file with the following keys:
```
AIRSHIP_APP_KEY=
AIRSHIP_APP_SECRET=
APPSFLYER_API_KEY=
MOENGAGE_APP_KEY=
BRAZE_API_KEY=
BRAZE_CUSTOM_END_POINT=
FIRE_BASE_SENDER_ID=
XTREMEPUSH_APP_KEY=
XTREMEPUSH_SENDER_ID=
XTREMEPUSH_PUBLIC_KEY=
```
## Typical Flow

```
App Launch → Settings Screen → User selects "Braze" → Braze SDK initializes 
```


## Adding Firebase Config
The google-services.json file is ignored by version control. It can can be downloaded from the Firebase Console
Once downloaded , Place it in the app module root directory- See illustration below
```
ShoppingCart/
├── app/
│   ├── google-services.json  ← The firebase file 
│   ├── src/
│   └── build.gradle.kts
├── msp-api/
├── msp-braze/
└── ...
```

## Building the App

```bash
./gradlew :app:assembleDebug
```
This produces an APK in `app/build/outputs/apk/debug/`

To generate a signed apk/app bundle that you can distribute to users  on Android Studio select:
Build-> generate signed App bundle or APK
