package shira.chonbirth.sakaadmin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = TintBlue,
    primaryVariant = BlueGray,
    secondary = TintBlue
)

private val LightColorPalette = lightColors(
    primary = BrightBlue,
    primaryVariant = LightBlue,
    secondary = TintBlue

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SakaAdminTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    SideEffect {
        if (darkTheme){
            systemUiController.setSystemBarsColor(color = Color(0xFF1f1f1f), darkIcons = false)
        } else {
            systemUiController.setSystemBarsColor(color = Color.White, darkIcons = true)
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}