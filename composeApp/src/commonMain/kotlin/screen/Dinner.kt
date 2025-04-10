package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.back
import room_cmp.composeapp.generated.resources.din_ner
import room_cmp.composeapp.generated.resources.empty
import room_cmp.composeapp.generated.resources.icon_easy
import room_cmp.composeapp.generated.resources.icon_healthy
import room_cmp.composeapp.generated.resources.icon_time
import room_cmp.composeapp.generated.resources.meal
import ui.home.MealPlanViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DinnerMealScreen(
    navController: NavController,
    viewModel: MealPlanViewModel,
    selectedDay: LocalDate,
    onNavigateToAddMeal: (String, LocalDate) -> Unit,
    onNavigateToUpdateMeal: (String, LocalDate, Int) -> Unit,
    onBackPress: () -> Unit


)
{
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)

    ) {

        // Image with top gradient and back button overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            // Background Image
            Image(
                painter = painterResource(Res.drawable.din_ner),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Gradient only at the top
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xCF000000), // 80% Black
                                Color(0xAC2A2A2A), // 100% Dark Gray
                                Color(0x00FFFFFF)  // Fully Transparent
                            )
                        )
                    )
                    .align(Alignment.TopCenter)
            )

            // Back Button
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


        // Date
        Text(
            text = "${selectedDay.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${selectedDay.toString()}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = "Today's Menu For Dinner Is",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        val mealPlanState by viewModel.mealPlanState.collectAsState()

        // Show only breakfast meals for the selected day
        val mealsForDay = mealPlanState.groupedByDay[selectedDay.dayOfWeek.name].orEmpty()
            .filterKeys { it == "Dinner" }

        DayMealPlanCard1(
            day = selectedDay,
            meals = mealsForDay,
        )


        Spacer(modifier = Modifier.height(16.dp))

        val hasMeals = mealsForDay["Dinner"]?.isNotEmpty() == true

        if (hasMeals) {
            Button(
                onClick = {
                    val firstMeal = mealsForDay["Dinner"]?.firstOrNull()
                    if (firstMeal != null) {
                        onNavigateToUpdateMeal("Dinner", selectedDay, firstMeal.id)
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
fun DayMealPlanCard1(
    day: LocalDate,
    meals: Map<String, List<MealPlan>>,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        val category = "Dinner"
        val mealList = meals[category]?.filter { it.date == day.toString() } ?: emptyList()

        if (mealList.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                mealList.forEach { meal ->
                    MealItem1(meal = meal)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            // When there are no meals, show a placeholder UI
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.empty), // Replace with a relevant empty state image
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
fun MealItem1(
    meal: MealPlan,
//    onDelete: (MealPlan) -> Unit,
//    onUpdate: (MealPlan) -> Unit
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
                        text = healthiness,
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