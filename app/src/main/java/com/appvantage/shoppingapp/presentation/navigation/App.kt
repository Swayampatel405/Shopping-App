package com.appvantage.shoppingapp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.appvantage.shoppingapp.R
import com.appvantage.shoppingapp.presentation.screens.AllCategoriesScreen
import com.appvantage.shoppingapp.presentation.screens.CartScreen
import com.appvantage.shoppingapp.presentation.screens.CheckOutScreen
import com.appvantage.shoppingapp.presentation.screens.EachCategoryProduct
import com.appvantage.shoppingapp.presentation.screens.EachProductDetailScreen
import com.appvantage.shoppingapp.presentation.screens.GetAllFav
import com.appvantage.shoppingapp.presentation.screens.GetAllProducts
import com.appvantage.shoppingapp.presentation.screens.HomeScreen
import com.appvantage.shoppingapp.presentation.screens.LoginScreenUi
import com.appvantage.shoppingapp.presentation.screens.ProfileScreen
import com.appvantage.shoppingapp.presentation.screens.SignUpScreen
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.google.firebase.auth.FirebaseAuth


data class BottomNavItem(val name: String, val icon: ImageVector, val unSelectedIcon: ImageVector)

@Composable
fun MyApp(
    firebaseAuth: FirebaseAuth
) {
    val navController = rememberNavController()
    val selectedItem = remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = remember { mutableStateOf(false) }

    val listOfBottomNavItem = listOf(
        BottomNavItem(
            name = "Home",
            icon = Icons.Default.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            name = "WishList",
            icon = Icons.Default.Favorite,
            unSelectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavItem(
            name = "Cart",
            icon = Icons.Default.ShoppingCart,
            unSelectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem(
            name = "Profile",
            icon = Icons.Default.Person,
            unSelectedIcon = Icons.Outlined.Person
        )
    )

    val startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LoginSignUpScreen
    } else {
        SubNavigation.MainHomeScreen
    }

    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routes.LoginScreen::class.qualifiedName, Routes.SignUpScreen::class.qualifiedName -> false
            else -> true
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        )
                ) {
                    AnimatedBottomBar(
                        selectedItem = selectedItem.intValue,
                        itemSize = listOfBottomNavItem.size,
                        containerColor = Color.Transparent,
                        indicatorColor = colorResource(R.color.orange),
                        indicatorDirection = IndicatorDirection.BOTTOM,
                        indicatorStyle = IndicatorStyle.FILLED
                    ) {
                        listOfBottomNavItem.forEachIndexed { index, bottomNavItem ->
                            BottomBarItem(
                                selected = selectedItem.intValue == index,
                                onClick = {
                                    selectedItem.intValue = index
                                    when (index) {
                                        0 -> navController.navigate(Routes.HomeScreen)
                                        1 -> navController.navigate(Routes.WishListScreen)
                                        2 -> navController.navigate(Routes.CartScreen)
                                        3 -> navController.navigate(Routes.ProfileScreen)

                                    }
                                },
                                imageVector = bottomNavItem.icon,
                                label = bottomNavItem.name,
                                containerColor = Color.Transparent
                            )
                        }

                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding( top = innerPadding.calculateTopPadding(),bottom = if (shouldShowBottomBar.value) 60.dp else 0.dp, start = 0.dp, end =0.dp )
        ) {
            NavHost(navController = navController, startDestination = startScreen){

                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.LoginScreen) {
                    composable<Routes.LoginScreen> {
                        LoginScreenUi(navController = navController)
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreen(navController = navController)
                    }
                }

                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen){
                    composable<Routes.HomeScreen> {
                        HomeScreen(navController = navController)
                    }

                    composable<Routes.ProfileScreen> {
                        ProfileScreen(navController = navController, firebaseAuth = firebaseAuth)
                    }

                    composable<Routes.WishListScreen> {
                        GetAllFav(navController = navController)
                    }

                    composable<Routes.CartScreen> {
                        CartScreen(navController = navController)
                    }

                    composable<Routes.SeeAllProductsScreen> {
                        GetAllProducts(navController = navController)
                    }

                    composable<Routes.AllCategoriesScreen> {
                        AllCategoriesScreen(navController = navController)
                    }

                    composable<Routes.EachProductDetailScreen> {
                        val product: Routes.EachProductDetailScreen = it.toRoute()
                        EachProductDetailScreen(navController = navController , productId = product.productId)
                    }
                    composable<Routes.EachCategoryItemsScreen> {
                        val category : Routes.EachCategoryItemsScreen =it.toRoute()
                        EachCategoryProduct(navController = navController, categoryName = category.categoryName)
                    }
                    composable<Routes.CheckoutScreen> {
                        val product :Routes.EachProductDetailScreen = it.toRoute()
                        CheckOutScreen(navController = navController,productId = product.productId)
                    }

                }

            }

        }

    }

}