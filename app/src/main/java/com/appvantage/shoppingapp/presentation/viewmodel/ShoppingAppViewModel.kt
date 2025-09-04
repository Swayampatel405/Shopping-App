package com.appvantage.shoppingapp.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appvantage.shoppingapp.common.HomeScreenState
import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.domain.models.CartDataModels
import com.appvantage.shoppingapp.domain.models.CategoryDataModels
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.models.UserData
import com.appvantage.shoppingapp.domain.models.UserDataParent
import com.appvantage.shoppingapp.domain.usecase.AddToCartUseCase
import com.appvantage.shoppingapp.domain.usecase.AddToFavUseCase
import com.appvantage.shoppingapp.domain.usecase.CreateUserUseCase
import com.appvantage.shoppingapp.domain.usecase.GetAllCategoryUseCase
import com.appvantage.shoppingapp.domain.usecase.GetAllFavUseCase
import com.appvantage.shoppingapp.domain.usecase.GetAllProductUseCase
import com.appvantage.shoppingapp.domain.usecase.GetAllSuggestedProducts
import com.appvantage.shoppingapp.domain.usecase.GetBannerUseCase
import com.appvantage.shoppingapp.domain.usecase.GetCartTotalUseCase
import com.appvantage.shoppingapp.domain.usecase.GetCartUseCase
import com.appvantage.shoppingapp.domain.usecase.GetCategoryInLimit
import com.appvantage.shoppingapp.domain.usecase.GetCheckoutUseCase
import com.appvantage.shoppingapp.domain.usecase.GetProductByIdUseCase
import com.appvantage.shoppingapp.domain.usecase.GetProductsInLimit
import com.appvantage.shoppingapp.domain.usecase.GetSpecificCategoryItems
import com.appvantage.shoppingapp.domain.usecase.GetUserUseCase
import com.appvantage.shoppingapp.domain.usecase.LoginUserUseCase
import com.appvantage.shoppingapp.domain.usecase.UpdateUserDataUseCase
import com.appvantage.shoppingapp.domain.usecase.UserProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sign

