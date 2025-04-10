import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import screen.DetailScreen
import screen.ViewRecipy

enum class Screens{
    Recipy, Detail
}

@Composable
fun RecipyNav(onScreenChanged: (String) -> Unit,
              onBackToHome: () -> Unit
) {
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            var currentScreen by remember { mutableStateOf(Screens.Recipy) }
            var selectedId by remember { mutableStateOf("") }
            val navController = rememberNavController()
            var showExitDialog by remember { mutableStateOf(false) }

            // Notify MainScreen about screen changes
            LaunchedEffect(currentScreen) {
                onScreenChanged(currentScreen.name)
            }
            // Register back handler for handling back presses on main screen (Recipy)
            registerBackHandler {
                onBackToHome()
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
                        onBackClick = {             onBackToHome()
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
