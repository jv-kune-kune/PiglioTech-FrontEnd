# PiglioTech Android v1.0.1

## Overview
Maintenance update for the PiglioTech Android application with important fixes for network connectivity and crash reporting.

## Try It Online!
You can instantly try PiglioTech without installing anything using our online demo:
[Try PiglioTech on Appetize.io](https://appetize.io/app/b_2zhjpdksyzcdyqzdv5iuqvd3tq?device=pixel7&osVersion=13.0&toolbar=true)

This web-based emulator lets you experience the app directly in your browser on a virtual Pixel 7 device.

## Installation Options

### Quick Install
1. Download the APK file (`PiglioTech-1.0.1.apk`) from the release_package directory
2. Transfer it to your Android device
3. Open the file on your device to install (you may need to enable installation from unknown sources)
4. Find PiglioTech in your app drawer and enjoy!

For detailed installation instructions, see the `INSTALL.md` file.

## Key Features
- **User Authentication**: Secure login and sign-up system
- **Book Management**: Add books to your personal library with barcode scanning
- **Book Swapping**: Request and manage book swaps with other users
- **User Profiles**: View and manage user profiles and libraries
- **Smart Backend Detection**: Handles Render.com free tier cold starts gracefully
- **Crash Reporting**: Automatic crash reporting to help identify and fix issues

## Technical Highlights

### Backend Integration
- Robust backend status detection system
- Cold start screen with progress tracking
- Automatic retry mechanism with exponential backoff
- Network connectivity monitoring

### Code Quality
- SonarQube compliance 
- Reduced code duplication in image loading utilities
- Modern network connectivity APIs (NetworkCapabilities)
- Text blocks for improved string readability

### Performance Improvements
- Optimized image loading with Glide
- Improved error handling across the application
- Smart connectivity detection for offline scenarios

## Recent Changes
- Fix: Added custom DNS resolver to fix network issues on some Android devices
- Fix: Enhanced network security configuration for better compatibility
- Fix: Added ACCESS_NETWORK_STATE permission for improved network detection
- Add: Integrated Firebase Crashlytics for automatic crash reporting
- Fix: Replaced String concatenation with Text block
- Fix: Updated to modern NetworkCallback API from deprecated NetworkInfo
- Fix: Removed unused code and improved SonarQube score
- Update: Improved cold start screen messaging
- Refactor: Reduced code duplication in key utility classes
- Update: Minimum SDK set to Android 8.0 (API 26) for adaptive icons support

## Compatibility
- Requires Android 8.0 (API 26) or higher
- Designed for both phones and tablets
- Supports portrait and landscape orientations

## Installation
Download the APK from the assets section below and install it on your Android device.

## Contributors
- JacksonR64
- Tchabva
- Ge-Drei
- Jnc-Panda
- Kadri-K

## Getting Started
After installation, create an account or log in with existing credentials to start sharing books with the community!

## Crash Reporting and Monitoring
PiglioTech now includes Firebase Crashlytics for automatic crash reporting. This helps developers identify and fix issues more quickly.

### For Users
- No action required! If the app crashes, an anonymous report is automatically sent to the development team.
- This helps us improve the app's stability on all devices.
- No personal information is collected in crash reports.

### For Developers
- Crash reports are available in the Firebase Console under the Crashlytics section.
- You can log in with the project credentials to view detailed stack traces and device information.
- Use the `CrashReportingHelper` utility class to log additional information or non-fatal exceptions:
  ```java
  // For crashes or exceptions
  CrashReportingHelper.logException(exception, "Contextual message about what happened");
  
  // For non-fatal errors that should be tracked
  CrashReportingHelper.logError("Something went wrong when fetching books");
  
  // For tracking user-specific issues
  CrashReportingHelper.setUserIdentifier(userId, userEmail);
  ```
