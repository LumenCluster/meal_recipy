package navigation

import FoodPreferencesScreen
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import screen.AddMealScreen
import screen.CongratulationsScreen
import screen.ProfileSelectionScreen
import screen.ProfileSetupScreen
import screen.SignupScreen
import screen.updateMealScreen
import ui.home.HomeScreen
import ui.home.MealPlanViewModel
import screen.ProfileScreen
import viewModel.ProfileViewModelFactory
import org.example.compose.home.ProfileViewModel
import org.example.compose.home.HomeViewModel
import screen.MainScreen


@Composable
fun ProfileScreenRoute(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
//    mealViewModel: MealPlanViewModel = viewModel(factory = MealPlanViewModelFactory(Graph.repository)),
//    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(Graph.repo)),
) {
    val mealViewModel: MealPlanViewModel = remember { MealPlanViewModel(Graph.repository) }
    val profileViewModel = remember { ProfileViewModel(Graph.repo) }

    NavHost(navController = navController, startDestination = "profile_screen") {
        composable("profile_screen") {
            ProfileScreen(navController = navController, profileViewModel = profileViewModel, onBackPress = { navController.popBackStack() } // Handle back press
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
                },
            )
        }

        composable("FoodPreferencesScreen/{name}/{age}/{selectedAvatar}/{foodImg}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: "Unknown"
            val ageString = backStackEntry.arguments?.getString("age") ?: "0"
            val selectedAvatar = backStackEntry.arguments?.getString("selectedAvatar") ?: ""
            val foodImg = backStackEntry.arguments?.getString("foodImg") ?: ""

            // Safe parsing of age
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
                navController.navigate("main") {
                    popUpTo("congratulationScreen") { inclusive = true }
                }
            }
        }
        composable("main") {
            MainScreen(homeViewModel, profileViewModel,mealViewModel)
        }

        composable("new") {
            LaunchedEffect(Unit) {
            }
            HomeScreen(
                viewModel = mealViewModel,
                onNavigateToAddMeal = { category, selectedDay ->
                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
                },
                onNavigateToCategoryScreen = { selectedDay ->
                    navController.navigate("category/${selectedDay.toString()}")
                },
                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                }
            )
        }

        composable(
            route = "addMeal/{category}/{date}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val category = backStackEntry.arguments?.getString("category")!!
            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()
            AddMealScreen(
                viewModel = mealViewModel,
                category = category,
                date = date,
                onMealSaved = {
                    navController.popBackStack()
                },
                onBackPress = { navController.popBackStack() } // Handle back navigation

            )
        }

        composable(
            route = "updateMeal/{category}/{date}/{mealId}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("mealId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val category = backStackEntry.arguments?.getString("category") ?: ""
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date") ?: "")
            val mealId = backStackEntry.arguments?.getInt("mealId") ?: 0

            updateMealScreen(
                viewModel = mealViewModel,
                category = category,
                date = date,
                mealId = mealId,
                onBackPress = { navController.popBackStack() }, // Handle back navigation
                onMealUpdated = { navController.popBackStack() }
            )
        }

    }


}