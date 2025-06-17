package com.appvantage.shoppingapp.common

import com.appvantage.shoppingapp.domain.models.BannerDataModels
import com.appvantage.shoppingapp.domain.models.CategoryDataModels
import com.appvantage.shoppingapp.domain.models.ProductDataModels

data class HomeScreenState(
    val isLoading : Boolean = true,
    val errorMessage : String? = null,
    val categories : List<CategoryDataModels>? = null,
    val products : List<ProductDataModels>? = null,
    val banner : List<BannerDataModels>? = null

)