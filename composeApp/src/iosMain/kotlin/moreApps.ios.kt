// iosMain
actual fun openMoreApps() {
    val developerPageUrl = "https://play.google.com/store/apps/dev?id=6484902670296746674"  // Your provided developer page link
    val url = NSURL(string = developerPageUrl)
    val options: Map<String, Any> = mapOf("UIApplicationOpenURLOptionsSourceApplicationKey" to "com.apple.mobilesafari")
    if (UIApplication.sharedApplication.canOpenURL(url!!)) {
        UIApplication.sharedApplication.openURL(url, options = options)
    }
}
