package com.appvantage.shoppingapp.data.repo

import android.net.Uri
import com.appvantage.shoppingapp.common.ADD_TO_CART
import com.appvantage.shoppingapp.common.ADD_TO_FAV
import com.appvantage.shoppingapp.common.CATEGORIES
import com.appvantage.shoppingapp.common.PRODUCT_COLLECTION
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
    private var firebaseAuth: FirebaseAuth, private var firebaseFirestore: FirebaseFirestore
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

    override fun getTotalCartAmount(userId: String): Flow<ResultState<Int>> = callbackFlow {
        trySend(ResultState.Loading)

        val cartRef =
            firebaseFirestore.collection(ADD_TO_CART).document(userId).collection("user_cart")

        val listenerRegistration = cartRef.addSnapshotListener { snapshot, error ->
            if(error != null){
                close(error)
                return@addSnapshotListener
            }

            var total =0
            snapshot?.documents?.forEach {doc->
                val price = doc.getString("price")?.toInt()?:0
                val quantity = doc.getString("quantity")?.toInt()?:0
                total += price.toInt() * quantity.toInt()
            }

            trySend(ResultState.Success(total))
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModels>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(CATEGORIES).limit(7).get().addOnSuccessListener {
                val categories = it.documents.mapNotNull { document ->
                    document.toObject(CategoryDataModels::class.java)
                }
                trySend(ResultState.Success(categories))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

            awaitClose {
                close()
            }
        }

    override fun getProductsInLimited(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(PRODUCT_COLLECTION).limit(10).get().addOnSuccessListener {

            val products = it.documents.mapNotNull { documents ->
                documents.toObject(ProductDataModels::class.java)?.apply {
                    productId = documents.id
                }
            }

            trySend(ResultState.Success(products))

        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }

        awaitClose {
            close()
        }
    }

    override fun getAllProducts(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(PRODUCT_COLLECTION).get().addOnSuccessListener {
            val products = it.documents.mapNotNull { document ->
                document.toObject(ProductDataModels::class.java)?.apply {
                    productId = document.id
                }
            }

            trySend(ResultState.Success(products))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }

    }

    override fun getProductById(productId: String): Flow<ResultState<ProductDataModels>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION).document(productId).get()
                .addOnSuccessListener {
                    val product = it.toObject(ProductDataModels::class.java)

                    trySend(ResultState.Success(product!!))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }

            awaitClose {
                close()
            }
        }

    override fun addToCarts(cartDataModels: CartDataModels): Flow<ResultState<String>> =
        callbackFlow {

            trySend(ResultState.Loading)

            val uid = firebaseAuth.currentUser?.uid

            if (uid == null) {
                trySend(ResultState.Error("User not logged in"))
                close()
                return@callbackFlow
            }

            val cartDoc = firebaseFirestore.collection(ADD_TO_CART)
                .document(uid.toString())
                .collection("user_cart")
                .document(cartDataModels.productId!!) // productId as doc id

            firebaseFirestore.runTransaction { tx ->
                val snapshot = tx.get(cartDoc)
                if (snapshot.exists()) {
                    // already in cart → increment quantity (stored as String)
                    val currentQtyStr = snapshot.getString("quantity") ?: "0"
                    val currentQty = currentQtyStr.toIntOrNull() ?: 0
                    tx.update(cartDoc, "quantity", (currentQty + 1).toString())
                } else {
                    // not in cart → create new doc with quantity = "1"
                    val newCartItem = cartDataModels.copy(quantity = "1")
                    tx.set(cartDoc, newCartItem)
                }
            }
                .addOnSuccessListener {
                    trySend(ResultState.Success("Product added/updated in cart"))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }

            awaitClose {
                close()
            }

        }

    override fun addToFav(productDataModels: ProductDataModels): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val uid = firebaseAuth.currentUser!!.uid

            val favDoc = firebaseFirestore.collection(ADD_TO_FAV)
                .document(uid)
                .collection("user_fav")
                .document(productDataModels.productId!!)

            favDoc.set(productDataModels)
                .addOnSuccessListener {
                    trySend(ResultState.Success("Product added to wishlist"))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }

            awaitClose {
                close()
            }
        }

    override fun getAllFav(): Flow<ResultState<List<ProductDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
            .collection("user_fav").get()
            .addOnSuccessListener {
                val fav = it.documents.mapNotNull {
                    it.toObject(ProductDataModels::class.java)
                }
                trySend(ResultState.Success(fav))

            }.addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }
        awaitClose {
            close()
        }
    }

    override fun getCart(): Flow<ResultState<List<CartDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(ADD_TO_CART).document(firebaseAuth.currentUser!!.uid)
            .collection("user_cart").get()
            .addOnSuccessListener {
                val cart = it.documents.mapNotNull {
                    it.toObject(CartDataModels::class.java)?.apply {
                        cartId = it.id
                    }
                }
                trySend(ResultState.Success(cart))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

        awaitClose {
            close()
        }
    }

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(CATEGORIES).get().addOnSuccessListener {
            val categories = it.documents.mapNotNull {
                it.toObject(CategoryDataModels::class.java)
            }
            trySend(ResultState.Success(categories))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.toString()))
        }
        awaitClose {
            close()
        }
    }

    override fun getCheckOut(productId: String): Flow<ResultState<ProductDataModels>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION).document(productId).get()
                .addOnSuccessListener {
                    val product = it.toObject(ProductDataModels::class.java)
                    trySend(ResultState.Success(product!!))

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }

            awaitClose {
                close()
            }
        }

    override fun getBanner(): Flow<ResultState<List<BannerDataModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection("banner").get()
            .addOnSuccessListener {
                val banner = it.documents.mapNotNull {
                    it.toObject(BannerDataModels::class.java)
                }
                trySend(ResultState.Success(banner))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it.toString()))
            }

        awaitClose {
            close()
        }
    }

    override fun getSpecificCategoryItems(categoryName: String): Flow<ResultState<List<ProductDataModels>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(PRODUCT_COLLECTION).whereEqualTo("category", categoryName)
                .get()
                .addOnSuccessListener {
                    val products = it.documents.mapNotNull {
                        it.toObject(ProductDataModels::class.java)?.apply {
                            productId = it.id
                        }
                    }
                    trySend(ResultState.Success(products))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))

                }

            awaitClose {
                close()
            }
        }

    override fun getAllSuggestedProducts(): Flow<ResultState<List<ProductDataModels>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(ADD_TO_FAV).document(firebaseAuth.currentUser!!.uid)
                .collection("user_fav").get()
                .addOnSuccessListener {
                    val fav = it.documents.mapNotNull {
                        it.toObject(ProductDataModels::class.java)
                    }
                    trySend(ResultState.Success(fav))
                }
                .addOnFailureListener {
                    trySend(ResultState.Error(it.toString()))
                }

            awaitClose {
                close()
            }
        }
}