package com.example.ladespensa24

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun CategoriesScreen(navController: NavController, viewModel: MyViewModel) {

    BackHandler(enabled = true) {
        navController.navigate("mainScreen") {
            // Limpia la pila para evitar volver atrás otra vez a esta pantalla
            popUpTo("mainScreen") { inclusive = false }
            launchSingleTop = true
        }
    }
    val isLogged by viewModel.isLogged.observeAsState(false)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()))
                AppHeader(navController, Color.Black)
            }
        }, content = { innerPadding ->
            CategoriesScreenContent(innerPadding, navController)
        }, bottomBar = {
            AppFooter(modifier = Modifier.navigationBarsPadding().fillMaxWidth(), navController, viewModel, isLogged)
        }
    )
}

@Composable
fun CategoriesScreenContent(innerPadding: PaddingValues, navController: NavController) {

    Column {
        Box(modifier = Modifier.fillMaxSize().background(Color.White).padding(12.dp).padding(top = 100.dp)) {
            LazyColumn (verticalArrangement = Arrangement.spacedBy(20.dp)) {
                item {
                    Text(
                        text = "Categorías",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        color = Color.Black,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                item {
                    CategoryCard("Fruta",1, navController)
                }
                item {
                    CategoryCard("Verdura",1, navController)
                }
                item {
                    CategoryCard("Horno",1, navController)
                }
            }
        }
    }
}

@Composable
fun CategoryCard(titulo: String, img: Int, navController: NavController) {
    Card(modifier = Modifier.fillMaxWidth().height(40.dp),
        shape = RoundedCornerShape(16.dp),) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.supermarket),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = titulo,
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(4.dp),
            )
        }
    }
}