package com.example.afer_login.Fitting_and_cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.afer_login.ButtonTextFont
import com.example.afer_login.SearchFont
import com.example.afer_login.TextFont
import com.example.afer_login.buttonColor
import com.example.afer_login.cardColor
import com.example.afer_login.searchBar
import com.example.afer_login.searchBarBound
import com.example.afer_login.searchBar_Text

@Composable
fun cartPage(){
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
            .padding(bottom = 70.dp) // Add padding at bottom to make space for buttons
        ) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)) {
                var searchingitem by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier
                        .width(280.dp)
                        .padding(horizontal = 5.dp),
                    value = searchingitem,
                    onValueChange = {newText -> searchingitem = newText},
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedLabelColor = searchBar,
                        unfocusedContainerColor = searchBar,
                        unfocusedIndicatorColor = searchBar_Text,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = searchBarBound,
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon", tint = searchBar_Text)
                    },
                    
                    label = {Text("Search in cart", fontFamily = SearchFont, color = searchBar_Text)},
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    )
                )
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = buttonColor
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top= 8.dp, start = 5.dp, end = 5.dp)
                ){
                    Text(text = "Search", fontFamily = ButtonTextFont, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            var myList = getAllSavedClothes()

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(myList, itemContent = { index, item ->
                    ClothesCard(item = item)
                })
            }
        }
        
        // Display cart summary information
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 8.dp)
                .padding(bottom = 50.dp)
        ) {
            val itemCount = getAllSavedClothes().size
            val totalPrice = getAllSavedClothes().sumOf { it.price ?: 0 }
            
            Text(
                text = "Total Items: $itemCount | Total Price: $$totalPrice",
                fontFamily = TextFont,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Fixed button row at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 15.dp, vertical = 15.dp)
        ) {
            Button(
                onClick = { /* TODO: Implement back action */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = buttonColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = "Remove", fontFamily = ButtonTextFont, fontSize = 14.sp)
            }
            
            Button(
                onClick = { /* TODO: Implement clear cart */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = buttonColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = "Clear Cart", fontFamily = ButtonTextFont, fontSize = 14.sp)
            }
            
            Button(
                onClick = { /* TODO: Implement checkout */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    containerColor = buttonColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = "Checkout", fontFamily = ButtonTextFont, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun ClothesCard(item: savedClothes){
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        content = {
            Row(){
                // Use Coil to load images from URLs
                Image(
                    painter = rememberImagePainter(item.imgLink),
                    contentDescription = item.name ?: "Clothing item",
                    modifier = Modifier.size(150.dp).padding(3.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Column(){
                    Text(text = "Brand: ${item.brand ?: "N/A"}", fontFamily = TextFont )
                    Text(text = "Size: ${item.sizes ?: "N/A"}", fontFamily = TextFont )
                    Text(text = "Type: ${item.type ?: "N/A"}", fontFamily = TextFont )
                    Text(text = item.name ?: "Unknown", fontFamily = TextFont )
                    Text(text = "Price: $${item.price ?: 0}", fontFamily = TextFont )
                }
            }
        }
    )
}