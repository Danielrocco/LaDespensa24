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

val productsInStorage = listOf(
    Product("Queso", "500g aprox",12.02, "Charcuteria", R.drawable.queso, true, false, 0, true),
    Product("Pan", "1 barra de pan",12.02, "Panaderia", R.drawable.pan, true, true, 50, false),
    Product("Carne", "1k aprox",12.02, "Carniceria", R.drawable.carne, true, true, 40, false),
    Product("Manzana", "180g ",12.02, "Verdura", R.drawable.manzana, true, false, 0, true),
    Product("lechuga", "200g",12.02, "Verdura", R.drawable.lechuga, true, true, 60, false)
)

open class Product(
    private var title: String,
    private var description: String,
    private var price: Double,
    private var category: String,
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
        return category
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
}

@Composable
fun ProductCard(product: Product) {
    var isProductDiscounted = product.getIsDiscounted()
    if (!product.getIsDiscounted()) {
        isProductDiscounted = false
    }

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(Modifier.background(Color.White)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
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
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.getTitle(),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Descripcion",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 12.sp
                    )
                }
                Column {
                    Text(
                        text = (if (isProductDiscounted) (product.getPrice()/100*product.getDiscount()).toString() + "€/kg" else { product.getPrice().toString() + "€/kg" }),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        color = if (isProductDiscounted) Color.Black else Color.White,
                        text = product.getPrice().toString() + "€/kg",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 9.sp
                    )
                }
            }

            // Botón "Añadir a la cesta"
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .background(if (isProductDiscounted) Color.Red else Color.White)
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
                        .height(40.dp)
                        .width(120.dp)
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
fun DiscountedProductCard(product: Product) {

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    ) {
        Column(Modifier.background(Color.White)) {
            // Imagen en la parte superior
            Image(
                painter = painterResource(id = product.getImage()), // Reemplaza con tu imagen
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Altura de la imagen
            )

            // Fila para el título y el precio
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
                        text = "Descripcion",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontSize = 11.sp
                    )
                }
                Column {
                    Text(
                        text = (product.getPrice()/100*product.getDiscount()).toString() + "€/kg",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = product.getPrice().toString() + "€/kg",
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
                        .background(Color.Red)
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
                        .height(40.dp)
                        .width(120.dp)
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
fun NewProductCard(product: Product) {
    var isProductDiscounted = product.getIsDiscounted()
    if (!product.getIsDiscounted()) {
        isProductDiscounted = false
    }

    Card(
        modifier = Modifier
            .padding(12.dp)
            .width(280.dp),
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
                    .height(320.dp)
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
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
                        fontSize = 10.sp
                    )
                }
                Column {
                    Text(
                        text = (if (isProductDiscounted) (product.getPrice()/100*product.getDiscount()).toString() + "€/kg" else { product.getPrice().toString() + "€/kg" }),
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        color = if (isProductDiscounted) Color.Black else Color.White,
                        text = product.getPrice().toString() + "€/kg",
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
                        .height(40.dp)
                        .width(120.dp)
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