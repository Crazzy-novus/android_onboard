package com.example.localizeme

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.os.LocaleListCompat
import com.example.localizeme.ui.theme.LocalizeMeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val languageCode = getSavedLanguage(this)?: "en"
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            val lightThemeIconId = if (darkTheme) R.drawable.dark_theme_icon_50 else R.drawable.light_theme_icon_50
            val rotationAngle by animateFloatAsState(targetValue = if (darkTheme) 180f else 0f)
            LocalizeMeTheme(darkTheme = darkTheme) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.secondary,
                            ),
                            title = {
                                Text(
                                    text = stringResource(R.string.tittle),
                                    color = MaterialTheme.colorScheme.secondary
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
                                        tint = MaterialTheme.colorScheme.secondary,
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
                    Box( modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                        contentAlignment = Alignment.Center,
                    )
                    {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                        ){
                            var visibility by remember { mutableStateOf(true) }
                            val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
                            visibility = windowSize.width.value.dp >= 840.dp || visibility // Set visibility to true if width is greater than medium Size
                            AnimatedVisibility(
                                visible = visibility,
                                enter = fadeIn() + expandVertically(),
                                exit = slideOutVertically() + shrinkVertically() + fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(innerPadding)
                                        .background(MaterialTheme.colorScheme.secondary),
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
                            Greeting(stringResource(R.string.greeting_button),
                                onClick = { visibility = !visibility }
                            )
                            Spacer(Modifier.size(20.dp))
                            LanguageChangeButton(stringResource(R.string.click_to_tamil) , "ta")
                            Spacer(Modifier.size(20.dp))
                            LanguageChangeButton(stringResource(R.string.click_to_english), "en")
                        }
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

private fun getSavedLanguage(context: Context) : String?
{
    val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("language", null)
}

@Composable
fun Greeting(name: String, onClick: () -> Unit)
{
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(50.dp)
            .width(180.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            text = name,
            color = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
fun LanguageChangeButton(buttonText : String, languageToggle : String)
{
    val context = LocalContext.current
    Button(
        onClick = {
            if (languageToggle == "ta")
            {
                setLanguage(context = context, "ta")
                (context as? Activity)?.recreate()

            }
            else if (languageToggle == "en")
            {
                setLanguage(context = context, "en")
                (context as? Activity)?.recreate()
            }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(50.dp)
            .width(180.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.background,
        )
    }
}