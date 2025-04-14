package ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import database.entity.MealPlan
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import repository.MealPlanRepository
import kotlin.reflect.KClass
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MealPlanViewModel(private val mealPlanRepository: MealPlanRepository) : ViewModel() {
    private val _mealPlanState = MutableStateFlow(MealPlanState())
    val mealPlanState = _mealPlanState.asStateFlow()
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        deleteOldMealPlans() // New function to delete previous dates' meals
        loadMealPlans()
    }

    private fun deleteOldMealPlans() {
        viewModelScope.launch {
            try {
                val currentDate = getCurrentDate()
                mealPlanRepository.deleteMealsBeforeDate(currentDate)
            } catch (exception: Exception) {
            }
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        return currentDate.toString() // Convert LocalDate to String in "yyyy-MM-dd" format
    }

    fun loadMealPlans() {
        viewModelScope.launch {
            mealPlanRepository.getAllMealPlans()
                .catch { exception ->
                }
                .collect { meals ->
                    _mealPlanState.update { state ->
                        state.copy(
                            meals = meals,
                            groupedByDay = groupMealsByDay(meals)
                        )
                    }
                }
        }
    }

    fun loadMealPlanByDate(date: String) {
        viewModelScope.launch {
            try {
                mealPlanRepository.getMealPlanByDate(date)
                    ?.let { mealPlan ->
                        _mealPlanState.update { state ->
                            state.copy(
                                meals = listOf(mealPlan),
                                groupedByDay = groupMealsByDay(listOf(mealPlan))
                            )
                        }
                    } ?: _mealPlanState.update { state ->
                    state.copy(meals = emptyList(), groupedByDay = emptyMap())
                }
            } catch (exception: Exception) {
            }
        }
    }

    fun saveMealPlan(
        day: String,
        category: String,
        description: String,
        timeTaken: Int,
        difficulty: String,
        healthiness: String,
        servings: Int,
        date: String,
        vegetarian: Boolean
    ) {
        viewModelScope.launch {
            try {
                val mealPlan = MealPlan(
                    day = day,
                    category = category,
                    description = description,
                    timeTaken = timeTaken,
                    difficulty = difficulty,
                    healthiness = healthiness,
                    servings = servings,
                    date = date,
                    vegetarian = vegetarian
                )
                mealPlanRepository.saveMealPlan(mealPlan)
                _mealPlanState.update { state ->
                    state.copy(
                        meals = state.meals + mealPlan,
                        groupedByDay = groupMealsByDay(state.meals + mealPlan)
                    )
                }
            } catch (exception: Exception) {
            }
        }
    }

    fun updateMealPlan(
        id: Int,
        day: String,
        category: String,
        description: String,
        timeTaken: Int,
        difficulty: String,
        healthiness: String,
        servings: Int,
        date: String,
        vegetarian: Boolean
    ) {
        viewModelScope.launch {
            try {
                // Create updated meal plan
                val updatedMealPlan = MealPlan(
                    id = id,
                    day = day,
                    category = category,
                    description = description,
                    timeTaken = timeTaken,
                    difficulty = difficulty,
                    healthiness = healthiness,
                    servings = servings,
                    date = date,
                    vegetarian = vegetarian
                )

                // Update in database
                mealPlanRepository.updateMealPlan(updatedMealPlan)

                // Optimistically update UI state
                _mealPlanState.update { currentState ->
                    val updatedMeals = currentState.meals.map {
                        if (it.id == id) updatedMealPlan else it
                    }

                    currentState.copy(
                        meals = updatedMeals,
                        groupedByDay = groupMealsByDay(updatedMeals),
//                        lastUpdated = Clock.System.currentTimeMillis() // Add timestamp for change tracking
                    )
                }

                // Force refresh from database to ensure consistency
                loadMealPlans()

            } catch (exception: Exception) {
                // Handle error and revert state if needed
                _mealPlanState.update { currentState ->
                    currentState.copy(
//                        error = "Failed to update meal: ${exception.localizedMessage}"
                    )
                }
                // Optionally reload original data
                loadMealPlans()
            }
        }
    }
    fun deleteMealPlan(mealPlan: MealPlan) {
        viewModelScope.launch {
            try {
                mealPlanRepository.deleteMealPlan(mealPlan)
                _mealPlanState.update { state ->
                    val remainingMeals = state.meals.filterNot { it.id == mealPlan.id }
                    state.copy(
                        meals = remainingMeals,
                        groupedByDay = groupMealsByDay(remainingMeals)
                    )
                }
            } catch (exception: Exception) {
            }
        }
    }

    fun deleteAllMealPlans() {
        viewModelScope.launch {
            try {
                mealPlanRepository.deleteAllMealPlans()
                _mealPlanState.update { state ->
                    state.copy(meals = emptyList(), groupedByDay = emptyMap())
                }
            } catch (exception: Exception) {
            }
        }
    }

    fun getAllMealPlansAsString(): String {
        return _mealPlanState.value.meals.joinToString("\n") { meal ->
            "${meal.day} - ${meal.category}: ${meal.description}, Servings: ${meal.servings}"
        }
    }

    private fun groupMealsByDay(meals: List<MealPlan>): Map<String, Map<String, List<MealPlan>>> {
        return meals.groupBy { meal -> meal.day }
            .mapValues { (_, dayMeals) ->
                dayMeals.groupBy { meal -> meal.category }
            }
    }
}

// State class to hold meal plan data and its grouping
data class MealPlanState(
    val meals: List<MealPlan> = emptyList(),
    val groupedByDay: Map<String, Map<String, List<MealPlan>>> = emptyMap()
)

// Factory class for creating MealPlanViewModel instances
@Suppress("UNCHECKED_CAST")
class MealPlanViewModelFactory(private val mealPlanRepository: MealPlanRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        return MealPlanViewModel(mealPlanRepository = mealPlanRepository) as T
    }
}
