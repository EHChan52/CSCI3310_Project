
package com.example.afer_login.Homepage

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.afer_login.*
import android.util.Log
import com.example.afer_login.dataFetch.DataFetcher
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomePageContent(navController: NavController){
    

    // Use state to hold the fetched items
    var allItems by remember { mutableStateOf(listOf<Any>()) }
    val dataFetcher = remember { DataFetcher() }

    // Launch the data fetching in a coroutine
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            dataFetcher.connect("mongodb://Admin:12345678@20.2.218.171:5222/", "csci3310Project")
            dataFetcher.fetchData("clothes")
            val items = dataFetcher.getAllItems()
            // Update state on the main thread
            allItems = items
        }
    }


    // Add logging to view fetched data
    val TAG = "HomePage"
    Log.d(TAG, "Total items fetched: ${allItems.size}")
    allItems.forEach { item ->
        Log.d(TAG, "Item: $item")
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

        var myList = getAllsearchedClothes()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding =  PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(myList, itemContent = { index, item ->
                ClothesItem(item = item)
            }
            )
        }
    }

}


@Composable
private fun ClothesItem(item: searchedClothes){
    Image(painter = painterResource(id = item.imageRes),
        contentDescription = item.name,
        modifier = Modifier.size(150.dp).padding(3.dp))
}


