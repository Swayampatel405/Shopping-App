package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.UserData
import com.appvantage.shoppingapp.domain.models.UserDataParent
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(private val repo: Repo) {

    fun updateUserData(userDataParent: UserDataParent):Flow<ResultState<String>>{
        return repo.updateUserData(userDataParent)
    }
}