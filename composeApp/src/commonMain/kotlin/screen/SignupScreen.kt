package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.example.compose.home.ProfileViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import room_cmp.composeapp.generated.resources.Res
import room_cmp.composeapp.generated.resources.signup1
import room_cmp.composeapp.generated.resources.user


@OptIn(ExperimentalResourceApi::class)
@Composable
fun SignupScreen(navController: NavController, viewModel: ProfileViewModel, onSignupComplete: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Let's Get You Started!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = "Create An Account",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(Res.drawable.signup1),
            contentDescription = "Signup Image",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Please Enter Your Name",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Name Input
        TextField(
            value = name,
            onValueChange = { name = it },
            leadingIcon = { Icon(painter = painterResource(Res.drawable.user), contentDescription = "User Icon") },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Please Enter Your Age",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(2.dp))

        // Age Input
        TextField(
            value = age,
            onValueChange = { age = it },
            leadingIcon = { Icon(painter = painterResource(Res.drawable.user), contentDescription = "User Icon") },
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (name.isNotBlank() && age.isNotBlank()) {
                    if (name.isNotEmpty() && age.isNotEmpty()) {
                        onSignupComplete(name, age) // Pass input values to callback
                    } else {
                        // Handle empty input (optional)
                    }
//                    // Encode name and age to prevent issues with special characters
//                    val encodedName = encodeForUrl(name)
//                    val encodedAge = encodeForUrl(age)
//
//                    // Navigate to ProfileSelectionScreen with encoded parameters
//                    navController.navigate("profileSelectionScreen?name=$encodedName&age=$encodedAge") {
//                        // Remove "signup" screen from the backstack to prevent going back to it
//                        popUpTo("signup") { inclusive = true }
//                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(bottom = 50.dp)
                .width(170.dp)
                .height(40.dp),
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
fun encodeForUrl(value: String): String {
    return value.replace(" ", "%20")
}
