package com.example.ladespensa24

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun UserScreen(navController: NavController, viewModel: MyViewModel) {
    Scaffold(
        content = { innerPadding ->
            ProfileContent(innerPadding, navController)
        },
        bottomBar = {
            AppFooter(Modifier.navigationBarsPadding().fillMaxWidth(), navController, viewModel)
        }
    )
}

@Composable
fun ProfileContent(innerPadding: PaddingValues, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // Distribuye los elementos entre el inicio y el final
        ) {
            Column {
                HeaderProfileContent()
                Spacer(Modifier.size(22.dp))
                OptionCard("Datos Personales", navController, Color(0xffb5e354), Color.White, )
                Spacer(Modifier.size(22.dp))
                OptionCard("Metodos de pago", navController, Color(0xffb5e354), Color.White, )
                Spacer(Modifier.size(22.dp))
                OptionCard("Dirección", navController, Color(0xffb5e354), Color.White, )
                Spacer(Modifier.size(22.dp))
                OptionCard("Mis Pedidos", navController, Color(0xffb5e354), Color.White, )
            }
            Column {
                OptionCard("Cerrar Sesión", navController, Color(0xFFFF6A6A), Color.White,)
                Spacer(Modifier.size(46.dp)) // Espacio debajo del último elemento
            }
        }
    }
}

@Composable
fun HeaderProfileContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Hola Daniel",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Entrega en 46026",
                color = Color(0xffb5e354),
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun OptionCard(titulo: String, navController: NavController, backgroundColor: Color, textColor: Color) {
    Card(modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(16.dp),) {
        Box(modifier = Modifier.fillMaxSize().background(color = backgroundColor)) {
            Text(
                text = titulo,
                fontFamily = FontFamily(Font(R.font.muli)),
                color = textColor,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(4.dp).padding(start = 20.dp),
            )
        }
    }
}