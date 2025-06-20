package com.appvantage.shoppingapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.appvantage.shoppingapp.R
import com.appvantage.shoppingapp.domain.models.ProductDataModels
import com.appvantage.shoppingapp.presentation.navigation.Routes
import com.appvantage.shoppingapp.presentation.utils.Banner
import com.appvantage.shoppingapp.presentation.viewmodel.ShoppingAppViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val state = viewModel.homeScreenState.collectAsStateWithLifecycle()
    val getAllSuggestedProducts =
        viewModel.getAllSuggestedProductsState.collectAsStateWithLifecycle()
    val getSuggestedProductData = getAllSuggestedProducts.value.userData.orEmpty().filterNotNull()

    LaunchedEffect(Unit) {
        viewModel.getAllSuggestedProducts()
    }

    if (state.value.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.value.errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = state.value.errorMessage!!)
        }
    } else {

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ){
                // Search Bar
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxSize().weight(1f).height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = { Text("Search") },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    )

//                    Spacer(modifier = Modifier.width(5.dp))
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                }

                //Category Section
                Column {
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "See More",
                            color = colorResource(R.color.orange),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { navController.navigate(Routes.AllCategoriesScreen) }
                        )
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(state.value.categories?: emptyList()){category->
                            CategoryItem(
                                imageUri = category.categoryImage,
                                categoryName = category.name,
                                onItemClick = {
                                    navController.navigate(Routes.EachCategoryItemsScreen(categoryName = category.name))
                                }
                            )
                        }
                    }
                }

                //Banner Section
                state.value.banner?.let { banners->
                    Banner(banner = banners)
                }

                //Flash Sale Section
                Column {
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Flash Sale",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "See More",
                            color = colorResource(R.color.orange),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { navController.navigate(Routes.SeeAllProductsScreen) }
                        )
                    }

                    LazyRow (
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        items(state.value.products?: emptyList()){ product->
                            ProductCard(product = product,navController = navController)
                        }
                    }
                }

                //Build the suggested products
                Column(
                    modifier = Modifier.padding(top = 16.dp, bottom = 5.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Suggested For You ",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "See More",
                            color = colorResource(R.color.orange),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.clickable { navController.navigate(Routes.SeeAllProductsScreen) }
                        )
                    }

                    when{
                        getAllSuggestedProducts.value.isLoading->{
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                CircularProgressIndicator()
                            }
                        }
                        getAllSuggestedProducts.value.errorMessages != null->{
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = getAllSuggestedProducts.value.errorMessages!!)
                            }
                        }
                        else->{
                            LazyRow (
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ){
                                items(getSuggestedProductData){ product->
                                    ProductCard(product = product,navController = navController)
                                }
                            }

                        }
                    }

                }


            }

        }


    }


}

@Composable
fun CategoryItem(
    imageUri: String,
    categoryName: String,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable {
                onItemClick()
            }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color = Color.LightGray, shape = CircleShape)
        ) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(shape = CircleShape)
            )
        }
        Text(
            text = categoryName,
            style = MaterialTheme.typography.bodyMedium
        )

    }

}


@Composable
fun ProductCard(
    product: ProductDataModels,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { navController.navigate(Routes.EachProductDetailScreen(productId = product.productId)) }
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = product.image,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .width(100.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.finalPrice}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.titleSmall,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${product.availableUnits} left)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}