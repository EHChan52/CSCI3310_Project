package com.example.afer_login.Homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.afer_login.*
import kotlin.random.Random


@Composable
fun SearchPage(navController:NavController){
    Column(modifier = Modifier.padding(15.dp)) {
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
        Spacer(modifier = Modifier.height(15.dp))
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
        Spacer(modifier = Modifier.height(15.dp))
        Text("Trending Now", fontFamily = ButtonTextFont, fontSize = 20.sp)
        RandomWordList()
        Spacer(modifier = Modifier.height(15.dp))

        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
            Text("Recent Search", fontFamily = ButtonTextFont,fontSize = 20.sp)
            Button(colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent),
                onClick = {

            }){
                Text("Delete All", fontFamily = ButtonTextFont,
                    color = searchBar_Text,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }



    }

}
@Composable
fun RandomWordList(wordCount: Int = 20,minCellSize: Int = 50) {
    // Use remember so that the random words are generated only once per composition.
    val words = remember { generateRandomWords(wordCount) }

    LazyVerticalGrid (modifier = Modifier.padding(8.dp), columns = GridCells.Adaptive(minSize = minCellSize.dp)) {
        items(items = words) { word ->
            Text(
                text = word,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.padding(8.dp).wrapContentSize().clip(RoundedCornerShape(10.dp)).background(color = searchBar)
            )
        }
    }
}

/**
 * Utility function to generate a list of random words. Each word's length is random
 * in a range (e.g., between 3 and 10 characters).
 */
fun generateRandomWords(count: Int): List<String> {
    val random = Random(System.currentTimeMillis())
    return List(count) {
        val wordLength = random.nextInt(3, 11) // random length between 3 and 10 characters
        (1..wordLength)
            .map { ('a'..'z').random(random) }
            .joinToString("")
    }
}
