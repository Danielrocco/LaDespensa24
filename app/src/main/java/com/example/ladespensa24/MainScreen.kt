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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            AppHeader()
        }, content = { innerPadding ->
            MainScreenContent(innerPadding)
        }, bottomBar = {
            AppFooter()
        }
    )
}

@Preview
@Composable
fun AppFooter() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ), // Bordes superiores redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFA49458)) // Color de fondo dentro de la Card
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NormalImage(
                Modifier
                    .height(30.dp)
                    .weight(1f)
                    .clickable { },
                R.drawable.casa,
                Color.White
            )
            NormalImage(
                Modifier
                    .height(28.dp)
                    .weight(1f)
                    .clickable { },
                R.drawable.cuadrados,
                Color.White
            )
            NormalImage(
                Modifier
                    .height(30.dp)
                    .weight(1f)
                    .clickable { },
                R.drawable.persona,
                Color.White
            )
        }
    }
}


@Composable
fun MainScreenContent(paddingValues: PaddingValues) {
    Column {
        Spacer(modifier = Modifier.size(30.dp))
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(12.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Spacer(modifier = Modifier.size(12.dp))
                    FeaturedLazyRow()
                }
                item {
                    Spacer(modifier = Modifier.size(12.dp))
                    DiscountedLazyRow()
                }
                item {
                    Spacer(modifier = Modifier.size(12.dp))
                    NewsLazyRow()
                }
            }
        }
    }

}

@Composable
fun NewsLazyRow() {
    val filteredNewProducts = remember {
        productsInStorage.filter { it.getIsNew() }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredNewProducts.size) { index ->
            NewProductCard(filteredNewProducts[index])
        }
    }
}

@Composable
fun DiscountedLazyRow() {
    val filteredDiscountedProducts = remember {
        productsInStorage.filter { it.getIsDiscounted() }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredDiscountedProducts.size) { index ->
            DiscountedProductCard(filteredDiscountedProducts[index])
        }
    }
}

@Composable
fun FeaturedLazyRow() {

    val filteredTrendingProducts = remember {
        productsInStorage.filter { it.getFeatured() }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredTrendingProducts.size) { index ->
            ProductCard(filteredTrendingProducts[index])
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
            .padding(10.dp),
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
                text = "Buscar producto",
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
