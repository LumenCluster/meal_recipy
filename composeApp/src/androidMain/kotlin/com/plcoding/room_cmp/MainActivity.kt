package com.plcoding.room_cmp

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.plcoding.room_cmp.database.getPeopleDatabase
import setCurrentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentActivity(this)

        val database = getPeopleDatabase(applicationContext)
        val mealDao = database.mealPlanDao()
        val profileDao = database.profileDao()


        Graph.initialize(database)


        setContent {
            App(mealDao, profileDao)
        }
    }
}
