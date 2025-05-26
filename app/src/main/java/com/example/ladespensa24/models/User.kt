package com.example.ladespensa24.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.ladespensa24.viewmodel.MyViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class User(
    var name: String = "",
    var surname: String = "",
    var address: String = "",
    var payCard: String = "",
    var cartProducts: MutableList<InCartProduct> = mutableListOf(),
    var favouriteProducts: MutableList<Product> = mutableListOf(),
    var purchases: MutableList<Purchase> = mutableListOf(),
) {

    fun getEntireAmountOfCart(): Double {
        var entireAmount = 0.0

        cartProducts?.forEach { product ->
            entireAmount += product.getTotalPrice()
        }
        return entireAmount

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buyProducts(viewModel: MyViewModel) {
        val localDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = localDate.format(formatter)

        val purchasedList = cartProducts.map { product ->
            InCartProduct(
                product.id, product.title, product.description,
                product.price, product.category, product.isFeatured,
                product.isDiscounted, product.discount,
                product.isNew, product.units
            )
        }

        val newPurchase = Purchase(
            inCartProducts = purchasedList,
            date = formattedDate,
            cardNumber = payCard
        )

        purchases.add(newPurchase)
        cartProducts.clear()

        // **Actualizar en Firestore inmediatamente**
        viewModel.actualizarUsuarioEnFirestore(this)
    }

    fun addToCart(product: Product, units: Int, viewModel: MyViewModel) {
        val existentProduct = cartProducts.find { it.title == product.title }
        existentProduct?.addUnits() ?: cartProducts.add(
            InCartProduct(
                product.id, product.title, product.description,
                product.price, product.category, product.isFeatured,
                product.isDiscounted, product.discount,
                product.isNew, units
            )
        )
        viewModel.actualizarUsuarioEnFirestore(this) // 🔥 Actualización inmediata
    }

    fun removeFromKart(productTitle: String, viewModel: MyViewModel) {
        cartProducts?.removeIf { it.title == productTitle }
        viewModel.actualizarUsuarioEnFirestore(this) // 🔥 Actualización inmediata en Firestore
    }

    fun addToFavorites(product: Product, viewModel: MyViewModel) {
        if (favouriteProducts == null) {
            favouriteProducts = mutableListOf()
        }
        if (!favouriteProducts!!.contains(product)) {
            favouriteProducts!!.add(product)
        }
        viewModel.actualizarUsuarioEnFirestore(this) // 🔥 Actualización inmediata en Firestore
    }

    fun removeFromFavorites(product: Product, viewModel: MyViewModel) {
        favouriteProducts?.remove(product)
        viewModel.actualizarUsuarioEnFirestore(this) // 🔥 Actualización inmediata en Firestore
    }

    fun isFavorite(product: Product): Boolean {
        return favouriteProducts?.contains(product) ?: false
    }

}