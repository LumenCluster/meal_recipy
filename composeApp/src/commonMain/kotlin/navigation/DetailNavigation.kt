package navigation

import RecipyNav
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import org.example.compose.home.HomeViewModel
import org.example.compose.home.ProfileViewModel
import screen.AboutAppScreen
import screen.AddMealScreen
import screen.BreakfastMealScreen
import screen.CategoryScreen
import screen.DetailScreen
import screen.DinnerMealScreen
import screen.FirstScreen
import screen.LunchMealScreen
import screen.MainScreen
import screen.Tabs
import screen.updateMealScreen
import ui.home.HomeScreen
import ui.home.MealPlanViewModel
import ui.home.MealPlanViewModelFactory
import viewModel.ProfileViewModelFactory


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{mealId}") {
        fun createRoute(mealId: String) = "detail/$mealId"
    }
}

@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel = viewModel(),
    onBackToHome: () -> Unit,

//    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(Graph.repo)),
    onScreenChanged: (String) -> Unit

) {
    val navController = rememberNavController()
    val mealViewModel: MealPlanViewModel = viewModel(factory = MealPlanViewModelFactory(Graph.repository))
    val profileViewModel = remember { ProfileViewModel(Graph.repo) }



    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            FirstScreen(
                homeViewModel = homeViewModel,
                profileViewModel = profileViewModel,
                navigateToDetail = { mealId ->
                    navController.navigate(Screen.Detail.createRoute(mealId))
                },
                navController = navController,
                onScreenChanged = onScreenChanged
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                onScreenChanged("DetailScreen")
            }

            val mealId = backStackEntry.arguments?.getString("mealId") ?: return@composable
            DetailScreen(
                id = mealId,
                navigateBack = { navController.navigateUp() }
            )
        }

        composable("profile") {
            LaunchedEffect(Unit) {
                onScreenChanged("ProfileScreen")
            }
            val navController0 = rememberNavController()
            ProfileScreenRoute(navController0)
        }

        composable("view") {
            LaunchedEffect(Unit) {
                onScreenChanged("view")
            }
            RecipyNav(onScreenChanged = { }, onBackToHome ={
                navController.navigateUp()
            } )

        }


        composable("about") {
            LaunchedEffect(Unit) {
                onScreenChanged("about")
            }
            AboutAppScreen(onBackClick = { navController.navigateUp() })
        }


        composable(
            route = "breakfast/{date}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                onScreenChanged("BreakfastMealScreen")
            }

            // Retrieve the date argument safely
            val dateString = backStackEntry.arguments?.getString("date")
            val date = dateString?.takeIf { it.matches(Regex("""\d{4}-\d{2}-\d{2}""")) }
                ?.let { LocalDate.parse(it) }

            // If date is invalid, you can handle this case
            if (date == null) {
            }

            BreakfastMealScreen(
                navController = navController,
                viewModel = mealViewModel,
                selectedDay = date ?: LocalDate(2025, 2, 14), // Fallback date
                onNavigateToAddMeal = { category, selectedDay ->
                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
                },
                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                },
                onBackPress = { navController.navigateUp() } // Handle back press
            )
        }


        composable(
            route = "Lunch/{date}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                onScreenChanged("LunchMealScreen")
            }

            // Retrieve the date argument safely
            val dateString = backStackEntry.arguments?.getString("date")
            val date = dateString?.takeIf { it.matches(Regex("""\d{4}-\d{2}-\d{2}""")) }
                ?.let { LocalDate.parse(it) }

            // Handle case if date is invalid
            if (date == null) {
                // Handle error: either navigate back or use a default value
            }

            LunchMealScreen(
                navController = navController,
                viewModel = mealViewModel,
                selectedDay = date ?: LocalDate(2025, 2, 14), // Default fallback value if date is invalid

                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                },
                onBackPress = { navController.navigateUp() } // Handle back navigation
            )
        }
        composable(
            route = "Dinner/{date}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                onScreenChanged("DinnerMealScreen")
            }

            // Retrieve the date argument safely
            val dateString = backStackEntry.arguments?.getString("date")
            val date = dateString?.takeIf { it.matches(Regex("""\d{4}-\d{2}-\d{2}""")) }
                ?.let { LocalDate.parse(it) }

            if (date == null) {

            }

            DinnerMealScreen(
                navController = navController,
                viewModel = mealViewModel,
                selectedDay = date ?: LocalDate(2025, 2, 14), // Default fallback value if date is invalid
                onNavigateToAddMeal = { category, selectedDay ->
                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
                },
                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                },
                onBackPress = { navController.navigateUp() } // Handle back navigation
            )
        }

        // Category Screen
        composable(
            route = "category/{date}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            LaunchedEffect(Unit) {
                onScreenChanged("CategoryMealScreen")
            }
            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()

            CategoryScreen(
                viewModel = mealViewModel,
                selectedDay = date,
                onNavigateToAddMeal = { category, selectedDay ->
                    navController.navigate("addMeal/$category/${selectedDay.toString()}")
                },
                onNavigateToUpdateMeal = { category, selectedDay, mealId ->
                    navController.navigate("updateMeal/$category/${selectedDay.toString()}/$mealId")
                },
                onBackPress = { navController.navigateUp() } // Handle back navigation

            )
        }
//        composable("breakfast") { backStackEntry ->
//            LaunchedEffect(Unit) {
//                onScreenChanged("ProfileScreen")
//            }
//            val navController = rememberNavController() // Ensure NavController is available
////            val date = LocalDate.parse(backStackEntry.arguments?.getString("date") ?: "")
//
//
//            BreakfastMealScreen(
//                navController = navController,
//                )
//        }


        composable("new") {
            LaunchedEffect(Unit) {
                onScreenChanged("HomeScreen")
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
                },
                onBackClick = {
                    navController.navigateUp()

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
            onScreenChanged("AddMealScreen")

            val category = backStackEntry.arguments?.getString("category")!!
            val date = backStackEntry.arguments?.getString("date")!!.toLocalDate()
            AddMealScreen(
                viewModel = mealViewModel,
                category = category,
                date = date,
                onMealSaved = {
                    navController.popBackStack()
                },
                onBackPress = { navController.navigateUp() } // Handle back navigation

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
            onScreenChanged("UpdateMealScreen")

            val category = backStackEntry.arguments?.getString("category") ?: ""
            val date = LocalDate.parse(backStackEntry.arguments?.getString("date") ?: "")
            val mealId = backStackEntry.arguments?.getInt("mealId") ?: 0
            val currentDate = kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            updateMealScreen(
                viewModel = mealViewModel,
                category = category,
                date = date,
                mealId = mealId,
                onBackPress = { navController.navigateUp() } ,// Handle back navigation
                onMealUpdated ={
                    navController.navigateUp()                }


//                {
//                    when (category.lowercase()) {
//                        "Breakfast" -> navController.navigate("breakfast/$currentDate") // Correct format YYYY-MM-DD
//                        {
//                            popUpTo("updateMeal/$category/$date/$mealId") { inclusive = true }
//                            mealViewModel.loadMealPlans()
//                        }
//                        "lunch" -> navController.navigate("Lunch/${date}") {
//                            popUpTo("updateMeal/$category/$date/$mealId") { inclusive = true }
//                        }
//                        "dinner" -> navController.navigate("Dinner/${date}") {
//                            popUpTo("updateMeal/$category/$date/$mealId") { inclusive = true }
//                        }
//                        else -> navController.popBackStack() // fallback
//                    }
//                }

            )


        }






    }
}

