package shira.chonbirth.sakaadmin.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val DarkGray = Color(0xFF525252)
val Gray = Color(0xFF8D8D8D)
val BlueGray = Color(0xFF2e2f33)
val LightBlue = Color(0xFFebf2fc)
val TintBlue = Color(0xFFc3e7ff)
val BrightBlue = Color(0xFF0d58cd)

val LightBackground = Color(0xFFf5f5f5)
val DarkBackground = Color(0xFF000000)

val Colors.Header: Color
    @Composable
    get() = if(isLight) BrightBlue else Color(0xFF01579B)

val Colors.Pill: Color
    @Composable
    get() = if(isLight) BrightBlue else TintBlue

val Colors.Background: Color
    @Composable
    get() = if(isLight) LightBackground else DarkBackground

val Colors.Texture: Color
    @Composable
    get() = if(isLight) Color.White else Color(0xFF1f1f1f)

val Colors.PrimaryText: Color
    @Composable
    get() = if(isLight) Color.Black else Color.White

val Colors.SecondaryText: Color
    @Composable
    get() = if(isLight) DarkGray else LightBlue

val Colors.TintedText: Color
    @Composable
    get() = if(isLight) Color.Black else Color.White

val Colors.Items: Color
    @Composable
    get() = if(isLight) Color(0xFFB3E5FC) else Color(0xFF01579B)

//val Colors.StrongBlue: Color
//    @Composable
//    get() = if(isLight) BrightBlue else TintBlue