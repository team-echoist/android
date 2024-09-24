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
import com.echoist.linkedout.presentation.community.bookmark.CommunitySavedEssayPage
import com.echoist.linkedout.presentation.community.CommunityPage
import com.echoist.linkedout.presentation.community.CommunityViewModel
import com.echoist.linkedout.presentation.community.FullSubscriberPage
import com.echoist.linkedout.presentation.essay.detail.CommunityDetailPage
import com.echoist.linkedout.presentation.essay.write.CropImagePage
import com.echoist.linkedout.presentation.essay.write.WritingViewModel
import com.echoist.linkedout.presentation.home.NotificationPage
import com.echoist.linkedout.presentation.home.drawable.inquiry.InquiryScreen
import com.echoist.linkedout.presentation.home.drawable.legal_Notice.FontCopyRight
import com.echoist.linkedout.presentation.home.drawable.legal_Notice.LocationPolicyPage
import com.echoist.linkedout.presentation.home.drawable.legal_Notice.PrivacyPolicyPage
import com.echoist.linkedout.presentation.home.drawable.legal_Notice.TermsAndConditionsPage
import com.echoist.linkedout.presentation.home.drawable.linkedoutsupport.LinkedOutSupportScreen
import com.echoist.linkedout.presentation.home.drawable.notice.NoticeDetailPage
import com.echoist.linkedout.presentation.home.drawable.notice.NoticeScreen
import com.echoist.linkedout.presentation.home.drawable.notificationsetting.NotificationSettingScreen
import com.echoist.linkedout.presentation.home.drawable.support.SupportScreen
import com.echoist.linkedout.presentation.home.drawable.support.SupportViewModel
import com.echoist.linkedout.presentation.home.drawable.thememode.ThemeModeScreen
import com.echoist.linkedout.presentation.home.drawable.updatehistory.UpdateHistoryScreen
import com.echoist.linkedout.presentation.home.home.HomePage
import com.echoist.linkedout.presentation.login.agreeofprovisions.AgreeOfProvisionsPage
import com.echoist.linkedout.presentation.login.LoginPage
import com.echoist.linkedout.presentation.login.onboarding.OnBoardingPage
import com.echoist.linkedout.presentation.login.signup.SignUpCompletePage
import com.echoist.linkedout.presentation.login.signup.SignUpPage
import com.echoist.linkedout.presentation.login.signup.SignUpViewModel
import com.echoist.linkedout.presentation.myLog.CompletedEssayPage
import com.echoist.linkedout.presentation.myLog.DetailEssayInStoryScreen
import com.echoist.linkedout.presentation.myLog.MyLogDetailPage
import com.echoist.linkedout.presentation.myLog.MyLogPage
import com.echoist.linkedout.presentation.myLog.MyLogViewModel
import com.echoist.linkedout.presentation.myLog.StoryDetailPage
import com.echoist.linkedout.presentation.myLog.StoryPage
import com.echoist.linkedout.presentation.myLog.TemporaryStoragePage
import com.echoist.linkedout.presentation.myLog.Token
import com.echoist.linkedout.presentation.myLog.WritingCompletePage
import com.echoist.linkedout.presentation.myLog.WritingPage
import com.echoist.linkedout.presentation.userInfo.MyPage
import com.echoist.linkedout.presentation.userInfo.account.AccountPage
import com.echoist.linkedout.presentation.userInfo.account.changeemail.ChangeEmailPage
import com.echoist.linkedout.presentation.userInfo.account.changepassword.ChangePwPage
import com.echoist.linkedout.presentation.userInfo.account.changepassword.ResetPwPage
import com.echoist.linkedout.presentation.userInfo.account.changepassword.ResetPwPageWithEmail
import com.echoist.linkedout.presentation.userInfo.account.deleteaccount.AccountWithdrawalPage
import com.echoist.linkedout.presentation.userInfo.badge.BadgePage
import com.echoist.linkedout.presentation.userInfo.recentviewedessay.RecentEssayDetailPage
import com.echoist.linkedout.presentation.userInfo.recentviewedessay.RecentViewedEssayPage
import com.echoist.linkedout.presentation.userInfo.subscriber.ProfilePage
import com.echoist.linkedout.presentation.util.Routes

@Composable
fun MobileApp(
    navController: NavHostController,
    startDestination: String
) {
    val writingViewModel: WritingViewModel = hiltViewModel()
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val myLogViewModel: MyLogViewModel = hiltViewModel()
    val communityViewModel: CommunityViewModel = hiltViewModel()
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
            SignUpCompletePage(navController = navController)
        }
        composable(
            "${Routes.Home}/{statusCode}",
            arguments = listOf(navArgument("statusCode") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val statusCode =
                backStackEntry.arguments?.getInt("statusCode") ?: 200
            HomePage(navController, writingViewModel = writingViewModel, statusCode = statusCode)
        }
        composable(Routes.ThemeModeScreen) {
            ThemeModeScreen(navController)
        }
        composable(Routes.NotificationPage) {
            NotificationPage(navController)
        }
        composable(Routes.NotificationSettingScreen) {
            NotificationSettingScreen(navController)
        }
        composable(Routes.SupportScreen) {
            SupportScreen(navController)
        }
        composable(Routes.LinkedOutSupportScreen) {
            LinkedOutSupportScreen(navController)
        }
        composable(Routes.InquiryScreen) {
            InquiryScreen(navController)
        }
        composable(Routes.NoticeScreen) {
            NoticeScreen(navController, supportViewModel)
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
        composable(Routes.UpdateHistoryScreen) {
            UpdateHistoryScreen(navController)
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
            DetailEssayInStoryScreen(
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
            BadgePage(navController)
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

