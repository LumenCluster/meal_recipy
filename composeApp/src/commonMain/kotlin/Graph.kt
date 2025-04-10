import database.AppDatabase
import database.getRoomDatabase
import repository.MealPlanRepository
import repository.ProfileRepository
//
//object Graph {
//    lateinit var repository: MealPlanRepository
//    lateinit var repo: ProfileRepository
//
//    fun initialize(database: AppDatabase) {
//        repository = MealPlanRepository(database.mealPlanDao())
//        repo = ProfileRepository(database.profileDao())
//    }
//}

object Graph {
    private var _repository: MealPlanRepository? = null
    private var _repo: ProfileRepository? = null

    val repository: MealPlanRepository
        get() = _repository ?: throw IllegalStateException("Graph.repository not initialized!")

    val repo: ProfileRepository
        get() = _repo ?: throw IllegalStateException("Graph.repo not initialized!")

    fun initialize(database: AppDatabase) {
        _repository = MealPlanRepository(database.mealPlanDao())
        _repo = ProfileRepository(database.profileDao())
    }
}

