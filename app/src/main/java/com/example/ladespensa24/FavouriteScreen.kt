package com.example.ladespensa24

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FavouriteScreen(navController: NavController, viewModel: MyViewModel) {
    Scaffold(
        content = { innerPadding ->
            FavouriteContent(innerPadding, navController)
        },
        bottomBar = {
            AppFooter(
                Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth(), navController, viewModel
            )
        }
    )
}

@Composable
fun FavouriteContent(innerPadding: PaddingValues, navController: NavController) {
    val filteredFavouriteProducts = remember {
        productsInStorage.filter { it.getFeatured() }
    }

    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)

        ) {
            val chunkedProducts = filteredFavouriteProducts.chunked(2) // Divide en grupos de 2
            item {
                Box(Modifier.fillMaxWidth().height(90.dp).background(Color(0xFF7EA24C))) {
                    Spacer(Modifier.size(12.dp))
                    Text(
                        text = "PRODUCTOS FAVORITOS",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
                    )
                    Spacer(Modifier.size(12.dp))
                }
            }
            items(chunkedProducts.size) { index -> // Itera por índice sobre los grupos
                val productPair = chunkedProducts[index] // Obtiene el par actual
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Primer producto
                    FavouriteProductCard(productPair[0], navController, Modifier.weight(1f))

                    // Segundo producto, si existe
                    if (productPair.size > 1) {
                        FavouriteProductCard(productPair[1], navController, Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.weight(1f)) // Rellena el espacio
                    }
                }
            }
        }
    }
}
