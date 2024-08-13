package com.echoist.linkedout.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.echoist.linkedout.Routes
import com.echoist.linkedout.page.login.OnBoardingPage
import com.echoist.linkedout.presentation.TabletLoginRoute

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
                navigateToResetPassword = { navController.navigate("ResetPwPageWithEmail") },
                navigateToSignUp = { navController.navigate("SIGNUP") }
            )
        }
        composable(Routes.OnBoarding) {
            OnBoardingPage(navController)
        }
    }
}
