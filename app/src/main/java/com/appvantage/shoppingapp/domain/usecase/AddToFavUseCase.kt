package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.CartDataModels
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToFavUseCase @Inject constructor(private val repo: Repo) {

    fun addToFav(productDataModels: ProductDataModels) :Flow<ResultState<String>>{
          return repo.addToFav(productDataModels)
    }

}