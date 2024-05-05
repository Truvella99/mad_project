package it.polito.uniteam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    secondary = LightBlue,
    tertiary = LightBlue,//Color(0xff018FF3),//Azzurro fotocamera
    background =  DarkBlue,
    surface = LightBlue,
    onPrimary = Color.White,
    onSecondary = Color.White,//Color(0xff018FF3),//Azzurro fotocamera
    onTertiary = Color(0xFF15455a), //
    onBackground = Color.White,
    onSurface = Color.White ,//Color(0xFF1C1B1F),
    error = Color.Red,
    onPrimaryContainer = Color(0xff018FF3),//Azzurro fotocamera


)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = Orange,
    tertiary = Color(0xff018FF3),//fotocamera
    // Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Blue,
    onSecondary = Color.White,
    onTertiary = Orange,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    error = Color.Red

)


@Composable
fun UniTeamTheme(
    darkTheme: Boolean = isSystemInDarkTheme() ,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    /*val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }*/
    val colorScheme = DarkColorScheme;
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.DarkGray.toArgb()
            //window.colorMode = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )


}