package navigation

import ExitDialog
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import exitApp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import registerBackHandler
import screen.AddMealScreen
import screen.CategoryScreen
import screen.updateMealScreen
import ui.home.HomeScreen
import ui.home.MealPlanViewModel

@Composable
fun HomeNavigation(
    viewModel: MealPlanViewModel,
    onScreenChanged: (String) -> Unit,
    onBackToHome: () -> Unit

) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryFlow.collectAsState(null)
    val currentRoute = currentBackStackEntry.value?.destination?.route
    var showExitDialog by remember { mutableStateOf(false) }

    registerBackHandler {
        if (currentRoute == "home") {
            // We're on HomeScreen -> show exit dialog
            showExitDialog = true // Show the exit confirmation dialog

        } else {
            // On any other screen -> just pop the back stack
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
    NavHost(navController = navController, startDestination = "home") {
        // Home Screen
        composable("home") {
            onScreenChanged("HomeScreen") // ✅ Notify MainScreen

            HomeScreen(
                viewModel = viewModel,
                onNavigateToAddMeal = { category, selectedDay ->
                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
                },
                onNavigateToCategoryScreen = { selectedDay ->
                    navController.navigate("category/${selectedDay.toString()}")
                },
                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                },
                onBackClick = {
                    onBackToHome()
                } // Handle back navigation

            )
        }

        composable(
            route = "addMeal/{category}/{date}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            onScreenChanged("AddMealScreen")

            val category = backStackEntry.arguments?.getString("category")!!
            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()
            AddMealScreen(
                viewModel = viewModel,
                category = category,
                date = date,
                onMealSaved = {
                    navController.popBackStack()
                },
                onBackPress = { navController.popBackStack() }

            )
        }

        // Update Meal Screen
        composable(
            route = "updateMeal/{category}/{date}/{mealId}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("mealId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            onScreenChanged("UpdateMealScreen") // ✅ Notify MainScreen

            val category = backStackEntry.arguments?.getString("category") ?: ""
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date") ?: "")
            val mealId = backStackEntry.arguments?.getInt("mealId") ?: 0

            updateMealScreen(
                viewModel = viewModel,
                category = category,
                date = date,
                mealId = mealId,
                onBackPress = { navController.navigateUp() },
                onMealUpdated = {
                    // Trigger a refresh of the meal data
                    navController.navigateUp() // Navigate back after updating
                }
            )
        }




//         Category Screen
        composable(
            route = "category/{date}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            onScreenChanged("CategoryScreen") // ✅ Notify MainScreen

            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()
            CategoryScreen(
                viewModel = viewModel,
                selectedDay = date,
                onNavigateToAddMeal = { category, selectedDay ->
                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
                },
                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                },
                onBackPress = { navController.popBackStack() } // Handle back navigation

            )
        }
    }
}




//@Composable
//fun HomeNavigation(viewModel: MealPlanViewModel) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "mealPlanner") {
//        // MealPlannerUI as the first screen
//        composable("mealPlanner") {
//            MealPlannerUI()
//        }
//
//        // Home screen
//        composable("home") {
//            HomeScreen(
//                viewModel = viewModel,
//                onNavigateToAddMeal = { category, selectedDay ->
//                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
//                }
//            )
//        }
//
//        // AddMeal screen
//        composable(
//            route = "addMeal/{category}/{date}",
//            arguments = listOf(
//                navArgument("category") { type = NavType.StringType },
//                navArgument("date") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val category = backStackEntry.arguments?.getString("category")!!
//            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()
//            AddMealScreen(
//                viewModel = viewModel,
//                category = category,
//                date = date,
//                onMealSaved = {
//                    navController.popBackStack()
//                }
//            )
//        }
//    }
//}


//
//@Composable
//fun HomeNavigation(viewModel: MealPlanViewModel) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "home") {
//        composable("home") {
//            HomeScreen(
//                viewModel = viewModel,
//                onNavigateToAddMeal = { category, selectedDay ->
//                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
//                }
//            )
//        }
//        composable(
//            route = "addMeal/{category}/{date}",
//            arguments = listOf(
//                navArgument("category") { type = NavType.StringType },
//                navArgument("date") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val category = backStackEntry.arguments?.getString("category")!!
//            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()
//            AddMealScreen(
//                viewModel =viewModel,
//                category = category,
//                date = date,
//                onMealSaved = {
//                    navController.popBackStack()
//                }
//            )
//        }
//    }
//}
