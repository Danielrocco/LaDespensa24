package com.example.ladespensa24.userScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ladespensa24.AppFooter
import com.example.ladespensa24.R
import com.example.ladespensa24.models.User
import com.example.ladespensa24.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserScreen(navController: NavController, viewModel: MyViewModel) {
    BackHandler(enabled = true) {
        navController.navigate("homeScreen") {
            popUpTo("homeScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    val isLogged by viewModel.isLogged
    val user = viewModel.getUsuarioEnUso()

    Scaffold(
        content = { innerPadding ->
            ProfileContent(innerPadding, navController, user, viewModel)
        },
        bottomBar = {
            AppFooter(
                Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth(), navController, viewModel, isLogged
            )
        }
    )
}

@Composable
fun ProfileContent(
    innerPadding: PaddingValues,
    navController: NavController,
    user: User,
    viewModel: MyViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                HeaderProfileContent(user)
                Spacer(Modifier.size(18.dp))
                OptionCard(
                    "Datos Usuario",
                    navController,
                    Color(0xFFDEDEDE),
                    Color.Black,
                    "userInfoScreen",
                    Icons.Default.Person
                )
                Spacer(Modifier.size(18.dp))
                OptionCard(
                    "Mis Pedidos",
                    navController,
                    Color(0xFFDEDEDE),
                    Color.Black,
                    "purchasesScreen",
                    Icons.Default.ShoppingCart
                )
            }
            Column {
                LogOut("Cerrar Sesión", navController, Color(0xFFFF6A6A), Color.White, viewModel)
                Spacer(Modifier.size(78.dp))
            }
        }
    }
}

@Composable
fun HeaderProfileContent(user: User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color(0xFF3D3D3D))
    ) {
        Image(
            painter = painterResource(id = R.drawable.personafeliz),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .clip(CircleShape),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ("Hola " + user.name).uppercase(),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Entrega en " + user.address,
                color = Color(0xFFEEEEEE),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun OptionCard(
    titulo: String,
    navController: NavController,
    backgroundColor: Color,
    textColor: Color,
    route: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)
            .height(45.dp),
        onClick = {
            navController.navigate(route) {
                launchSingleTop = true
            }
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(24.dp)
                )
                Text(
                    text = titulo,
                    fontFamily = FontFamily(Font(R.font.muli)),
                    color = textColor,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun LogOut(
    titulo: String,
    navController: NavController,
    backgroundColor: Color,
    textColor: Color,
    viewModel: MyViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(12.dp),
        onClick = { viewModel.logout(navController, viewModel) },
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
        ) {
            Text(
                text = titulo,
                fontFamily = FontFamily(Font(R.font.muli)),
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}