package com.echoist.linkedout.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.community.TabletCommunityRoute
import com.echoist.linkedout.presentation.community.bookmark.CommunitySavedEssayPage
import com.echoist.linkedout.presentation.community.search.TabletSearchScreen
import com.echoist.linkedout.presentation.essay.detail.CommunityDetailPage
import com.echoist.linkedout.presentation.essay.write.TabletEssayWriteRoute
import com.echoist.linkedout.presentation.essay.write.WritingCompletePage
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.home.HomeViewModel
import com.echoist.linkedout.presentation.home.TabletHomeRoute
import com.echoist.linkedout.presentation.login.TabletLoginRoute
import com.echoist.linkedout.presentation.login.agreeofprovisions.TabletAgreeOfProvisionScreen
import com.echoist.linkedout.presentation.login.onboarding.TabletOnBoardingRoute
import com.echoist.linkedout.presentation.login.signup.TabletSignUpCompleteRoute
import com.echoist.linkedout.presentation.login.signup.TabletSignUpRoute
import com.echoist.linkedout.presentation.myLog.TemporaryStoragePage
import com.echoist.linkedout.presentation.myLog.mylog.CompletedEssayPage
import com.echoist.linkedout.presentation.myLog.mylog.MyLogDetailPage
import com.echoist.linkedout.presentation.myLog.mylog.MyLogViewModel
import com.echoist.linkedout.presentation.myLog.mylog.TabletMyLogRoute
import com.echoist.linkedout.presentation.myLog.story.DetailEssayInStoryScreen
import com.echoist.linkedout.presentation.myLog.story.StoryDetailPage
import com.echoist.linkedout.presentation.myLog.story.StoryPage
import com.echoist.linkedout.presentation.userInfo.TabletMyInfoRoute
import com.echoist.linkedout.presentation.userInfo.account.TabletAccountRoute
import com.echoist.linkedout.presentation.userInfo.account.changeemail.TabletChangeEmailScreen
import com.echoist.linkedout.presentation.userInfo.account.changepassword.TabletChangePasswordScreen
import com.echoist.linkedout.presentation.userInfo.account.changepassword.TabletResetPwRoute
import com.echoist.linkedout.presentation.userInfo.account.deleteaccount.TabletDeleteAccountRoute
import com.echoist.linkedout.presentation.userInfo.badge.TabletBadgeRoute
import com.echoist.linkedout.presentation.userInfo.recentviewedessay.RecentEssayDetailPage
import com.echoist.linkedout.presentation.userInfo.recentviewedessay.TabletRecentEssayScreen
import com.echoist.linkedout.presentation.util.Routes
import com.echoist.linkedout.presentation.util.TYPE_STORY
import com.echoist.linkedout.presentation.util.navigateWithClearBackStack

@Composable
fun TabletNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.LoginPage
) {
    val writingViewModel: WritingViewModel = hiltViewModel()
    val myLogViewModel: MyLogViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val communityViewModel: CommunityViewModel = hiltViewModel()

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
                onStartClick = { navigateWithClearBackStack(navController, Routes.LoginPage) }
            )
        }
        composable(Routes.MyLogDetailPage) {
            MyLogDetailPage(
                navController = navController,
                myLogViewModel,
                writingViewModel
            )
        }
        composable(Routes.ResetPwPageWithEmail) {
            TabletResetPwRoute(navController = navController)
        }
        composable(Routes.SignUp) {
            TabletSignUpRoute(navController = navController)
        }
        composable(Routes.WritingCompletePage) {
            WritingCompletePage(navController, writingViewModel)
        }
        composable(Routes.SignUpComplete) {
            TabletSignUpCompleteRoute {
                navigateWithClearBackStack(navController, "${Routes.Home}/200")
            }
        }
        composable(Routes.CompletedEssayPage) {
            CompletedEssayPage(
                navController = navController,
                myLogViewModel,
                writingViewModel
            )
        }
        composable(
            route = "${Routes.Home}/{statusCode}",
        ) { backStackEntry ->
            val statusCode = backStackEntry.arguments?.getInt("statusCode") ?: 200
            TabletHomeRoute(
                statusCode = statusCode,
                viewModel = homeViewModel
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
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .padding(top = 56.dp),
                viewModel = myLogViewModel,
                writingViewModel = writingViewModel
            )
        }
        composable(Routes.Community) {
            TabletCommunityRoute(
                navController = navController,
                modifier = Modifier.padding(contentPadding)
            )
        }
        composable(Routes.CommunityDetailPage) {
            CommunityDetailPage(navController, communityViewModel)
        }
        composable(Routes.Settings) {
            TabletMyInfoRoute(
                navController = navController,
                modifier = Modifier.padding(contentPadding)
            )
        }
        composable(Routes.WritingPage) {
            TabletEssayWriteRoute(navController, writingViewModel)
        }
        composable(Routes.Search) {
            TabletSearchScreen(navController)
        }
        composable(Routes.BadgePage) {
            TabletBadgeRoute(contentPadding = contentPadding)
        }
        composable(Routes.TemporaryStoragePage) {
            TemporaryStoragePage(navController, writingViewModel)
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
        composable(Routes.RecentEssayDetailPage) {
            RecentEssayDetailPage(navController, communityViewModel)
        }
        composable(Routes.AgreeOfProvisionsPage) {
            TabletAgreeOfProvisionScreen(navController) {
                navController.popBackStack()
            }
        }
        composable(
            Routes.StoryPage,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { 2000 },
                    animationSpec = tween(durationMillis = 500)
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { 2000 },
                    animationSpec = tween(durationMillis = 500)
                )
            }
        ) {
            StoryPage(myLogViewModel, navController)
        }
        composable(Routes.StoryDetailPage) {
            StoryDetailPage(myLogViewModel, navController)
        }
        composable(
            route = "${Routes.DetailEssayInStoryPage}/{essayId}/{type}/{num}",
            arguments = listOf(navArgument("essayId") { type = NavType.IntType },
                navArgument("type") { type = NavType.StringType },
                navArgument("num") { type = NavType.IntType })
        ) { backStackEntry ->
            val essayId = backStackEntry.arguments?.getInt("essayId") ?: 0
            val type = backStackEntry.arguments?.getString("type") ?: TYPE_STORY
            val num = backStackEntry.arguments?.getInt("num") ?: 0
            DetailEssayInStoryScreen(
                essayId, type, num,
                navController,
                myLogViewModel,
                writingViewModel
            )
        }
        composable(Routes.CommunitySavedEssayPage) {
            CommunitySavedEssayPage(navController, communityViewModel)
        }
    }
}