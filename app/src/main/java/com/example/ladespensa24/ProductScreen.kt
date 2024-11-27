@file:Suppress("UNREACHABLE_CODE")

package com.example.ladespensa24

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController

@Composable
fun ProductScreen(navController: NavController, viewModel: MyViewModel, product: Product) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp, // Aumenta la elevación para una sombra más visible
                        shape = RoundedCornerShape(0.dp), // Mantiene los bordes rectos
                        ambientColor = Color.Black.copy(alpha = 1.2f), // Color negro con más opacidad
                        spotColor = Color.Black.copy(alpha = 1.2f) // Ajusta el color de la sombra proyectada
                    )
            ) {
                ProductHeader(product, navController)
            }
        },
        content = { innerPadding ->
            ProductScreenContent(innerPadding, product, navController)
        }
    )
}

@Composable
fun ProductScreenContent(innerPadding: PaddingValues, product: Product, navController: NavController) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
    ) {
        item {
            Image(
                painter = painterResource(id = product.getImage()),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
        item {
            Text(
                text = product.getTitle(),
                fontFamily = FontFamily(Font(R.font.muli)),
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
        }
        item {
            Text(
                text = product.getDescription(),
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.Gray,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            )
        }
        item {
            Row (modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = (if (product.getIsDiscounted()) product.getDiscountedPrice().toString() + " €" else { product.getPrice().toString() + "€" }),
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Text(
                    text = product.getPrice().toString() + " €",
                    color = if (product.getIsDiscounted()) Color.Gray else Color.White,
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = 20.sp
                )
            }
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xffb5e354)
                ),
                onClick = {}
            ) {
                Text(
                    "Añadir a la cesta",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        item {
            Spacer(modifier = Modifier.size(20.dp))
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray)
            ) {
                Column {
                    Text(
                        text = "Productos Relacionados",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp)
                    )
                    RelatedLazyRow(navController)
                }
            }
        }
    }
}

@Composable
fun RelatedLazyRow(navController: NavController) {

    val filteredTrendingProducts = remember {
        productsInStorage.filter { it.getFeatured() }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredTrendingProducts.size) { index ->
            ProductCard(filteredTrendingProducts[index], navController)
        }
    }
}

@Composable
fun ProductHeader(product: Product, navController: NavController) {
    var isFavorite: Boolean by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF855C41))
            .zIndex(1f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.popBackStack()
                        navController.navigate("mainScreen") },
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
            IconButton(
                modifier = Modifier
                    .size(30.dp),
                onClick = {
                    isFavorite = !isFavorite
                }
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}

