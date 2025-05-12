package com.example.ladespensa24

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

open class User(
    private var name: String,
    private var surname: String,
    private var email: String,
    private var passwd: String,
    private var address: String,
    private var payCard: String,
    private var cartProducts: MutableList<InCartProduct>?,
    private var favouriteProducts: List<Product>?,
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

    fun getFavouriteProducts(): List<Product>? {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun buyProducts() {
        val localDate = LocalDate.now()
        val date: Date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val purchasedList = mutableListOf<InCartProduct>()

        cartProducts?.forEach { product ->
            purchasedList.add(
                InCartProduct(
                    product.getTitle(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategory(),
                    product.getImage(),
                    product.getFeatured(),
                    product.getIsDiscounted(),
                    product.getDiscount(),
                    product.getIsNew(),
                    product.getTotalPrice(),
                    product.getUnits()
                )
            )
        }

        // 💝 Crear la nueva Purchase
        val newPurchase = Purchase(purchasedList, date)

        // 🛍️ Agregarla a la lista de purchases
        if (purchases == null) {
            purchases = mutableListOf()
        }
        (purchases as MutableList<Purchase>).add(newPurchase)

        // 🧼 Limpiar carrito
        cartProducts?.clear()
    }


    fun removeFromKart(productTitle: String) {
        cartProducts?.removeIf { it.getTitle() == productTitle }
    }

    fun addToCart(product: Product, units: Int) {
        var existentProduct: InCartProduct? = null

        for (item in cartProducts.orEmpty()) {
            if (item.getTitle() == product.getTitle() ) {
                existentProduct = item
                break
            }
        }

        if (existentProduct != null) {
            existentProduct.addUnits()
        } else {
            cartProducts?.add(
                InCartProduct(
                    product.getTitle(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategory(),
                    product.getImage(),
                    product.getFeatured(),
                    product.getIsDiscounted(),
                    product.getDiscount(),
                    product.getIsNew(),
                    product.getPrice() * units,
                    units
                )
            )
        }
    }
}