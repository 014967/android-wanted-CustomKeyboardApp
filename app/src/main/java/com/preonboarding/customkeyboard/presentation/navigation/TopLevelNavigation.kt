package com.preonboarding.customkeyboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.preonboarding.customkeyboard.presentation.ui.CustomKeyBoardAppState
import com.preonboarding.customkeyboard.presentation.ui.info.InfoRoute

/**
 * @Created by 김현국 2022/10/12
 */
internal fun NavGraphBuilder.infoGraph(
    customKeyBoardAppState: CustomKeyBoardAppState
) {
    navigation(
        route = NavigationRoute.InfoScreenGraph.route,
        startDestination = NavigationRoute.InfoScreenGraph.InfoScreen.route
    ) {
        composable(
            route = NavigationRoute.InfoScreenGraph.InfoScreen.route
        ) {
            InfoRoute()
        }
    }
}
internal fun NavGraphBuilder.keyboardTestGraph(
    customKeyBoardAppState: CustomKeyBoardAppState
) {
    navigation(
        route = NavigationRoute.KeyBoardTestScreenGraph.route,
        startDestination = NavigationRoute.KeyBoardTestScreenGraph.KeyBoardTestScreen.route
    ){

    }
}
