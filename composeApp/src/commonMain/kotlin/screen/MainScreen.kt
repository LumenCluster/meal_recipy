@file:OptIn(ExperimentalResourceApi::class)

package screen

import RecipyNav
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import navigation.AppNavigation
import navigation.HomeNavigation
import org.example.compose.home.HomeViewModel
import org.example.compose.home.ProfileViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ui.home.MealPlanViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.add_meal
import room_cmp.composeapp.generated.resources.home
import room_cmp.composeapp.generated.resources.meal_add
import room_cmp.composeapp.generated.resources.recipe
import viewModel.ProfileViewModelFactory

enum class Tabs @OptIn(ExperimentalResourceApi::class) constructor(val text: String, val iconRes: DrawableResource) {
    @OptIn(ExperimentalResourceApi::class)
    Home("Home", Res.drawable.home),
    @OptIn(ExperimentalResourceApi::class)
    Add("Add", Res.drawable.add_meal),
    Planner("Recipe", Res.drawable.recipe),
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    mealViewModel: MealPlanViewModel = viewModel(),

) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { Tabs.entries.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

    var currentScreen by remember { mutableStateOf("FirstScreen") }
    var recipyScreen by remember { mutableStateOf("Recipy") }
    var addScreen by remember { mutableStateOf("AddScreen") } // Track Add tab navigation

    Scaffold(topBar = {}) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)                        .background(Color.White)
            .background(Color.White)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (Tabs.entries[page]) {
                    Tabs.Home -> {
                        AppNavigation(
                            homeViewModel = homeViewModel,
                            onScreenChanged = { screenName -> currentScreen = screenName },
                            onBackToHome = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(Tabs.Home.ordinal)
                                }
                            }
                        )
                    }
                    Tabs.Planner -> {
                        RecipyNav(
                            onScreenChanged = { screenName -> recipyScreen = screenName },
                            onBackToHome = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(Tabs.Home.ordinal)
                                }
                            }
                        )
                    }
                    Tabs.Add -> {
                        HomeNavigation(
                            viewModel = mealViewModel,
                            onScreenChanged = { screenName -> addScreen = screenName },
                            onBackToHome = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(Tabs.Home.ordinal)
                                }
                            }
                        )
                    }
                    else -> GenericTabContent(tab = Tabs.entries[page])
                }
            }

            val shouldShowBottomBar =
                (selectedTabIndex == Tabs.Home.ordinal && currentScreen == "FirstScreen") ||
                        (selectedTabIndex == Tabs.Planner.ordinal && recipyScreen == "Recipy") ||
                        (selectedTabIndex == Tabs.Add.ordinal && addScreen == "HomeScreen")

            if (shouldShowBottomBar && currentScreen != "DrawerOpen") {
                BottomNavigationBar(pagerState, coroutineScope, selectedTabIndex)
            }
        }
    }
}

@Composable
fun TabItem(tab: Tabs, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(
                color = if (isSelected) Color(0xFF13A39F) else Color(0xFFC9C9C9),
            )
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(tab.iconRes),
            contentDescription = tab.text,
            tint = Color.White,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
        Text(
            text = tab.text,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomNavigationBar(pagerState: PagerState, coroutineScope: CoroutineScope, selectedTabIndex: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 7.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home Tab at the Start
            TabItem(Tabs.Home, selectedTabIndex == Tabs.Home.ordinal) {
                coroutineScope.launch { pagerState.animateScrollToPage(Tabs.Home.ordinal) }
            }

            // Custom FloatingActionButton with Gradient Background
            Box(
                modifier = Modifier
                    .size(64.dp) // Standard FAB size
                    .offset(y = (-30).dp) // Raise FAB halfway above TabRow
                    .clip(CircleShape) // Ensure circular shape
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF32F2ED), Color(0xFF007370))
                        )
                    )
                    .clickable {
                        coroutineScope.launch { pagerState.animateScrollToPage(Tabs.Add.ordinal) }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.meal_add),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Planner Tab at the End
            TabItem(Tabs.Planner, selectedTabIndex == Tabs.Planner.ordinal) {
                coroutineScope.launch { pagerState.animateScrollToPage(Tabs.Planner.ordinal) }
            }
        }
    }
}

@Composable
fun GenericTabContent(tab: Tabs) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = tab.text, style = MaterialTheme.typography.h4)
    }
}
