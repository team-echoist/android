package com.echoist.linkedout.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.echoist.linkedout.Routes
import com.echoist.linkedout.page.home.HomePage
import com.echoist.linkedout.page.login.OnBoardingPage
import com.echoist.linkedout.presentation.TabletCommunityRoute
import com.echoist.linkedout.presentation.TabletEssayWriteRoute
import com.echoist.linkedout.presentation.TabletHomeRoute
import com.echoist.linkedout.presentation.TabletLoginRoute
import com.echoist.linkedout.presentation.TabletMyInfoRoute
import com.echoist.linkedout.presentation.TabletMyLogRoute
import com.echoist.linkedout.presentation.TabletOnBoardingRoute
import com.echoist.linkedout.presentation.TabletResetPwRoute
import com.echoist.linkedout.presentation.TabletSettingRoute
import com.echoist.linkedout.presentation.TabletSignUpCompleteRoute
import com.echoist.linkedout.presentation.TabletSignUpRoute

@Composable
fun TabletNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.LoginPage
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Routes.LoginPage) {
            TabletLoginRoute(
                navController = navController,
                onBackPressed = { navController.popBackStack() },
                navigateToResetPassword = { navController.navigate(Routes.ResetPwPageWithEmail) },
                navigateToSignUp = { navController.navigate(Routes.SignUp) }
            )
        }
        composable(Routes.OnBoarding) {
            TabletOnBoardingRoute(
                onStartClick = { navController.navigate(Routes.LoginPage) }
            )
        }
        composable(Routes.ResetPwPageWithEmail) {
            TabletResetPwRoute(navController = navController)
        }
        composable(Routes.SignUp) {
            TabletSignUpRoute(navController = navController)
        }
        composable(Routes.SignUpComplete) {
            TabletSignUpCompleteRoute {
                navController.navigate("${Routes.Home}/200")
            }
        }
        composable(
            route = "${Routes.Home}/{statusCode}",
        ) { backStackEntry ->
            val statusCode = backStackEntry.arguments?.getInt("statusCode") ?: 200
            TabletHomeRoute(
                statusCode = statusCode
            )
        }
        composable(
            route = "${Routes.MyLog}/{page}",
            arguments = listOf(navArgument("page") { type = NavType.IntType })
        ) { backStackEntry ->
            val page = backStackEntry.arguments?.getInt("page") ?: 0
            TabletMyLogRoute(navController = navController, page = page, modifier = Modifier.padding(contentPadding))
        }
        composable(Routes.Community) {
            TabletCommunityRoute(navController = navController, modifier = Modifier.padding(contentPadding))
        }
        composable(Routes.Settings) {
            TabletMyInfoRoute(navController = navController, modifier = Modifier.padding(contentPadding))
        }
        composable(Routes.WritingPage) {
            TabletEssayWriteRoute(navController = navController)
        }
    }
}
