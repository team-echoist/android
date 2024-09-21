package com.echoist.linkedout.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.echoist.linkedout.Routes
import com.echoist.linkedout.presentation.TabletAccountRoute
import com.echoist.linkedout.presentation.TabletBadgeRoute
import com.echoist.linkedout.presentation.TabletChangeEmailScreen
import com.echoist.linkedout.presentation.TabletChangePasswordScreen
import com.echoist.linkedout.presentation.TabletCommunityRoute
import com.echoist.linkedout.presentation.TabletDeleteAccountRoute
import com.echoist.linkedout.presentation.TabletEssayWriteRoute
import com.echoist.linkedout.presentation.TabletHomeRoute
import com.echoist.linkedout.presentation.TabletLoginRoute
import com.echoist.linkedout.presentation.TabletMyInfoRoute
import com.echoist.linkedout.presentation.TabletMyLogRoute
import com.echoist.linkedout.presentation.TabletOnBoardingRoute
import com.echoist.linkedout.presentation.TabletRecentEssayScreen
import com.echoist.linkedout.presentation.TabletResetPwRoute
import com.echoist.linkedout.presentation.TabletSearchScreen
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
            TabletMyLogRoute(
                navController = navController,
                page = page,
                modifier = Modifier.padding(contentPadding)
            )
        }
        composable(Routes.Community) {
            TabletCommunityRoute(
                navController = navController,
                modifier = Modifier.padding(contentPadding)
            )
        }
        composable(Routes.Settings) {
            TabletMyInfoRoute(
                navController = navController,
                modifier = Modifier.padding(contentPadding)
            )
        }
        composable(Routes.WritingPage) {
            TabletEssayWriteRoute(navController = navController)
        }
        composable(Routes.Search) {
            TabletSearchScreen(navController)
        }
        composable(Routes.BadgePage) {
            TabletBadgeRoute(contentPadding = contentPadding)
        }
        composable(Routes.AccountPage) {
            TabletAccountRoute(
                contentPadding = contentPadding,
                onClickChangeEmail = { navController.navigate(Routes.ChangeEmail) },
                onClickChangePassword = { navController.navigate(Routes.ChangePassword) },
                onClickDeleteAccount = { navController.navigate(Routes.DeleteAccount) },
                isLogoutClicked = {
                    navController.popBackStack(
                        Routes.LoginPage,
                        true
                    )
                    navController.navigate(Routes.LoginPage)
                }
            )
        }
        composable(Routes.ChangeEmail) {
            TabletChangeEmailScreen(contentPadding = contentPadding) {
                navController.navigate(Routes.LoginPage) {
                    popUpTo(Routes.LoginPage) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Routes.ChangePassword) {
            TabletChangePasswordScreen(
                contentPadding = contentPadding,
                onClickResetPassword = { navController.navigate(Routes.ResetPwPageWithEmail) }
            )
        }
        composable(Routes.DeleteAccount) {
            TabletDeleteAccountRoute(contentPadding = contentPadding, navController = navController)
        }
        composable(Routes.RecentViewedEssayPage) {
            TabletRecentEssayScreen(
                contentPadding = contentPadding,
                navController = navController
            )
        }
    }
}
