package com.example.ladespensa24.userScreens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ladespensa24.AppFooter
import com.example.ladespensa24.NormalImage
import com.example.ladespensa24.R
import com.example.ladespensa24.models.Purchase
import com.example.ladespensa24.models.User
import com.example.ladespensa24.viewmodel.MyViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PurchasesScreen(navController: NavController, viewModel: MyViewModel) {

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
            PurchasesContent(innerPadding, navController, user, viewModel)
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
    user: User,
    viewModel: MyViewModel
) {

    val purchases = remember {
        user.purchases ?: emptyList()
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
                LazyColumnPurchases(user, navController, viewModel)
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
fun PurchaseCard(
    purchase: Purchase,
    imageUrls: Map<String, String?>,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("purchaseDetails/${purchase.purchaseId}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            val productos = purchase.inCartProducts
            val imagenes = productos.take(4)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                imagenes.forEach { producto ->
                    val imageUrl = imageUrls[producto.id]
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Imagen del producto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Compra ${purchase.purchaseId}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Text(
                    text = "Fecha: ${purchase.date}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Text(
                    text = String.format("Total: %.2f €", purchase.total),
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
fun LazyColumnPurchases(
    user: User,
    navController: NavController,
    viewModel: MyViewModel
) {
    val purchases = user.purchases?.reversed() ?: emptyList()
    val imageUrls by viewModel.imageUrls.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(purchases.size) { index ->
            val purchase = purchases[index]
            PurchaseCard(
                purchase = purchase,
                imageUrls = imageUrls,
                navController = navController
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PurchaseTicketScreen(navController: NavController, purchase: Purchase) {

    val productos = purchase.inCartProducts

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.popBackStack() },
                containerColor = Color(0xffb5e354),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Text(
                    "Volver",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
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
                    "${it.title} x${it.units} - %.2f € p/u > %.2f €".format(
                        if (it.isDiscounted) it.getDiscountedPrice() else it.price,
                        it.getTotalPrice()
                    ),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(
                thickness = 1.dp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                "Fecha: ${purchase.date}",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.muli))
            )
            Text(
                "Tarjeta: ${purchase.getMaskedCardNumber()}",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.muli))
            )
            Text(
                "Total gastado: %.2f €".format(purchase.total),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.muli))
            )
        }
    }
}