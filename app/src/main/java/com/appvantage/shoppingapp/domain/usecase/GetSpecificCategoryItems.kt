package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpecificCategoryItems @Inject constructor(private val repo: Repo){

    fun getSpecificCategoryItems(categoryName : String): Flow<ResultState<List<ProductDataModels>>> {
        return repo.getSpecificCategoryItems(categoryName)
    }

}