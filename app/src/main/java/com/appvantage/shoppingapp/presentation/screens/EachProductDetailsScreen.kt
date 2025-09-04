package com.appvantage.shoppingapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.appvantage.shoppingapp.R
import com.appvantage.shoppingapp.domain.models.CartDataModels
import com.appvantage.shoppingapp.presentation.navigation.Routes
import com.appvantage.shoppingapp.presentation.viewmodel.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EachProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {

    val getProductByIdState = viewModel.getProductByIdState.collectAsStateWithLifecycle()

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var selectedSize by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour

            )
        }
    ) { inner ->

        when {
            getProductByIdState.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            getProductByIdState.value.errorMessages != null -> {
                Text(text = "Sorry ! Unable to get Information")
                Text(text = getProductByIdState.value.errorMessages!!)
                Button(onClick = { navController.popBackStack() }) { Text("Back") }
            }

            getProductByIdState.value.userData != null -> {

                val product = getProductByIdState.value.userData!!.copy(productId = productId)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier.height(250.dp)) {
                        AsyncImage(
                            model = product.image,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${product.price}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Size",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("S", "M", "L", "XL").forEach {
                                OutlinedButton(
                                    onClick = { selectedSize = it },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (selectedSize == it) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (selectedSize == it) Color.White else MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.height(40.dp)
                                ) {
                                    Text(text = it)
                                }
                            }

                        }
                        Text(
                            text = "Quantity",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                Text("-", style = MaterialTheme.typography.headlineSmall)
                            }

                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )

                            IconButton(onClick = { quantity++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(17.dp))
                            }
                        }
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )
                        Text(text = product.description)

                        Button(
                            onClick = {
                                val cartDataModels = CartDataModels(
                                    name = product.name,
                                    image = product.image,
                                    price = product.price,
                                    quantity = quantity.toString(),
                                    size = selectedSize,
                                    productId = product.productId,
                                    description = product.description,
                                    category = product.category
                                )
                                viewModel.addToCart(cartDataModels)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selectedSize.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.orange))
                        ) {
                            Text("Add to Cart")
                        }
                        Spacer(modifier = Modifier.height(5.dp))

                        Button(
                            onClick = {
                                navController.navigate(Routes.CheckoutScreen(productId = productId))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.orange))
                        ) {
                            Text("Buy Now")
                        }

                        OutlinedButton(
                            onClick = {
                                isFavorite = !isFavorite
                                viewModel.addToFav(product)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isFavorite) "Remove from WishList" else "Add to WishList")
                            }

                        }
                    }
                }
            }
        }
    }
}