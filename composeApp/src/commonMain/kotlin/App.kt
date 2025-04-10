import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import database.ProfileDao
import database.dao.MealPlanDao
import kotlinx.coroutines.launch
import org.example.compose.home.HomeViewModel
import org.example.compose.home.ProfileViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import repository.MealPlanRepository
import repository.ProfileRepository
import screen.*
import ui.home.MealPlanViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.Icon
import org.jetbrains.compose.resources.painterResource

import registerBackHandler // expect function
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.exit
import room_cmp.composeapp.generated.resources.onback

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(
    mealDao: MealPlanDao,
    profileDao: ProfileDao
) {
    val mealRepository = remember { MealPlanRepository(mealDao) }
    val profileRepository = remember { ProfileRepository(profileDao) }

    val profileViewModel = remember { ProfileViewModel(profileRepository) }
    val mealViewModel = remember { MealPlanViewModel(mealRepository) }
    val homeViewModel: HomeViewModel = viewModel()

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    var isDatabaseInitialized by remember { mutableStateOf<Boolean?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }
    var shouldExit by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Register back handler
    registerBackHandler {
        if (currentRoute == "main") {
            showExitDialog = true
        } else {
            navController.popBackStack()
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
    // DB check
    LaunchedEffect(Unit) {
        scope.launch {
            val profileCount = profileViewModel.getProfileCount()
            isDatabaseInitialized = profileCount > 0
        }
    }

    MaterialTheme {
        NavHost(
            navController,
            startDestination = if (isDatabaseInitialized == true) "main" else "splash"
        ) {
            composable("splash") {
                SplashScreen {
                    navController.navigate(if (isDatabaseInitialized == true) "main" else "onboarding1") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }

            composable("onboarding1") {
                OnboardingScreen(
                    onNext = {
                        navController.navigate("signup") {
                            popUpTo("onboarding1") { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }

            composable("signup") {
                SignupScreen(navController, profileViewModel) { name, age ->
                    navController.navigate("profileSelectionScreen/$name/$age") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            }

            composable(
                route = "profileSelectionScreen/{name}/{age}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("age") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
                val age = backStackEntry.arguments?.getString("age") ?: "0"

                ProfileSelectionScreen(profileViewModel, name, age) { name, age, selectedAvatar ->
                    navController.navigate("ProfileSelectionScreen2/$name/$age/$selectedAvatar") {
                        popUpTo("profileSelectionScreen") { inclusive = true }
                    }
                }
            }

            composable(
                route = "ProfileSelectionScreen2/{name}/{age}/{selectedAvatar}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("age") { type = NavType.StringType },
                    navArgument("selectedAvatar") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
                val ageString = backStackEntry.arguments?.getString("age") ?: "0"
                val selectedAvatar = backStackEntry.arguments?.getString("selectedAvatar") ?: ""

                ProfileSetupScreen(
                    name = name,
                    age = ageString,
                    selectedAvatar = selectedAvatar,
                    onNext = { foodImg ->
                        navController.navigate("FoodPreferencesScreen/$name/$ageString/$selectedAvatar/$foodImg") {
                            popUpTo("ProfileSelectionScreen2") { inclusive = true }
                        }
                    }
                )
            }

            composable("FoodPreferencesScreen/{name}/{age}/{selectedAvatar}/{foodImg}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
                val ageString = backStackEntry.arguments?.getString("age") ?: "0"
                val selectedAvatar = backStackEntry.arguments?.getString("selectedAvatar") ?: ""
                val foodImg = backStackEntry.arguments?.getString("foodImg") ?: ""
                val age = ageString.toIntOrNull() ?: 0

                FoodPreferencesScreen(
                    name = name,
                    age = age.toString(),
                    selectedAvatar = selectedAvatar,
                    foodImg = foodImg,
                    viewModel = profileViewModel,
                    onNext = {
                        navController.navigate("congratulationScreen") {
                            popUpTo("FoodPreferencesScreen") { inclusive = true }
                        }
                    }
                )
            }

            composable("congratulationScreen") {
                CongratulationsScreen {
                    while (navController.popBackStack()) { /* pop all */ }
                    navController.navigate("main") {
                        launchSingleTop = true
                    }
                }
            }

            composable("main") {
                MainScreen(homeViewModel, profileViewModel, mealViewModel)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(24.dp)
                .width(280.dp), // adjust width if needed
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))


                Icon(
                    painter = painterResource(Res.drawable.exit),
                    contentDescription = "Exit",
                    tint = Color(0xFF00796B), // teal color
                    modifier = Modifier
                        .size(68.dp)
                        .padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))

                // Title or Message
                Text(
                    text = "Are You Sure You Want To Exit?",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))


                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF007370)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(40.dp)
                        .width(160.dp)
                ) {
                    Text("EXIT APP", color = Color.White)
                }

                // Cancel Button
                TextButton(onClick = onDismiss) {
                    Text(
                        "CANCEL",
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}


