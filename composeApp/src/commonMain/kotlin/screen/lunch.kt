package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import database.entity.MealPlan
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.*
import ui.home.MealPlanViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LunchMealScreen(
    navController: NavController,
    viewModel: MealPlanViewModel,
    selectedDay: LocalDate,
    onNavigateToUpdateMeal: (String, LocalDate, Int) -> Unit,
    onBackPress: () -> Unit
) {
    val mealPlanState by viewModel.mealPlanState.collectAsState()

    // Refresh data when screen is focused
    LaunchedEffect(Unit) {
        viewModel.loadMealPlans()
    }

    val mealsForDay by derivedStateOf {
        mealPlanState.groupedByDay[selectedDay.dayOfWeek.name].orEmpty()
    }

    val lunchMeals = mealsForDay["Lunch"].orEmpty()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Image with top gradient and back button overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.lun_ch),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xCF000000),
                                Color(0xAC2A2A2A),
                                Color(0x00FFFFFF)
                            )
                        )
                    )
                    .align(Alignment.TopCenter)
            )

            IconButton(
                onClick = { onBackPress() },
                modifier = Modifier
                    .padding(16.dp)
                    .size(36.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${selectedDay.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${selectedDay}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = "Today's Menu For Lunch Is",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                DayMealPlanCard2(
                    day = selectedDay,
                    meals = mealsForDay,
                    key = mealsForDay.hashCode()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show "EDIT MEAL" button only if there is exactly one meal
        if (lunchMeals.size == 1) {
            Button(
                onClick = {
                    lunchMeals.firstOrNull()?.let { meal ->
                        onNavigateToUpdateMeal("Lunch", selectedDay, meal.id)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxWidth(0.6f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00796B))
            ) {
                Text(text = "EDIT MEAL", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DayMealPlanCard2(
    day: LocalDate,
    meals: Map<String, List<MealPlan>>,
    key: Int? = null
) {
    key(key) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
            val category = "Lunch"
            val mealList = meals[category]?.filter { it.date == day.toString() } ?: emptyList()

            if (mealList.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    mealList.forEach { meal ->
                        MealItem2(meal = meal)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.empty),
                        contentDescription = "No meals",
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No meals added for lunch.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealItem2(meal: MealPlan) {
    val description by remember(meal.id) { mutableStateOf(meal.description) }
    val timeTaken by remember(meal.id) { mutableStateOf(meal.timeTaken) }
    val difficulty by remember(meal.id) { mutableStateOf(meal.difficulty) }
    val healthiness by remember(meal.id) { mutableStateOf(meal.healthiness) }
    val servings by remember(meal.id) { mutableStateOf(meal.servings) }
    val veg by remember(meal.id) { mutableStateOf(meal.vegetarian) }

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
                        text = "$servings Serving",
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        fontSize = 10.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_time),
                        contentDescription = "Time Taken",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$timeTaken min", style = MaterialTheme.typography.caption, fontSize = 10.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_easy),
                        contentDescription = "Difficulty",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = difficulty, style = MaterialTheme.typography.caption, fontSize = 10.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_healthy),
                        contentDescription = "Healthiness",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Healthy", style = MaterialTheme.typography.caption, fontSize = 10.sp)
                }

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
