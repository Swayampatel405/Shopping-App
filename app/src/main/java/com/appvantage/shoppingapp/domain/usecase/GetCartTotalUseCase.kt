package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartTotalUseCase @Inject constructor(private val repo: Repo) {
     fun getTotalCartAmount(userId: String): Flow<ResultState<Int>>{
        return repo.getTotalCartAmount(userId)
    }
}