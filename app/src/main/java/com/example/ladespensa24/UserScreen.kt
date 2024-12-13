package com.example.ladespensa24

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
    Scaffold(
        content = { innerPadding ->
            ProfileContent(innerPadding, navController)
        },
        bottomBar = {
            AppFooter(
                Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth(), navController, viewModel)
        }
    )
}

@Composable
fun ProfileContent(innerPadding: PaddingValues, navController: NavController) {
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
                HeaderProfileContent()
                Spacer(Modifier.size(18.dp))
                OptionCard("Datos Personales", navController, Color(0xFFDEDEDE), Color.Black, "", Icons.Default.Person)
                Spacer(Modifier.size(18.dp))
                OptionCard("Metodos de pago", navController, Color(0xFFDEDEDE), Color.Black, "", Icons.Default.Menu)
                Spacer(Modifier.size(18.dp))
                OptionCard("Dirección", navController, Color(0xFFDEDEDE), Color.Black, "", Icons.Default.LocationOn)
                Spacer(Modifier.size(18.dp))
                OptionCard("Mis Pedidos", navController, Color(0xFFDEDEDE), Color.Black, "", Icons.Default.ShoppingCart)
            }
            Column {
                LogOut("Cerrar Sesión", navController, Color(0xFFFF6A6A), Color.White)
                Spacer(Modifier.size(50.dp)) // Espacio debajo del último elemento
            }
        }
    }
}

@Composable
fun HeaderProfileContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(Color(0xFF3D3D3D))
    ) {
        Image(
            painter = painterResource(id = R.drawable.supermarket),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(200.dp).align(Alignment.Center).clip(CircleShape),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Hola Daniel",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Entrega en 46026",
                    color = Color(0xFFEEEEEE),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
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
    Card(modifier = Modifier
        .fillMaxWidth()
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
fun LogOut(titulo: String, navController: NavController, backgroundColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(12.dp),
        onClick = {},
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)) {
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