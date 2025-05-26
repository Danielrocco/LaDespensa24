package com.example.ladespensa24

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ladespensa24.models.Product
import com.example.ladespensa24.models.ProductCard
import com.example.ladespensa24.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController, viewModel: MyViewModel) {

    val isLogged by viewModel.isLogged

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                    )
                )
                AppHeader(navController, Color.White)
            }
        }, content = { innerPadding ->
            HomeScreenContent(innerPadding, navController, viewModel, isLogged)
        }, bottomBar = {
            AppFooter(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth(),
                navController,
                viewModel,
                isLogged
            )
        }
    )
}

@Composable
fun AppFooter(
    modifier: Modifier,
    navController: NavController,
    viewModel: MyViewModel,
    isLogged: Boolean
) {
    val selectedIcon = viewModel.selectedIcon.value

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 40.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF7A7A7A))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NormalImage(
                Modifier
                    .height(24.dp)
                    .weight(1f)
                    .clickable {
                        navController.popBackStack()
                        navController.navigate("homeScreen")
                        viewModel.selectIcon("homeScreen")
                    },
                R.drawable.casa,
                if (selectedIcon == "homeScreen") Color.White else Color(0xFFD2D2D2)
            )
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = if (selectedIcon == "categoriesScreen") Color.White else Color(0xFFD2D2D2),
                modifier = Modifier
                    .height(34.dp)
                    .weight(1f)
                    .clickable {
                        navController.popBackStack()
                        navController.navigate("categoriesScreen")
                        viewModel.selectIcon("categoriesScreen")
                    }
            )
            NormalImage(
                Modifier
                    .height(24.dp)
                    .weight(1f)
                    .clickable {
                        navController.popBackStack()
                        if (isLogged) {
                            navController.navigate("cartScreen")
                            viewModel.selectIcon("cartScreen")
                        } else {
                            navController.navigate("loginScreen")
                            viewModel.selectIcon("loginScreen")
                        }
                    },
                R.drawable.carrito,
                if (selectedIcon == "cartScreen") Color.White else Color(0xFFD2D2D2)
            )
            NormalImage(
                Modifier
                    .height(24.dp)
                    .weight(1f)
                    .clickable {
                        navController.popBackStack()
                        if (isLogged) {
                            navController.navigate("favouriteScreen")
                            viewModel.selectIcon("favouriteScreen")
                        } else {
                            navController.navigate("loginScreen")
                            viewModel.selectIcon("loginScreen")
                        }
                    },
                R.drawable.corazon,
                if (selectedIcon == "favouriteScreen") Color.White else Color(0xFFD2D2D2)
            )
            NormalImage(
                Modifier
                    .height(24.dp)
                    .weight(1f)
                    .clickable {
                        navController.popBackStack()
                        if (isLogged) {
                            navController.navigate("userScreen")
                            viewModel.selectIcon("userScreen")
                        } else {
                            navController.navigate("loginScreen")
                            viewModel.selectIcon("loginScreen")
                        }
                    },
                R.drawable.persona,
                if (selectedIcon == "userScreen") Color.White else Color(0xFFD2D2D2)
            )
        }
    }
}

@Composable
fun HomeScreenContent(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: MyViewModel,
    isLogged: Boolean
) {
    Column {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)

        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.supermarket),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(
                            text = stringResource(id = R.string.HomeScreenES),
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
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(Color(0xFF3D3D3D))
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Row(Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .height(30.dp)
                                    .background(Color(0xffb5e354))
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.TrendingFlagES),
                                    fontFamily = FontFamily(Font(R.font.muli)),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(30.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = "/ / / / / / / / / / / / / / /",
                                    fontFamily = FontFamily(Font(R.font.muli)),
                                    color = Color.Gray,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                        }

                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    FeaturedLazyRow(navController, viewModel, isLogged)
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Row(
                            Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                text = "/ / / / / /",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                color = Color.Gray,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                            Box(
                                Modifier
                                    .weight(1.5f)
                                    .fillMaxHeight()
                                    .background(Color(0xff6fd5e9))
                            ) {
                                Text(
                                    text = stringResource(id = R.string.NewsFlagES),
                                    fontFamily = FontFamily(Font(R.font.muli)),
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                text = "/ / / / / /",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                color = Color.Gray,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    NewsLazyRow(navController, viewModel, isLogged)
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Row(Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(30.dp)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = "/ / / / / / / / / / / / / / / / / / /",
                                    fontFamily = FontFamily(Font(R.font.muli)),
                                    color = Color.Gray,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .height(30.dp)
                                    .background(Color.Red)
                                    .padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.DiscountedFlagES),
                                    fontFamily = FontFamily(Font(R.font.muli)),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    DiscountedLazyRow(navController, viewModel, isLogged)
                    Spacer(modifier = Modifier.size(70.dp))
                }
            }
        }
    }
}

