package com.preonboarding.customkeyboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.preonboarding.customkeyboard.presentation.ui.CustomKeyBoardAppState
import com.preonboarding.customkeyboard.presentation.ui.info.InfoRoute
import com.preonboarding.customkeyboard.presentation.ui.test.ClipBoardTestRoute
import com.preonboarding.customkeyboard.presentation.ui.test.TestRoute

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
            InfoRoute(
                navigateTestScreen = { customKeyBoardAppState.navigateRoute(NavigationRoute.KeyBoardTestScreenGraph.KeyBoardTestScreen.route) }
            )
        }
    }
}
internal fun NavGraphBuilder.keyboardTestGraph(
    customKeyBoardAppState: CustomKeyBoardAppState
) {
    navigation(
        route = NavigationRoute.KeyBoardTestScreenGraph.route,
        startDestination = NavigationRoute.KeyBoardTestScreenGraph.KeyBoardTestScreen.route
    ) {
        composable(
            route = NavigationRoute.KeyBoardTestScreenGraph.KeyBoardTestScreen.route
        ) {
            TestRoute(
                navigateClipBoard = {
                    customKeyBoardAppState.navigateRoute(NavigationRoute.KeyBoardTestScreenGraph.ClipBoardTestScreen.route)
                }
            )
        }

        composable(
            route = NavigationRoute.KeyBoardTestScreenGraph.ClipBoardTestScreen.route
        ) {
            ClipBoardTestRoute()
        }
    }
}