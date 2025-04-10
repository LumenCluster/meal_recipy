package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.abut
import room_cmp.composeapp.generated.resources.onback

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AboutAppScreen( onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.onback),
                contentDescription = "About Icon",

                tint = Color.Black,
                modifier = Modifier.clickable { onBackClick() }

            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "About App",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,

                )
        }

        Image(
            painter = painterResource(Res.drawable.abut),
            contentDescription = "Info Icon",
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(50.dp))
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    shape = RoundedCornerShape(16.dp),
                    elevation = 4.dp


                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "A Meal Planner App is a convenient tool designed to help users organize their daily and weekly meals efficiently. This app not only allows users to plan their meals in advance but also provides a collection of delicious and healthy recipes to choose from. With an intuitive interface, users can explore various meal options, save their favorite recipes, and generate shopping lists based on selected meals. Whether someone is looking to maintain a balanced diet, save time on meal preparation, or discover new dishes, the app serves as a perfect companion. By offering personalized meal suggestions, nutritional information, and step-by-step cooking instructions, the Meal Planner App makes healthy eating effortless and enjoyable.",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


