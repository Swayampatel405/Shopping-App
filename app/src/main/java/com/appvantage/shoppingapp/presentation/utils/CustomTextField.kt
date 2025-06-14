package com.appvantage.shoppingapp.presentation.utils

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomTextField(
    value:String,
    onValueChange:(String)->Unit,
    label:String,
    modifier: Modifier=Modifier,
    leadingIcon:ImageVector?=null,
    singleLine:Boolean=true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label ={Text(text = label)},
        modifier = modifier,
        singleLine = singleLine,
        leadingIcon = leadingIcon?.let{
            { Icon(imageVector = it, contentDescription = null)}
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )



}