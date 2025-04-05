package com.example.login.takeuserface

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color

class colourAnalyzer {
    
    /**
     * Analyzes the color at the middle of a PNG image
     * @param pngImagePath The path to the PNG image
     * @return RGBA color code as a String in format "rgba(r,g,b,a)"
     */
    fun analyzeMiddleColorPNG(pngImagePath: String): String {
        val bitmap = BitmapFactory.decodeFile(pngImagePath)
        
        // Get the middle pixel
        val middleX = bitmap.width / 2
        val middleY = bitmap.height / 2
        val pixelColor = bitmap.getPixel(middleX, middleY)
        
        // Extract RGBA values
        val red = Color.red(pixelColor)
        val green = Color.green(pixelColor)
        val blue = Color.blue(pixelColor)
        val alpha = Color.alpha(pixelColor)
        
        // Format as RGBA string
        return "rgba($red,$green,$blue,$alpha)"
    }
    
    /**
     * Get the middle color of a PNG image as an integer color value
     */
    fun getMiddleColorPNG(pngImagePath: String): Int {
        val bitmap = BitmapFactory.decodeFile(pngImagePath)
        val middleX = bitmap.width / 2
        val middleY = bitmap.height / 2
        return bitmap.getPixel(middleX, middleY)
    }
}

