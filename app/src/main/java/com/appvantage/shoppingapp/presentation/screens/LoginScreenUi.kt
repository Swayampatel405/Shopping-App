package com.appvantage.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.appvantage.shoppingapp.R
import com.appvantage.shoppingapp.presentation.navigation.Routes
import com.appvantage.shoppingapp.presentation.navigation.SubNavigation
import com.appvantage.shoppingapp.presentation.utils.CustomTextField
import com.appvantage.shoppingapp.presentation.utils.SuccessAlertDialog
import com.appvantage.shoppingapp.presentation.viewmodel.ShoppingAppViewModel

//@Preview(showBackground = true)
@Composable
fun LoginScreenUi(navController: NavController, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    val state = viewModel.loginScreenState.collectAsStateWithLifecycle()
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current


    if (state.value.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else if (state.value.errorMessages != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = state.value.errorMessages!!)
        }
    } else if (state.value.userData != null) {
        SuccessAlertDialog(
            onClick = { navController.navigate(SubNavigation.MainHomeScreen) }
        )
    } else {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = 16.dp)
            )

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 4.dp)
            )
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 4.dp)
            )

            Text(
                text = "Forgot Password ?",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 8.dp)
                    .clickable {
                        //TODO Forgot password logic
                    },
                style = TextStyle(fontSize = 14.sp)
            )

            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {

                    } else {
                        Toast.makeText(
                            context,
                            "Please enter valid Email and Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange))
            ) {
                Text(
                    text = " Login",
                    fontSize = 20.sp,
                    color = colorResource(R.color.white)
                )
            }

            TextButton(
                onClick = {navController.navigate(Routes.SignUpScreen)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Don't have an account?", color = Color.Black)
                    Text(text = " SignUp", color = colorResource(R.color.orange))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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