package com.example.afer_login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.csci3310.R
import com.example.afer_login.dataFetch.User
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SettingPage(user: User? = null){

    Column(modifier = Modifier.padding(15.dp)){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically)
        {
            Spacer(modifier = Modifier.width(8.dp))
            
            // Display user profile image from imgLink if available, otherwise use default
            if (user != null && user.customClaims.imgLink.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.customClaims.imgLink)
                        .crossfade(true)
                        .build(),
                    contentDescription = "User profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.usericon),
                    contentDescription = "Default user image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
            }
            
            Spacer(modifier = Modifier.width(15.dp))
            
            // Display the user's displayName if available, otherwise show default text
            Text(
                text = if (user != null) "  ${user.displayName}" else "  User Name",
                color = setting_page_font,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontFamily = TextFont,
                modifier = Modifier.wrapContentWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = setting_page_font, thickness = 2.dp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "  Avatar Management  ",
            color = setting_page_font,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            fontFamily = TextFont,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
        UniformButton("Edit Avatar", onClick = {}, modifier = Modifier)
        Spacer(modifier = Modifier.height(8.dp))
        UniformButton("Delete Avatar", onClick = {}, modifier = Modifier)
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "  Account Management  ",
            color = setting_page_font,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            fontFamily = TextFont,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        UniformButton("Change Password", onClick = {}, modifier = Modifier)
        Spacer(modifier = Modifier.height(8.dp))
        UniformButton("Change Icon", onClick = {}, modifier = Modifier)
        Spacer(modifier = Modifier.height(8.dp))
        UniformButton("Change Email", onClick = {}, modifier = Modifier)
        }
    }
}

@Composable
fun UniformButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(3.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = buttonTextColor,
            containerColor = buttonColor
        )
    )
    {
        Text(text = text,
            color = setting_page_font,
            fontFamily = TextFont2,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth())
    }
}
