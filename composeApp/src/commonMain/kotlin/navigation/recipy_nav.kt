import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import screen.DetailScreen
import screen.ViewRecipy

enum class Screens {
    Recipy, Detail
}

@Composable
fun RecipyNav(
    onScreenChanged: (String) -> Unit,
    onBackToHome: () -> Unit
) {
    MaterialTheme {
        Column(Modifier.fillMaxWidth()) {
            var currentScreen by remember { mutableStateOf(Screens.Recipy) }
            var selectedId by remember { mutableStateOf("") }
            var showExitDialog by remember { mutableStateOf(false) }

            // Notify screen change
            LaunchedEffect(currentScreen) {
                onScreenChanged(currentScreen.name)
            }

            registerBackHandler {
                when (currentScreen) {
                    Screens.Detail -> {
                        currentScreen = Screens.Recipy
                        selectedId = ""                    }

                    Screens.Recipy -> {
                        // Simulate popBackStack or go to home
                        showExitDialog = true // Show the exit confirmation dialog

                    }
                }
            }

            if (showExitDialog) {
                ExitDialog(
                    onConfirm = {
                        exitApp()
                        showExitDialog = false
                    },
                    onDismiss = {
                        showExitDialog = false
                    }
                )
            }

            when (currentScreen) {
                Screens.Recipy -> {
                    ViewRecipy(
                        onMealClick = {
                            currentScreen = Screens.Detail
                            selectedId = it.idMeal
                        },
                        onBackClick = {
                            onBackToHome()
                        }
                    )
                }

                Screens.Detail -> {
                    DetailScreen(
                        id = selectedId,
                        navigateBack = {
                            currentScreen = Screens.Recipy
                            selectedId = ""
                        }
                    )
                }
            }
        }
    }
}
