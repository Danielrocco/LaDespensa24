package com.example.ladespensa24

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ProductScreen() {
    Scaffold(
        topBar = {
            ProductHeader()
        }, content = { innerPadding ->
            ProductScreenContent(innerPadding)
        }
    )
}

@Composable
fun ProductScreenContent(innerPadding: PaddingValues) {
    Column {
        Box(modifier = Modifier.fillMaxSize()) {

        }
    }
}

@Composable
fun ProductHeader() {
    TODO("Not yet implemented")
}
