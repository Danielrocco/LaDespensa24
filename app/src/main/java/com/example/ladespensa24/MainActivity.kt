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
import com.example.ladespensa24.authScreens.LoginScreen
import com.example.ladespensa24.authScreens.RegisterScreen
import com.example.ladespensa24.models.Product
import com.example.ladespensa24.otherScreens.CategoriesScreen
import com.example.ladespensa24.otherScreens.FilteredCategoriesScreen
import com.example.ladespensa24.otherScreens.ProductScreen
import com.example.ladespensa24.otherScreens.SearchScreen
import com.example.ladespensa24.userScreens.CartScreen
import com.example.ladespensa24.userScreens.FavouriteScreen
import com.example.ladespensa24.userScreens.PurchaseTicketScreen
import com.example.ladespensa24.userScreens.PurchasesScreen
import com.example.ladespensa24.userScreens.UserInfoScreen
import com.example.ladespensa24.userScreens.UserScreen
import com.example.ladespensa24.viewmodel.MyViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

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

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { HomeScreen(navController, viewModel) }
        composable("loginScreen") { LoginScreen(navController, viewModel) }
        composable("registerScreen") { RegisterScreen(navController, viewModel) }
        composable("favouriteScreen") { FavouriteScreen(navController, viewModel) }
        composable("categoriesScreen") { CategoriesScreen(navController, viewModel) }
        composable("cartScreen") { CartScreen(navController, viewModel) }
        composable("searchScreen") { SearchScreen(navController, viewModel) }
        composable("userInfoScreen") { UserInfoScreen(navController, viewModel) }
        composable("userScreen") { UserScreen(navController, viewModel) }
        composable("purchasesScreen") { PurchasesScreen(navController, viewModel) }

        composable(
            "productScreen/{productName}",
            arguments = listOf(navArgument("productName") { type = NavType.StringType })
        ) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            val product = productName?.let { getProductByName(it, viewModel) }
            if (product != null) {
                ProductScreen(navController, viewModel, product)
            }
        }

        composable(
            "filteredCategoriesScreen/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            FilteredCategoriesScreen(navController, viewModel, category)
        }

        composable(
            "purchaseDetails/{purchaseId}",
            arguments = listOf(navArgument("purchaseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val purchaseId = backStackEntry.arguments?.getString("purchaseId")
            val purchase = purchaseId?.let { id ->
                viewModel.getUsuarioEnUso().getPurchases()?.find { it.getId() == id }
            }
            if (purchase != null) {
                PurchaseTicketScreen(navController, purchase)
            }
        }
    }
}

private fun getProductByName(productName: String, viewModel: MyViewModel): Product? {
    return viewModel.products.value.find { it.getTitle() == productName }
}