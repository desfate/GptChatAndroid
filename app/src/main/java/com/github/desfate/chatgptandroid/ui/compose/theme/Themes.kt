package com.github.desfate.chatgptandroid.ui.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


/**
 * 主题配置参考
 * https://developer.android.google.cn/jetpack/compose/designsystems/anatomy?hl=zh-cn
 *
 * Compose 主题分为
 * 1. 颜色 Color
 * 2. 排版 Typography 类似h5的标签系统 统一的排版格式
 * 3. 形状 Shapes
 */

@Composable  // 主题也算可组合函数
fun MainTheme(
    isDark: Boolean = isSystemInDarkTheme(),  // 是否识别黑暗主题
    isDynamicColor: Boolean = true,           // 是否关联动态主题
    content: @Composable () -> Unit
){
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val myColorScheme = when {
        dynamicColor && isDark -> {  // 直接使用 material3 的默认主题
            dynamicDarkColorScheme(LocalContext.current)
        }
        dynamicColor && !isDark -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        isDark -> DartTheme   // 这里使用自定义主题
        else -> LightTheme
    }

    MaterialTheme(
        colorScheme = myColorScheme,
        typography = MyTypography,
        content = content
    )
}


private val DartTheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Blue20,
    primaryContainer = Blue30,
    onPrimaryContainer = Blue90,
    inversePrimary = Blue40,
    secondary = DarkBlue80,
    onSecondary = DarkBlue20,
    secondaryContainer = DarkBlue30,
    onSecondaryContainer = DarkBlue90,
    tertiary = Yellow80,
    onTertiary = Yellow20,
    tertiaryContainer = Yellow30,
    onTertiaryContainer = Yellow90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Grey10,
    onBackground = Grey90,
    surface = Grey10,
    onSurface = Grey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey20,
    surfaceVariant = BlueGrey30,
    onSurfaceVariant = BlueGrey80,
    outline = BlueGrey60
)

private val LightTheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue90,
    onPrimaryContainer = Blue10,
    inversePrimary = Blue80,
    secondary = DarkBlue40,
    onSecondary = Color.White,
    secondaryContainer = DarkBlue90,
    onSecondaryContainer = DarkBlue10,
    tertiary = Yellow40,
    onTertiary = Color.White,
    tertiaryContainer = Yellow90,
    onTertiaryContainer = Yellow10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = Grey99,
    onSurface = Grey10,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = BlueGrey90,
    onSurfaceVariant = BlueGrey30,
    outline = BlueGrey50
)