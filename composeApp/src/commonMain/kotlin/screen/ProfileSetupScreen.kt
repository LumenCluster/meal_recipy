package screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.ava1
import room_cmp.composeapp.generated.resources.ava10
import room_cmp.composeapp.generated.resources.ava11
import room_cmp.composeapp.generated.resources.ava12
import room_cmp.composeapp.generated.resources.ava2
import room_cmp.composeapp.generated.resources.ava3
import room_cmp.composeapp.generated.resources.ava4
import room_cmp.composeapp.generated.resources.ava5
import room_cmp.composeapp.generated.resources.ava6
import room_cmp.composeapp.generated.resources.ava7
import room_cmp.composeapp.generated.resources.ava8
import room_cmp.composeapp.generated.resources.ava9
import room_cmp.composeapp.generated.resources.back
import room_cmp.composeapp.generated.resources.bread
import room_cmp.composeapp.generated.resources.cheese0
import room_cmp.composeapp.generated.resources.chick
import room_cmp.composeapp.generated.resources.corn
import room_cmp.composeapp.generated.resources.egg
import room_cmp.composeapp.generated.resources.fish
import room_cmp.composeapp.generated.resources.meat
import room_cmp.composeapp.generated.resources.mik
import room_cmp.composeapp.generated.resources.milk
import room_cmp.composeapp.generated.resources.milkee
import room_cmp.composeapp.generated.resources.potato
import room_cmp.composeapp.generated.resources.shrims
import room_cmp.composeapp.generated.resources.veges
import room_cmp.composeapp.generated.resources.vegetarian
import room_cmp.composeapp.generated.resources.yiugurt

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileSetupScreen(
    name: String,
    age: String,
    selectedAvatar: String,
    onNext: (String) -> Unit // Now correctly sending only food names
) {
    val foodItems = listOf(
        "Chicken" to Res.drawable.chick,
        "Red Meat" to Res.drawable.meat,
        "Eggs" to Res.drawable.egg,
        "Milk" to Res.drawable.milkee,
        "Bread" to Res.drawable.bread,
        "Vegies" to Res.drawable.veges,
        "Corn" to Res.drawable.corn,
        "Sweet Potato" to Res.drawable.potato,
        "Yogurt" to Res.drawable.yiugurt,
        "Cheese" to Res.drawable.cheese0,
        "Shrimps" to Res.drawable.shrims,
        "Fish" to Res.drawable.fish
    )

    val selectedItems = remember { mutableStateListOf<String>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize() .background(Color.White)

            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Set Your Profile...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(25.dp))
        StepperIndicator0(currentStep = 2, totalSteps = 3)
        Spacer(modifier = Modifier.height(30.dp))

        Text("Which Material Do You Like The Most", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Text("YOU CAN CHOOSE MORE THAN ONE ANSWER", fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(foodItems) { (foodName, imageRes) ->
                val isSelected = selectedItems.contains(foodName)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = if (isSelected) Color(0xFFE3F0EF) else Color(0xFFF4F5F7),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 4.dp,
                            color = if (isSelected) Color(0xFF007370) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (isSelected) selectedItems.remove(foodName) else selectedItems.add(foodName)
                            errorMessage = null // Reset error when an item is selected
                        }
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = foodName,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = foodName.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (selectedItems.isEmpty()) {
                    errorMessage = "Please select at least one item."
                } else {
                    val selectedFoodNames = selectedItems.joinToString(",")
                    onNext(selectedFoodNames)
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .width(170.dp)
                .height(35.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF007370))
        ) {
            Text(text = "NEXT", color = Color.White)
        }
    }
}


@Composable
fun StepperIndicator0(currentStep: Int, totalSteps: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(
                        if (i <= currentStep) Color(0xFF007370) else Color(0xFFD3E0E0),
                        shape = RoundedCornerShape(50)
                    )
            )
            if (i < totalSteps) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
