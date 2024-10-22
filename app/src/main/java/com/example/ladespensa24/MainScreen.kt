package com.example.ladespensa24


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

@Composable
fun AppFooter() {
    TODO("Not yet implemented")
}

@Composable
fun MainScreenContent(paddingValues: PaddingValues) {
    val products = listOf("pan", "queso", "agua")

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(products.size) { index ->
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(products.chunked(2).size) { imagePair ->
                        FeaturedProductsRow(imagePair)
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedProductsRow(imagePair: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column() {

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
