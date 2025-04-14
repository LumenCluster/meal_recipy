package ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import database.entity.MealPlan
import kotlinx.datetime.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.delete
import room_cmp.composeapp.generated.resources.edit
import room_cmp.composeapp.generated.resources.empty
import room_cmp.composeapp.generated.resources.icon_easy
import room_cmp.composeapp.generated.resources.icon_healthy
import room_cmp.composeapp.generated.resources.icon_time
import room_cmp.composeapp.generated.resources.meal
import room_cmp.composeapp.generated.resources.onback

val LocalDateSaver = object : Saver<LocalDate, String> {
    override fun SaverScope.save(value: LocalDate): String = value.toString()
    override fun restore(value: String): LocalDate = LocalDate.parse(value)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MealPlanViewModel,
    onNavigateToAddMeal: (String, LocalDate) -> Unit,
    onNavigateToUpdateMeal: (String, LocalDate, Int) -> Unit,
    onNavigateToCategoryScreen: (LocalDate) -> Unit,
    onBackClick: () -> Unit
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val daysOfWeek = (0 until 7).map { offset -> today.plus(offset, DateTimeUnit.DAY) }
    var selectedDay by rememberSaveable(stateSaver = LocalDateSaver) { mutableStateOf(daysOfWeek.first()) }

    // Collect state and ensure recomposition on changes
    val mealPlanState by viewModel.mealPlanState.collectAsState()

    // Refresh data when screen is focused
    LaunchedEffect(Unit) {
        viewModel.loadMealPlans()
    }

    val mealsForDay by derivedStateOf {
        mealPlanState.groupedByDay[selectedDay.dayOfWeek.name].orEmpty()
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.onback),
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Meal Plan",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        DaysOfWeekHeader(daysOfWeek, selectedDay, onDaySelected = { selectedDay = it })

        LazyColumn {
            item {
                DayMealPlanCard(
                    day = selectedDay,
                    meals = mealsForDay,
                    onDeleteMeal = { viewModel.deleteMealPlan(it) },
                    onAddMeal = { category -> onNavigateToAddMeal(category, selectedDay) },
                    onNavigateToCategoryScreen = { onNavigateToCategoryScreen(selectedDay) },
                    onUpdateMeal = { category, mealId -> onNavigateToUpdateMeal(category, selectedDay, mealId) },
                    key = mealsForDay.hashCode()
                )
            }
        }
    }
}


@Composable
fun DaysOfWeekHeader(
    daysOfWeek: List<LocalDate>,
    selectedDay: LocalDate,
    onDaySelected: (LocalDate) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(daysOfWeek.size) { index ->
            val day = daysOfWeek[index]
            val isSelected = day == selectedDay
            DayItem(
                day = day,
                isSelected = isSelected,
                onClick = { onDaySelected(day) }
            )
        }
    }
}

@Composable
fun DayItem(
    day: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .background(
                color = if (isSelected) Color(0xFF007370) else Color(0xFFDBEBEB),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable { onClick() }
            .width(40.dp) // Increase width
            .height(65.dp) // Increase height
            .padding(5.dp)
    ) {
        Text(
            text = day.dayOfWeek.name.take(3),
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 12.sp
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.White) // Highlight today's date
        ) {
            Text(text = day.dayOfMonth.toString(), color = Color.Black)
        }
    }
}
@OptIn(ExperimentalResourceApi::class)
@Composable
fun DayMealPlanCard(
    day: LocalDate,
    meals: Map<String, List<MealPlan>>,
    onDeleteMeal: (MealPlan) -> Unit,
    onAddMeal: (String) -> Unit,
    onUpdateMeal: (String, Int) -> Unit,
    onNavigateToCategoryScreen: () -> Unit,
    key: Int? = null
) {
    key(key) {
        val hasMeals = meals.values.any { it.isNotEmpty() }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 10.dp)
                .padding(bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${day.dayOfWeek.name}, ${day.dayOfMonth} ${day.month.name}",
                    style = MaterialTheme.typography.h6,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { onNavigateToCategoryScreen() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5959)),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "ADD MEAL",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Icon",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (hasMeals) {
                listOf("Breakfast", "Lunch", "Dinner").forEach { category ->
                    val mealList = meals[category] ?: emptyList()
                    if (mealList.isNotEmpty()) {
                        MealCategorySection(category, mealList, onDeleteMeal, onAddMeal, onUpdateMeal)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.empty),
                        contentDescription = "No Meal Plans",
                        modifier = Modifier.size(150.dp).padding(16.dp)
                    )
                    Text(
                        text = "No Meal Plans Added",
                        style = MaterialTheme.typography.h6,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}





@Composable
fun MealCategorySection(
    category: String,
    meals: List<MealPlan>,
    onDelete: (MealPlan) -> Unit,
    onAdd: (String) -> Unit,
    onUpdate: (String, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = category,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        meals.forEach { meal ->
            MealItem(meal, { onDelete(meal) }, { onUpdate(category, meal.id) })
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealItem(
    meal: MealPlan,
    onDelete: (MealPlan) -> Unit,
    onUpdate: (MealPlan) -> Unit
) {
    var description by rememberSaveable { mutableStateOf(meal.description) }
    var timeTaken by rememberSaveable { mutableStateOf(meal.timeTaken) }
    var difficulty by rememberSaveable { mutableStateOf(meal.difficulty) }
    var healthiness by rememberSaveable { mutableStateOf(meal.healthiness) }
    var servings by rememberSaveable { mutableStateOf(meal.servings) }
    var veg by rememberSaveable { mutableStateOf(meal.vegetarian) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.meal),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.surface)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = description.ifEmpty { "Meal Description" },
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        fontSize = 13.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Servings: $servings",
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onDelete(meal) }) {
                        Image(
                            painter = painterResource(Res.drawable.delete),
                            contentDescription = "Delete Meal",
                            modifier = Modifier.size(15.dp),
                            alignment = Alignment.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(1.dp))

                    IconButton(onClick = { onUpdate(meal) }) {
                        Image(
                            painter = painterResource(Res.drawable.edit),
                            contentDescription = "Edit Meal",
                            modifier = Modifier.size(15.dp),
                            alignment = Alignment.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time Taken
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_time),
                        contentDescription = "Time Taken",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$timeTaken min",
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp
                    )
                }

                // Difficulty
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_easy),
                        contentDescription = "Difficulty",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = difficulty,
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp
                    )
                }

                // Healthiness
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_healthy),
                        contentDescription = "Healthiness",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Healthy",
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp
                    )
                }

                // Servings
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_healthy),
                        contentDescription = "Servings",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (veg) "Veg" else "Non-Veg",
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp
                    )

                }
            }
        }
    }
}




