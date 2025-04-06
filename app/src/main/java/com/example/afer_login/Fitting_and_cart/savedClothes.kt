package com.example.afer_login.Fitting_and_cart

import com.example.afer_login.dataFetch.Product

data class savedClothes(
    val brand : String? = null,
    val gender : String? = null,
    val imgLink : String? = null,
    val name : String? = null,
    val price : Int? = null,
    val sizes : String? = null,
    val type : String? = null,
    val shopItemlink : String? = null,
)

// Private mutable list to store all saved clothes
private val savedClothesList = mutableListOf<savedClothes>()

//suppose this list is grap data from local database
fun getAllSavedClothes() : List<savedClothes>{
    return savedClothesList
}

fun setAllSavedClothes(clothes: Product) {
    //append the product to the list by converting it to savedClothes
    val newSavedClothes = savedClothes(
        brand = clothes.brand,
        gender = clothes.gender,
        imgLink = clothes.imgLink,
        name = clothes.name,
        price = clothes.price,
        sizes = clothes.sizes,
        type = clothes.type,
        shopItemlink = clothes.shopItemlink
    )
    savedClothesList.add(newSavedClothes)
}
