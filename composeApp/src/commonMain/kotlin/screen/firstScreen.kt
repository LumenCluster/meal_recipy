package screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.example.compose.data.network.models.Meal
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import database.entity.Profile
import kotlinx.coroutines.launch
import org.example.compose.home.HomeViewModel
import org.example.compose.home.ProfileViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.abt
import room_cmp.composeapp.generated.resources.add_meal_0
import room_cmp.composeapp.generated.resources.breakfast
import room_cmp.composeapp.generated.resources.dinner
import room_cmp.composeapp.generated.resources.lunch
import room_cmp.composeapp.generated.resources.moree
import room_cmp.composeapp.generated.resources.ratee
import room_cmp.composeapp.generated.resources.shre
import room_cmp.composeapp.generated.resources.view
import room_cmp.composeapp.generated.resources.weekly

@Composable
fun FirstScreen(
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    navigateToDetail: (String) -> Unit,
    navController: NavController,
    onScreenChanged: (String) -> Unit

) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    LaunchedEffect(drawerState.isOpen) {
        onScreenChanged(if (drawerState.isOpen) "DrawerOpen" else "FirstScreen")
    }
    val profileViewModel = remember { ProfileViewModel(Graph.repo) }
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = { route -> navController.navigate(route) }, // Navigate on click
                drawerState = drawerState,
                profileViewModel = profileViewModel,
                navController = navController // Pass navController to DrawerContent
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    backgroundColor = Color.White,
                    elevation = 0.dp
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    val userProfile by remember { profileViewModel.userProfile }

                    if (userProfile != null) {
                        HeaderSection(
                            profile = userProfile!!,
                            onProfileClick = {
                                navController.navigate("profile") // replace "profile" with your actual route
                            }
                        )
                    }


                    Spacer(modifier = Modifier.height(30.dp))

                    SectionHeader(title = "Today's Meal", actionText = "Track Meal >",  onActionClick = {
                        navController.navigate("new")
                    })
                    MealCardsRow(navController)

                    Spacer(modifier = Modifier.height(16.dp))

                    SectionHeader(title = "Weekly Meal", actionText = "View Plan >",  onActionClick = {
                        navController.navigate("new")
                    })
                    WeeklyCalendar(navController)

                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(
//                        text = "Food Recipes For You",
//                        style = MaterialTheme.typography.h6,
//                        color = Color.Black
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))

                    FoodScreen(
                        modifier = Modifier.weight(1f),
                        homeViewModel = homeViewModel,
                        onMealClick = navigateToDetail
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HeaderSection(
    profile: Profile,
    onProfileClick: () -> Unit // Lambda for handling click event
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Meal Planner App",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome, ${profile.name}!",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Medium,
            )
        }

        Image(
            painter = painterResource(getDrawableResource(profile.profileImg)),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .clickable { onProfileClick() } // Navigate on click
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DrawerContent(
    onItemClick: (String) -> Unit,
    drawerState: DrawerState,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val userProfile by remember { profileViewModel.userProfile }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(17.dp))

        // Profile Section
        if (userProfile != null) {
            ProfileSection(userProfile!!, navController)
        }

        Divider(color = Color(0xFFE1E1E1), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

        Spacer(modifier = Modifier.height(17.dp))

        // Menu Items
        DrawerMenuItem(icon = Res.drawable.weekly, title = "Your Weekly Planner") {
            navController.navigate("new")
        }
        DrawerMenuItem(icon = Res.drawable.add_meal_0, title = "Add Meal Plan") {
            navController.navigate("new")
        }
        DrawerMenuItem(icon = Res.drawable.view, title = "View Recipes") {
            navController.navigate("view")
        }

        Divider(color = Color(0xFFE1E1E1), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

        DrawerMenuItem(icon = Res.drawable.abt, title = "About App") {
            navController.navigate("about") // Navigate to profile screen
        }

        DrawerMenuItem(icon = Res.drawable.shre, title = "Share App") {
//            profileViewModel.deleteAllProfiles()
        }

        DrawerMenuItem(icon = Res.drawable.ratee, title = "Rate Our App") {
        }

        DrawerMenuItem(icon = Res.drawable.moree, title = "More Apps") {
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DrawerMenuItem(icon: DrawableResource, title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileSection(profile: Profile, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.weight(1f)) { // This pushes the image to the right
            Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Text(
                text = "Go To Profile >>",
                fontSize = 14.sp,
                color = Color(0xFF038A86),
                modifier = Modifier.clickable {
                    navController.navigate("profile") // Navigate to profile screen
                },
                        fontWeight = FontWeight.SemiBold
            )
        }

        Image(
            painter = painterResource(getDrawableResource(profile.profileImg)),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
    }
}


fun onNavigateToProfile(navController: NavController) {
    navController.navigate("profile")
}

@Composable
fun FoodScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onMealClick: (String) -> Unit
) {
    val homeState by homeViewModel.homeState.collectAsState()
    val showAll = remember { mutableStateOf(false) }

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Food Recipes For You",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )

            Button(
                onClick = { showAll.value = !showAll.value },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                modifier = Modifier.height(32.dp) // You can tweak this height as needed
            ) {
                Text(
                    text = if (showAll.value) "SHOW LESS" else "VIEW ALL",
                    color = Color.White,
                    fontSize = 12.sp // Smaller text
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        when {
            homeState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            homeState.error != null -> Text(homeState.error.orEmpty(), color = Color(0xFFFF5959))
            else -> {
                val mealsToDisplay = if (showAll.value) homeState.meals else homeState.meals.take(5)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp), // Ensure consistent spacing
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(mealsToDisplay) { _, meal ->
                        FoodItem(
                            meal = meal,
                            onMealClick = { onMealClick(meal.idMeal) },
                            modifier = Modifier.fillMaxWidth() // Ensure uniform size
                        )
                    }
                }


            }
        }
    }
}

@Composable
fun FoodItem(meal: Meal, onMealClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp) // Ensure uniform height
            .clickable { onMealClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Set a fixed height for the image
            ) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "See Recipe >>",
                    color = Color(0xFF1E88E5),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Composable
fun SectionHeader(title: String, actionText: String,     onActionClick: () -> Unit // <-- Add this line
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = actionText,
            style = MaterialTheme.typography.body2.copy(color = Color(0xFF007370)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}



@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealCardsRow(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        MealCard("BREAKFAST",
            Res.drawable.breakfast
        ) {
            val currentDate = kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            navController.navigate("breakfast/$currentDate") // Correct format YYYY-MM-DD
        }

        MealCard("LUNCH",
            Res.drawable.lunch

        ) {
            val currentDate = kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            navController.navigate("Lunch/$currentDate") // Correct format YYYY-MM-DD
        }
        MealCard("DINNER",
            Res.drawable.dinner
        ) {
            val currentDate = kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            navController.navigate("Dinner/$currentDate") // Correct format YYYY-MM-DD
        }

    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MealCard(mealType: String,
             imageRes: DrawableResource,
             onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(95.dp)
            .height(118.dp)
            .clickable { onClick() }, // Make the card clickable
        elevation = 5.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(95.dp)
                    .height(87.dp)
            )
            Text(
                text = mealType,
                style = MaterialTheme.typography.body2,
                color = Color(0xFF4D4D4D),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
@Composable
fun WeeklyCalendar(navController: NavController) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val currentWeek = (0..6).map { today.plus(it, DateTimeUnit.DAY) } // Start from today

        currentWeek.forEach { selectedDay ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selectedDay == selectedDate) Color.Gray else Color(0xFFDBEBEB))
                    .clickable {
                        selectedDate = selectedDay
                        navController.navigate("category/${selectedDay}")
                    }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = selectedDay.dayOfWeek.name.take(3), // First 3 letters of the day
                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
                        color = Color(0xFF436C6B)
                    )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(if (selectedDay == today) Color.White else Color.White)
                    ) {
                        Text(text = selectedDay.dayOfMonth.toString(), color = Color.Black)
                    }
                }
            }
        }
    }
}


//@Composable
//fun WeeklyCalendar(navController: NavController) {
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
//        val startOfWeek = today.minus(today.dayOfWeek.ordinal, DateTimeUnit.DAY)
//        val currentWeek = (0..6).map { startOfWeek.plus(it, DateTimeUnit.DAY) }
//
//        currentWeek.forEach { date ->
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(20.dp))
//                    .background(Color(0xFFDBEBEB))
//                    .clickable {
//                    }
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.padding(5.dp)
//                ) {
//                    Text(
//                        text = date.dayOfWeek.name.take(3),
//                        style = MaterialTheme.typography.body2.copy(fontSize = 10.sp),
//                        color = Color(0xFF436C6B)
//                    )
//
//                    Box(
//                        contentAlignment = Alignment.Center,
//                        modifier = Modifier
//                            .size(30.dp)
//                            .clip(CircleShape)
//                            .background(if (date == today) Color.White else Color.White)
//                    ) {
//                        Text(text = date.dayOfMonth.toString(), color = Color.Black)
//                    }
//                }
//            }
//        }
//    }
//}


