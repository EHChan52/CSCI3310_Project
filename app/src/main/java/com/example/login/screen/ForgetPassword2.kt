package com.example.login.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.csci3310project.Screen
import com.example.login.R
import com.example.login.ui.theme.LoginPageButtonColor
import com.example.login.ui.theme.PromptfontFamily
import com.example.login.ui.theme.RegisterPageButtonColor
import com.example.login.ui.theme.titlefontFamily

@Composable
fun ForgetPassword2Screen(navController: NavController){
    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.forgetpassword2),
            contentDescription = "demo background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "Confirm\n\nPassword",
            fontSize = 48.sp,
            fontFamily = titlefontFamily,
            textAlign = TextAlign.Center,
            color = White
        )
        Column (modifier = Modifier.padding(20.dp).wrapContentSize().background(color = White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("New Password", data = remember { mutableStateOf(password) })
            Spacer(modifier = Modifier.height(10.dp))
            Inputdata("Confirm New Password", data = remember { mutableStateOf(confirmedPassword) })
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(RegisterPageButtonColor),
                shape =RoundedCornerShape(5.dp),
                onClick = {
                    navController.navigate(Screen.Login.route)
                }
            ) {
                Text("Confirm",fontSize = 16.sp, fontFamily = PromptfontFamily, color = Black)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

}
