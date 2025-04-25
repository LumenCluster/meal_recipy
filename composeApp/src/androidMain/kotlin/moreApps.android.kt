import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri

// androidMain
// androidMain
actual fun openMoreApps() {
    val context = AppContextHolder.context
    val developerPageUrl = "https://play.google.com/store/apps/dev?id=6484902670296746674"  // Your provided developer page link
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(developerPageUrl)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // Handle error if no browser is available
        val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(developerPageUrl))
        context.startActivity(fallbackIntent)
    }
}

