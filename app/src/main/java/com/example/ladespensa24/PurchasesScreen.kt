package com.example.ladespensa24

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.collections.chunked


@Composable
fun PurchasesScreen(navController: NavController, viewModel: MyViewModel) {

    BackHandler(enabled = true) {
        navController.navigate("mainScreen") {
            popUpTo("mainScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    val user = viewModel.getUsuarioEnUso()
    val isLogged by viewModel.isLogged.observeAsState(false)

    Scaffold(
        content = { innerPadding ->
            PurchasesContent(innerPadding, navController, user)
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
fun PurchasesContent(
    innerPadding: PaddingValues,
    navController: NavController,
    user: User
) {

    val purchases = remember {
        user.getPurchases() ?: emptyList()
    }

    Column {
        HeaderPurchasesContent()
        Box {
            if (purchases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No has comprado nada todavía.",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.muli)),
                    )
                }
            } else {
                LazyColumnPurchases(user, navController)
            }
        }
    }
}

@Composable
fun HeaderPurchasesContent() {
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
                text = "TUS COMPRAS",
                fontFamily = FontFamily(Font(R.font.muli)),
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            NormalImage(
                Modifier
                    .height(24.dp),
                R.drawable.carrito,
                Color.White
            )
        }
    }
}

@Composable
fun PurchaseCard(purchase: Purchase, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("purchaseDetails/${purchase.getId()}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            val productos = purchase.getInCartProducts()
            val imagenes = productos.take(4)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                imagenes.forEach {
                    NormalImage(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        image = it.getImage(),
                        tint = null
                    )
                }
            }

            // 🧾 Información de la compra
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Compra ${purchase.getId()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Text(
                    text = "Fecha: ${purchase.getDate()}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Text(
                    text = String.format("Total: %.2f €", purchase.getTotal()),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.muli)),
                    color = Color(0xFF333333)
                )
            }
        }
    }
}

@Composable
fun LazyColumnPurchases(user: User, navController: NavController) {
    val purchases = user.getPurchases()?.reversed() ?: emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(purchases.size) { index ->
            val purchase = purchases[index]
            PurchaseCard(purchase = purchase, navController = navController)
        }
    }
}

@Composable
fun PurchaseDetailsScreen(navController: NavController, purchase: Purchase) {

    val productos = purchase.getInCartProducts()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.popBackStack() },
                containerColor = Color(0xffb5e354),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text(
                    "Volver a las compras",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.size(32.dp))
            Text(
                "DETALLE DE LA COMPRA",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.muli))
            )

            Spacer(modifier = Modifier.height(16.dp))

            productos.forEach {
                Text(
                    "${it.getTitle()} x${it.getUnits()} - %.2f € p/u > %.2f €".format(
                        if (it.getIsDiscounted()) it.getDiscountedPrice() else it.getPrice(),
                        it.getTotalPrice()
                    ),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.padding(vertical = 16.dp))

            Text(
                "Fecha: ${purchase.getDate()}",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.muli))
            )
            Text(
                "Tarjeta: ${purchase.getMaskedCardNumber()}",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.muli))
            )
            Text(
                "Total gastado: %.2f €".format(purchase.getTotal()),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.muli))
            )
        }
    }
}