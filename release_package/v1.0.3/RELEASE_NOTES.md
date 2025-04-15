# PiglioTech Android v1.0.3

## Overview
This maintenance release focuses on improving application stability by fixing null reference vulnerabilities that were causing crashes in specific user scenarios. Version 1.0.3 builds upon the reliability improvements introduced in v1.0.2, with particular attention to region-related data handling.

## Try It Online!
You can instantly try PiglioTech without installing anything using our online demo:
[Try PiglioTech on Appetize.io](https://appetize.io/app/b_2zhjpdksyzcdyqzdv5iuqvd3tq?device=pixel7&osVersion=13.0&toolbar=true)

This web-based emulator lets you experience the app directly in your browser on a virtual Pixel 7 device.

## Installation Options

### Quick Install
1. Download the APK file (`PiglioTech-1.0.3.apk`) from the release_package directory
2. Transfer it to your Android device
3. Open the file on your device to install (you may need to enable installation from unknown sources)
4. Find PiglioTech in your app drawer and enjoy!

For detailed installation instructions, see the `INSTALL.md` file.

## What's New
- **Fixed region null reference crashes**: Resolved crashes occurring when user profiles contained null region data
- **Improved defensive programming**: Added comprehensive null checks throughout the application
- **Enhanced testing**: Added unit tests to verify null handling behavior

## Technical Highlights

### Bug Fixes
- **ProfileViewModel**: Added null check in `regionEnumToString()` method to safely handle null region values
- **HomeViewModel**: Implemented fallback mechanism for null DisplayName (region) values
- **SignUpViewModel**: Added null handling in `regionStringToEnum()` method

### Code Quality Improvements
- Added unit test coverage for null region handling
- Improved logging to track when fallback mechanisms are triggered
- Added PowerMock-based testing infrastructure for private methods

## Current Limitations
- Backend cold start still takes approximately 5 minutes on first launch after extended inactivity
- Some UI elements may render differently on various Android versions

## Contributors
- JacksonR64
- Tchabva
- Ge-Drei
- Jnc-Panda
- Kadri-K 