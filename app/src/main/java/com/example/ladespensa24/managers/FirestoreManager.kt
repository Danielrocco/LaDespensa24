package com.example.ladespensa24.managers

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
                    val product = doc.toObject(Product::class.java)
                    product?.apply { id = (doc.id) }
                }
                trySend(products)
            }
        }

        awaitClose { subscription.remove() }
    }
}