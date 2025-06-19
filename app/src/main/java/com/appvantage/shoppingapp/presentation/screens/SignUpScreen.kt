package com.appvantage.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.appvantage.shoppingapp.R
import com.appvantage.shoppingapp.domain.models.UserData
import com.appvantage.shoppingapp.presentation.navigation.Routes
import com.appvantage.shoppingapp.presentation.navigation.SubNavigation
import com.appvantage.shoppingapp.presentation.utils.CustomTextField
import com.appvantage.shoppingapp.presentation.utils.SuccessAlertDialog
import com.appvantage.shoppingapp.presentation.viewmodel.ShoppingAppViewModel
import okhttp3.Route

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: ShoppingAppViewModel = hiltViewModel())
{

    val state = viewModel.signUpScreenState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    if(state.value.isLoading){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }else if (state.value.errorMessages != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = state.value.errorMessages!!)
        }
    }else if (state.value.userData != null) {
        SuccessAlertDialog(
            onClick = {
                navController.navigate(SubNavigation.MainHomeScreen)
            }
        )
    }else{

        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SignUp",
                fontSize = 24.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.Start)
            )

            CustomTextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = "First Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                leadingIcon = Icons.Default.Person
            )

            CustomTextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = "Last Name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                leadingIcon = Icons.Default.Person
            )

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = Icons.Default.Email
            )

            CustomTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = Icons.Default.Call
            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Create Password",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            CustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                leadingIcon = Icons.Default.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                        if (password == confirmPassword) {
                            val userData = UserData(
                                firstName = firstname,
                                lastName = lastname,
                                email = email,
                                password = password,
                                phoneNumber = phoneNumber
                            )
                            viewModel.createUser(userData = userData)
                        } else {
                            Toast.makeText(context,"Password and Confirm Password doesn't match.",Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange))
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 20.sp,
                    color = colorResource(R.color.white)
                )
            }

            TextButton(
                onClick = {navController.navigate(Routes.LoginScreen)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Already have an account?", color = Color.Black)
                    Text(text = " Login", color = colorResource(R.color.orange))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("OR", modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Text(
                    "Log in with Google",
                    modifier = Modifier.padding(start = 6.dp),
                    color = Color.Black
                )
            }

        }

    }




}