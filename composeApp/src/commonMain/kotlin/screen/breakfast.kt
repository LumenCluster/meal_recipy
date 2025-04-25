package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import database.entity.MealPlan
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.back
import room_cmp.composeapp.generated.resources.break_fast
import room_cmp.composeapp.generated.resources.empty
import room_cmp.composeapp.generated.resources.icon_easy
import room_cmp.composeapp.generated.resources.icon_healthy
import room_cmp.composeapp.generated.resources.icon_time
import room_cmp.composeapp.generated.resources.meal
import ui.home.MealPlanViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BreakfastMealScreen(
    navController: NavController,
    viewModel: MealPlanViewModel,
    selectedDay: LocalDate,
    onNavigateToAddMeal: (String, LocalDate) -> Unit,
    onNavigateToUpdateMeal: (String, LocalDate, Int) -> Unit,
    onBackPress: () -> Unit
) {
    val mealPlanState by viewModel.mealPlanState.collectAsState()
    val mealsForDay by derivedStateOf {
        mealPlanState.groupedByDay[selectedDay.dayOfWeek.name].orEmpty()
    }

    LaunchedEffect(Unit) {
        viewModel.loadMealPlans()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(300.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.break_fast),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )

            IconButton(
                onClick = { onBackPress() },
                modifier = Modifier.padding(16.dp).size(36.dp).align(Alignment.TopStart)
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
            text = "${selectedDay.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, $selectedDay",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = "Today's Menu For Breakfast Is",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        DayMealPlanCard0(
            day = selectedDay,
            meals = mealsForDay,
            key = mealsForDay.hashCode()
        )

        Spacer(modifier = Modifier.height(16.dp))

        val breakfastMeals = mealsForDay["Breakfast"]?.filter { it.date == selectedDay.toString() } ?: emptyList()

        if (breakfastMeals.size == 1) {
            Button(
                onClick = {
                    onNavigateToUpdateMeal("Breakfast", selectedDay, breakfastMeals.first().id)
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
fun DayMealPlanCard0(
    day: LocalDate,
    meals: Map<String, List<MealPlan>>,
    key: Int? = null
) {
    key(key) {
        val category = "Breakfast"
        val mealList = meals[category]?.filter { it.date == day.toString() } ?: emptyList()

        if (mealList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mealList) { meal ->
                    MealItem0(meal = meal)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    text = "No meals added for breakfast.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealItem0(meal: MealPlan) {
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
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Text(
                        text = "$timeTaken min",
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp
                    )
                }

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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.icon_healthy),
                        contentDescription = "Veg Status",
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
