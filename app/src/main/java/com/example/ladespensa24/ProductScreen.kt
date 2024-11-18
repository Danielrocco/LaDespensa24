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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ProductScreen() {
    Scaffold(
        topBar = {
            ProductHeader()
        }, content = { innerPadding ->
            ProductScreenContent(innerPadding)
        }
    )
}

@Composable
fun ProductScreenContent(innerPadding: PaddingValues) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding) // Usar innerPadding para respetar el espaciado
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.queso),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
        item {
            Text(
                text = "TITULO",
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
                text = "Descripcion",
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.Gray,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
            )
        }
        item {
            Text(
                text = "12.02€/kg",
                fontFamily = FontFamily(Font(R.font.muli)),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(start = 16.dp, top = 18.dp, end = 16.dp)
            )
        }
        item {
            Text(
                text = "12.02€/kg",
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.muli)),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.LineThrough,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 26.dp)
            )
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
                    fontSize = 20.sp
                )
            }
        }
        item {
            Spacer(modifier = Modifier.size(30.dp))
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
                        modifier = Modifier.padding(16.dp)
                    )
                    RelatedLazyRow()
                }
            }
        }
    }
}

@Composable
fun RelatedLazyRow() {

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
fun ProductHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // Distribuye los elementos con espacio entre ellos
        ) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { },
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xffb5e354)
            )
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { },
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Color(0xffb5e354)
            )
        }
    }
}
