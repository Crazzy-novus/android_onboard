package com.example.localizeme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


@Composable
fun ProFlavourDemo(onResultChange: () -> Unit)
{
    CustomButton(
        stringResource(R.string.subtract_button),
        onClick = onResultChange
    )
}