@HiltViewModel
class ShoppingAppViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val userProfileImageUseCase: UserProfileImageUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getAllCategoriesUseCase: GetAllCategoryUseCase,
    private val addToFavUseCase: AddToFavUseCase,
    private val getAllFavUseCase: GetAllFavUseCase,
    private val getAllProductUseCase: GetAllProductUseCase,
    private val getAllSuggestedProducts: GetAllSuggestedProducts,
    private val getBannerUseCase: GetBannerUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val getCategoryInLimit: GetCategoryInLimit,
    private val getCheckoutUseCase: GetCheckoutUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductsInLimit: GetProductsInLimit,
    private val getSpecificCategoryItems: GetSpecificCategoryItems,
    private val getCartTotalUseCase: GetCartTotalUseCase
) : ViewModel() {
    private val _signUpScreenState = MutableStateFlow(SignUpScreenState())
    val signUpScreenState = _signUpScreenState.asStateFlow()

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState = _loginScreenState.asStateFlow()

    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState = _updateScreenState.asStateFlow()

    private val _userProfileImageState = MutableStateFlow(UploadUserProfileImageState())
    val userProfileImageState = _userProfileImageState.asStateFlow()

    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState = _addToCartState.asStateFlow()

    private val _addToFavState = MutableStateFlow(AddToFavState())
    val addToFavState = _addToFavState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()

    private val _getAllFavState = MutableStateFlow(GetAllFavState())
    val getAllFavState = _getAllFavState.asStateFlow()

    private val _getAllProductState = MutableStateFlow(GetAllProductState())
    val getAllProductState = _getAllProductState.asStateFlow()

    private val _getCartTotalState = MutableStateFlow(GetTotalCartAmountState())
    val getCartTotalState = _getCartTotalState.asStateFlow()

    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState = _getCartState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState = _getAllCategoriesState.asStateFlow()

    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState = _getCheckoutState.asStateFlow()

    private val _getSpecificCategoryItemsState = MutableStateFlow(GetSpecificCategoryItemsState())
    val getSpecificCategoryItemsState = _getSpecificCategoryItemsState.asStateFlow()

    private val _getAllSuggestedProductsState = MutableStateFlow(GetAllSuggestedProductsState())
    val getAllSuggestedProductsState = _getAllSuggestedProductsState.asStateFlow()

    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState = _profileScreenState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()


    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData() {

        viewModelScope.launch {
            combine(
                getCategoryInLimit.getCategoryInLimit(),
                getProductsInLimit.getProductsInLimit(),
                getBannerUseCase.getBannerUseCase()
            ) { categoriesResult, productsResult, bannerResult ->
                when {
                    categoriesResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = categoriesResult.message)
                    }

                    productsResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = productsResult.message)
                    }

                    bannerResult is ResultState.Error -> {
                        HomeScreenState(isLoading = false, errorMessage = bannerResult.message)
                    }

                    categoriesResult is ResultState.Success && productsResult is ResultState.Success && bannerResult is ResultState.Success -> {
                        HomeScreenState(
                            isLoading = false,
                            categories = categoriesResult.data,
                            products = productsResult.data,
                            banner = bannerResult.data
                        )
                    }

                    else -> {
                        HomeScreenState(isLoading = true)
                    }
                }
            }.collect { state ->
                _homeScreenState.value = state
            }
        }

    }

    fun getSpecificCategoryItems(categoryName: String) {
        viewModelScope.launch {
            getSpecificCategoryItems.getSpecificCategoryItems(categoryName).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getSpecificCategoryItemsState.value =
                            _getSpecificCategoryItemsState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                    }

                    is ResultState.Loading -> {
                        _getSpecificCategoryItemsState.value =
                            _getSpecificCategoryItemsState.value.copy(
                                isLoading = true
                            )
                    }

                    is ResultState.Success -> {
                        _getSpecificCategoryItemsState.value =
                            _getSpecificCategoryItemsState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                    }

                }
            }
        }
    }

    fun getCheckOut(productId: String) {

        viewModelScope.launch {
            getCheckoutUseCase.getCheckout(productId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            errorMessages = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase.getAllCategories().collect {
                when (it) {
                    is ResultState.Error -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = false,
                            errorMessages = it.message
                        )
                    }

                    is ResultState.Loading -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = false,
                            userData = it.data
                        )
                    }
                }
            }
        }
    }

    fun getCartTotal(userId: String) {
        viewModelScope.launch {
            getCartTotalUseCase.getTotalCartAmount(userId)
                .collect {
                    when (it) {
                        is ResultState.Error -> {
                            _getCartTotalState.value = _getCartTotalState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _getCartTotalState.value = _getCartTotalState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            Log.d("CartTotal", "Total from Firestore: ${it.data}")
                            _getCartTotalState.value = _getCartTotalState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }
        }

        fun getCart() {

            viewModelScope.launch {
                getCartUseCase.getCart().collect {
                    when (it) {
                        is ResultState.Error -> {
                            _getCartState.value = _getCartState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _getCartState.value = _getCartState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _getCartState.value = _getCartState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }

                }
            }
        }

        fun getAllProducts() {

            viewModelScope.launch {
                getAllProductUseCase.getAllProducts().collect {
                    when (it) {
                        is ResultState.Error -> {
                            _getAllProductState.value = _getAllProductState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _getAllProductState.value = _getAllProductState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _getAllProductState.value = _getAllProductState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }
        }

        fun getAllFav() {
            viewModelScope.launch {
                getAllFavUseCase.getAllFav().collect {
                    when (it) {
                        is ResultState.Error -> {
                            _getAllFavState.value = _getAllFavState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _getAllFavState.value = _getAllFavState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _getAllFavState.value = _getAllFavState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }
        }

        fun addToFav(productDataModels: ProductDataModels) {
            viewModelScope.launch {
                addToFavUseCase.addToFav(productDataModels).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _addToFavState.value = _addToFavState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _addToFavState.value = _addToFavState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _addToFavState.value = _addToFavState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }
        }

        fun getProductById(productId: String) {
            viewModelScope.launch {
                getProductByIdUseCase.getProductById(productId).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _getProductByIdState.value = _getProductByIdState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _getProductByIdState.value = _getProductByIdState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _getProductByIdState.value = _getProductByIdState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }

            }
        }

        fun addToCart(cartDataModels: CartDataModels) {
            viewModelScope.launch {
                addToCartUseCase.addToCart(cartDataModels).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _addToCartState.value = _addToCartState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _addToCartState.value = _addToCartState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _addToCartState.value = _addToCartState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }

                }
            }
        }

        fun uploadUserProfileImage(uri: Uri) {
            viewModelScope.launch {
                userProfileImageUseCase.userProfileImage(uri).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _userProfileImageState.value = _userProfileImageState.value.copy(
                                isLoading = false,
                                errorMessages = it.message

                            )
                        }

                        is ResultState.Loading -> {
                            _userProfileImageState.value = _userProfileImageState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _userProfileImageState.value = _userProfileImageState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }

            }
        }

        fun updateUserData(userDataParent: UserDataParent) {
            viewModelScope.launch {
                updateUserDataUseCase.updateUserData(userDataParent).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _updateScreenState.value = _updateScreenState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _updateScreenState.value = _updateScreenState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _updateScreenState.value = _updateScreenState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }

            }
        }

        fun createUser(userData: UserData) {
            viewModelScope.launch {
                createUserUseCase.createUser(userData).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _signUpScreenState.value = _signUpScreenState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _signUpScreenState.value = _signUpScreenState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _signUpScreenState.value = _signUpScreenState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }
        }

        fun loginUser(userData: UserData) {
            viewModelScope.launch {
                loginUserUseCase.loginUser(userData).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _loginScreenState.value = _loginScreenState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _loginScreenState.value = _loginScreenState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _loginScreenState.value = _loginScreenState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }
        }

        fun getUserById(uid: String) {
            viewModelScope.launch {
                getUserUseCase.getUser(uid).collect {
                    when (it) {
                        is ResultState.Error -> {
                            _profileScreenState.value = _profileScreenState.value.copy(
                                isLoading = false,
                                errorMessages = it.message
                            )
                        }

                        is ResultState.Loading -> {
                            _profileScreenState.value = _profileScreenState.value.copy(
                                isLoading = true
                            )
                        }

                        is ResultState.Success -> {
                            _profileScreenState.value = _profileScreenState.value.copy(
                                isLoading = false,
                                userData = it.data
                            )
                        }
                    }
                }
            }

        }

        fun getAllSuggestedProducts() {
            viewModelScope.launch {
                getAllSuggestedProducts.getAllSuggestedProducts().collect {
                    when (it) {
                        is ResultState.Error -> {
                            _getAllSuggestedProductsState.value =
                                _getAllSuggestedProductsState.value.copy(
                                    isLoading = false,
                                    errorMessages = it.message
                                )
                        }

                        is ResultState.Loading -> {
                            _getAllSuggestedProductsState.value =
                                _getAllSuggestedProductsState.value.copy(
                                    isLoading = true
                                )
                        }

                        is ResultState.Success -> {
                            _getAllSuggestedProductsState.value =
                                _getAllSuggestedProductsState.value.copy(
                                    isLoading = false,
                                    userData = it.data
                                )
                        }
                    }
                }
            }
        }

    }


    data class ProfileScreenState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: UserDataParent? = null
    )

    data class SignUpScreenState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: String? = null
    )

    data class LoginScreenState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: String? = null
    )

    data class UpdateScreenState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: String? = null
    )

    data class UploadUserProfileImageState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: String? = null
    )

    data class AddToCartState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: String? = null
    )

    data class AddToFavState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: String? = null
    )

    data class GetProductByIdState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: ProductDataModels? = null
    )

    data class GetAllFavState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: List<ProductDataModels>? = emptyList()
    )

    data class GetAllProductState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: List<ProductDataModels?> = emptyList()
    )

    data class GetCartState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: List<CartDataModels?> = emptyList()
    )

    data class GetAllCategoriesState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: List<CategoryDataModels?> = emptyList()
    )

    data class GetCheckoutState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: ProductDataModels? = null
    )

    data class GetSpecificCategoryItemsState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: List<ProductDataModels?> = emptyList()
    )

    data class GetAllSuggestedProductsState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: List<ProductDataModels?> = emptyList()
    )

    data class GetTotalCartAmountState(
        val isLoading: Boolean = false,
        val errorMessages: String? = null,
        val userData: Int? = null
    )

