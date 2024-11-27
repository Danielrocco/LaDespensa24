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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController, viewModel: MyViewModel) {
    Scaffold(
        topBar = {
            AppHeader()
        }, content = { innerPadding ->
            MainScreenContent(innerPadding, navController)
        }, bottomBar = {
            AppFooter(modifier = Modifier.navigationBarsPadding().fillMaxWidth(), navController, viewModel)
        }
    )
}

@Composable
fun AppFooter(modifier: Modifier, navController: NavController, viewModel: MyViewModel) {

    val selectedIcon = viewModel.selectedIcon.value

    Card(
        modifier = modifier,
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF855C41)) // Color de fondo dentro de la Card
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NormalImage(
                Modifier
                    .height(30.dp)
                    .weight(1f)
                    .clickable { navController.popBackStack()
                        navController.navigate("mainScreen")
                        viewModel.selectIcon("mainScreen")
                               },
                R.drawable.casa,
                if (selectedIcon == "mainScreen") Color.White else Color(0xFFD2D2D2)
            )
            NormalImage(
                Modifier
                    .height(28.dp)
                    .weight(1f)
                    .clickable { navController.popBackStack()
                        navController.navigate("categoriesScreen")
                        viewModel.selectIcon("categoriesScreen")},
                R.drawable.cuadrados,
                if (selectedIcon == "categoriesScreen") Color.White else Color(0xFFD2D2D2)
            )
            NormalImage(
                Modifier
                    .height(28.dp)
                    .weight(1f)
                    .clickable { },
                R.drawable.corazon,
                Color(0xFFD2D2D2)
            )
            NormalImage(
                Modifier
                    .height(30.dp)
                    .weight(1f)
                    .clickable { navController.popBackStack()
                        navController.navigate("userScreen")
                        viewModel.selectIcon("userScreen") },
                R.drawable.persona,
                if (selectedIcon == "userScreen") Color.White else Color(0xFFD2D2D2)
            )
        }
    }
}

@Composable
fun MainScreenContent(paddingValues: PaddingValues, navController: NavController) {
    Column {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)

        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)) {
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
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Row (Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .height(30.dp)
                                    .background(Color(0xffb5e354))
                                    .padding(horizontal = 10.dp)
                            ){
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
                            ){
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
                    FeaturedLazyRow(navController)
                }

                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                    ) {
                        Row (Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                text = "/ / / / / /",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                color = Color.Gray,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                            Box(Modifier.weight(1.5f).fillMaxHeight().background(Color(0xff6fd5e9))) {
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
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
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
                    NewsLazyRow(navController)

                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Row (Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(30.dp)
                                    .padding(horizontal = 10.dp)
                            ){
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
                            ){
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
                    DiscountedLazyRow(navController)
                    Spacer(modifier = Modifier.size(70.dp))
                }
            }
        }
    }

}

@Composable
fun NewsLazyRow(navController: NavController) {
    val filteredNewProducts = remember {
        productsInStorage.filter { it.getIsNew() }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredNewProducts.size) { index ->
            NewProductCard(filteredNewProducts[index], navController)
        }
    }
}

@Composable
fun DiscountedLazyRow(navController: NavController) {
    val filteredDiscountedProducts = remember {
        productsInStorage.filter { it.getIsDiscounted() }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredDiscountedProducts.size) { index ->
            ProductCard(filteredDiscountedProducts[index], navController)
        }
    }
}

@Composable
fun FeaturedLazyRow(navController: NavController) {

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

@Preview
@Composable
fun SearchBarButton() {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .padding(top = 30.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 20.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Gray,
            containerColor = Color.White,
        )
    ) {
        Row {
            NormalImage(
                modifier = Modifier
                    .size(20.dp),
                image = R.drawable.lupa,
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.size(18.dp))
            Text(
                text = stringResource(id = R.string.SearchBarPlaceholderES),
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.muli)),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
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
fun Logo() {
    TODO("pipi")
}

@Composable
fun AppHeader() {
    Row {
        SearchBarButton()
        //Logo()
    }
    Spacer(modifier = Modifier.size(18.dp))
}
