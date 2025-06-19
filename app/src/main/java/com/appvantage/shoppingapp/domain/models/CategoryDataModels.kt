package com.appvantage.shoppingapp.domain.models

data class CategoryDataModels(
    val name : String = "",
    val date: Long = System.currentTimeMillis(),
    val createdBy : String = "",
    val categoryImage : String =""
)