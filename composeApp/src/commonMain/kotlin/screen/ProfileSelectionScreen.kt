package screen


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import database.entity.Profile
import org.example.compose.home.ProfileViewModel
import org.jetbrains.compose.resources.DrawableResource
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
import room_cmp.composeapp.generated.resources.check0
import room_cmp.composeapp.generated.resources.cheese0
import room_cmp.composeapp.generated.resources.chick
import room_cmp.composeapp.generated.resources.corn
import room_cmp.composeapp.generated.resources.egg
import room_cmp.composeapp.generated.resources.fish
import room_cmp.composeapp.generated.resources.meat
import room_cmp.composeapp.generated.resources.mik
import room_cmp.composeapp.generated.resources.potato
import room_cmp.composeapp.generated.resources.shrims
import room_cmp.composeapp.generated.resources.veges
import room_cmp.composeapp.generated.resources.vegetarian
import room_cmp.composeapp.generated.resources.yiugurt

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileSelectionScreen(
    viewModel: ProfileViewModel,
    name: String,
    age: String,
    onNext: (String, String, String) -> Unit
) {
    var selectedAvatar by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
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
//            Text(
//                text = "SKIP",
//                fontSize = 14.sp,
//                color = Color.Gray,
//                modifier = Modifier.clickable { onNext(name, age, "") }
//            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        StepperIndicator(currentStep = 1, totalSteps = 3)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Choose An Avatar\nFor Your Profile!",
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(30.dp))

        val avatars = listOf(
            Res.drawable.ava1, Res.drawable.ava2, Res.drawable.ava3,
            Res.drawable.ava4, Res.drawable.ava5, Res.drawable.ava6,
            Res.drawable.ava7, Res.drawable.ava8, Res.drawable.ava9,
            Res.drawable.ava10, Res.drawable.ava11, Res.drawable.ava12
        )

        AvatarGrid(avatars, selectedAvatar) { selectedAvatar = it }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {Button(
            onClick = {
                if (selectedAvatar != null) {
                    val selectedImageName = when (avatars[selectedAvatar!!]) {
                        Res.drawable.ava1 -> "ava1"
                        Res.drawable.ava2 -> "ava2"
                        Res.drawable.ava3 -> "ava3"
                        Res.drawable.ava4 -> "ava4"
                        Res.drawable.ava5 -> "ava5"
                        Res.drawable.ava6 -> "ava6"
                        Res.drawable.ava7 -> "ava7"
                        Res.drawable.ava8 -> "ava8"
                        Res.drawable.ava9 -> "ava9"
                        Res.drawable.ava10 -> "ava10"
                        Res.drawable.ava11 -> "ava11"
                        Res.drawable.ava12 -> "ava12"
                        else -> "ava1" // Default fallback
                    }
                    onNext(name, age, selectedImageName) // Store only the image name in the database
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(170.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedAvatar != null) Color(0xFF007370) else Color.Gray
            ),
            enabled = selectedAvatar != null
        ) {
            Text(text = "NEXT", color = Color.White)
        }

        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}


// Stepper Indicator
@Composable
fun StepperIndicator(currentStep: Int, totalSteps: Int) {
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

// Avatar Grid
@OptIn(ExperimentalResourceApi::class)
@Composable
fun AvatarGrid(avatars: List<DrawableResource>, selectedAvatar: Int?, onSelect: (Int) -> Unit) {
    Column {
        repeat(4) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (colIndex in 0 until 3) {
                    val index = rowIndex * 3 + colIndex
                    if (index < avatars.size) {
                        AvatarItem(avatars[index], selectedAvatar == index) { onSelect(index) }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// Avatar Item
@OptIn(ExperimentalResourceApi::class)
@Composable
fun AvatarItem(imageRes: DrawableResource, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(92.dp) // Ensure enough space for selection indicator
            .clickable { onClick() },
        contentAlignment = Alignment.Center // Centers the avatar inside
    ) {
        // Avatar Image with Selection Border
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) Color(0xFF007370) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center // Ensures the image is centered
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(80.dp) // Fits well inside the circle
                    .clip(CircleShape)
            )
        }

        // **Selection Checkmark - Centered in Badge**
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp) // The checkmark container size
                    .clip(CircleShape)
                    .background(Color(0xFF007370)) // Green background
                    .border(2.dp, Color.White, CircleShape) // White border for better visibility
                    .align(Alignment.TopEnd) // Positions at the top-right corner
                    .padding(2.dp), // Slight padding for centering
                contentAlignment = Alignment.Center // Ensures checkmark is perfectly centered
            ) {
                Image(
                    painter = painterResource(Res.drawable.check0),
                    contentDescription = "Selected",
                    modifier = Modifier
                        .size(14.dp) // Adjust the checkmark size for better alignment
                )
            }
        }
    }
}






//viewModel.insert(Profile(name = name, age = age.toInt(), profileImg = selectedImage, foodImg = "", foodPreference = ""))

