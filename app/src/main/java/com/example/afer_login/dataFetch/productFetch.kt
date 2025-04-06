package com.example.afer_login.dataFetch

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class Product(val brand : String? = null,
                   val gender : String? = null,
                   val imgLink : String? = null,
                   val name : String? = null,
                   val price : Int? = null,
                   val sizes : String? = null,
                   val type : String? = null,
                   val shopItemlink : String? = null,
){
}

class ProductRepository {
    private val database: FirebaseDatabase = Firebase.database
    private val productsRef = database.getReference()
    
    // Fetch all products
    suspend fun getAllProducts(): List<Product> = withContext(Dispatchers.IO) {
        try {
            val snapshot = productsRef.get().await()
            val products = mutableListOf<Product>()
            for (childSnapshot in snapshot.children) {
                childSnapshot.getValue(Product::class.java)?.let {
                    products.add(it)
                }
            }
            return@withContext products
        } catch (e: Exception) {
            println("Error getting products: ${e.message}")
            return@withContext emptyList()
        }
    }
    
    // Fetch product by ID
    suspend fun getProductById(productId: String): Product? = withContext(Dispatchers.IO) {
        try {
            val snapshot = productsRef.child(productId).get().await()
            return@withContext snapshot.getValue(Product::class.java)
        } catch (e: Exception) {
            println("Error getting product: ${e.message}")
            return@withContext null
        }
    }
    
    // Fetch products by brand
    suspend fun getProductsByBrand(brand: String): List<Product> = withContext(Dispatchers.IO) {
        try {
            val snapshot = productsRef.orderByChild("brand").equalTo(brand).get().await()
            val products = mutableListOf<Product>()
            for (childSnapshot in snapshot.children) {
                childSnapshot.getValue(Product::class.java)?.let {
                    products.add(it)
                }
            }
            return@withContext products
        } catch (e: Exception) {
            println("Error getting products by brand: ${e.message}")
            return@withContext emptyList()
        }
    }
    
    // Fetch products with multiple filters
    suspend fun getFilteredProducts(
        brand: String? = null,
        type: String? = null,
        gender: String? = null,
        maxPrice: Int? = null
    ): List<Product> = withContext(Dispatchers.IO) {
        try {
            // First, query by the most selective filter
            val snapshot = when {
                brand != null -> productsRef.orderByChild("brand").equalTo(brand).get().await()
                type != null -> productsRef.orderByChild("type").equalTo(type).get().await()
                gender != null -> productsRef.orderByChild("gender").equalTo(gender).get().await()
                maxPrice != null -> productsRef.orderByChild("price").endAt(maxPrice.toDouble()).get().await()
                else -> productsRef.get().await()
            }
            
            // Apply remaining filters on client side
            val products = mutableListOf<Product>()
            for (childSnapshot in snapshot.children) {
                val product = childSnapshot.getValue(Product::class.java)
                product?.let {
                    if ((brand == null || it.brand == brand) &&
                        (type == null || it.type == type) &&
                        (gender == null || it.gender == gender) &&
                        (maxPrice == null || (it.price != null && it.price <= maxPrice))) {
                        products.add(it)
                    }
                }
            }
            return@withContext products
        } catch (e: Exception) {
            println("Error getting filtered products: ${e.message}")
            return@withContext emptyList()
        }
    }
}