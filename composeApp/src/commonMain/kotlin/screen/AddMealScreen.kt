package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import ui.home.MealPlanViewModel
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.add
import room_cmp.composeapp.generated.resources.dropdown
import room_cmp.composeapp.generated.resources.minus
import room_cmp.composeapp.generated.resources.non
import room_cmp.composeapp.generated.resources.onback
import room_cmp.composeapp.generated.resources.veg


@OptIn(ExperimentalResourceApi::class)
@Composable
fun AddMealScreen(
    viewModel: MealPlanViewModel,
    category: String,
    date: LocalDate,
    onMealSaved: () -> Unit,
    onBackPress: () -> Unit

) {
    var mealName by rememberSaveable { mutableStateOf("") }
    var timeTaken by rememberSaveable { mutableStateOf(0) }
    var difficulty by rememberSaveable { mutableStateOf("Difficulty") }
    var servings by rememberSaveable { mutableStateOf(1) }
    var mealType by rememberSaveable { mutableStateOf("Vegetarian") }
    var validationError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)

    ) {

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

        Text(
            text = "Plan Meal For",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )

        Text(
            text = "${date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, ${date.toString()}",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add Meal Plan",
            style = MaterialTheme.typography.subtitle1,
            fontSize = 13.sp
        )
// Meal Name Input
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF5F5F5), // Background color
                    shape = RoundedCornerShape(10.dp) // Rounded corners
                )
        ) {
            TextField(
                value = mealName,
                onValueChange = { mealName = it },
                placeholder = { Text("Add Meal Name Please...") },
                        modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent), // Transparent background for TextField itself
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent, // Transparent to preserve outer background
                    focusedIndicatorColor = Color.Transparent, // Hide underline when focused
                    unfocusedIndicatorColor = Color.Transparent // Hide underline when unfocused
                ),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp) // Set text size for the input text

            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        DifficultyAndTimeTakenRow(
            difficulty = difficulty,
            timeTaken = if (timeTaken > 0) "$timeTaken minutes" else "Time Taken",
            onDifficultySelected = { difficulty = it },
            onTimeTakenSelected = { selected ->
                timeTaken = selected.filter { it.isDigit() }.toInt()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Meal Servings",
            style = MaterialTheme.typography.subtitle1,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(5.dp))
        MealServingsRow(
            servings = servings,
            onIncrement = { servings++ },
            onDecrement = { if (servings > 1) servings-- }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add Meal Plan",
            style = MaterialTheme.typography.subtitle1,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        )
        {
            ToggleButton(
                text = "Vegetarian",
                isSelected = mealType == "Vegetarian",
                onSelected = { mealType = "Vegetarian" },
                icon = {
                    Image(
                        painter = painterResource(Res.drawable.veg), // Replace with dropdown arrow resource
                        contentDescription = "Vegetarian Icon",
//                        tint = if (mealType == "Vegetarian") Color.White else Color(0xFF007370)
                    )
                }
            )
            ToggleButton(
                text = "Non-Vegetarian",
                isSelected = mealType == "Non-Vegetarian",
                onSelected = { mealType = "Non-Vegetarian" },
                icon = {
                    Image(
                        painter = painterResource(Res.drawable.non), // Replace with dropdown arrow resource
                        contentDescription = "Non-Vegetarian Icon",
//                        tint = if (mealType == "Non-Vegetarian") Color.White else Color(0xFF666666)
                    )
                }
            )
        }
        // Show error message if validation fails
        if (validationError.isNotEmpty()) {
            Text(
                text = validationError,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Save Meal Button
        Button(
            onClick = {
                if (mealName.isBlank() || timeTaken == 0 || difficulty == "Difficulty") {
                    validationError = "Enter data in all fields to save"
                } else {
                    validationError = ""
                    viewModel.saveMealPlan(
                        day = date.dayOfWeek.name,
                        category = category,
                        description = mealName,
                        timeTaken = timeTaken,
                        difficulty = difficulty,
                        servings = servings,
                        healthiness = "Healthy", // Default value
                        date = date.toString(),
                        vegetarian = mealType == "Vegetarian"
                    )
                    onMealSaved()
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Set the height to 50dp
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007370) // Background color
            )
        ) {
            Text(
                text = "Save Meal",
                color = Color.White // Set the text color to white
            )
        }



    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StyledDropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5), // Background color
                shape = RoundedCornerShape(10.dp) // Rounded corners
            )
            .clickable { expanded = true }
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Dropdown Text
            Text(
                text = selectedOption,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                fontSize = 12.sp, // Custom font size for the option text
                modifier = Modifier.weight(1f) // Text takes up available space
            )

            // Vertical Divider
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f), // Adjust color and opacity
                modifier = Modifier
                    .height(24.dp) // Divider height
                    .width(1.dp) // Divider width
            )

            // Dropdown Arrow
            Icon(
                painter = painterResource(Res.drawable.dropdown), // Replace with dropdown arrow resource
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                ) {
                    Text(text = option, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}



@Composable
fun DifficultyAndTimeTakenRow(
    difficulty: String,
    timeTaken: String,
    onDifficultySelected: (String) -> Unit,
    onTimeTakenSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StyledDropdownMenuField(
            label = "Difficulty",
            options = listOf("Easy", "Medium", "Hard"),
            selectedOption = difficulty,
            onOptionSelected = onDifficultySelected,
            modifier = Modifier.weight(1f),

        )
        StyledDropdownMenuField(
            label = "Time Taken",
            options = listOf("5 minutes", "10 minutes", "30 minutes", "60 minutes"),
            selectedOption = timeTaken,
            onOptionSelected = onTimeTakenSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    icon: @Composable (() -> Unit)? = null // Optional icon composable
) {
    Button(
        onClick = onSelected,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color(0xFF007370) else Color(0xFFF5F5F5), // Selected and unselected background colors
            contentColor = if (isSelected) Color.White else MaterialTheme.colors.onSurface // Selected and unselected text colors
        ),
        shape = RoundedCornerShape(10.dp), // Rounded corners
        elevation = ButtonDefaults.elevation(0.dp), // Flat button with no elevation
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .height(46.dp)
            .width(150.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize() // Ensure content fills the button space
        ) {
            if (icon != null) {
                // Icon is displayed first
                icon()
                Spacer(modifier = Modifier.width(6.dp)) // Add spacing between icon and text
            }
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 12.sp, // Set the font size to 12sp
                    color = if (isSelected) Color.White else MaterialTheme.colors.onSurface // Consistent text color with button
                ),
                modifier = Modifier.padding(start = if (icon != null) 4.dp else 0.dp) // Add left padding if icon exists
            )
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealTypeToggleButtons(mealType: String, onMealTypeChange: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ToggleButton(
            text = "Vegetarian",
            isSelected = mealType == "Vegetarian",
            onSelected = { onMealTypeChange("Vegetarian") },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.veg), // Replace with dropdown arrow resource
                    contentDescription = "Vegetarian Icon",
//                    tint = if (mealType == "Vegetarian") Color.White else Color(0xFF007370)
                )
            }
        )
        ToggleButton(
            text = "Non-Vegetarian",
            isSelected = mealType == "Non-Vegetarian",
            onSelected = { onMealTypeChange("Non-Vegetarian") },
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.non), // Replace with dropdown arrow resource
                    contentDescription = "Non-Vegetarian Icon",
//                    tint = if (mealType == "Non-Vegetarian") Color.White else Color(0xFF666666)
                )
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealServingsRow(
    servings: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5), // Background color
                shape = RoundedCornerShape(10.dp) // Rounded corners
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Meal Servings",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.weight(1f), // Takes up available space
                    fontSize = 12.sp // Custom font size for the option text

        )

        // Decrement Button
        IconButton(
            onClick = { if (servings > 1) onDecrement() },
            modifier = Modifier
                .size(36.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.minus),
                contentDescription = "Decrease servings",
                tint = Color.Black // Icon color
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Serving Count
        Text(
            text = servings.toString(),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Increment Button
        IconButton(
            onClick = onIncrement,
            modifier = Modifier
                .size(36.dp)

        ) {
            Icon(
                painter = painterResource(Res.drawable.add),
                contentDescription = "Increase servings",
                tint = Color.Black // Icon color
            )
        }
    }
}





