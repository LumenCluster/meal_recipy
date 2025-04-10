
package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import ui.home.MealPlanViewModel
import database.entity.MealPlan
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.delete
import room_cmp.composeapp.generated.resources.edit
import room_cmp.composeapp.generated.resources.icon_add
import room_cmp.composeapp.generated.resources.meal
import room_cmp.composeapp.generated.resources.onback

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CategoryScreen(
    viewModel: MealPlanViewModel,
    selectedDay: LocalDate,
    onNavigateToAddMeal: (String, LocalDate) -> Unit,
    onNavigateToUpdateMeal: (String, LocalDate, Int) -> Unit,
    onBackPress: () -> Unit

    // Updated with meal id
) {
    val mealPlanState by viewModel.mealPlanState.collectAsState() // Move this outside LazyColumn
    val mealsForDay = mealPlanState.groupedByDay[selectedDay.dayOfWeek.name].orEmpty()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Icon(
                painter = painterResource(Res.drawable.onback),
                contentDescription = "About Icon",

                tint = Color.Black,
                modifier = Modifier
//                    .padding(8.dp) // <-- Add your desired padding here
                    .clickable { onBackPress() }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Meal Plan",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(
                text = "Plan Meal For",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Text(
                text = "${selectedDay.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${selectedDay.toString()}",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            DayMealPlanCard(
                day = selectedDay,
                meals = mealsForDay,
                onDeleteMeal = { mealPlan -> viewModel.deleteMealPlan(mealPlan) },
                onAddMeal = { category -> onNavigateToAddMeal(category, selectedDay) },
                onNavigateToUpdateMeal = onNavigateToUpdateMeal
            )
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
    onNavigateToUpdateMeal: (String, LocalDate, Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        listOf("Breakfast", "Lunch", "Dinner").forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 2.dp,
                backgroundColor = Color.White
            ) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.uppercase(),
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onAddMeal(category) }) {
                            Image(
                                painter = painterResource(Res.drawable.icon_add),
                                contentDescription = "Add Meal",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    val mealList = meals[category] ?: emptyList()
                    if (mealList.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            mealList.forEach { meal ->
                                MealCategorySectionNew(
                                    category = category,
                                    meal = meal,
                                    onDelete = { onDeleteMeal(meal) },
                                    onAdd = { onAddMeal(category) },
                                    onUpdate = {
                                        onNavigateToUpdateMeal(category, day, meal.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealCategorySectionNew(
    category: String,
    meal: MealPlan,
    onDelete: () -> Unit,
    onAdd: () -> Unit,
    onUpdate: (MealPlan) -> Unit
) {
    var description by rememberSaveable { mutableStateOf(meal.description) }
    var servings by rememberSaveable { mutableStateOf(meal.servings) }

    LaunchedEffect(meal) {
        description = meal.description
        servings = meal.servings
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Meal image
            Image(
                painter = painterResource(Res.drawable.meal), // replace with actual image
                contentDescription = "Meal Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = description.ifEmpty { "Meal Description" },
                    style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$servings Serving",
                    style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp).padding(0.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.delete),
                        contentDescription = "Delete Meal",
                        tint = Color.Red,
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = { onUpdate(meal) },
                    modifier = Modifier.size(24.dp).padding(0.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.edit),
                        contentDescription = "Edit Meal",
                        tint = Color.Black,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        Divider(
            color = Color(0xFFDFDFDF),
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 14.dp)
        )
    }
}

