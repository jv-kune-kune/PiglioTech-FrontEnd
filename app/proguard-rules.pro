# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep entry points and Fragment constructors
-keep public class * extends androidx.fragment.app.Fragment
-keep public class com.northcoders.pigliotech_frontend.PiglioTechApp
-keep public class * extends android.app.Activity
-keep public class com.northcoders.pigliotech_frontend.data.models.*
-keep public class * { public <init>(); }

# Keep test classes
#-keep class com.northcoders.pigliotech_frontend.ExampleInstrumentedTest
#-keep class com.northcoders.pigliotech_frontend.ExampleUnitTest

# Retrofit and other library rules
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }
-keepclassmembers class * { @retrofit2.http.* <methods>; }