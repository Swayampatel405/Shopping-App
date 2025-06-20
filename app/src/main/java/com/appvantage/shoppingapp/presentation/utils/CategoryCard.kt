package com.appvantage.shoppingapp.presentation.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.appvantage.shoppingapp.domain.models.CategoryDataModels

@Composable
fun CategoryCard(category: CategoryDataModels, onCategoryClick: () -> Unit) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onCategoryClick()
            }
    ) {
        Column(

        ) {
            AsyncImage(
                model = category.categoryImage,
                contentDescription = category.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }

}