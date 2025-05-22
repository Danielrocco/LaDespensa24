@file:Suppress("UNREACHABLE_CODE")

package com.example.ladespensa24.otherScreens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ladespensa24.R
import com.example.ladespensa24.models.Product
import com.example.ladespensa24.models.ProductCard
import com.example.ladespensa24.viewmodel.MyViewModel

@Composable
fun ProductScreen(navController: NavController, viewModel: MyViewModel, product: Product) {

    val isLogged by viewModel.isLogged.observeAsState(false)

    Scaffold(
        content = { innerPadding ->
            ProductScreenContent(innerPadding, product, navController, viewModel, isLogged)
        }
    )
}

@Composable
fun ProductScreenContent(
    innerPadding: PaddingValues,
    product: Product,
    navController: NavController,
    viewModel: MyViewModel,
    isLogged: Boolean
) {

    var unidades by remember { mutableIntStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }
    val imageUrls by viewModel.imageUrls.collectAsState()

    val currentFavorite = viewModel.isProductFavorite(product)
    LaunchedEffect(currentFavorite) {
        isFavorite = currentFavorite
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            AsyncImage(
                model = imageUrls[product.id],
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.getTitle(),
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (isLogged) {
                            viewModel.toggleFavorite(product)
                            isFavorite = viewModel.isProductFavorite(product)
                        } else {
                            navController.navigate("loginScreen")
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (product.getIsDiscounted())
                        String.format("%.2f €", product.getDiscountedPrice())
                    else
                        String.format("%.2f €", product.getPrice()),
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
            Row(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Unidades: $unidades",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Remove unity",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            if (unidades != 1) {
                                unidades--
                            }
                        }
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add unity",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            unidades++
                        })
            }
        }
        item {
            val priceToShow =
                if (product.getIsDiscounted()) product.getDiscountedPrice() else product.getPrice()

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color(0xffb5e354)
                ),
                onClick = {
                    if (isLogged) {
                        viewModel.getUsuarioEnUso().addToCart(product, unidades)
                        navController.navigate("cartScreen")
                    } else navController.navigate("loginScreen")
                }
            ) {
                Text(
                    "Añadir a la cesta (${String.format("%.2f", priceToShow * unidades)}€)",
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
                    RelatedLazyRow(product, navController, viewModel, isLogged)
                    Spacer(modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}

@Composable
fun RelatedLazyRow(
    currentProduct: Product,
    navController: NavController,
    viewModel: MyViewModel,
    isLogged: Boolean
) {
    val products by viewModel.products.collectAsState()
    val imageUrls by viewModel.imageUrls.collectAsState()

    val relatedProducts = remember(currentProduct, products) {
        products.filter {
            it.getCategory() == currentProduct.getCategory() && it.getTitle() != currentProduct.getTitle()
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(relatedProducts.size) { index ->
            val product = relatedProducts[index]
            val imageUrl = imageUrls[product.id]

            ProductCard(
                product = product,
                imageUrl = imageUrl,
                navController = navController,
                isLogged = isLogged,
                viewModel
            )
        }
    }
}