//@Composable
//fun DayMealPlanCard(
//    day: LocalDate,
//    meals: Map<String, MealPlan?>,
//    onDeleteMeal: (MealPlan) -> Unit,
//    onAddMeal: (String) -> Unit,
//    onUpdateMeal: (String, Int) -> Unit,
//    onNavigateToCategoryScreen: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .padding(10.dp)
//            .verticalScroll(rememberScrollState())
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "${day.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${day.dayOfMonth.toString().padStart(2, '0')} ${day.month.name.lowercase().replaceFirstChar { it.uppercase() }}",
//                style = MaterialTheme.typography.h6,
//                fontSize = 15.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Button(
//                onClick = { onNavigateToCategoryScreen() },
//                modifier = Modifier
//                    .padding(end = 2.dp)
//                    .height(30.dp),
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5959)),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "ADD MEAL",
//                        color = Color.White,
//                        fontSize = 11.sp
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Image(
//                        painter = painterResource(Res.drawable.plus),
//                        contentDescription = "Add Meal",
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        listOf("Breakfast", "Lunch", "Dinner").forEach { category ->
//            val meal = meals[category]
//            MealCategorySection(
//                category = category,
//                meal = meal,
//                onDelete = { meal?.let { onDeleteMeal(it) } },
//                onAdd = { onAddMeal(category) },
//                onUpdate = {
//                    meal?.let { onUpdateMeal(category, it.id) }
//                }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealCategorySection(
    category: String,
    meals: List<MealPlan>, // Updated to handle multiple meals
    onDelete: (MealPlan) -> Unit,
    onAdd: () -> Unit,
    onUpdate: (MealPlan) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        meals.forEach { meal ->
            var description by rememberSaveable { mutableStateOf(meal.description.orEmpty()) }
            var timeTaken by rememberSaveable { mutableStateOf(meal.timeTaken ?: 0) }
            var difficulty by rememberSaveable { mutableStateOf(meal.difficulty ?: "Easy") }
            var healthiness by rememberSaveable { mutableStateOf(meal.healthiness ?: "Healthy") }
            var servings by rememberSaveable { mutableStateOf(meal.servings ?: 1) }

            LaunchedEffect(meal) {
                description = meal.description.orEmpty()
                timeTaken = meal.timeTaken ?: 0
                difficulty = meal.difficulty ?: "Easy"
                healthiness = meal.healthiness ?: "Healthy"
                servings = meal.servings ?: 1
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.meal),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = description.ifEmpty { "Meal Description" },
                                style = MaterialTheme.typography.subtitle1,
                                maxLines = 1,
                                fontSize = 13.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = description.ifEmpty { "Meal Description" },
                                style = MaterialTheme.typography.subtitle1,
                                maxLines = 1,
                                fontSize = 10.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onDelete(meal) }) {
                                Image(
                                    painter = painterResource(Res.drawable.delete),
                                    contentDescription = "Delete Meal",
                                    modifier = Modifier.size(15.dp),
                                    alignment = Alignment.Center
                                )
                            }

                            Spacer(modifier = Modifier.width(1.dp))

                            IconButton(onClick = { onUpdate(meal) }) {
                                Image(
                                    painter = painterResource(Res.drawable.edit),
                                    contentDescription = "Edit Meal",
                                    modifier = Modifier.size(15.dp),
                                    alignment = Alignment.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MealInfoRow(Res.drawable.icon_time, "$timeTaken min")
                        MealInfoRow(Res.drawable.icon_easy, difficulty)
                        MealInfoRow(Res.drawable.icon_healthy, healthiness)
                        MealInfoRow(Res.drawable.icon_healthy, "Servings: $servings")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealInfoRow(iconRes: DrawableResource, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            fontSize = 10.sp
        )
    }
}

