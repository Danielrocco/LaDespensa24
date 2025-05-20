package com.example.ladespensa24

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun UserInfoScreen(navController: NavController, viewModel: MyViewModel) {

    val isLogged by viewModel.isLogged.observeAsState(false)

    Scaffold(
        content = { innerPadding ->
            UserInfoContent(innerPadding, navController, viewModel)
        },
        bottomBar = {
            AppFooter(modifier = Modifier.navigationBarsPadding().fillMaxWidth(), navController, viewModel, isLogged)
        }
    )
}

@Composable
fun UserInfoContent(innerPadding: PaddingValues, navController: NavController, viewModel: MyViewModel) {
    val context = LocalContext.current
    val user = viewModel.getUsuarioEnUso()

    var nameText by remember { mutableStateOf(user.getName()) }
    var surnameText by remember { mutableStateOf(user.getSurname()) }
    var addressText by remember { mutableStateOf(user.getAddress()) }
    var payCardText by remember { mutableStateOf(user.getPayCard()) }

    val isNameValid = nameText.isNotBlank()
    val isSurnameValid = surnameText.isNotBlank()
    val isAddressValid = addressText.isNotBlank()
    val isPayCardValid by remember(payCardText) {
        mutableStateOf(isValidPayCardNumber(payCardText))
    }
    Column {
        HeaderUserInfoContent()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(24.dp)
                .padding(top = 36.dp)
        ) {

            ValidatedTextField(
                label = "Nombre",
                value = nameText,
                onValueChange = { nameText = it },
                isValid = isNameValid
            )

            ValidatedTextField(
                label = "Apellidos",
                value = surnameText,
                onValueChange = { surnameText = it },
                isValid = isSurnameValid
            )

            ValidatedTextField(
                label = "Dirección",
                value = addressText,
                onValueChange = { addressText = it },
                isValid = isAddressValid
            )

            ValidatedTextField(
                label = "Tarjeta (16 digit)",
                value = payCardText,
                onValueChange = { payCardText = it },
                keyboardType = KeyboardType.Number,
                isValid = isPayCardValid
            )

            SaveData(
                titulo = "Guardar datos",
                backgroundColor = if (isPayCardValid) Color(0xffb5e354) else Color(0xFFBDBDBD),
                textColor = if (isPayCardValid) Color.Black else Color.Gray,
                enabled = isPayCardValid,
                onClick = {
                    if (isPayCardValid) {
                        user.setName(nameText)
                        user.setSurname(surnameText)
                        user.setAddress(addressText)
                        user.setPayCard(payCardText)

                        Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                        navController.navigate("userScreen") {
                            popUpTo("userScreen") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ValidatedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isValid: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF383838),
            unfocusedTextColor = Color(0xFF383838),
            focusedContainerColor = Color(0xFFF3F3F3),
            unfocusedContainerColor = Color(0xFFF3F3F3),
            disabledContainerColor = Color(0xFFF3F3F3),
            focusedIndicatorColor = if (isValid) Color.Green else Color.Red,
            unfocusedIndicatorColor = if (isValid) Color.Green else Color.Red,
        )
    )
    Spacer(modifier = Modifier.height(12.dp))
}


fun isValidPayCardNumber(cardNumber: String): Boolean {
    return cardNumber.length == 16 && cardNumber.all { it.isDigit() }
}

@Composable
fun SaveData(
    titulo: String,
    backgroundColor: Color,
    textColor: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(12.dp)
            .alpha(if (enabled) 1f else 0.5f),
        onClick = { if (enabled) onClick() },
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
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun HeaderUserInfoContent() {
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
                text = "EDITA TUS DATOS",
                fontFamily = FontFamily(Font(R.font.muli)),
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            NormalImage(
                Modifier
                    .height(24.dp),
                R.drawable.lapiz,
                Color.White
            )
        }
    }
}