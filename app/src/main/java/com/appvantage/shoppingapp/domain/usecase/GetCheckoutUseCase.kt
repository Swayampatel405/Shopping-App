package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCheckoutUseCase @Inject constructor(private val repo: Repo) {

    fun getCheckout(productId : String):Flow<ResultState<ProductDataModels>> {
       return repo.getCheckOut(productId)
    }

}