//import database.AppDatabase
//import repository.MealPlanRepository
//import repository.ProfileRepository
//
//
//object RepositoryProvider {
//    private val database by lazy { AppDatabase.getDatabase(App.instance) } // Ensure you have a singleton AppDatabase
//
//    fun getProfileRepository(): ProfileRepository {
//        return ProfileRepository(database.p())
//    }
//
//    fun getMealPlanRepository(): MealPlanRepository {
//        return MealPlanRepository(database.mealPlanDao()) // Adjust based on your implementation
//    }
//}
