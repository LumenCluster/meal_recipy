package database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import database.entity.MealPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM meal_plan_table")
    fun getAll(): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plan_table WHERE day = :day")
    fun getMealsForDay(day: String): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plan_table WHERE date = :date")
    fun getMealsForDate(date: String): Flow<List<MealPlan>>

    @Query("SELECT * FROM meal_plan_table WHERE day = :day AND category = :category")
    fun getMealsForCategory(day: String, category: String): Flow<List<MealPlan>> // Fetch multiple items by category

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Avoid replacing items
    suspend fun insertMealPlan(mealPlan: MealPlan)

    @Update
    suspend fun updateMealPlan(mealPlan: MealPlan) // New update method

    @Delete
    suspend fun deleteMealPlan(mealPlan: MealPlan)

    // New method to delete all meal plans
    @Query("DELETE FROM meal_plan_table")
    suspend fun deleteAllMealPlans() // Deletes all records from the table
    @Query("DELETE FROM meal_plan_table WHERE date < :currentDate")
    suspend fun deleteMealsBeforeDate(currentDate: String)
}


