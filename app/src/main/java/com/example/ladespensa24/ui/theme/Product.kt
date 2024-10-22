package com.example.ladespensa24.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ladespensa24.R

val productsInStorage = listOf(
    Product("Pan", 12.02, "Panaderia", R.drawable.pan, true),
    Product("Queso", 12.02, "Charcuteria", R.drawable.queso, true),
    Product("Carne", 12.02, "Carniceria", R.drawable.carne, true),
    Product("Manzana", 12.02, "Verdura", R.drawable.manzana, true),
    Product("lechuga", 12.02, "Verdura", R.drawable.queso, true),
)

open class Product(
    private var title: String,
    private var price: Double,
    private var category: String,
    private var image: Int,
    private var isTrending: Boolean
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

    fun getTrending(): Boolean {
        return isTrending
    }
}

@Preview
@Composable
fun CardProduct() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(R.drawable.queso),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
            )
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .padding(14.dp)
            ) {
                Text(
                    text = "Queso",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    fontFamily = FontFamily(Font(R.font.muli))
                )
                Text(
                    text = "12.00€",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily(Font(R.font.muli))
                )
            }
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Black
                )
            ) {
                Text(
                    text = "Añadir a la cesta",
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Preview
@Composable
fun ProductCard2() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column {
            // Imagen en la parte superior
            Image(
                painter = painterResource(id = R.drawable.queso), // Reemplaza con tu imagen
                contentDescription = "Imagen del producto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Altura de la imagen
            )

            // Fila para el título y el precio
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Título del Producto",
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "$29.99",
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            // Botón en la parte inferior derecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Unidades en carro")
                    Text("9")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /* Acción del botón */ }) {
                    Text(
                        "Agregar al carrito",
                        fontFamily = FontFamily(Font(R.font.muli)),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

