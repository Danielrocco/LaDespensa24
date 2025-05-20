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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun SearchScreen(navController: NavController, viewModel: MyViewModel) {
    val isLogged by viewModel.isLogged.observeAsState(false)

    Scaffold(
        containerColor = Color.White,
        content = { innerPadding ->
            SearchScreenContent(innerPadding, navController, viewModel, isLogged)
        },
        bottomBar = {
            AppFooter(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth(),
                navController = navController,
                isLogged = isLogged,
                viewModel = viewModel
            )
        }
    )
}
@Composable
private fun SearchScreenContent(
    innerPadding: PaddingValues,
    navController: NavController,
    viewModel: MyViewModel,
    isLogged: Boolean
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    fun performSearch() {
        keyboardController?.hide()
    }

    var query by remember { mutableStateOf("") }
    val filteredProducts by remember(query) {
        derivedStateOf {
            viewModel.getAllProducts().filter { it.getTitle().contains(query, ignoreCase = true) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        performSearch()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF383838),
                    unfocusedTextColor = Color(0xFF383838),
                    focusedContainerColor = Color(0xFFF3F3F3),
                    unfocusedContainerColor = Color(0xFFF3F3F3),
                    disabledContainerColor = Color(0xFFF3F3F3),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                placeholder = {
                    Text(
                        text = "Busca producto",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        modifier = Modifier.clickable {
                            performSearch()
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.size(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (filteredProducts.isEmpty()) {
                    EmptySearchMessage()
                } else {
                    LazyFilteredColumn(
                        filteredProducts = filteredProducts,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        viewModel = viewModel,
                        isLogged = isLogged
                    )
                }
            }
        }
    }
}



@Composable
fun EmptySearchMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No hay productos con ese nombre",
            color = Color.Gray,
            fontFamily = FontFamily(Font(R.font.muli)),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LazyFilteredColumn(
    filteredProducts: List<Product>,
    navController: NavController,
    modifier: Modifier,
    viewModel: MyViewModel,
    isLogged: Boolean
) {
    LazyColumn(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(filteredProducts.size) { index ->
            FilteredProductCard(
                product = filteredProducts[index],
                navController,
                viewModel,
                isLogged
            )
        }
    }
}

@Composable
fun FilteredProductCard(
    product: Product,
    navController: NavController,
    viewModel: MyViewModel,
    isLogged: Boolean
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate("productScreen/${product.getTitle()}") {
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
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    painter = painterResource(product.getImage()),
                    contentDescription = "Imagen del producto",
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.getTitle(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = product.getDescription(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp
                    )
                }
                Column {
                    Text(
                        text = if (product.getIsDiscounted())
                            String.format("%.2f €", product.getDiscountedPrice())
                        else
                            String.format("%.2f €", product.getPrice()),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        color = if (product.getIsDiscounted()) Color.Black else Color.White,
                        text = product.getPrice().toString() + " €",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 9.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier
                        .height(50.dp)
                        .width(130.dp)
                        .padding(8.dp),
                    onClick = {
                        if (isLogged) {
                            viewModel.getUsuarioEnUso().addToCart(product, 1)
                            navController.navigate("cartScreen")
                        } else navController.navigate("loginScreen")
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(0xffb5e354)
                    ),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                ) {
                    Text(
                        "Añadir a la cesta",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}