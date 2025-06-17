package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.UserData
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(private val repo: Repo) {

    fun loginUser(userData: UserData): Flow<ResultState<String>>{
        return repo.loginWithEmailAndPassword(userData)
    }

}