# Case Deck has no reflection-based code. Keep the activity.
-keep class com.godwinsetsoafia.casedeck.MainActivity { *; }
-keep class com.godwinsetsoafia.casedeck.MainActivity$TtsBridge { *; }
-keepclassmembers class * { @android.webkit.JavascriptInterface <methods>; }
