package com.example.afer_login.Fitting_and_cart

import com.example.csci3310.R

data class savedClothes(
    var name: String,
    var imageRes : Int
)
//suppose this list is grap data from local database
fun getAllSavedClothes() : List<savedClothes>{
    return listOf<savedClothes>(
        savedClothes("image1", R.drawable.image1),
        savedClothes("image2", R.drawable.image2),
        savedClothes("image3", R.drawable.image3),
        savedClothes("image4", R.drawable.image4),
        savedClothes("image5", R.drawable.image5),
        savedClothes("image6", R.drawable.image6),
        savedClothes("image1", R.drawable.image1),
        savedClothes("image2", R.drawable.image2),
        savedClothes("image3", R.drawable.image3),
        savedClothes("image4", R.drawable.image4),
        savedClothes("image5", R.drawable.image5),
        savedClothes("image6", R.drawable.image6),
        savedClothes("image1", R.drawable.image1),
        savedClothes("image2", R.drawable.image2),
        savedClothes("image3", R.drawable.image3),
        savedClothes("image4", R.drawable.image4),
        savedClothes("image5", R.drawable.image5),
        savedClothes("image6", R.drawable.image6),
        savedClothes("image1", R.drawable.image1),
        savedClothes("image2", R.drawable.image2),
        savedClothes("image3", R.drawable.image3),
        savedClothes("image4", R.drawable.image4),
        savedClothes("image5", R.drawable.image5),
        savedClothes("image6", R.drawable.image6),

    )
}