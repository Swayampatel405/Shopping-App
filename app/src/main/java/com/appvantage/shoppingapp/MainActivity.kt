
package com.appvantage.shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appvantage.shoppingapp.presentation.LoginScreenUi
import com.appvantage.shoppingapp.presentation.SignUpScreen
import com.appvantage.shoppingapp.ui.theme.ShoppingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LoginScreenUi(modifier = Modifier.padding(it))
                }
            }
        }
    }
}
