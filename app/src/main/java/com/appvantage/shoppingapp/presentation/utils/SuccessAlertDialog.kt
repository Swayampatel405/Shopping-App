package com.appvantage.shoppingapp.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appvantage.shoppingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessAlertDialog(
    onClick: () -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = {},
        modifier = Modifier.background(shape = RoundedCornerShape(16.dp), color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(color = colorResource(R.color.orange), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check",
                    tint = Color.Gray,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Success",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.orange)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Congratulation, You have \n completed your registration!",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.orange)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Go to Home",
                        color = Color.Gray
                    )
                }

            }
        }
    }
}