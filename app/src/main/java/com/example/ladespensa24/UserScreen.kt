package com.example.ladespensa24

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UserScreen(navController: NavController, viewModel: MyViewModel) {
    Scaffold(
        content = { innerPadding ->
            ProfileContent(innerPadding)
        },
        bottomBar = {
            AppFooter(Modifier.navigationBarsPadding().fillMaxWidth(), navController, viewModel)
        }
    )
}

@Composable
fun ProfileContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        Text(
            text = "Daniel",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Entrega en 46026",
            color = Color(0xFF4CAF50),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Menu items
        ProfileOption(img = Icons.Default.Person, text = "Datos personales")
        ProfileOption(img = Icons.Default.LocationOn, text = "Direcciones")
        ProfileOption(img = Icons.Default.List, text = "Mis pedidos")
        ProfileOption(img = Icons.Default.Build, text = "Idioma")
        Spacer(modifier = Modifier.weight(1f)) // Espaciador para empujar el botón hacia abajo

        // Logout option
        ProfileOption(
            img = Icons.Default.ExitToApp,
            text = "Cerrar sesión",
            textColor = Color.Red
        )
    }
}

@Composable
fun ProfileOption(
    img: ImageVector,
    text: String,
    textColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = img,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp
        )
    }
}
