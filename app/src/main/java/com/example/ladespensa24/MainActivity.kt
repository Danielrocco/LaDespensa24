package com.example.ladespensa24

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.ladespensa24.ui.theme.LaDespensa24Theme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaDespensa24Theme {
                    MyApp()
                }
            }
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.black)

        }
    }

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val viewModel: MyViewModel = viewModel()

    NavHost(navController = navController, startDestination = "userScreen" + "") {
        composable("mainScreen") { MainScreen(navController, viewModel) }
        composable("userScreen") { UserScreen(navController, viewModel) }
        composable("categoriesScreen") { CategoriesScreen(navController, viewModel) }
        composable(
            "productScreen/{productName}",
            arguments = listOf(navArgument("productName") { type = NavType.StringType })
        ) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            val product = productName?.let { getProductByName(it) }
            if (product != null) {
                ProductScreen(navController, viewModel, product = product)
            }
        }
    }
}

private fun getProductByName(productName: String): Product? {
    return productsInStorage.find { it.getTitle() == productName }
}