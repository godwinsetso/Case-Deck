# CASE DECK 1.0 · ANDROID APP AND IPHONE/IPAD WEB APP

Created by Godwin Setsoafia for the e-Crime Bureau Financial Crimes and Investigations practice.

This project builds two things from one folder:

- **app/** · the Android app (phone and tablet). It runs Case Deck from inside the app, fully offline.
- **web/** · the installable web app for iPhone and iPad (also works on Android and desktop browsers).

## SECURITY DESIGN

- No permissions requested: no internet, storage, camera, contacts or location.
- The app loads only its own bundled files through a secure virtual address (https://appassets.androidplatform.net). Links to any other site are blocked.
- File and content access from the web view are switched off. Mixed content is blocked and Safe Browsing is on.
- Screenshots and screen recording are blocked (FLAG_SECURE). To allow them, set `SECURE_SCREEN = false` in `MainActivity.java`.
- App data is excluded from cloud backup and device transfer.
- Release builds are shrunk and signed with APK Signature Scheme v1, v2 and v3, targeting Android 15 (API 35).
- Inside the app, every copy is locked until a valid access key is entered (issued with the Key Studio).

Android always asks permission before installing an app from outside the Play Store, and Google Play Protect may offer to scan it. With no permissions and no network access, Case Deck should pass that scan.

## OPTION A · BUILD THE APK ON GITHUB (NO SOFTWARE TO INSTALL)

1. Create a **private** repository on GitHub and upload the contents of this folder (keep the `.github` folder).
2. In the repository, open **Settings > Secrets and variables > Actions** and add four secrets from your Owner Kit:
   - `CASEDECK_KEYSTORE_BASE64`: the contents of `casedeck-release.jks.base64.txt`
   - `CASEDECK_KEYSTORE_PASSWORD`: the password in `keystore.properties`
   - `CASEDECK_KEY_ALIAS`: `casedeck`
   - `CASEDECK_KEY_PASSWORD`: the same password
3. Open the **Actions** tab and run **Build Case Deck** (it also runs on every push to `main`).
4. When it finishes, download **CaseDeck-1.0-apk** from the run's Artifacts. Unzip it to get the APK.
5. Copy the APK to the tablet or phone, open it and allow installation from that source when asked.

The same workflow publishes the iPhone/iPad web app with GitHub Pages. Turn Pages on under **Settings > Pages** with the source set to **GitHub Actions**. GitHub Pages sites are public even when the repository is private, but Case Deck stays locked without a key.

## OPTION B · BUILD WITH ANDROID STUDIO

1. Install Android Studio (Koala or newer) and open this folder.
2. Copy `keystore.properties` and `casedeck-release.jks` from your Owner Kit into this folder (next to `settings.gradle.kts`). They are already set up to work from there and are excluded from Git.
3. Choose **Build > Generate Signed App Bundle / APK > APK > release**, or run `gradle assembleRelease`.
4. The APK is in `app/build/outputs/apk/release/`.

Keep the same keystore for every future version. Android only installs an update over an existing app when it is signed with the same key, and that is also what keeps each analyst's progress.

## IPHONE AND IPAD

iOS cannot run an HTML file opened from Files or Mail, which is why it failed before. Use the web app instead:

1. Host the `web` folder over HTTPS: GitHub Pages (above), or drag the folder onto Netlify Drop, or any HTTPS web server.
2. On the iPhone or iPad, open the link in **Safari**.
3. Tap **Share**, then **Add to Home Screen**. Case Deck opens full screen like an app and works offline after the first visit.

## SHIPPING A NEW VERSION

- Content updates (new cases, modules, questions): sign a `.cdupdate` file in the Key Studio. Analysts install it from **About > Install update**. No reinstall needed.
- New app builds: replace `app/src/main/assets/index.html` and `web/index.html` with the new build, raise `versionCode` and `versionName` in `app/build.gradle.kts`, change `VERSION` in `web/sw.js`, and rebuild.
