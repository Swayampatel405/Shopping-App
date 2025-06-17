package com.appvantage.shoppingapp.domain.usecase

import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.CategoryDataModels
import com.appvantage.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryInLimit @Inject constructor(private val repo: Repo)  {

    fun getCategoryInLimit():Flow<ResultState<List<CategoryDataModels>>>{
        return repo.getCategoriesInLimited()
    }
}