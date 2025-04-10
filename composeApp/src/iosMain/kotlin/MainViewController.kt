import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import database.getPeopleDatabase

fun MainViewController() = ComposeUIViewController {
    val database = remember { getPeopleDatabase() }
    val mealDao = remember { database.mealPlanDao() }
    val profileDao = remember { database.profileDao() }


    LaunchedEffect(Unit) {
        Graph.initialize(database)
    }
    App(mealDao, profileDao)
}
