package com.example.ladespensa24


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            AppHeader()
        }, content = { innerPadding ->
            MainScreenContent()
        }, bottomBar = {
            AppFooter()
        }
    )
}

@Composable
fun AppFooter() {
    TODO("Not yet implemented")
}

@Preview
@Composable
fun MainScreenContent() {
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

@Composable
fun SearchBarButton() {
    TODO("Not yet implemented")
}

@Composable
fun Logo() {
    TODO("pipi")
}

@Composable
fun AppHeader() {
    Row {
        SearchBarButton()
        Logo()
    }
    Spacer(modifier = Modifier.size(18.dp))
}
