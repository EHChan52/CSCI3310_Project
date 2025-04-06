package com.example.login.takeuserface

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource

@Composable
fun AvatarColorPreviewScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // States to hold our data
    var middleColorString by remember { mutableStateOf("Loading...") }
    var middleColorInt by remember { mutableStateOf(0) }
    var coloredAvatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var saveStatus by remember { mutableStateOf<String?>(null) }
    
    // Load and analyze the image when the screen is first displayed
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // Find the user-face.png file
                    val storageDir = context.getExternalFilesDir("Pictures/FaceAuth")
                    val facePngFile = File(storageDir, "user-face.png")
                    
                    if (facePngFile.exists()) {
                        // Analyze the middle color
                        val colorAnalyzer = colourAnalyzer()
                        val colorInt = colorAnalyzer.getMiddleColorPNG(facePngFile.absolutePath)
                        val colorString = colorAnalyzer.analyzeMiddleColorPNG(facePngFile.absolutePath)
                        
                        middleColorInt = colorInt
                        middleColorString = colorString
                        
                        // Create the avatar with the middle color
                        val avatarBitmap = createColoredAvatar(context, colorInt)
                        coloredAvatarBitmap = avatarBitmap
                    } else {
                        Log.e("AvatarPreview", "user-face.png not found")
                        middleColorString = "Error: Image not found"
                    }
                }
            } catch (e: Exception) {
                Log.e("AvatarPreview", "Error analyzing image: ${e.message}", e)
                middleColorString = "Error: ${e.message}"
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Avatar Preview",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        // Display the detected color
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Detected Color: $middleColorString",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Color preview box
            if (middleColorInt != 0) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(androidx.compose.ui.graphics.Color(middleColorInt))
                        .border(1.dp, androidx.compose.ui.graphics.Color.Black)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Display the avatar with replaced colors
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(androidx.compose.ui.graphics.Color.LightGray)
                .border(2.dp, androidx.compose.ui.graphics.Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            coloredAvatarBitmap?.let { bitmap -> 
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Colored Avatar",
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .padding(16.dp)
                )              
            } ?: Text(
                text = "Loading avatar...",
                fontSize = 18.sp
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Show save status if any
        saveStatus?.let {
            Box(modifier = Modifier.padding(8.dp)) {
                Snackbar {
                    Text(it)
                }
            }
        }
        
        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Gray)
            ) {
                Text("Back")
            }
            
            Button(
                onClick = { 
                    coroutineScope.launch {
                        val result = saveColoredAvatar(context, coloredAvatarBitmap)
                        saveStatus = result
                        
                        // Navigate to Login screen if avatar was saved successfully
                        if (result == "Avatar saved successfully!") {
                            // Short delay to allow user to see success message
                            kotlinx.coroutines.delay(1000)
                            navController.navigate("login_screen") {
                                // Clear the back stack so user can't go back to avatar screens
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Blue),
                enabled = coloredAvatarBitmap != null
            ) {
                Text("Use This Avatar", color = androidx.compose.ui.graphics.Color.White)
            }
        }
    }
}