@Composable
fun DiscountedLazyRow(navController: NavController, viewModel: MyViewModel, isLogged: Boolean) {
    val products by viewModel.products.collectAsState()
    val imageUrls by viewModel.imageUrls.collectAsState()
    val discountedProducts = products.filter { it.isDiscounted }

    LaunchedEffect(products) {
        Log.d("FEATURED_PRODUCTS", "Cantidad total: ${products.size}")
        Log.d("FEATURED_PRODUCTS", "Featured: ${products.count { it.isDiscounted }}")
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(discountedProducts.size) { index ->
            val product = discountedProducts[index]
            val imageUrl = imageUrls[product.id]
            ProductCard(product, imageUrl, navController, isLogged, viewModel)
        }
    }
}

@Composable
fun NewsLazyRow(navController: NavController, viewModel: MyViewModel, isLogged: Boolean) {
    val products by viewModel.products.collectAsState()
    val imageUrls by viewModel.imageUrls.collectAsState()
    val newProducts = products.filter { it.isNew }

    LaunchedEffect(products) {
        Log.d("FEATURED_PRODUCTS", "Cantidad total: ${products.size}")
        Log.d("FEATURED_PRODUCTS", "Featured: ${products.count { it.isNew }}")
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(newProducts.size) { index ->
            val product = newProducts[index]
            val imageUrl = imageUrls[product.id]
            NewProductCard(product, imageUrl, navController, isLogged, viewModel)
        }
    }
}

@Composable
fun FeaturedLazyRow(navController: NavController, viewModel: MyViewModel, isLogged: Boolean) {
    val products by viewModel.products.collectAsState()
    val imageUrls by viewModel.imageUrls.collectAsState()

    LaunchedEffect(products) {
        Log.d("DEBUG_FEATURED", "Cantidad total de productos: ${products.size}")
        products.forEach { product ->
            Log.d("DEBUG_FEATURED", "Producto: ${product.title} | isFeatured=${product.isFeatured}")
        }
    }

    val featuredProducts = products.filter { it.isFeatured }

    LaunchedEffect(products) {
        Log.d("FEATURED_PRODUCTS", "Cantidad total: ${products.size}")
        Log.d("FEATURED_PRODUCTS", "Featured: ${products.count { it.isFeatured }}")
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(featuredProducts.size) { index ->
            val product = featuredProducts[index]
            val imageUrl = imageUrls[product.id]
            ProductCard(product, imageUrl, navController, isLogged, viewModel)
        }
    }
}

@Composable
fun SearchBarButton(navController: NavController, modifier: Modifier = Modifier) {
    Button(
        onClick = {
            navController.navigate("searchScreen")
        },
        modifier = modifier
            .height(56.dp), // Altura estándar
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 20.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Gray,
            containerColor = Color.White,
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.SearchBarPlaceholderES),
                modifier = Modifier.weight(1f),
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.muli)),
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            NormalImage(
                modifier = Modifier.size(20.dp),
                image = R.drawable.lupa,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun NormalImage(modifier: Modifier, image: Int, tint: Color?) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "Logo",
        modifier = modifier,
        colorFilter = if (tint != null)
            ColorFilter.tint(tint)
        else null
    )
}

@Composable
fun AppHeader(navController: NavController, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NormalImage(
            modifier = Modifier
                .size(56.dp),
            image = R.drawable.logo,
            tint = color
        )
        Spacer(modifier = Modifier.size(12.dp))
        SearchBarButton(
            navController = navController,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(18.dp))
}

@Composable
fun NewProductCard(
    product: Product,
    imageUrl: String?,
    navController: NavController,
    isLogged: Boolean,
    viewModel: MyViewModel
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(260.dp),
        onClick = {
            navController.navigate("productScreen/${product.title}") {
                launchSingleTop = true
            }
        },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            Text(
                text = "NOVEDAD",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff6fd5e9))
                    .padding(4.dp),
                fontFamily = FontFamily(Font(R.font.muli)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            AsyncImage(
                model = imageUrl,
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.title,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = product.description,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp
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
                        fontSize = 12.sp
                    )
                    Text(
                        color = if (product.isDiscounted) Color.Black else Color.White,
                        text = product.price.toString() + " €",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 9.sp
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier
                        .height(50.dp)
                        .width(130.dp)
                        .padding(8.dp),
                    onClick = {
                        if (isLogged) {
                            viewModel.getUsuarioEnUso().addToCart(product, 1, viewModel)
                            navController.navigate("cartScreen")
                        } else {
                            navController.navigate("loginScreen")
                        }
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
