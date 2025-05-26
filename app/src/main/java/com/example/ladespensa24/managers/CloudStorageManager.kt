package com.example.ladespensa24.managers

import com.example.ladespensa24.models.Product
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

class CloudStorageManager {
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    fun getProductImage(product: Product, callback: (String?) -> Unit) {
        val imageRef = storageRef.child("products/${product.title.lowercase()}.png")

        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                callback(uri.toString())
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}