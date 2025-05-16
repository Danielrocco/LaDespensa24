package com.example.ladespensa24

import android.os.Build
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CartScreen(navController: NavController, viewModel: MyViewModel) {

    BackHandler(enabled = true) {
        navController.navigate("mainScreen") {
            // Limpia la pila para evitar volver atrás otra vez a esta pantalla
            popUpTo("mainScreen") { inclusive = false }
            launchSingleTop = true
        }
    }

    val user = viewModel.getUsuarioEnUso()
    viewModel.setEntireAmount(viewModel.getUsuarioEnUso())

    val isLogged by viewModel.isLogged.observeAsState(false)
    val entireAmount by viewModel.entireAmount.observeAsState(viewModel.getUsuarioEnUso().getEntireAmountOfCart())

    Scaffold(
        content = { innerPadding ->
            CartScreenContent(innerPadding, user, navController, viewModel)
        },
        floatingActionButton = {
            val isCartEmpty = entireAmount <= 0

            Button(
                onClick = {
                    if (!isCartEmpty) {
                        user.buyProducts()
                        navController.navigate("mainScreen")
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB5E354), // más oscuro si está vacío
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFA8A8A8)
                ),
                enabled = !isCartEmpty
            ) {
                Text(
                    text = "Comprar ($entireAmount€)",
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        bottomBar = {
            AppFooter(modifier = Modifier.navigationBarsPadding().fillMaxWidth(), navController = navController, isLogged = isLogged, viewModel = viewModel)
        }
    )
}



@Composable
private fun CartScreenContent(innerPadding: PaddingValues, user: User, navController: NavController, viewModel: MyViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            HeaderCartContent()
            MainCart(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 70.dp), // solo padding inferior
                user = user,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun MainCart(modifier: Modifier, user: User, navController: NavController, viewModel: MyViewModel) {
    val cartProducts = user.getCart() ?: emptyList()

    if (cartProducts.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tu carrito está vacío.",
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.muli)),
            )
        }
    } else {
        Box(modifier = modifier.padding(12.dp)) {
            LazyColumnCartProducts(user = user, navController = navController, viewModel)
        }
    }

}



@Composable
fun HeaderCartContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color(0xFF696969))
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
                text = "CARRITO",
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
private fun LazyColumnCartProducts(user: User, navController: NavController, viewModel: MyViewModel) {
    val productsInHisCart = user.getCart()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (productsInHisCart != null) {
            items(productsInHisCart.size) { index ->
                CartProductCard(
                    inCartProduct = productsInHisCart[index],
                    navController = navController,
                    user,
                    viewModel
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun CartProductCard(
    inCartProduct: InCartProduct,
    navController: NavController,
    user: User,
    viewModel: MyViewModel
) {
    var unidadesActuales by remember { mutableStateOf(inCartProduct.getUnits()) }

    val precioUnitario = if (inCartProduct.getIsDiscounted())
        inCartProduct.getDiscountedPrice()
    else
        inCartProduct.getPrice()

    val totalPrecio = remember(unidadesActuales) {
        precioUnitario * unidadesActuales
    }

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate("productScreen/${inCartProduct.getTitle()}") {
                launchSingleTop = true
            }
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            // Imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    painter = painterResource(inCartProduct.getImage()),
                    contentDescription = "Imagen del producto",
                    contentScale = ContentScale.Crop
                )
            }

            // Título y descripción
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f) // Esta empuja a la otra hacia la derecha
                ) {
                    Text(
                        text = inCartProduct.getTitle(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = inCartProduct.getDescription(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End, // Alinea el contenido dentro de la columna
                ) {
                    Text(
                        text = String.format("%.2f €", precioUnitario),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = String.format("Total: %.2f €", totalPrecio),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }

            // Contador de unidades
            Row(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Unidades: $unidadesActuales",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Remove unity",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            if (unidadesActuales != 1) {
                                unidadesActuales--
                                inCartProduct.removeUnits()
                                viewModel.setEntireAmount(viewModel.getUsuarioEnUso())
                            } else {
                                user.removeFromKart(inCartProduct.getTitle())
                                viewModel.setEntireAmount(viewModel.getUsuarioEnUso())
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
                            unidadesActuales++
                            inCartProduct.addUnits()
                            viewModel.setEntireAmount(viewModel.getUsuarioEnUso())
                        }
                )
            }
        }
    }
}


