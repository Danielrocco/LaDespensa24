package com.example.ladespensa24.models

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ladespensa24.R
import com.example.ladespensa24.viewmodel.MyViewModel
import java.util.UUID

enum class ProductCategories {
    FRUTERÍA, CARNICERÍA, HORNO
}

open class Product(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var category: ProductCategories = ProductCategories.FRUTERÍA,
    var isFeatured: Boolean = false,
    var isDiscounted: Boolean = false,
    var discount: Int = 0,
    var isNew: Boolean = false
) {
    fun getDiscountedPrice(): Double = (price - (price / 100 * discount))
}

open class InCartProduct() : Product() { // 🔥 Constructor sin argumentos
    var units: Int = 1

    constructor(
        id: String,
        title: String,
        description: String,
        price: Double,
        category: ProductCategories,
        isFeatured: Boolean,
        isDiscounted: Boolean,
        discount: Int,
        isNew: Boolean,
        units: Int
    ) : this() { // 🔥 Llama al constructor sin argumentos
        this.id = id
        this.title = title
        this.description = description
        this.price = price
        this.category = category
        this.isFeatured = isFeatured
        this.isDiscounted = isDiscounted
        this.discount = discount
        this.isNew = isNew
        this.units = units
    }

    fun getTotalPrice(): Double {
        val pricePerUnit = if (isDiscounted) getDiscountedPrice() else price
        return pricePerUnit * units
    }

    fun addUnits() {
        units++
    }

    fun removeUnits() {
        units--
    }
}

data class Purchase(
    var inCartProducts: List<InCartProduct> = emptyList(),
    var date: String = "",
    var cardNumber: String = "",
    var purchaseId: String = UUID.randomUUID().toString().take(8)
) {
    var total: Double = 0.0
        private set

    init {
        calcularTotal()
    }

    private fun calcularTotal() {
        total = inCartProducts.sumOf {
            val precioUnitario = if (it.isDiscounted) it.getDiscountedPrice() else it.price
            precioUnitario * it.units
        }
    }

    fun getMaskedCardNumber(): String = "**** **** **** " + cardNumber.takeLast(4)
}

@Composable
fun ProductCard(
    product: Product,
    imageUrl: String?,
    navController: NavController,
    isLogged: Boolean,
    viewModel: MyViewModel
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(200.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate("productScreen/${product.title}") {
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
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen del producto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(end = 10.dp) // 🔥 Añade espacio entre título/descripción y precio
                ) {
                    Text(
                        text = product.title,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = product.description,
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp,

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(if (product.isDiscounted) Color.Red else Color.White)
                ) {
                    Text(
                        text = " -" + product.discount.toString() + "% ",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
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