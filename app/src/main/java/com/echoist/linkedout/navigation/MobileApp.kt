package com.echoist.linkedout.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.echoist.linkedout.Routes
import com.echoist.linkedout.components.CropImagePage
import com.echoist.linkedout.page.community.CommunityDetailPage
import com.echoist.linkedout.page.community.CommunityPage
import com.echoist.linkedout.page.community.CommunitySavedEssayPage
import com.echoist.linkedout.page.community.FullSubscriberPage
import com.echoist.linkedout.page.home.DarkModeSettingPage
import com.echoist.linkedout.page.home.HomePage
import com.echoist.linkedout.page.home.InquiryPage
import com.echoist.linkedout.page.home.LinkedOutSupportPage
import com.echoist.linkedout.page.home.NoticeDetailPage
import com.echoist.linkedout.page.home.NoticePage
import com.echoist.linkedout.page.home.NotificationPage
import com.echoist.linkedout.page.home.NotificationSettingPage
import com.echoist.linkedout.page.home.SupportPage
import com.echoist.linkedout.page.home.UpdateHistoryPage
import com.echoist.linkedout.page.home.legal_Notice.FontCopyRight
import com.echoist.linkedout.page.home.legal_Notice.LocationPolicyPage
import com.echoist.linkedout.page.home.legal_Notice.PrivacyPolicyPage
import com.echoist.linkedout.page.home.legal_Notice.TermsAndConditionsPage
import com.echoist.linkedout.page.login.AgreeOfProvisionsPage
import com.echoist.linkedout.page.login.LoginPage
import com.echoist.linkedout.page.login.OnBoardingPage
import com.echoist.linkedout.page.login.SignUpCompletePage
import com.echoist.linkedout.page.login.SignUpPage
import com.echoist.linkedout.page.myLog.CompletedEssayPage
import com.echoist.linkedout.page.myLog.DetailEssayInStoryPage
import com.echoist.linkedout.page.myLog.MyLogDetailPage
import com.echoist.linkedout.page.myLog.MyLogPage
import com.echoist.linkedout.page.myLog.StoryDetailPage
import com.echoist.linkedout.page.myLog.StoryPage
import com.echoist.linkedout.page.myLog.TemporaryStoragePage
import com.echoist.linkedout.page.myLog.Token
import com.echoist.linkedout.page.myLog.WritingCompletePage
import com.echoist.linkedout.page.myLog.WritingPage
import com.echoist.linkedout.page.settings.AccountPage
import com.echoist.linkedout.page.settings.AccountWithdrawalPage
import com.echoist.linkedout.page.settings.BadgePage
import com.echoist.linkedout.page.settings.ChangeEmailPage
import com.echoist.linkedout.page.settings.ChangePwPage
import com.echoist.linkedout.page.settings.MyPage
import com.echoist.linkedout.page.settings.ProfilePage
import com.echoist.linkedout.page.settings.RecentEssayDetailPage
import com.echoist.linkedout.page.settings.RecentViewedEssayPage
import com.echoist.linkedout.page.settings.ResetPwPage
import com.echoist.linkedout.page.settings.ResetPwPageWithEmail
import com.echoist.linkedout.viewModels.CommunityViewModel
import com.echoist.linkedout.viewModels.HomeViewModel
import com.echoist.linkedout.viewModels.MyLogViewModel
import com.echoist.linkedout.viewModels.MyPageViewModel
import com.echoist.linkedout.viewModels.SignUpViewModel
import com.echoist.linkedout.viewModels.SupportViewModel
import com.echoist.linkedout.viewModels.WritingViewModel

