package com.example.localizeme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.localizeme.ui.theme.LocalizeMeTheme


class FlavourDemo : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalizeMeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    FlavourDemoScreen()
                }
            }
        }
    }
}
@Composable
fun FlavourDemoScreen ()
{
    val context = LocalContext.current // need to Verify
    var number1 by remember { mutableStateOf("") } // Variable to store value from text Field !
    var number2 by remember { mutableStateOf("") } // Variable to store value form text Field 2
    var result by remember { mutableDoubleStateOf(0.0) } // Variable to store result value
    var isExpanded by remember { mutableStateOf(false) } // Boolean toggle to expand Icon true - expanded/false - normal size
    val iconSize by animateDpAsState(if (isExpanded) 500.dp else 100.dp, label = "iconSize")
    val scale by animateFloatAsState(targetValue = if (isExpanded) 1.7f else 1.0f, label = "iconScale")
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        IconButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.size(200.dp)
        )
        {
            Icon(
                painter = painterResource(
                    R.drawable.alarm
                ),
                contentDescription = "Expand Icon",
                modifier = Modifier.size(iconSize)
                    .scale(scale)
                    .background(color = Color.Blue),
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedTextField(
            value = number1,
            onValueChange = { newValue ->
                number1 = newValue
            },
            label = {
                Text(text = stringResource(R.string.enter_the_number))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.size(20.dp))

        OutlinedTextField(
            value = number2,
            onValueChange = { newValue ->
                number2 = newValue
            },
            label = {
                Text(text = stringResource(R.string.enter_the_number))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.size(20.dp))
        CustomButton(
            stringResource(R.string.add_button),
            onClick = {
                result = number2.toDoubleOrNull()?.let { number1.toDoubleOrNull()?.plus(it) } ?: 0.0
            })
        Spacer(modifier = Modifier.size(20.dp))
        if (BuildConfig.IS_PRO == true)
        {
            ProFlavourDemo(
                onResultChange = {result = number2.toDoubleOrNull()?.let { number1.toDoubleOrNull()?.minus(it) } ?: 0.0}
            )
            Spacer(modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.size(20.dp))

        Text(text = stringResource(
            R.string.answer_text) + result.toString()
        )
        Spacer(modifier = Modifier.size(20.dp))
        CustomButton(
            stringResource(R.string.back_button),
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        )

    }
}


