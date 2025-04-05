package com.example.afer_login.Homepage

import com.example.csci3310.R

data class searchedClothes(
    var name: String,
    var imageRes : Int
)
//suppose this list is grap data from online
fun getAllsearchedClothes() : List<searchedClothes>{
    return listOf<searchedClothes>(
        searchedClothes("image1", R.drawable.image1),
        searchedClothes("image2", R.drawable.image2),
        searchedClothes("image3", R.drawable.image3),
        searchedClothes("image4", R.drawable.image4),
        searchedClothes("image5", R.drawable.image5),
        searchedClothes("image6", R.drawable.image6),
        searchedClothes("image1", R.drawable.image1),
        searchedClothes("image2", R.drawable.image2),
        searchedClothes("image3", R.drawable.image3),
        searchedClothes("image4", R.drawable.image4),
        searchedClothes("image5", R.drawable.image5),
        searchedClothes("image6", R.drawable.image6),
        searchedClothes("image1", R.drawable.image1),
        searchedClothes("image2", R.drawable.image2),
        searchedClothes("image3", R.drawable.image3),
        searchedClothes("image4", R.drawable.image4),
        searchedClothes("image5", R.drawable.image5),
        searchedClothes("image6", R.drawable.image6),
        searchedClothes("image1", R.drawable.image1),
        searchedClothes("image2", R.drawable.image2),
        searchedClothes("image3", R.drawable.image3),
        searchedClothes("image4", R.drawable.image4),
        searchedClothes("image5", R.drawable.image5),
        searchedClothes("image6", R.drawable.image6),

    )
}