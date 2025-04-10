package screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import org.example.compose.data.network.models.Meal
import org.example.compose.home.HomeViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import registerBackHandler
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.back

@OptIn(ExperimentalMaterialApi::class, ExperimentalResourceApi::class)
@Composable
fun ViewRecipy(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    onMealClick: (Meal) -> Unit,
    onBackClick: () -> Unit
) {
    val homeState by homeViewModel.homeState.collectAsState()
    var selectedCategory by remember { mutableStateOf("Chinese") }
    var showPopup by remember { mutableStateOf(false) }
    val categories = listOf("American", "Canadian", "Chinese", "French", "Indian", "Mexican", "Thai", "Turkish")

    var iconPosition by remember { mutableStateOf(0f) }

    LaunchedEffect(selectedCategory) {
        homeViewModel.fetchMeals(selectedCategory)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Res.drawable.back),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "All Recipes",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("COUNTRY", color = Color(0xFF7D7D7D)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow",
                            modifier = Modifier
                                .clickable { showPopup = true }
                                .onGloballyPositioned { coordinates ->
                                    iconPosition = coordinates.size.height.toFloat()
                                }
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        backgroundColor = Color.Transparent
                    )
                )

                if (showPopup) {
                    Popup(
                        alignment = Alignment.TopEnd,
                        onDismissRequest = { showPopup = false },
                        offset = IntOffset(0, iconPosition.toInt() + 10) // Adjust to position below the arrow
                    ) {
                        Card(
                            modifier = Modifier
                                .width(250.dp)
                                .border(0.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
                            elevation = 8.dp,
                            backgroundColor = Color(0xFFF0F0F0) // ✅ Set background to #F0F0F0
                        ) {
                            Column {
                                categories.forEach { category ->
                                    Text(
                                        text = category,
                                        fontSize = 14.sp,
                                        color = Color.Black, // Ensures text visibility on light background
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedCategory = category
                                                showPopup = false
                                            }
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                    )
                                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when {
            homeState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            homeState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Network issue, please check your internet connection.",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(homeState.meals) { meal ->
                        FoodItems(meal = meal, onMealClick = { onMealClick(meal) })
                    }
                }
            }
        }
    }
}








@Composable
fun FoodItems(
    meal: Meal,
    onMealClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp, horizontal = 5.dp)
            .clickable { onMealClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = meal.strMealThumb,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = meal.strMeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "See Recipe >>",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF038A86),
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}
