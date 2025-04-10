package repository

import database.dao.MealPlanDao
import database.entity.MealPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MealPlanRepository(private val mealPlanDao: MealPlanDao) {

    fun getAllMealPlans(): Flow<List<MealPlan>> = mealPlanDao.getAll()

    fun getMealsForDay(day: String): Flow<List<MealPlan>> = mealPlanDao.getMealsForDay(day)

    fun getMealsForDate(date: String): Flow<List<MealPlan>> = mealPlanDao.getMealsForDate(date)
    suspend fun getMealPlanByDate(date: String): MealPlan? {
        return mealPlanDao.getMealsForDate(date).firstOrNull()?.firstOrNull()  // Assuming only one meal plan per date
    }
    suspend fun deleteAllMealPlans() {
        mealPlanDao.deleteAllMealPlans()
    }
    fun getMealsForCategory(day: String, category: String): Flow<List<MealPlan>> =
        mealPlanDao.getMealsForCategory(day, category) // Fetch all items for a category

    suspend fun saveMealPlan(mealPlan: MealPlan) {
        // Directly insert new items without checking for duplicates
        mealPlanDao.insertMealPlan(mealPlan)
    }
    suspend fun updateMealPlan(mealPlan: MealPlan) {
        println("Updating Meal Plan: $mealPlan")
        mealPlanDao.updateMealPlan(mealPlan)
    }
    suspend fun deleteMealPlan(mealPlan: MealPlan) {
        mealPlanDao.deleteMealPlan(mealPlan)
    }

    suspend fun deleteMealsBeforeDate(currentDate: String) {
        mealPlanDao.deleteMealsBeforeDate(currentDate)
    }
}
