package com.example.afer_login.Homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.afer_login.*
import com.example.csci3310.R

@Composable
fun ResultPage(navController: NavController){
    Column (modifier = Modifier.padding(15.dp)){
        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Button(
                modifier = Modifier.padding(horizontal = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = searchBar,
                    contentColor = searchBar_Text
                ),
                onClick = {  navController.navigate(Screen.Home.route)})
            {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "return button", tint = searchBar_Text)
            }

            var searchitem by remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier.width(280.dp).padding(horizontal = 5.dp),
                value = searchitem,
                onValueChange = {newText -> searchitem = newText},
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

                label = {Text("Search Clothes", fontFamily = SearchFont, color = searchBar_Text)},
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor
            ),
            onClick = {  navController.navigate(Screen.Result.route)})
        {
            Text("Search", fontFamily = SearchFont, color = buttonTextColor)
        }
        Spacer(modifier = Modifier.height(3.dp))
        HorizontalDivider(color = searchBar_Text)
        Spacer(modifier = Modifier.height(3.dp))
        Row (modifier = Modifier,horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
            Button(
                modifier = Modifier.padding(3.dp).wrapContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = searchBar
                ),
                onClick = {}) {
                Text("price", color = buttonTextColor)
            }
            Button(
                modifier = Modifier.padding(3.dp).wrapContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = searchBar
                ),
                onClick = {}) {
                Text("most searched",color = buttonTextColor)
            }
            Button(
                modifier =  Modifier.padding(3.dp).wrapContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = searchBar,
                    ),
                onClick = { },
            ){
                Image(
                    painter = painterResource(id = R.drawable.sort), // Replace with your drawable resource ID
                    contentDescription = "Circular Image",
                    modifier = Modifier.size(20.dp)
                )
            }
            Button(
                modifier =  Modifier.padding(3.dp).wrapContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = searchBar,
                ),
                onClick = { },
            ){
                Image(
                    painter = painterResource(id = R.drawable.filter), // Replace with your drawable resource ID
                    contentDescription = "Circular Image",
                    modifier = Modifier.size(20.dp)
                )

            }
        }
        //HorizontalDivider(color = searchBar_Text)
        var myList = getAllsearchedClothes()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding =  PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(myList, itemContent = { index, item ->
                SearchedItems(item = item)
            }
            )
        }
        }
}
@Composable
private fun SearchedItems(item: searchedClothes) {
    // Move the toggle state here so it persists for the lifetime of this composable
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(150.dp)
                    .padding(3.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                // Row for type and favorite button.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        fontFamily = TextFont,
                        fontSize = 14.sp,
                    )
                    // The favorite toggle button.
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { isFavorite = !isFavorite } // Toggle state on click
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (isFavorite) "Favorite" else "Not Favorite",
                            tint = if (isFavorite) Color.Red else searchBar_Text
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Row for item name and price.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Brand",
                        fontFamily = TextFont,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "price",
                        fontFamily = TextFont,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
