# PiglioTech CI/CD Workflows

This directory contains GitHub Actions workflows for automating the CI/CD process for the PiglioTech Android application.

## Workflows

### 1. Publish Package to GitHub Packages (`publish-package.yml`)

This workflow automatically publishes a new version of the app to GitHub Packages when a new version tag is pushed.

**Trigger:**
- Push to a tag matching the pattern `v*.*.*` (e.g., v1.0.2)
- Manual trigger via the GitHub Actions interface

**What it does:**
- Sets up JDK 17
- Builds the app
- Generates the release bundle (AAB)
- Publishes to GitHub Packages

### 2. Create GitHub Release (`create-release.yml`)

This workflow automatically creates a GitHub release when a new version tag is pushed.

**Trigger:**
- Push to a tag matching the pattern `v*.*.*` (e.g., v1.0.2)

**What it does:**
- Sets up JDK 17
- Builds the app
- Copies the APK to the release directory and generates checksums
- Checks if release notes exist (creates a template if they don't)
- Creates a GitHub release with the APK and checksums attached

## How to Use

### Creating a New Release

To create a new release and update the GitHub Package:

1. Update the version in `app/build.gradle`:
   ```gradle
   versionCode 4  // Increment this
   versionName "1.0.3"  // Update this
   ```

2. Create release notes in `release_package/v1.0.3/RELEASE_NOTES.md`

3. Commit your changes:
   ```bash
   git add app/build.gradle release_package/v1.0.3/RELEASE_NOTES.md
   git commit -m "chore: prepare v1.0.3 release"
   ```

4. Create and push a tag:
   ```bash
   git tag v1.0.3
   git push origin v1.0.3
   ```

5. The workflows will automatically:
   - Build the app
   - Create a GitHub release
   - Publish to GitHub Packages

### Manual Trigger

You can also manually trigger the "Publish Package to GitHub Packages" workflow from the GitHub Actions tab. 