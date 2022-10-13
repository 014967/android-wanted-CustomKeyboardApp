package com.preonboarding.customkeyboard.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.preonboarding.customkeyboard.presentation.navigation.NavigationRoute
import com.preonboarding.customkeyboard.presentation.navigation.infoGraph
import com.preonboarding.customkeyboard.presentation.navigation.keyboardTestGraph
import com.preonboarding.customkeyboard.presentation.theme.CustomKeyBoardTheme

/**
 * @Created by 김현국 2022/10/12
 */

@Composable
fun CustomKeyBoardApp() {
    val customKeyBoardAppState = rememberCustomKeyBoardAppState()

    Scaffold(
        backgroundColor = CustomKeyBoardTheme.color.white
    ) { paddingValues ->
        Box {
            NavHost(
                modifier = Modifier.padding(paddingValues = paddingValues),
                navController = customKeyBoardAppState.navController,
                startDestination = NavigationRoute.InfoScreenGraph.route
            ) {
                infoGraph(
                    customKeyBoardAppState = customKeyBoardAppState
                )
                keyboardTestGraph(
                    customKeyBoardAppState = customKeyBoardAppState
                )
            }
        }
    }
}
