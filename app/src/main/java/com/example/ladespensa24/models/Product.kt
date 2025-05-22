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
    private var title: String = "",
    private var description: String = "",
    private var price: Double = 0.0,
    private var category: ProductCategories = ProductCategories.FRUTERÍA,
    private var isFeatured: Boolean = false,
    private var isDiscounted: Boolean = false,
    private var discount: Int = 0,
    private var isNew: Boolean = false
) {
    fun getTitle(): String {
        return title
    }

    fun getPrice(): Double {
        return price
    }

    fun getCategory(): ProductCategories {
        return category
    }

    fun getIsFeatured(): Boolean {
        return isFeatured
    }

    fun getIsDiscounted(): Boolean {
        return isDiscounted
    }

    fun getDiscount(): Int {
        return discount
    }

    fun getIsNew(): Boolean {
        return isNew
    }

    fun getDescription(): String {
        return description
    }

    fun getDiscountedPrice(): Double {
        return (getPrice() - (getPrice() / 100 * getDiscount()))
    }
}

open class InCartProduct(
    id: String, // 🆕 nuevo campo
    title: String,
    description: String,
    price: Double,
    category: ProductCategories,
    isFeatured: Boolean,
    isDiscounted: Boolean,
    discount: Int,
    isNew: Boolean,
    private var units: Int
) : Product(
    id,
    title,
    description,
    price,
    category,
    isFeatured,
    isDiscounted,
    discount,
    isNew
) {

    fun getTotalPrice(): Double {
        val pricePerUnit = if (getIsDiscounted()) getDiscountedPrice() else getPrice()
        return pricePerUnit * units
    }

    fun getUnits(): Int {
        return units
    }

    fun addUnits() {
        units++
    }

    fun removeUnits() {
        units--
    }
}

class Purchase(
    private val inCartProducts: List<InCartProduct>,
    private val date: String,
    private val cardNumber: String,
    private val purchaseId: String = UUID.randomUUID().toString().take(8)
) {
    private val total: Double = inCartProducts.sumOf {
        val precioUnitario = if (it.getIsDiscounted()) it.getDiscountedPrice() else it.getPrice()
        precioUnitario * it.getUnits()
    }

    fun getInCartProducts() = inCartProducts
    fun getDate() = date
    fun getId() = purchaseId
    fun getTotal() = total
    fun getCardNumber() = cardNumber

    fun getMaskedCardNumber(): String {
        return "**** **** **** " + cardNumber.takeLast(4)
    }
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
            navController.navigate("productScreen/${product.getTitle()}") {
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
                Column {
                    Text(
                        text = product.getTitle(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = product.getDescription(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp
                    )
                }
                Column {
                    Text(
                        text = if (product.getIsDiscounted())
                            String.format("%.2f €", product.getDiscountedPrice())
                        else
                            String.format("%.2f €", product.getPrice()),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        color = if (product.getIsDiscounted()) Color.Black else Color.White,
                        text = product.getPrice().toString() + " €",
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
                        .background(if (product.getIsDiscounted()) Color.Red else Color.White)
                ) {
                    Text(
                        text = " -" + product.getDiscount().toString() + "% ",
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
                            viewModel.getUsuarioEnUso().addToCart(product, 1)
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