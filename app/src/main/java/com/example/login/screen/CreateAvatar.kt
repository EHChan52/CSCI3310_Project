@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.login.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login.R
import com.example.login.ui.theme.PromptfontFamily
import com.example.login.ui.theme.titlefontFamily
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.navigation.NavController
import com.example.login.ui.theme.AbuttonColor
import com.example.login.ui.theme.processbar
import com.example.login.ui.theme.processbarbackground

@Composable
fun CreateAvatar(navController: NavController)
{
    var progress by remember { mutableStateOf(0.25f) }
    var stage by remember { mutableStateOf(1) }
    var height by remember { mutableStateOf(0) }
    var gender by remember { mutableStateOf("") }
    var shoulder by remember { mutableStateOf(0) }
    var chest by remember { mutableStateOf(0) }
    var waist by remember { mutableStateOf(0) }
    var hip by remember { mutableStateOf(0) }
    var back by remember { mutableStateOf(0) }
    var high_hip by remember { mutableStateOf(0) }
    var lengthofarm by remember { mutableStateOf(0) }
    var lengthofleg by remember { mutableStateOf(0) }
    var thicknessofarm by remember { mutableStateOf(0) }
    var thicknessofleg by remember { mutableStateOf(0) }
    var avatarname by remember { mutableStateOf("") }

    var isExpanded by remember { mutableStateOf(false) }


    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.creation),
            contentDescription = "demo background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //Title
        Text(
            text = "Create\n\nAvatar",
            fontSize = 48.sp,
            fontFamily = titlefontFamily,
            textAlign = TextAlign.Center,
            color = White
        )
        //Things in the White area
        Column(
            modifier = Modifier
                .padding(30.dp)
                .height(550.dp)
                .clip(RoundedCornerShape(55.dp))
                .background(color = White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(30.dp)
            ) {

                // Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                ) {
                    // for the background of the ProgressBar
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(9.dp))
                            .background(processbarbackground)
                    )
                    // for the progress of the ProgressBar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(size)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(9.dp))
                            .background(processbar)
                            .animateContentSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                //The input receiver
                Column(modifier = Modifier
                    .height(350.dp)
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    when(stage){
                        1 -> {
                            //firstpage
                            progress = 0.25f
                            Inputdata("Height",data = remember { mutableStateOf( height) })
                            Spacer(modifier = Modifier.height(5.dp))
                            //the dropdown menu
                            ExposedDropdownMenuBox(
                                expanded = isExpanded,
                                onExpandedChange = {isExpanded = it}
                            ) {
                                OutlinedTextField(
                                    value = gender,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(Black),
                                    modifier = Modifier.menuAnchor()
                                        .width(300.dp)
                                        .padding(vertical = 4.dp, horizontal = 10.dp),
                                )
                                ExposedDropdownMenu(
                                    expanded = isExpanded,
                                    onDismissRequest = {isExpanded = false}
                                ) {
                                    DropdownMenuItem(
                                        modifier = Modifier.background(color = White),
                                        text = {
                                            Text(text = "Male")
                                        }, onClick = {
                                            gender = "Male"
                                            isExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        modifier = Modifier.background(color = White),
                                        text = {
                                            Text(text = "Female")
                                        }, onClick = {
                                            gender = "Female"
                                            isExpanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        modifier = Modifier.background(color = White),
                                        text = {
                                            Text(text = "Other")
                                        }, onClick = {
                                            gender = "Other"
                                            isExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        2->{
                            //second page
                            progress = 0.5f
                            //using LazyColumn for easy scrollable
                            LazyColumn (contentPadding = PaddingValues(5.dp), modifier = Modifier.fillMaxWidth()){
                                item{
                                    Inputdata("Shoulder",data = remember { mutableStateOf( shoulder) })
                                    Inputdata("Chest",data = remember { mutableStateOf( chest) })
                                    Inputdata("Waist",data = remember { mutableStateOf( waist) })
                                    Inputdata("Hip",data = remember { mutableStateOf( hip) })
                                    Inputdata("Back",data = remember { mutableStateOf( back) })
                                }
                            }
                        }
                        3->{
                            //third page
                            progress = 0.75f
                            //using LazyColumn for easy scrollable
                            LazyColumn(contentPadding = PaddingValues(5.dp), modifier = Modifier.fillMaxWidth())
                            {
                                item{
                                    Inputdata("High Hip", data = remember { mutableStateOf(high_hip) })
                                    Inputdata("Length of arms", data = remember { mutableStateOf(lengthofarm) })
                                    Inputdata("Length of legs", data = remember { mutableStateOf(lengthofleg) })
                                    Inputdata("Thickness of arms", data = remember { mutableStateOf(thicknessofarm) })
                                    Inputdata("Thickness of legs", data = remember { mutableStateOf(thicknessofleg) })
                            }
                            }
                        }
                        4->{
                            //last page
                            progress = 1f

                            Text("Name of Avatar:", fontSize = 16.sp, fontFamily = PromptfontFamily )
                            OutlinedTextField(
                                value = avatarname,
                                onValueChange = {avatarname = it },
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(Black),
                                modifier = Modifier
                                    .width(300.dp)
                                    .padding(vertical = 4.dp, horizontal = 10.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                //The two button below
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                        colors = ButtonDefaults.buttonColors(AbuttonColor),
                        onClick = { if(stage==1)  navController.popBackStack()
                                    else stage = decrease(stage) })
                    {
                        Text("Back")
                    }
                    Button(
                        modifier = Modifier.wrapContentSize(),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                        colors = ButtonDefaults.buttonColors(AbuttonColor),
                        onClick = { stage = increase(stage) }
                    ) {
                        Text(text = if(stage==4) "Finish" else "Next")
                    }

                }
            }
        }
    }
}

@Composable
fun Inputdata(Prompt:String ,data: MutableState<Int>){
    Column (modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text("$Prompt:", fontSize = 16.sp, fontFamily = PromptfontFamily )
        OutlinedTextField(
            value = data.value.toString(),
            onValueChange =  {input -> input.toIntOrNull()?.let{newvalue -> data.value = newvalue} },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(Black),
            suffix = { Text("cm")},
            modifier = Modifier
                .width(300.dp)
                .padding(vertical = 4.dp, horizontal = 10.dp),
        )
    }
}


fun increase(progressCount:Int): Int {
    var tmp:Int = progressCount
    if (progressCount < 4) {
        tmp = progressCount + 1
    }
    return tmp
}

fun decrease(progressCount:Int): Int {
    var tmp:Int = progressCount
    if (progressCount > 1) {
        tmp = progressCount - 1
    }
    return tmp
}
