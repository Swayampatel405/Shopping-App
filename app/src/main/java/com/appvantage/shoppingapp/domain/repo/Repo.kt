package com.appvantage.shoppingapp.domain.repo

import android.net.Uri
import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.BannerDataModels
import com.appvantage.shoppingapp.domain.models.CartDataModels
import com.appvantage.shoppingapp.domain.models.CategoryDataModels
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.models.UserData
import com.appvantage.shoppingapp.domain.models.UserDataParent
import kotlinx.coroutines.flow.Flow

interface Repo {

    fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun loginWithEmailAndPassword(userData: UserData): Flow<ResultState<String>>
    fun getUserById(uid: String): Flow<ResultState<UserDataParent>>
    fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>>
    fun userProfileImage(uri: Uri):Flow<ResultState<String>>
    fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModels>>>
    fun getProductsInLimited(): Flow<ResultState<List<ProductDataModels>>>
    fun getAllProducts(): Flow<ResultState<List<ProductDataModels>>>
    fun getProductById(productId: String): Flow<ResultState<ProductDataModels>>
    fun addToCarts(cartDataModels: CartDataModels): Flow<ResultState<String>>
    fun addToFav(productDataModels: ProductDataModels): Flow<ResultState<String>>
    fun getAllFav(): Flow<ResultState<List<ProductDataModels>>>
    fun getCart(): Flow<ResultState<List<CartDataModels>>>
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModels>>>
    fun getCheckOut(productId: String):Flow<ResultState<ProductDataModels>>
    fun getBanner():Flow<ResultState<List<BannerDataModels>>>
    fun getSpecificCategoryItems(categoryName: String):Flow<ResultState<List<ProductDataModels>>>
    fun getAllSuggestedProducts():Flow<ResultState<List<ProductDataModels>>>
}