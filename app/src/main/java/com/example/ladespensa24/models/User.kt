package com.example.ladespensa24.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class User(
    private var name: String,
    private var surname: String,
    private var email: String,
    private var passwd: String,
    private var address: String,
    private var payCard: String,
    private var cartProducts: MutableList<InCartProduct>?,
    private var favouriteProducts: MutableList<Product>?,
    private var purchases: MutableList<Purchase>?,
) {

    fun getName(): String {
        return name
    }

    fun getSurname(): String {
        return surname
    }

    fun getEmail(): String {
        return email
    }

    fun getPasswd(): String {
        return passwd
    }

    fun getAddress(): String {
        return address
    }

    fun getPayCard(): String {
        return payCard
    }

    fun getCart(): MutableList<InCartProduct>? {
        return cartProducts
    }

    fun getFavouriteProducts(): MutableList<Product>? {
        return favouriteProducts
    }

    fun getPurchases(): MutableList<Purchase>? {
        return purchases
    }

    fun getEntireAmountOfCart(): Double {
        var entireAmount = 0.0

        cartProducts?.forEach { product ->
            entireAmount += product.getTotalPrice()
        }
        return entireAmount

    }

    fun setName(value: String) {
        name = value
    }

    fun setSurname(value: String) {
        surname = value
    }

    fun setAddress(value: String) {
        address = value
    }

    fun setPayCard(value: String) {
        payCard = value
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun buyProducts() {
        val localDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = localDate.format(formatter)

        val purchasedList = mutableListOf<InCartProduct>()

        cartProducts?.forEach { product ->
            purchasedList.add(
                InCartProduct(
                    product.id,
                    product.getTitle(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategory(),
                    product.getIsFeatured(),
                    product.getIsDiscounted(),
                    product.getDiscount(),
                    product.getIsNew(),
                    product.getUnits()
                )
            )
        }

        val cardNumber = getPayCard()

        val newPurchase = Purchase(
            inCartProducts = purchasedList,
            date = formattedDate,
            cardNumber = cardNumber
        )

        if (purchases == null) {
            purchases = mutableListOf()
        }
        (purchases as MutableList<Purchase>).add(newPurchase)

        cartProducts?.clear()
    }

    fun removeFromKart(productTitle: String) {
        cartProducts?.removeIf { it.getTitle() == productTitle }
    }

    fun addToFavorites(product: Product) {
        if (favouriteProducts == null) {
            favouriteProducts = mutableListOf()
        }
        if (!favouriteProducts!!.contains(product)) {
            favouriteProducts!!.add(product)
        }
    }

    fun removeFromFavorites(product: Product) {
        favouriteProducts?.remove(product)
    }

    fun isFavorite(product: Product): Boolean {
        return favouriteProducts?.contains(product) ?: false
    }

    fun addToCart(product: Product, units: Int) {
        var existentProduct: InCartProduct? = null

        for (item in cartProducts.orEmpty()) {
            if (item.getTitle() == product.getTitle()) {
                existentProduct = item
                break
            }
        }

        if (existentProduct != null) {
            existentProduct.addUnits()
        } else {
            cartProducts?.add(
                InCartProduct(
                    product.id,
                    product.getTitle(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategory(),
                    product.getIsFeatured(),
                    product.getIsDiscounted(),
                    product.getDiscount(),
                    product.getIsNew(),
                    units
                )
            )
        }
    }
}