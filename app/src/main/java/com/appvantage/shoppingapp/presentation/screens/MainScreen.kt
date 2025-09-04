package com.appvantage.shoppingapp.presentation.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appvantage.shoppingapp.R
import com.google.firebase.auth.FirebaseAuth
import com.appvantage.shoppingapp.presentation.navigation.MyApp

@Composable
fun MainScreen(
    firebaseAuth: FirebaseAuth,
    payTest:()-> Unit
//    onPayTest:()->Unit
) {
    val showSplash = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({ showSplash.value = false }, 3000)
    }

    if (showSplash.value) {
        SplashScreen()
    } else {
        MyApp(firebaseAuth, { payTest() })
    }

}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "App Icon",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            BasicText(
                text = "Welcome to the clothing store",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            )

        }
    }
}