// Function to save the colored avatar as a PNG file
private suspend fun saveColoredAvatar(context: android.content.Context, bitmap: Bitmap?): String {
    if (bitmap == null) {
        return "Error: No avatar to save"
    }
    
    return withContext(Dispatchers.IO) {
        try {
            // Create directory if it doesn't exist
            val storageDir = context.getExternalFilesDir("Avatars")
            if (storageDir == null) {
                return@withContext "Error: Could not access storage"
            }
            
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            
            // Create file with timestamp to ensure uniqueness
            val timeStamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
            val imageFileName = "colored_avatar_$timeStamp.png"
            val imageFile = File(storageDir, imageFileName)
            
            // Save bitmap to file
            FileOutputStream(imageFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            
            // Save the path to SharedPreferences so other components can find it
            val sharedPrefs = context.getSharedPreferences("avatar_prefs", android.content.Context.MODE_PRIVATE)
            sharedPrefs.edit().putString("latest_avatar_path", imageFile.absolutePath).apply()
            
            Log.d("AvatarSave", "Avatar saved successfully at: ${imageFile.absolutePath}")
            
            return@withContext "Avatar saved successfully!"
        } catch (e: Exception) {
            Log.e("AvatarSave", "Error saving avatar: ${e.message}", e)
            return@withContext "Error saving avatar: ${e.message}"
        }
    }
}

// Function to create a colored avatar by replacing white pixels with the detected color
// and overlaying the user's face on top of it
private suspend fun createColoredAvatar(context: android.content.Context, colorInt: Int): Bitmap {
    return withContext(Dispatchers.IO) {
        try {
            // Load the default avatar from resources
            val resourceId = context.resources.getIdentifier(
                "human_avatar_default", "drawable", context.packageName
            )
            
            val originalBitmap = if (resourceId != 0) {
                BitmapFactory.decodeResource(context.resources, resourceId)
            } else {
                // Fallback - try to find it in assets
                context.assets.open("human_avatar_default.png").use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
            
            // Create a mutable copy to modify
            val mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
            
            // Extract RGB components from the target color
            val targetRed = Color.red(colorInt)
            val targetGreen = Color.green(colorInt)
            val targetBlue = Color.blue(colorInt)
            
            // For each pixel in the bitmap
            for (x in 0 until mutableBitmap.width) {
                for (y in 0 until mutableBitmap.height) {
                    val pixelColor = mutableBitmap.getPixel(x, y)
                    
                    // Extract components
                    val pixelAlpha = Color.alpha(pixelColor)
                    val pixelRed = Color.red(pixelColor)
                    val pixelGreen = Color.green(pixelColor)
                    val pixelBlue = Color.blue(pixelColor)
                    
                    // If the pixel is predominantly white (allowing for some variation)
                    if (pixelRed > 240 && pixelGreen > 240 && pixelBlue > 240 && pixelAlpha > 200) {
                        // Replace with the target color
                        mutableBitmap.setPixel(
                            x, y,
                            Color.argb(pixelAlpha, targetRed, targetGreen, targetBlue)
                        )
                    }
                }
            }
            
            // Now, load the user's face image with background removed
            val storageDir = context.getExternalFilesDir("Pictures/FaceAuth")
            val userFaceFile = File(storageDir, "user-face.png")
            
            if (userFaceFile.exists()) {
                try {
                    // Load the user's face image
                    val userFaceBitmap = BitmapFactory.decodeFile(userFaceFile.absolutePath)
                    
                    // Create a canvas to draw on the mutable bitmap
                    val canvas = android.graphics.Canvas(mutableBitmap)
                    
                    // Calculate the position to center the user's face on the avatar
                    // The face should be positioned in the upper part of the avatar
                    // We'll scale it to be approximately 30% of the avatar's width (reduced from 70%)
                    val scaleFactor = (mutableBitmap.width * 0.3f) / userFaceBitmap.width
                    
                    // Create a matrix for scaling and positioning
                    val matrix = android.graphics.Matrix()
                    matrix.postScale(scaleFactor, scaleFactor)
                    
                    // Calculate position to center horizontally and position vertically
                    // at about 10% from the top of the avatar (reduced from 20%)
                    val dx = (mutableBitmap.width - (userFaceBitmap.width * scaleFactor)) / 2
                    val dy = mutableBitmap.height * 0.003f  // Position at ~0% from top
                    matrix.postTranslate(dx, dy)
                    
                    // Create a Paint object for drawing with alpha blending
                    val paint = android.graphics.Paint().apply {
                        isAntiAlias = true
                        isFilterBitmap = true
                    }
                    
                    // Draw the scaled user face onto the avatar
                    canvas.drawBitmap(userFaceBitmap, matrix, paint)
                    
                    // Recycle the user face bitmap to free memory
                    userFaceBitmap.recycle()
                    
                    Log.d("AvatarPreview", "Successfully overlaid user face onto avatar")
                } catch (e: Exception) {
                    Log.e("AvatarPreview", "Error overlaying user face: ${e.message}", e)
                    // If there's an error overlaying the face, we'll still return the colored avatar
                }
            } else {
                Log.w("AvatarPreview", "User face image not found, returning colored avatar only")
            }
            
            // Return the modified bitmap (either with just color or with face overlay)
            mutableBitmap
        } catch (e: Exception) {
            Log.e("AvatarPreview", "Error creating colored avatar: ${e.message}", e)
            // Return an empty bitmap on error
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }
}
