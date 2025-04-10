package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.img
import room_cmp.composeapp.generated.resources.img0

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun OnboardingScreen(onNext: () -> Unit,
                     navController: NavController,

                     ) {
    // List of onboarding pages


    val pages = listOf(
        OnboardingPage(
            title = "Meal Plan App",
            description = "Plan Your WeekEnd Meal",
            imageRes = Res.drawable.img,
            desc2 = "Track your meals and stay on top of your nutrition with ease."
        ),
        OnboardingPage(
            title = "Meal Plan App",
            description = "Get Free Recipy",
            imageRes = Res.drawable.img0,
            desc2 = "Discover, save, and share delicious recipes for every occasion."
        )
    )

    // Pager state to handle page scrolling
    val pagerState = rememberPagerState(pageCount = { pages.size })

    // State for tracking button clicks
    var shouldAnimateToNextPage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),

    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Skip Button
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "SKIP",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.clickable {
                    navController.navigate("signup")

                }
            )
        }
        // HorizontalPager to show the onboarding pages
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            OnboardingPageContent(pages[page])
        }

        // Dots Indicator positioned below the image
        DotsIndicator(
            totalDots = pages.size,
            selectedIndex = pagerState.currentPage // Dynamic index based on pager state
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate to the next page or finish onboarding
        Button(
            onClick = {
                shouldAnimateToNextPage = true
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(bottom = 100.dp)
                .width(160.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF007370)
            )
        ) {
            Text(
                text = if (pagerState.currentPage == pages.size - 1) "GET STARTED" else "NEXT",
                color = Color.White
            )
        }
    }

    // LaunchedEffect to animate page change when button is clicked
    LaunchedEffect(shouldAnimateToNextPage) {
        if (shouldAnimateToNextPage) {
            if (pagerState.currentPage < pages.size - 1) {
                // Animate to the next page
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                // End onboarding and proceed to the main screen
                onNext()
            }
            shouldAnimateToNextPage = false
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize() // Ensure the column takes up all available space
            .padding(horizontal = 24.dp) // Horizontal padding
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = page.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description of the page
        Text(
            text = page.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Image of the page (updated)
        Image(
            painter = painterResource(page.imageRes),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxWidth()  // Set width to match parent (screen width)
                .height(250.dp)  // Set the height to 250dp (adjust as needed)
                .clip(RoundedCornerShape(10.dp)), // Optional: to round the image corners
            contentScale = ContentScale.Crop // Ensure the image scales correctly
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Additional description text (desc2)
        Text(
            text = page.desc2,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == selectedIndex) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == selectedIndex) Color(0xFF007370) // Selected dot color
                        else Color.Gray // Unselected dot color (light gray)
                    )
            )
        }
    }
}

data class OnboardingPage @OptIn(ExperimentalResourceApi::class) constructor(
    val title: String,
    val description: String,
    val desc2: String,
    val imageRes: DrawableResource,
)





//@Composable
//fun OnboardingScreen1(onNext: () -> Unit) {
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome to Onboarding 1", fontSize = 24.sp)
//        Button(onClick = onNext) {
//            Text("Next")
//        }
//    }
//}
//
//@Composable
//fun OnboardingScreen2(onFinish: () -> Unit) {
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Welcome to Onboarding 2", fontSize = 24.sp)
//        Button(onClick = onFinish) {
//            Text("Finish")
//        }
//    }
//}
