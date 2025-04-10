package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan_table")
data class MealPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "day") val day: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "difficulty") val difficulty: String = "undefined",
    @ColumnInfo(name = "healthiness") val healthiness: String = "undefined",
    @ColumnInfo(name = "servings") val servings: Int = 1,
    @ColumnInfo(name = "timeTaken") val timeTaken: Int = 0,
    @ColumnInfo(name = "date") val date: String,  // New date column
    @ColumnInfo(name = "vegetarian") val vegetarian: Boolean // New vegetarian column
)

@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "profile_img") val profileImg: String, // Store as a URI or file path
    @ColumnInfo(name = "food_img") val foodImg: String, // Store as a URI or file path
    @ColumnInfo(name = "food_preference") val foodPreference: String
)
