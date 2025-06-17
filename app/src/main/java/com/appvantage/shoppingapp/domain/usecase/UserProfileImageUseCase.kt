package com.appvantage.shoppingapp.domain.usecase

import android.net.Uri
import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileImageUseCase @Inject constructor(private val repo: Repo){

    fun userProfileImage(uri: Uri):Flow<ResultState<String>>{
        return repo.userProfileImage(uri)
    }
}