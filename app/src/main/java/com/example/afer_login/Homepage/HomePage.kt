package com.example.afer_login.Homepage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.afer_login.Fitting_and_cart.setAllSavedClothes
import com.example.afer_login.SearchFont
import com.example.afer_login.TextFont
import com.example.afer_login.buttonColor
import com.example.afer_login.dataFetch.Product
import com.example.afer_login.dataFetch.ProductRepository
import com.example.afer_login.searchBar
import com.example.afer_login.searchBar_Text
import com.example.afer_login.setting_page_font

private const val TAG = "HomePageContent"

@Composable
fun HomePageContent(navController: NavController){
    // State to store the fetched products
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val repository = remember { ProductRepository() }
    
    // Fetch products when the composable is first displayed
    LaunchedEffect(key1 = Unit) {
        try {
            // Call the repository function to get all products
            val fetchedProducts = repository.getAllProducts()
            
            // Update the state with the fetched products
            products = fetchedProducts
            isLoading = false
            
            // Log the count and details of fetched products
            Log.d(TAG, "Successfully fetched ${fetchedProducts.size} products from Firestore")
            fetchedProducts.forEachIndexed { index, product ->
                Log.d(TAG, "Product ${index + 1}: Name=${product.name}, Brand=${product.brand}, " +
                        "Price=${product.price}, Type=${product.type}, Gender=${product.gender}, " +
                        "Sizes=${product.sizes}, ImageLink=${product.imgLink}, shopItemLink=${product.shopItemlink}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching products: ${e.message}", e)
            isLoading = false
        }
    }
    
    Column (modifier = Modifier.fillMaxSize().padding(15.dp), verticalArrangement = Arrangement.Top){
        Text(
            text = "What you are looking for?",
            color = setting_page_font,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            fontFamily = TextFont,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = searchBar,
                contentColor = searchBar_Text),
                onClick = {  navController.navigate(Screen.Search.route)})
            {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon", tint = searchBar_Text)
            Text("Search", fontFamily = SearchFont, color = searchBar_Text)
            }
        Spacer(modifier = Modifier.height(5.dp))
        
        // Display loading message or product grid
        if (isLoading) {
            Text(
                text = "Loading products...",
                fontFamily = TextFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            // Use products from Firebase instead of getAllsearchedClothes()
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
            ) {
                items(products) { product ->
                    ClothesItem(product = product)
                }
            }
        }
    }
}

@Composable
private fun ClothesItem(product: Product) {
    // State to track if dialog is showing
    var showDialog by remember { mutableStateOf(false) }
    
    // If dialog should be shown, display it
    if (showDialog) {
        ProductDetailDialog(
            product = product,
            onDismiss = { showDialog = false }
        )
    }
    
    Column(
        // Make the column clickable
        modifier = Modifier.clickable { showDialog = true }
    ) {
        // Load image from URL using AsyncImage (Coil)
        AsyncImage(
            model = product.imgLink,
            contentDescription = product.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(150.dp).padding(3.dp)
        )
        product.name?.let {
            Text(
                text = it + "  @ ${product.brand}",
                fontFamily = TextFont,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
        }
        product.price?.let {
            Text(
                text = "$$it",
                fontFamily = TextFont,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Add a new composable for the product detail dialog
@Composable
private fun ProductDetailDialog(product: Product, onDismiss: () -> Unit) {
    val context = LocalContext.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = product.name ?: "Product Details",
                fontFamily = TextFont,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                AsyncImage(
                    model = product.imgLink,
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Brand: ${product.brand ?: "N/A"}",
                    fontFamily = TextFont
                )
                
                Text(
                    text = "Price: $${product.price ?: "N/A"}",
                    fontFamily = TextFont
                )
                
                Text(
                    text = "Type: ${product.type ?: "N/A"}",
                    fontFamily = TextFont
                )
                
                Text(
                    text = "Gender: ${
                        when (product.gender) {
                            "1" -> "Man"
                            "2" -> "Woman"
                            "3" -> "Unisex"
                            else -> product.gender ?: "N/A"
                        }
                    }",
                    fontFamily = TextFont
                )
                
                Text(
                    text = "Available Sizes: ${product.sizes ?: "N/A"}",
                    fontFamily = TextFont
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    // Add the product to savedClothes list
                    setAllSavedClothes(product)
                    
                    // Show a confirmation toast
                    Toast.makeText(
                        context, 
                        "${product.name} added to cart", 
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    onDismiss() 
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.Black
                )
            ) {
                Text("Add to Cart", fontFamily = TextFont)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = searchBar_Text
                )
            ) {
                Text("Cancel", fontFamily = TextFont)
            }
        }
    )
}


