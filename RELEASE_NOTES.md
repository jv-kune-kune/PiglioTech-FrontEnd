# PiglioTech Android v1.0.0

## Overview
Initial release of the PiglioTech Android application - a mobile interface for the book sharing platform that connects users through their love of reading.

## Key Features
- **User Authentication**: Secure login and sign-up system
- **Book Management**: Add books to your personal library with barcode scanning
- **Book Swapping**: Request and manage book swaps with other users
- **User Profiles**: View and manage user profiles and libraries
- **Smart Backend Detection**: Handles Render.com free tier cold starts gracefully

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
