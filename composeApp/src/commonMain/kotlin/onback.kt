import androidx.compose.runtime.Composable


@Composable

expect fun registerBackHandler(onBack: @Composable () -> Unit)
