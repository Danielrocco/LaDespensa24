package com.example.ladespensa24

open class User(
    private var name: String,
    private var surname: String,
    private var address: String,
    private var payCard: Int,
    private var favouriteProducts: List<Product>?,
    private var boughtProducts: List<Product>?

    ) {

}