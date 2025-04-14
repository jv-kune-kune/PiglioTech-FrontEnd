# PiglioTech Front-End

## Try the App Online!

Try PiglioTech instantly without installation:
[**Try PiglioTech on Appetize.io**](https://appetize.io/app/b_2zhjpdksyzcdyqzdv5iuqvd3tq?device=pixel7&osVersion=13.0&toolbar=true)

This web-based emulator lets you experience the app directly in your browser on a virtual Pixel 7 device.

## Overview
The front-end is an Android application that interacts with the [back-end API](https://github.com/jv-kune-kune/PiglioTech-BackEnd) to provide users with a mobile-friendly interface for the app's features.

---

## Features
- Intuitive user interface for interacting with the API.
- Integration with back-end endpoints for real-time data.
- Support for multiple screen sizes and device configurations.
- Smart backend status detection for handling cold starts.
- Book scanning via device camera.
- User authentication and profiles.

---

## Tech Stack
- **Language**: Java
- **Build Tool**: Gradle
- **Libraries**:
  - Retrofit: For HTTP requests.
  - Firebase Auth: For user authentication.
  - Google ML Kit: For barcode scanning.
  - Glide: For image loading and caching.
  - Material Design Components: For UI.

---

## Installation Options

### Quick Install (APK)
1. Download the latest APK file from our [GitHub Releases](https://github.com/jv-kune-kune/PiglioTech-FrontEnd/releases)
2. Transfer it to your Android device
3. On your device, tap the APK file to install (you may need to enable "Install from Unknown Sources" in your device settings)
4. Open the app from your app drawer

### Advanced Install (Source Code)
1. Clone the repository:
   ```bash
   git clone https://github.com/jv-kune-kune/PiglioTech-FrontEnd
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Run the app on an emulator or a physical device.

---

## Important Notes about Backend Cold Starts

PiglioTech uses a backend service hosted on Render.com's free tier, which has the following behaviors:

- The backend server goes to sleep after 15 minutes of inactivity
- Cold starts typically take 3-5 minutes (max 10 minutes in rare cases)
- The app has a built-in "Backend Starting Up" screen that automatically:
  - Detects when the backend is offline
  - Shows a countdown timer
  - Attempts reconnection at regular intervals
  - Proceeds automatically once the backend is available

This cold start behavior is normal and only happens when the app hasn't been used for a while. Once the backend is running, it stays active for all users until the idle timeout.

---

## Contribution
1. Fork the repository and create your feature branch:
   ```bash
   git checkout -b feature/YourFeatureName
   ```
2. Commit your changes:
   ```bash
   git commit -m 'Add some feature'
   ```
3. Push to the branch:
   ```bash
   git push origin feature/YourFeatureName
   ```
4. Create a pull request.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

## Acknowledgements
- **Team Members**:
  - [JacksonR64](https://github.com/JacksonR64)
  - [Tchabva](https://github.com/tchabva)
  - [Ge-Drei](https://github.com/ge-drei)
  - [Jnc-Panda](https://github.com/jnc-panda)
  - [Kadri-K](https://github.com/orgs/jv-kune-kune/people/kadri-k)

- **Tools and Libraries**:
  - Retrofit.
  - Google Services Plugin.
  - Glide.
  - Material Design Components.

---

## Contact
For queries or support, contact jvkunekune@gmail.com.
