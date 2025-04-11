package screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import registerBackHandler
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.cancel
import room_cmp.composeapp.generated.resources.onback
import viewModel.DetailViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = viewModel(),
    id: String,
    navigateBack: () -> Unit
) {
    var showAddToMealDialog by remember { mutableStateOf(false) } // State for Add to Meal Dialog
    val detailState by detailViewModel.detailState.collectAsState()

    LaunchedEffect(Unit) {
        detailViewModel.fetchMealById(id)
    }

    Column(
        modifier = Modifier
            .background(Color.White)

            .fillMaxSize()
    ) {
        // Display Progress Indicator if loading
        if (detailState.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        // Display Error if any
        if (detailState.error != null) {
            Text(
                text = detailState.error.orEmpty(),
                style = MaterialTheme.typography.body1,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Display Meal Details if available
        detailState.meals?.let { meal ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    // Meal Image with Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        // Meal Image
                        AsyncImage(
                            model = meal.strMealThumb,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Back Button
                        IconButton(
                            onClick = navigateBack, // Replace with your back navigation function
                            modifier = Modifier
                                .align(Alignment.TopStart) // Align to top-left corner
                                .padding(16.dp) // Add padding for spacing
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.onback), // Replace with your drawable resource
                                contentDescription = "Back",
                                tint = Color.White // White icon color for better contrast
                            )
                        }

                        // Add to Meal Button
//                        IconButton(
//                            onClick = { showAddToMealDialog = true },
//                            modifier = Modifier
//                                .align(Alignment.TopEnd) // Align to top-right corner
//                                .padding(16.dp) // Add padding for spacing
//
//                        ) {
//                            Image(
//                                painter = painterResource(Res.drawable.to_meal), // Replace with your drawable resource
//                                contentDescription = "Add to Meal",
//                                modifier = Modifier
//                                    .width(100.dp)  // Set width to 100dp
//                                    .height(50.dp)  // Set height to 50dp
//                            )
//
//                        }

                        // Meal Title Overlay
                        Surface(
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp)) // Rounded top corners
                        ) {
                            Text(
                                text = meal.strMeal,
                                style = MaterialTheme.typography.body2.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .padding(top = 25.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }





                    }

                    Spacer(Modifier.height(16.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Method Section
                    Text(
                        text = "Method",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Display Steps
                    meal.strInstructions.split("\n").forEachIndexed { index, step ->
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Step ${index + 1}:",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = step,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }

            // Show Add to Meal Dialog
            if (showAddToMealDialog) {
                AddToMealDialog(
                    onDismiss = { showAddToMealDialog = false }
                )
            }
        }
    }
}



@OptIn(ExperimentalResourceApi::class)
@Composable
fun AddToMealDialog(onDismiss: () -> Unit) {
    val selectedMeals = remember { mutableStateListOf<String>() }
    val mealOptions = listOf("BREAKFAST", "LUNCH", "DINNER")
    val unselectedBackgroundColor = Color(0xFFF5F5F5)
    val selectedBackgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.2f)
    val doneButtonColor = Color(0xFF007370)

    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Optional: Apply padding if needed
            ) {
                // "Done" Button (no padding)
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(150.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(backgroundColor = doneButtonColor)
                ) {
                    Text(text = "DONE", color = Color.White, fontSize = 12.sp)
                }
                // "Cancel" Button (no padding)
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth() // No padding
                ) {
                    Text(text = "CANCEL", color = MaterialTheme.colors.onSurface, fontSize = 12.sp)
                }
            }
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Close icon at the top-right
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(20.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.cancel), // Replace with your drawable resource
                        contentDescription = "Close dialog",
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Title text
                Text(
                    text = "Add This Recipe To:",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 50.dp) // Adjust top padding to ensure spacing below the icon
                )
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp) // Padding around the meal options
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    // Apply a Spacer here to add more space at the top
                    Spacer(modifier = Modifier.height(20.dp)) // Adjust this height for the desired top padding

                    mealOptions.forEachIndexed { index, meal ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (index == 0) 0.dp else 8.dp, // Remove top padding from first item
                                    bottom = 8.dp
                                )
                                .background(
                                    color = if (selectedMeals.contains(meal)) selectedBackgroundColor
                                    else unselectedBackgroundColor,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable {
                                    if (selectedMeals.contains(meal)) {
                                        selectedMeals.remove(meal)
                                    } else {
                                        selectedMeals.add(meal)
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = meal,
                                style = MaterialTheme.typography.body1,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f),
                                color = MaterialTheme.colors.onSurface
                            )
                            IconButton(
                                onClick = {
                                    if (selectedMeals.contains(meal)) {
                                        selectedMeals.remove(meal)
                                    } else {
                                        selectedMeals.add(meal)
                                    }
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (selectedMeals.contains(meal)) Icons.Default.Check else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (selectedMeals.contains(meal)) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(16.dp)
    )
}