@Composable
fun MobileApp(
    navController: NavHostController,
    startDestination: String
) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val writingViewModel: WritingViewModel = hiltViewModel()
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val myLogViewModel: MyLogViewModel = hiltViewModel()
    val communityViewModel: CommunityViewModel = hiltViewModel()
    val settingsViewModel: MyPageViewModel = hiltViewModel()
    val supportViewModel: SupportViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.OnBoarding) {
            OnBoardingPage(navController)
        }
        composable(Routes.LoginPage) {
            LoginPage(navController = navController)
        }
        composable(Routes.SignUp) {
            SignUpPage(navController, signUpViewModel)
        }
        composable(Routes.AgreeOfProvisionsPage) {
            AgreeOfProvisionsPage(navController, signUpViewModel)
        }
        composable(Routes.SignUpComplete) {
            SignUpCompletePage(homeViewModel, navController)
        }
        composable(
            "${Routes.Home}/{statusCode}",
            arguments = listOf(navArgument("statusCode") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val statusCode =
                backStackEntry.arguments?.getInt("statusCode") ?: 200
            HomePage(navController, homeViewModel, writingViewModel, statusCode)
        }
        composable(Routes.DarkModeSettingPage) {
            DarkModeSettingPage(navController)
        }
        composable(Routes.NotificationPage) {
            NotificationPage(navController)
        }
        composable(Routes.NotificationSettingPage) {
            NotificationSettingPage(navController)
        }
        composable(Routes.SupportPage) {
            SupportPage(navController)
        }
        composable(Routes.LinkedOutSupportPage) {
            LinkedOutSupportPage(navController)
        }
        composable(Routes.InquiryPage) {
            InquiryPage(navController)
        }
        composable(Routes.NoticePage) {
            NoticePage(navController, supportViewModel)
        }
        composable(
            route = "${Routes.NoticeDetailPage}/{noticeId}",
            arguments = listOf(navArgument("noticeId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val noticeId = backStackEntry.arguments?.getInt("noticeId")
            NoticeDetailPage(navController, noticeId!!)
        }
        composable(Routes.UpdateHistoryPage) {
            UpdateHistoryPage(navController)
        }
        composable(
            route = "${Routes.MyLog}/{page}",
            arguments = listOf(navArgument("page") { type = NavType.IntType })
        ) { backStackEntry ->
            val page = backStackEntry.arguments?.getInt("page") ?: 0
            MyLogPage(navController, myLogViewModel, writingViewModel, page)

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
        composable(Routes.DetailEssayInStoryPage) {
            DetailEssayInStoryPage(
                navController,
                myLogViewModel,
                writingViewModel
            )
        }
        composable(Routes.MyLogDetailPage) {
            MyLogDetailPage(
                navController = navController,
                myLogViewModel,
                writingViewModel
            )
        }
        composable(Routes.CompletedEssayPage) {
            CompletedEssayPage(
                navController = navController,
                myLogViewModel,
                writingViewModel
            )
        }
        composable(Routes.Community) {
            CommunityPage(navController = navController, communityViewModel)
        }
        composable(Routes.CommunityDetailPage) {
            CommunityDetailPage(navController, communityViewModel)
        }
        composable(Routes.CommunitySavedEssayPage) {
            CommunitySavedEssayPage(navController, communityViewModel)
        }
        composable(Routes.SubscriberPage) {
            ProfilePage(
                viewModel = communityViewModel,
                navController = navController
            )
        }
        composable(Routes.FullSubscriberPage) {
            FullSubscriberPage(communityViewModel, navController)
        }
        composable(Routes.Settings) {
            MyPage(navController)
        }
        composable(Routes.RecentViewedEssayPage) {
            RecentViewedEssayPage(navController, communityViewModel)
        }
        composable(Routes.RecentEssayDetailPage) {
            RecentEssayDetailPage(navController, communityViewModel)
        }
        composable(
            Routes.AccountPage,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://linkedoutapp.com/${Routes.AccountPage}"
            })
        ) {
            AccountPage(navController)
        }
        composable(Routes.ChangeEmailPage) {
            ChangeEmailPage(navController)
        }
        composable(Routes.ChangePwPage) {
            ChangePwPage(navController)
        }
        composable(Routes.ResetPwPageWithEmail) {
            ResetPwPageWithEmail(navController)
        }
        composable(
            Routes.ResetPwPage,
            deepLinks = listOf(navDeepLink {
                uriPattern =
                    "https://linkedoutapp.com/${Routes.ResetPwPage}?token={token}"
            }),

            arguments = listOf(navArgument("token") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            if (it.arguments?.getString("token").toString().isNotEmpty()) {
                Token.accessToken = it.arguments?.getString("token").toString()
                Log.i("header token by deepLink:", " ${Token.accessToken}")
            }
            ResetPwPage(
                navController,
                it.arguments?.getString("token").toString()
            )
        }
        composable(Routes.AccountWithdrawalPage) {
            AccountWithdrawalPage(navController)
        }
        composable(Routes.BadgePage) {
            BadgePage(navController, settingsViewModel)
        }
        composable(
            Routes.WritingPage,
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
            WritingPage(navController, writingViewModel)
        }
        composable(Routes.WritingCompletePage) {
            WritingCompletePage(navController, writingViewModel)
        }
        composable(Routes.TemporaryStoragePage) {
            TemporaryStoragePage(navController, writingViewModel)
        }
        composable(Routes.CropImagePage) {
            CropImagePage(navController, writingViewModel)
        }
        composable(Routes.TermsAndConditionsPage) {
            TermsAndConditionsPage(navController)
        }
        composable(Routes.PrivacyPolicyPage) {
            PrivacyPolicyPage(navController)
        }
        composable(Routes.LocationPolicyPage) {
            LocationPolicyPage(navController)
        }
        composable(Routes.FontCopyRight) {
            FontCopyRight(navController)
        }

    }
}

