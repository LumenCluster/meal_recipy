// androidMain/src/your_package/PlatformExit.kt

import android.app.Activity

private var currentActivity: Activity? = null

fun setCurrentActivity(activity: Activity) {
    currentActivity = activity
}

actual fun exitApp() {
    currentActivity?.finish()
}
