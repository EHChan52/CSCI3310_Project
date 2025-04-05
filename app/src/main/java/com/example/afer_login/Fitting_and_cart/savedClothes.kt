package com.example.afer_login.Fitting_and_cart

import com.example.csci3310.R

data class savedClothes(
    var name: String,
    var type: String,
    var imageRes : Int
)
//suppose this list is grap data from local database
fun getAllSavedClothes() : List<savedClothes>{
    return listOf<savedClothes>(
        savedClothes("image1", "top",R.drawable.image1),
        savedClothes("image2", "top",R.drawable.image2),
        savedClothes("image3", "top",R.drawable.image3),
        savedClothes("image4", "bottom",R.drawable.image4),
        savedClothes("image5", "bottom",R.drawable.image5),
        savedClothes("image6", "top",R.drawable.image6),
        savedClothes("image1", "top",R.drawable.image1),
        savedClothes("image2", "top",R.drawable.image2),
        savedClothes("image3", "top",R.drawable.image3),
        savedClothes("image4", "bottom",R.drawable.image4),
        savedClothes("image5", "bottom",R.drawable.image5),
        savedClothes("image6", "top",R.drawable.image6),
        

    )
}