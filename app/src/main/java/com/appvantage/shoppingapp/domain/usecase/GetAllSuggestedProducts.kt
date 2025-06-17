package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSuggestedProducts @Inject constructor(private val repo: Repo)  {

    fun getAllSuggestedProducts():Flow<ResultState<List<ProductDataModels>>>{
        return repo.getAllSuggestedProducts()
    }
}