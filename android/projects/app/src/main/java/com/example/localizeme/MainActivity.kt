package com.example.localizeme

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.localizeme.ui.theme.LocalizeMeTheme
import com.example.localizeme.ui.theme.fontFamily

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val languageCode = getSavedLanguage(this)?: "en"
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) } // To store system Theme false - light/ true - dark
            // To Icon Corresponding to Theme
            val lightThemeIconId = if (darkTheme) R.drawable.dark_theme_icon_50 else R.drawable.light_theme_icon_50
            // To store the transition value of icon while click the icon
            val rotationAngle by animateFloatAsState(targetValue = if (darkTheme) 180f else 0f)
            val context = LocalContext.current // need to verify it is correct
            LocalizeMeTheme(darkTheme = darkTheme) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.tittle)
                                )
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        darkTheme = !darkTheme
                                    })
                                {
                                    Icon(
                                        painter = painterResource(
                                            id = lightThemeIconId
                                        ),
                                        contentDescription = "Theme",
                                        //tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .graphicsLayer (rotationZ = rotationAngle)
                                    )
                                }
                            },
                        )
                    },
                )
                { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    )
                    {
                        var visibility by remember { mutableStateOf(false) }
                        val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
                        visibility = windowSize.width.value.dp >= 840.dp || visibility // Set visibility to true if width is greater than medium Size
                        AnimatedVisibility(
                            visible = visibility,
                            enter = fadeIn() + expandVertically(),
                            exit = slideOutVertically() + shrinkVertically() + fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(innerPadding),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Text(
                                    stringResource(R.string.magic_text),
                                    fontSize = 30.sp,
                                    modifier = Modifier.background(
                                        color = MaterialTheme.colorScheme.tertiary,
                                        shape = RectangleShape
                                        )
                                        .padding(10.dp)
                                    )
                            }
                        }
                        CustomButton(
                            stringResource(R.string.greeting_button),
                            onClick = { visibility = !visibility }
                        )
                        Spacer(Modifier.size(20.dp))
                        CustomButton(
                            stringResource(R.string.click_to_tamil) ,
                            onClick =  {
                                setLanguage(context = context, "ta")
                                (context as? Activity)?.recreate()

                            }
                        )
                        Spacer(Modifier.size(20.dp))
                        CustomButton(
                            stringResource(R.string.click_to_english),
                            onClick = {
                                setLanguage(context = context, "en")
                                (context as? Activity)?.recreate()
                            }
                        )
                        Spacer(Modifier.size(20.dp))
                        CustomButton(
                            stringResource(R.string.click_to_activate_flavor),
                            onClick = {
                                val intent = Intent(context, FlavourDemo::class.java)
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}
fun setLanguage(context: Context, languageCode: String)
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(languageCode)
    }
    else{
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }
    saveLanguage(context = context,  languageCode = languageCode)
}

private fun saveLanguage(context: Context, languageCode: String)
{
    val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("language", languageCode).apply()
}

//private fun getSavedLanguage(context: Context) : String?
//{
//    val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
//    return sharedPreferences.getString("language", null)
//}

@Composable
fun CustomButton(buttonText : String, onClick:() -> Unit)
{
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(80.dp)
            .width(180.dp),
    )
    {
        Text(
            text = buttonText,
            fontFamily = fontFamily,
        )
    }
}