package com.example.ladespensa24.userScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ladespensa24.AppFooter
import com.example.ladespensa24.NormalImage
import com.example.ladespensa24.R
import com.example.ladespensa24.models.Product
import com.example.ladespensa24.models.User
import com.example.ladespensa24.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FavouriteScreen(navController: NavController, viewModel: MyViewModel) {

    BackHandler(enabled = true) {
        navController.navigate("homeScreen") {
            popUpTo("homeScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    val user = viewModel.getUsuarioEnUso()
    val isLogged by viewModel.isLogged

    Scaffold(
        content = { innerPadding ->
            FavouriteContent(innerPadding, navController, viewModel, user)
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
fun FavouriteContent(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: MyViewModel,
    user: User
) {
    val filteredFavouriteProducts = remember {
        user.favouriteProducts ?: emptyList()
    }

    val imageUrls by viewModel.imageUrls.collectAsState()

    Column {
        HeaderFavouriteContent()
        Box {
            if (filteredFavouriteProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes productos favoritos todavía.",
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
                    val chunkedProducts = filteredFavouriteProducts.chunked(2)

                    items(chunkedProducts.size) { index ->
                        val productPair = chunkedProducts[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val product1 = productPair[0]
                            val imageUrl1 = imageUrls[product1.id]

                            FavouriteProductCard(
                                product = product1,
                                imageUrl = imageUrl1,
                                navController = navController,
                                modifier = Modifier.weight(1f)
                            )

                            if (productPair.size > 1) {
                                val product2 = productPair[1]
                                val imageUrl2 = imageUrls[product2.id]

                                FavouriteProductCard(
                                    product = product2,
                                    imageUrl = imageUrl2,
                                    navController = navController,
                                    modifier = Modifier.weight(1f)
                                )
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
fun HeaderFavouriteContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFF3D3D3D))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FAVORITOS",
                fontFamily = FontFamily(Font(R.font.muli)),
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            NormalImage(
                Modifier
                    .height(24.dp),
                R.drawable.corazon,
                Color.White
            )
        }
    }
}

@Composable
fun FavouriteProductCard(
    product: Product,
    imageUrl: String?,
    navController: NavController,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate("productScreen/${product.title}") {
                launchSingleTop = true
            }
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen del producto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 10.dp) // 🔥 Añade espacio entre título/descripción y precio
                ) {
                    Text(
                        text = product.title,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,

                        )
                    Text(
                        text = product.description,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 10.sp,

                        )
                }
                Column {
                    Text(
                        text = if (product.isDiscounted)
                            String.format("%.2f €", product.getDiscountedPrice())
                        else
                            String.format("%.2f €", product.price),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Text(
                        color = if (product.isDiscounted) Color.Black else Color.White,
                        text = "${product.price} €",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}