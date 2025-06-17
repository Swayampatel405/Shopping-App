package com.appvantage.shoppingapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{

    @Serializable
    object LoginSignUpScreen : SubNavigation()

    @Serializable
    object MainHomeScreen : SubNavigation()
}


sealed class Routes {

    @Serializable
    object LoginScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishListScreen

    @Serializable
    object CartScreen

    @Serializable
    data class CheckoutScreen(val productId: String)

    @Serializable
    object PayScreen

    @Serializable
    object SeeAllProductsScreen

    @Serializable
    data class EachProductDetailScreen(val productId: String)

    @Serializable
    object AllCategoriesScreen

    @Serializable
    data class EachCategoryItemsScreen(val categoryName: String)

}