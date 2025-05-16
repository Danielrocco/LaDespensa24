package com.example.ladespensa24

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UserScreen(navController: NavController, viewModel: MyViewModel) {
    BackHandler(enabled = true) {
        navController.navigate("mainScreen") {
            // Limpia la pila para evitar volver atrás otra vez a esta pantalla
            popUpTo("mainScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    val isLogged by viewModel.isLogged.observeAsState(false)
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
            verticalArrangement = Arrangement.SpaceBetween // Distribuye los elementos entre el inicio y el final
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
                Spacer(Modifier.size(70.dp)) // Espacio debajo del último elemento
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
            // Nombre alineado a la izquierda
            Text(
                text = ("Hola " + user.getName()).uppercase(),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // toma espacio disponible
            )

            // Dirección alineada a la derecha
            Text(
                text = "Entrega en " + user.getAddress(),
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
                verticalAlignment = Alignment.CenterVertically, // Centra el contenido verticalmente
                modifier = Modifier.fillMaxHeight() // Asegura que el Row ocupe toda la altura
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(24.dp) // Tamaño fijo para el ícono
                )
                Text(
                    text = titulo,
                    fontFamily = FontFamily(Font(R.font.muli)),
                    color = textColor,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 8.dp) // Espaciado adicional entre el ícono y el texto
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
        onClick = { viewModel.logout(navController) },
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