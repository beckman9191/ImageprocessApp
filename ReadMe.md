# Image Process App

## Overview
The Image Process App is an Android application built using Kotlin and the Jetpack Compose UI toolkit. It demonstrates the use of native C++ code to apply a grayscale filter to images. This app is a practical example of integrating native code into an Android project to enhance performance for image processing tasks.

## Features
- **Load Images**: Users can load images from their device's storage.
- **Apply Grayscale Filter**: Converts the loaded color images to grayscale using a native C++ method called via JNI.

## Prerequisites
Before building the project, ensure you have the following installed:
- **Android Studio** (version Arctic Fox or newer recommended).
- **Android SDK** with API level 31 or higher.
- **Android NDK** for compiling the native C++ code.
- **CMake** to manage native code building.

## Build Instructions
1. Open the Project
- Launch Android Studio
- Choose "Open an Existing Project" and navigate to the directory where you cloned the project.
- Select the project directory and click "OK" to open the project in Android Studio.

2. Build and Sync the Project
- Android Studio should automatically prompt you to sync the project files with Gradle. If it doesn't, you can force a sync by clicking File > Sync Project with Gradle Files.

3. Run the App
- Connect an Android device via USB or set up an emulator.
- Click on the "Run" icon in the toolbar or press Shift + F10 to run the app.
- Select your device or emulator and click "OK" to deploy the app.
