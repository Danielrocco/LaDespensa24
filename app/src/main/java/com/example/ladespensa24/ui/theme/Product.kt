package com.example.ladespensa24.ui.theme

import com.example.ladespensa24.R

val productsInStorage = listOf(
    Product("Pan", 12.02, "Panaderia", R.drawable.pan, true),
    Product("Queso", 12.02, "Charcuteria", R.drawable.pan, true),
    Product("Carne", 12.02, "Carniceria", R.drawable.pan, true),
    Product("Manzana", 12.02, "Verdura", R.drawable.pan, true),
    Product("lechuga", 12.02, "Verdura", R.drawable.pan, true),
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