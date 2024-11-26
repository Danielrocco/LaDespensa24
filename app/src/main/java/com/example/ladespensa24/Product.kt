package com.example.ladespensa24

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val productsInStorage = listOf(
    Product("Queso", "500g aprox",12.02, ProductCategories.Charcuteria, R.drawable.queso, true, false, 0, true),
    Product("Pan", "1 barra de pan",12.02, ProductCategories.Panaderia, R.drawable.pan, true, true, 50, false),
    Product("Carne", "1k aprox",12.02, ProductCategories.Carne, R.drawable.carne, true, true, 40, false),
    Product("Manzana", "180g ",12.02, ProductCategories.Fruta, R.drawable.manzana, true, false, 0, true),
    Product("lechuga", "200g",12.02, ProductCategories.Verdura, R.drawable.lechuga, true, true, 60, false)
)

enum class ProductCategories {
    Bebidas, Pastas, Dulces, Fruta, Verdura, Carne, Panaderia, Cafe, Charcuteria
}

open class Product(
    private var title: String,
    private var description: String,
    private var price: Double,
    private var category: ProductCategories,
    private var image: Int,
    private var isFeatured: Boolean,
    private var isDiscounted: Boolean,
    private var discount: Int,
    private var isNew: Boolean,
) {

    fun getTitle(): String {
        return title
    }

    fun getPrice(): Double {
        return price
    }

    fun getCategory(): String {
        return category.toString()
    }

    fun getImage(): Int {
        return image
    }

    fun getFeatured(): Boolean {
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
        return (getPrice() - (getPrice()/100*getDiscount()))
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(200.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { navController.navigate("productScreen/${product.getTitle()}") {
            launchSingleTop = true
        } },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    painter = painterResource(product.getImage()),
                    contentDescription = "Imagen del producto",
                    contentScale = ContentScale.Crop
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
                        text = (if (product.getIsDiscounted()) product.getDiscountedPrice().toString() + "€" else { product.getPrice().toString() + " €" }),
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
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(if (product.getIsDiscounted()) Color.Red else Color.White)
                ) {
                    Text(
                        text = " -" + product.getDiscount().toString()+ "% ",
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
                    onClick = { /* Acción del botón */ },
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

@Composable
fun NewProductCard(product: Product, navController: NavController) {

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(260.dp),
        onClick = { navController.navigate("productScreen/${product.getTitle()}") {
            launchSingleTop = true
        } },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            Text(
                text = "NOVEDAD",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xff6fd5e9))
                    .padding(4.dp),
                fontFamily = FontFamily(Font(R.font.muli)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Image(
                painter = painterResource(id = product.getImage()),
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
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
                        text = (if (product.getIsDiscounted()) product.getDiscountedPrice().toString() + "€" else { product.getPrice().toString() + " €" }),
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
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End // Alinear a la derecha
            ) {
                Button(
                    modifier = Modifier
                        .height(50.dp)
                        .width(130.dp)
                        .padding(8.dp),
                    onClick = { /* Acción del botón */ },
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