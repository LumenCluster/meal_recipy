package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
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
import room_cmp.composeapp.generated.resources.milkee
import room_cmp.composeapp.generated.resources.potato
import room_cmp.composeapp.generated.resources.shrims
import room_cmp.composeapp.generated.resources.veges
import room_cmp.composeapp.generated.resources.vegetarian
import room_cmp.composeapp.generated.resources.yiugurt


@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel, onBackPress: () -> Unit //
) {
    val userProfile by remember { profileViewModel.userProfile }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.back), // Replace with your back icon
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier.clickable { onBackPress() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (userProfile != null) {
            ProfileHeader(userProfile!!, onNext = {
                navController.navigate("signup")
            })

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileInfoSection(userProfile!!)
                MaterialsSection(userProfile!!)
                PreferenceSection(userProfile!!)
                WeeklyMealsSection(navController)
            }
        } else {
            CircularProgressIndicator()
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileHeader(profile: Profile, onNext: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
//        val drawableResource = getDrawableResource(profile.profileImg)

        Image(
            painter = painterResource(getDrawableResource(profile.profileImg)),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = profile.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF007370)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Text(text = "Edit Profile", color = Color.White, fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun getDrawableResource(imageName: String): DrawableResource {
    return when (imageName) {
        "ava1" -> Res.drawable.ava1
        "ava2" -> Res.drawable.ava2
        "ava3" -> Res.drawable.ava3
        "ava4" -> Res.drawable.ava4
        "ava5" -> Res.drawable.ava5
        "ava6" -> Res.drawable.ava6
        "ava7" -> Res.drawable.ava7
        "ava8" -> Res.drawable.ava8
        "ava9" -> Res.drawable.ava9
        "ava10" -> Res.drawable.ava10
        "ava11" -> Res.drawable.ava11
        "ava12" -> Res.drawable.ava12
        else -> Res.drawable.egg // Default
    }
}

@Composable
fun ProfileInfoSection(profile: Profile) {
    Section(title = "Your Profile Information", details = listOf(profile.name, "${profile.age} Years"))
}


@Composable
fun MaterialsSection(profile: Profile) {
    MaterialsRow(title = "Materials You Like Most", foodImgNames = profile.foodImg)
}

@Composable
fun MaterialsRow(title: String, foodImgNames: String) {
    val foodNamesList = foodImgNames.split(",") // Split names into a list

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Title
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp) // Space below title
        )

        // Horizontal scrollable row for food items
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F5F7),
                    shape = RoundedCornerShape(12.dp) // Rounded corners for the card
                ) // Background for the entire section
                .padding(16.dp) // Padding for the section

                .horizontalScroll(rememberScrollState()), // Enable horizontal scrolling
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Spacing between items
        ) {
            foodNamesList.forEach { foodName ->
                FoodItemCard(foodName = foodName)
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FoodItemCard(foodName: String) {
    Box(
        modifier = Modifier
            .size(80.dp) // Size of each card
            .background(
                color = Color(0xFFE3F0EF), // Background color of the card
                shape = RoundedCornerShape(12.dp) // Rounded corners for the card
            )
            .border(
                width = 2.dp,
                color = Color(0xFF007300), // Stroke color for the card
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center // Align content in the center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Food image
            Image(
                painter = painterResource(getFoodDrawableResource(foodName)),
                contentDescription = foodName,
                modifier = Modifier.size(40.dp) // Adjusted image size
            )
            Spacer(modifier = Modifier.height(4.dp)) // Space between image and text
            // Food name
            Text(
                text = foodName.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun getFoodDrawableResource(foodName: String): DrawableResource {
    return when (foodName) {
        "Chicken" -> Res.drawable.chick
        "Red Meat" -> Res.drawable.meat
        "Eggs" -> Res.drawable.egg
        "Milk" -> Res.drawable.milkee
        "Bread" -> Res.drawable.bread
        "Vegies" -> Res.drawable.veges
        "Corn" -> Res.drawable.corn
        "Sweet Potato" -> Res.drawable.potato
        "Yogurt" -> Res.drawable.yiugurt
        "Cheese" -> Res.drawable.cheese0
        "Shrimps" -> Res.drawable.shrims
        "Fish" -> Res.drawable.fish
        else -> Res.drawable.vegetarian // Default fallback image
    }
}


@Composable
fun WeeklyMealsSection(navController: NavController) {
    SectionWithIcon( navController = navController)
}
//
@Composable
fun PreferenceSection(profile: Profile) {
    Section(
        title = "Your Preference",
        details = listOf(profile.foodPreference)
    )
}

@Composable
fun Section(title: String, details: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section title
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Iterate over details
        details.forEach { detail ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF4F5F7))
                    .padding(horizontal = 12.dp), // Padding for spacing on the left and right
                contentAlignment = Alignment.CenterStart // Center content vertically and align it to the start horizontally
            ) {
                val parts = detail.split(":") // Split text at the colon
                if (parts.size > 1) {
                    Column(
                        verticalArrangement = Arrangement.Center, // Center text vertically within Column
                        horizontalAlignment = Alignment.Start // Align text horizontally at the start
                    ) {
                        // Text before the colon
                        Text(
                            text = parts[0].trim(),
                            fontSize = 12.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        // Text after the colon
                        Text(
                            text = parts[1].trim(),
                            fontSize = 11.sp,
                            color = Color.Black,
                            lineHeight = 16.sp
                        )
                    }
                } else {
                    // Display as a single line if no colon
                    Text(
                        text = detail,
                        fontSize = 13.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterStart) // Align text vertically center and horizontally start
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}



@Composable
fun SectionWithIcon(
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF4F5F7))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Weekly Meals",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f)) // Push icon to the end
                IconButton(onClick = {
                    navController.navigate("new")
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Expand",
                        tint = Color.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}




