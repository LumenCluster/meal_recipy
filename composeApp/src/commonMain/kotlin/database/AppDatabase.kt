package database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.dao.MealPlanDao
import database.entity.MealPlan
import database.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO


@Database(entities = [MealPlan::class, Profile::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun mealPlanDao(): MealPlanDao
    abstract fun profileDao(): ProfileDao

}

fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}