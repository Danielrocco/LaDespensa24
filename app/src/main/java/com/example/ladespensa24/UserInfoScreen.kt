package com.example.ladespensa24

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UserInfoScreen(navController: NavController, viewModel: MyViewModel) {
    Scaffold(
        content = { innerPadding ->
            UserInfoContent(innerPadding, navController, viewModel)
        },
        bottomBar = {
            AppFooter(modifier = Modifier.navigationBarsPadding().fillMaxWidth(), navController, viewModel)
        }
    )
}

@Composable
fun UserInfoContent(innerPadding: PaddingValues, navController: NavController, viewModel: MyViewModel) {
    var Nametext by remember { mutableStateOf("") }
    var SurnameText by remember { mutableStateOf("") }
    var AddressText by remember { mutableStateOf("") }
    var PaycardText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .padding(top = 36.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // Altura fija del Row
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nombre",
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
            Spacer(modifier = Modifier.size(16.dp))
            BasicTextField(
                value = Nametext,
                onValueChange = { Nametext = it },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFFF3F3F3),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp), // padding externo kawaii
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp) // 🌟 padding interno controlado
                    ) {
                        if (Nametext.isEmpty()) {
                            Text(
                                text = "Escribe aquí",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(36.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // Altura fija del Row
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Apellidos",
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
            Spacer(modifier = Modifier.size(16.dp))
            BasicTextField(
                value = SurnameText,
                onValueChange = { SurnameText = it },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFFF3F3F3),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp), // padding externo kawaii
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp) // 🌟 padding interno controlado
                    ) {
                        if (SurnameText.isEmpty()) {
                            Text(
                                text = "Escribe aquí",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(36.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // Altura fija del Row
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dirección",
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
            Spacer(modifier = Modifier.size(16.dp))
            BasicTextField(
                value = AddressText,
                onValueChange = { AddressText = it },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFFF3F3F3),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp), // padding externo kawaii
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp) // 🌟 padding interno controlado
                    ) {
                        if (AddressText.isEmpty()) {
                            Text(
                                text = "Escribe aquí",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(36.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), // Altura fija del Row
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tarjeta Pago",
                fontFamily = FontFamily(Font(R.font.muli)),
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
            )
            Spacer(modifier = Modifier.size(16.dp))
            BasicTextField(
                value = PaycardText,
                onValueChange = { PaycardText = it },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFFF3F3F3),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp), // padding externo kawaii
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.muli)),
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp) // 🌟 padding interno controlado
                    ) {
                        if (PaycardText.isEmpty()) {
                            Text(
                                text = "Escribe aquí",
                                fontFamily = FontFamily(Font(R.font.muli)),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(36.dp))
        SaveData("Guardar datos", navController, Color(0xffb5e354), Color.Black)
    }
}

@Composable
fun SaveData(titulo: String, navController: NavController, backgroundColor: Color, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(12.dp),
        onClick = {},
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)) {
            Text(
                text = titulo,
                fontFamily = FontFamily(Font(R.font.muli)),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}