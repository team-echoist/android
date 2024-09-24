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
import com.echoist.linkedout.presentation.community.TabletCommunityRoute
import com.echoist.linkedout.presentation.community.search.TabletSearchScreen
import com.echoist.linkedout.presentation.essay.write.TabletEssayWriteRoute
import com.echoist.linkedout.presentation.home.TabletHomeRoute
import com.echoist.linkedout.presentation.login.agreeofprovisions.AgreeOfProvisionsPage
import com.echoist.linkedout.presentation.login.TabletLoginRoute
import com.echoist.linkedout.presentation.login.onboarding.TabletOnBoardingRoute
import com.echoist.linkedout.presentation.login.signup.TabletSignUpCompleteRoute
import com.echoist.linkedout.presentation.login.signup.TabletSignUpRoute
import com.echoist.linkedout.presentation.myLog.mylog.TabletMyLogRoute
import com.echoist.linkedout.presentation.userInfo.TabletMyInfoRoute
import com.echoist.linkedout.presentation.userInfo.account.TabletAccountRoute
import com.echoist.linkedout.presentation.userInfo.account.changeemail.TabletChangeEmailScreen
import com.echoist.linkedout.presentation.userInfo.account.changepassword.TabletChangePasswordScreen
import com.echoist.linkedout.presentation.userInfo.account.changepassword.TabletResetPwRoute
import com.echoist.linkedout.presentation.userInfo.account.deleteaccount.TabletDeleteAccountRoute
import com.echoist.linkedout.presentation.userInfo.badge.TabletBadgeRoute
import com.echoist.linkedout.presentation.userInfo.recentviewedessay.TabletRecentEssayScreen
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack

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
                isLogoutClicked = { navigateWithClearBackStack(navController, Routes.LoginPage) }
            )
        }
        composable(Routes.ChangeEmail) {
            TabletChangeEmailScreen(contentPadding = contentPadding) {
                navigateWithClearBackStack(navController, Routes.LoginPage)
            }
        }
        composable(Routes.ChangePassword) {
            TabletChangePasswordScreen(
                contentPadding = contentPadding,
                onClickResetPassword = { navController.navigate(Routes.ResetPwPageWithEmail) },
                onChangePwFinished = { navController.popBackStack() }
            )
        }
        composable(Routes.DeleteAccount) {
            TabletDeleteAccountRoute(contentPadding = contentPadding) {
                navigateWithClearBackStack(navController, Routes.LoginPage)
            }
        }
        composable(Routes.RecentViewedEssayPage) {
            TabletRecentEssayScreen(
                contentPadding = contentPadding,
                navController = navController
            )
        }
        composable(Routes.AgreeOfProvisionsPage) {
            AgreeOfProvisionsPage(navController)
        }
    }
}
