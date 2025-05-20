package com.example.ladespensa24

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlin.collections.chunked


@Composable
fun FilteredCategoriesScreen(
    navController: NavController,
    viewModel: MyViewModel,
    selectedCategory: String
) {

    BackHandler(enabled = true) {
        navController.navigate("categoriesScreen") {
            popUpTo("categoriesScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    val user = viewModel.getUsuarioEnUso()
    val isLogged by viewModel.isLogged.observeAsState(false)

    Scaffold(
        content = { innerPadding ->
            FilteredCategoriesContent(innerPadding, navController, user,viewModel, selectedCategory)
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
fun FilteredCategoriesContent(
    innerPadding: PaddingValues,
    navController: NavController,
    user: User,
    viewModel: MyViewModel,
    selectedCategory: String
) {

    val allProducts = viewModel.getAllProducts()
    val filteredProducts = remember(selectedCategory) {
        allProducts.filter { it.getCategory().name.equals(selectedCategory, ignoreCase = true) }
    }

    Column {
        FilteredCategoriesHeader(selectedCategory)

        Box {
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos en la categoría $selectedCategory.",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.muli)),
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    val chunkedProducts = filteredProducts.chunked(2)
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .background(Color(0xFF3D3D3D))
                        )
                    }

                    items(chunkedProducts.size) { index ->
                        val productPair = chunkedProducts[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FavouriteProductCard(productPair[0], navController, Modifier.weight(1f))
                            if (productPair.size > 1) {
                                FavouriteProductCard(productPair[1], navController, Modifier.weight(1f))
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilteredCategoriesHeader(selectedCategory: String) {
    val imageResId = when (selectedCategory.lowercase()) {
        "frutería" -> R.drawable.fruteria
        "carnicería" -> R.drawable.carniceria
        "horno" -> R.drawable.horno
        else -> R.drawable.supermarket
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = selectedCategory.uppercase(),
            fontFamily = FontFamily(Font(R.font.muli)),
            color = Color.White,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}
