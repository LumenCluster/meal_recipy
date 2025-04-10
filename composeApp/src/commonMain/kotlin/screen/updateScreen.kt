@file:OptIn(ExperimentalResourceApi::class)

package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.add
import room_cmp.composeapp.generated.resources.dropdown
import room_cmp.composeapp.generated.resources.minus
import room_cmp.composeapp.generated.resources.non
import room_cmp.composeapp.generated.resources.onback
import room_cmp.composeapp.generated.resources.veg
import ui.home.MealPlanViewModel

@Composable
fun updateMealScreen(
    viewModel: MealPlanViewModel,
    category: String,
    date: LocalDate,
    mealId: Int, // Add mealId as a parameter
    onMealUpdated: () -> Unit,
    onBackPress: () -> Unit
// Add onMealUpdated as a callback parameter
) {
    var mealName by rememberSaveable { mutableStateOf("") }
    var timeTaken by rememberSaveable { mutableStateOf(0) }
    var difficulty by rememberSaveable { mutableStateOf("Difficulty") }
    var servings by rememberSaveable { mutableStateOf(1) }
    var mealType by rememberSaveable { mutableStateOf("Vegetarian") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

            .padding(16.dp)
    ) {
        // Header Section
        Icon(
            painter = painterResource(Res.drawable.onback),
            contentDescription = "About Icon",

            tint = Color.Black,
            modifier = Modifier
//                    .padding(8.dp) // <-- Add your desired padding here
                .clickable { onBackPress() }
        )
        Spacer(modifier = Modifier.height(30.dp))


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
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            TextField(
                value = mealName,
                onValueChange = { mealName = it },
                placeholder = { Text("Add Meal Name Please...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 13.sp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        DifficultyAndTimeTaken(
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
        MealServings(
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
        ) {
            ToggleBtn(
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
            ToggleBtn(
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

        Spacer(modifier = Modifier.height(32.dp))

        // Save Meal Button
        Button(
            onClick = {
                viewModel.updateMealPlan(
                    id = mealId,
                    day = date.dayOfWeek.name,
                    category = category,
                    description = mealName,
                    timeTaken = timeTaken,
                    difficulty = difficulty,
                    healthiness = "", // Optional: Add healthiness if needed
                    servings = servings,
                    date = date.toString(),
                    vegetarian = (mealType == "Vegetarian")
                )
                onMealUpdated() // Trigger the callback after updating the database
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), // Set the height to 50dp
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007370) // Background color
            )
        ) {
            Text(
                text = "Update",
                color = Color.White // Set the text color to white
            )
        }
    }
}


@Composable
fun StyledDropdownField(
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
fun DifficultyAndTimeTaken(
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
        StyledDropdownField(
            label = "Difficulty",
            options = listOf("Easy", "Medium", "Hard"),
            selectedOption = difficulty,
            onOptionSelected = onDifficultySelected,
            modifier = Modifier.weight(1f),

            )
        StyledDropdownField(
            label = "Time Taken",
            options = listOf("5 minutes", "10 minutes", "30 minutes", "60 minutes"),
            selectedOption = timeTaken,
            onOptionSelected = onTimeTakenSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ToggleBtn(
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

@Composable
fun MealServings(
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