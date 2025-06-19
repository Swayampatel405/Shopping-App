package com.appvantage.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.appvantage.shoppingapp.R
import com.appvantage.shoppingapp.presentation.viewmodel.ShoppingAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    navController: NavController,
    productId: String,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    val state = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }
    val selectedMethod = remember { mutableStateOf("Standard free delivery over Rs.4500") }

    LaunchedEffect(Unit) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shipping") },
                navigationIcon = {
                    // Back button
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            state.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            state.value.errorMessages != null -> {
                Toast.makeText(context, "Sorry ! Unable to get Inofrmation", Toast.LENGTH_SHORT)
                    .show()
            }

            state.value.userData == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Products Available")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        AsyncImage(
                            model = state.value.userData!!.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .border(width = 1.dp, color = Color.Gray)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = state.value.userData!!.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$${state.value.userData!!.finalPrice}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        Text("Contact Information", style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = {email.value = it},
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        Text("Shipping Address", style = MaterialTheme.typography.headlineSmall)

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = country.value,
                            onValueChange = {country.value = it},
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Country/Region") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = firstName.value,
                                onValueChange = {firstName.value = it},
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                label = { Text("First Name") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedTextField(
                                value = lastName.value,
                                onValueChange = {lastName.value = it},
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                label = { Text("Last Name") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = address.value,
                            onValueChange = {address.value = it},
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Address") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = city.value,
                                onValueChange = {city.value = it},
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("City") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedTextField(
                                value = postalCode.value,
                                onValueChange = {postalCode.value = it},
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Postal Code") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        Text(
                            text = "Shipping Method",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMethod.value == "Standard free delivery over Rs.4500",
                                onClick ={
                                    selectedMethod.value = "Standard free delivery over Rs.4500"
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Standard free delivery over Rs.4500",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMethod.value == "Cash on delivery Rs.50",
                                onClick ={
                                    selectedMethod.value = "Cash on delivery Rs.50"
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cash on delivery Rs.50",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.orange))
                        ){
                            Text(text = "Continue to Shipping")
                        }
                    }
                }
            }
        }


    }

}