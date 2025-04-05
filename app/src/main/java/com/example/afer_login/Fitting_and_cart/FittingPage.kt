package com.example.afer_login.Fitting_and_cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csci3310.R
import com.example.afer_login.*

@Preview
@Composable
fun FittingPage(){
    Column(modifier = Modifier.padding(15.dp)){
        Box(modifier = Modifier.fillMaxWidth().height(500.dp)){
            //where the avatar place
            Image(
                painter = painterResource(id = R.drawable.fitting_room_background),
                contentDescription = "Fitting room background",
                modifier = Modifier.fillMaxWidth().height(500.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)) {
                Image(
                    painter = painterResource(id = R.drawable.human_avatar_default),
                    contentDescription = "Human head",
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(20.dp)
                )

            }
            Column(horizontalAlignment = Alignment.End,
                modifier = Modifier.align(Alignment.BottomEnd)) {
                ButtonsForAvatar(R.drawable.plus, onClick = {})
                ButtonsForAvatar(R.drawable.transfer, onClick = {})
                ButtonsForAvatar(R.drawable.maximize, onClick = {})
            }
        }

        HorizontalDivider( thickness = 2.dp, color = setting_page_font, modifier = Modifier.padding(horizontal = 100.dp))

        Spacer(modifier = Modifier.height(3.dp))
        Text(text = "Fitting", fontFamily = TextFont, fontSize = 22.sp)
        var myList = getAllSavedClothes()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding =  PaddingValues(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(myList, itemContent = {
                index, it1 ->  ClothesinCart(it1)
            }
            )
        }
    }
}

@Composable
private fun ClothesinCart(item: savedClothes){
    Image(painter = painterResource(id = item.imageRes),
        contentDescription = item.name,
        modifier = Modifier.size(150.dp).padding(vertical = 3.dp, horizontal = 2.dp))
}

@Composable
fun ButtonsForAvatar(
    imageId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = { },
        modifier = Modifier.padding(3.dp),
        shape = RoundedCornerShape(0.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,

        )
    ){
        Image(
            painter = painterResource(id = imageId), // Replace with your drawable resource ID
            contentDescription = "Circular Image",
            modifier = Modifier.size(20.dp)
        )

    }
}

