package com.example.login.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.csci3310project.Screen
import com.example.login.R
import com.example.login.ui.theme.*

@Composable
fun registerScreen(navController: NavController){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var verification by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = "demo background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }


    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Text(
            text = "Register\n\nNow",
            fontSize = 48.sp,
            fontFamily = titlefontFamily,
            textAlign = TextAlign.Center,
            color = White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Inputdata("UserName", data = remember { mutableStateOf(username) })
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("Password", data = remember { mutableStateOf(password) })
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("Confirmed Password", data = remember { mutableStateOf(confirmedPassword) })
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("Email", data = remember { mutableStateOf(email) })
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("Phone Number", data = remember { mutableStateOf(phoneNumber) })
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("Verification Code", data = remember { mutableStateOf(verification) })
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 80.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(
                    modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(RegisterPageButtonColor),
                    shape =RoundedCornerShape(5.dp),
                    onClick = {
                        navController.navigate(Screen.Login.route)
                    }
                ) {
                    Text("Back",fontSize = 16.sp, fontFamily = PromptfontFamily, color = Black)
                }
                Button(
                    modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(RegisterPageButtonColor),
                    shape =RoundedCornerShape(5.dp),
                    onClick = {
                        navController.navigate(Screen.AvatarCreation.route)
                    }
                ) {
                    Text("Next",fontSize = 16.sp, fontFamily = PromptfontFamily, color = Black)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun Inputdata(Prompt:String ,data: MutableState<String>){
    Column (modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text("$Prompt:", fontSize = 16.sp, fontFamily = PromptfontFamily )
        TextField(
            value = data.value,
            onValueChange = { data.value = it },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(300.dp)
                .padding(vertical = 4.dp, horizontal = 10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Transparent,
                unfocusedIndicatorColor = Transparent
            )
        )
    }

}


