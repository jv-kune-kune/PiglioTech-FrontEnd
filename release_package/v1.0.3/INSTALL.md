# PiglioTech Android v1.0.3 - Installation Guide

## System Requirements
- Android device running Android 8.0 (Oreo) or higher
- Minimum 50MB free storage space
- Working internet connection

## Installation Methods

### Method 1: Direct APK Installation
1. Download the APK file (`PiglioTech-1.0.3.apk`) from the release_package directory
2. Transfer it to your Android device via USB, email, or cloud storage
3. Navigate to the file on your device and tap to open
4. If prompted about security settings:
   - Tap "Settings"
   - Enable "Install from this source" or "Unknown sources"
   - Return to the installation and proceed
5. Tap "Install" when prompted
6. Once installation completes, tap "Open" to launch PiglioTech

### Method 2: Using ADB (for developers)
1. Enable Developer Options on your Android device
   - Go to Settings > About phone
   - Tap "Build number" 7 times until you see "You are now a developer"
2. Enable USB Debugging in Developer Options
3. Connect your device to your computer and approve the debugging prompt
4. Open a terminal and run:
   ```
   adb install -r path/to/PiglioTech-1.0.3.apk
   ```
5. The app will be installed automatically on your device

## Troubleshooting

### Installation Blocked
If your device prevents installation:
1. Go to Settings > Security
2. Enable "Unknown sources" or "Install unknown apps"
3. Try the installation again

### App Won't Open
If the app installs but won't open:
1. Ensure your device meets the minimum requirements
2. Check internet connectivity
3. Restart your device and try again

### Backend Connectivity
On first launch, the application may need up to 5 minutes to connect to the backend if it has been inactive. This is normal behavior and only happens on the first connection after a long period of inactivity.

## Uninstallation
To remove PiglioTech from your device:
1. Long-press the app icon
2. Select "Uninstall" or drag to the "Uninstall" option
3. Confirm the uninstallation when prompted

## Support
For any installation issues, please open an issue on the GitHub repository with details about your device model, Android version, and the specific error encountered. 