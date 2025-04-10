import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import database.entity.Profile
import kotlinx.coroutines.launch
import org.example.compose.home.ProfileViewModel
import screen.StepperIndicator

@Composable
fun FoodPreferencesScreen(
    name: String,
    age: String,
    selectedAvatar: String,
    foodImg: String,
    viewModel: ProfileViewModel,
    onNext: () -> Unit
) {
    val items = listOf(
        "Flexible" to "Meat, Veg, Seafood Everything!",
        "Vegetarian" to "No Meat Or Seafood!",
        "Non-Vegetarian" to "No Animal Products!",
        "Pescatarian" to "Seafood But No Meat!"
    )

    var selectedIndex by remember { mutableStateOf(-1) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Set Your Profile...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "SKIP",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { onNext() }
            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        StepperIndicator(currentStep = 3, totalSteps = 3)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Food Preferences",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Do you have any food preferences?".uppercase(),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex

            Box(
                modifier = Modifier
                    .fillMaxWidth().height(100.dp)
                    .padding(vertical = 8.dp)
                    .background(
                        color = if (isSelected) Color(0xFFE3F0EF) else Color(0xFFF4F5F7),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Color(0xFF007370) else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        selectedIndex = index
                        showError = false // Hide error message when an option is selected
                    }
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = item.first,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.second,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (showError) {
            Text(
                text = "Please select a food preference!",
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (selectedIndex < 0) {
                    showError = true // Show error message if nothing is selected
                } else {
                    viewModel.viewModelScope.launch {
                        val existingProfile = viewModel.userProfile.value // Get existing profile

                        val selectedPreference = items[selectedIndex]
                        val profile = Profile(
                            id = existingProfile?.id ?: 0, // Use existing ID or default 0 (Room will replace)
                            name = name,
                            age = age.toInt(), // Ensure `age` is an integer
                            profileImg = selectedAvatar.toString(),
                            foodImg = foodImg,
                            foodPreference = "${selectedPreference.first}: ${selectedPreference.second}"
                        )
                        viewModel.upsert(profile) // Upsert profile with existing ID
                        onNext()
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
                .width(170.dp)
                .height(35.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007370)
            )
        ) {
            Text(
                text = "NEXT",
                color = Color.White
            )
        }
    }
}

