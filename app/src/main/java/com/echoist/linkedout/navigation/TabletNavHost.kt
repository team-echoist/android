package com.echoist.linkedout.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.echoist.linkedout.Routes
import com.echoist.linkedout.page.login.OnBoardingPage
import com.echoist.linkedout.presentation.TabletLoginRoute
import com.echoist.linkedout.presentation.TabletOnBoardingRoute
import com.echoist.linkedout.presentation.TabletResetPwRoute
import com.echoist.linkedout.presentation.TabletSignUpCompleteRoute
import com.echoist.linkedout.presentation.TabletSignUpRoute

@Composable
fun TabletNavHost(
    navController: NavHostController,
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
    }
}
