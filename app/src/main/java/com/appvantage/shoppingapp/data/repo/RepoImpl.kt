package com.appvantage.shoppingapp.data.repo

import android.net.Uri
import com.appvantage.shoppingapp.common.ResultState
import com.appvantage.shoppingapp.common.USER_COLLECTION
import com.appvantage.shoppingapp.domain.models.BannerDataModels
import com.appvantage.shoppingapp.domain.models.CartDataModels
import com.appvantage.shoppingapp.domain.models.CategoryDataModels
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.domain.models.UserData
import com.appvantage.shoppingapp.domain.models.UserDataParent
import com.appvantage.shoppingapp.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth, var firebaseFirestore: FirebaseFirestore
) : Repo {
    override fun registerUserWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)
            firebaseAuth.createUserWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        firebaseFirestore.collection(USER_COLLECTION).document(it.result.user!!.uid)
                            .set(userData).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(ResultState.Success("User registered successfully"))
                                } else {
                                    if (it.exception != null) {
                                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                                    }
                                }
                            }

                        trySend(ResultState.Success("User registered successfully"))

                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }
            awaitClose {
                close()
            }

        }

    override fun loginWithEmailAndPassword(userData: UserData): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseAuth.signInWithEmailAndPassword(userData.email, userData.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User logged in successfully"))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }

            awaitClose {
                close()
            }
        }

    override fun getUserById(uid: String): Flow<ResultState<UserDataParent>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(USER_COLLECTION).document(uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val data = it.result.toObject(UserData::class.java)!!
                val userDataParent = UserDataParent(it.result.id, data)
                trySend(ResultState.Success(userDataParent))
            } else {
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        }

        awaitClose {
            close()
        }
    }

    override fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(USER_COLLECTION).document(userDataParent.nodeId)
                .update(userDataParent.userData.toMap()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success("User data updated successfully"))
                    } else {
                        if (it.exception != null) {
                            trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                        }
                    }
                }

            awaitClose {
                close()
            }

        }

    override fun userProfileImage(uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        FirebaseStorage.getInstance().reference.child("userProfileImage/${System.currentTimeMillis()} + ${firebaseAuth.currentUser?.uid}")
            .putFile(uri).addOnCompleteListener {
                it.result.storage.downloadUrl.addOnSuccessListener {
                    trySend(ResultState.Success(it.toString()))
                }
                if (it.exception != null) {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        awaitClose {
            close()
        }
    }

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getProductsInLimited(): Flow<ResultState<List<ProductDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getProductById(productId: String): Flow<ResultState<ProductDataModels>> {
        TODO("Not yet implemented")
    }

    override fun addToCarts(cartDataModels: CartDataModels): Flow<ResultState<String>> {
        TODO("Not yet implemented")
    }

    override fun addToFav(productDataModels: ProductDataModels): Flow<ResultState<String>> {
        TODO("Not yet implemented")
    }

    override fun getAllFav(): Flow<ResultState<List<ProductDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getCart(): Flow<ResultState<List<CartDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getCheckOut(productId: String): Flow<ResultState<ProductDataModels>> {
        TODO("Not yet implemented")
    }

    override fun getBanner(): Flow<ResultState<List<BannerDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getSpecificCategoryItems(categoryName: String): Flow<ResultState<List<ProductDataModels>>> {
        TODO("Not yet implemented")
    }

    override fun getAllSuggestedProducts(): Flow<ResultState<List<ProductDataModels>>> {
        TODO("Not yet implemented")
    }

}