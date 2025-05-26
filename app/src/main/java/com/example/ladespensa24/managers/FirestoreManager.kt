package com.example.ladespensa24.managers

import android.util.Log
import com.example.ladespensa24.models.Product
import com.example.ladespensa24.models.ProductCategories
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreManager {

    private val firestore = FirebaseFirestore.getInstance()

    fun getAllProductsFlow(): Flow<List<Product>> = callbackFlow {
        val ref = firestore.collection("products")

        val subscription = ref.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                val products = it.documents.mapNotNull { doc ->
                    val data = doc.data

                    if (data != null) {
                        Product(
                            id = doc.id,
                            title = data["title"] as String,
                            description = data["description"] as String,
                            price = when (val priceValue = data["price"]) {
                                is Long -> priceValue.toDouble()
                                is Double -> priceValue
                                else -> 0.0
                            },
                            category = ProductCategories.valueOf(data["category"] as String),
                            isFeatured = (data["isFeatured"] as? Boolean) ?: false,
                            isDiscounted = (data["isDiscounted"] as? Boolean) ?: false,
                            discount = when (val discountValue = data["discount"]) { // 🔥 Convertimos `Long` a `Int`
                                is Long -> discountValue.toInt()
                                is Int -> discountValue
                                else -> 0
                            },
                            isNew = (data["isNew"] as? Boolean) ?: false
                        )
                    } else {
                        null
                    }
                }
                trySend(products)
            }
        }

        awaitClose { subscription.remove() }
    }
}