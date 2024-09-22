package com.echoist.linkedout.presentation.mobile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.echoist.linkedout.presentation.components.CommuTopAppBar
import com.echoist.linkedout.presentation.components.ProfilePage
import com.echoist.linkedout.presentation.mobile.community.SearchingPage
import com.echoist.linkedout.presentation.mobile.home.MyBottomNavigation
import com.echoist.linkedout.presentation.viewModels.CommunityViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(
    viewModel: CommunityViewModel,
    navController: NavController
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                    ModalDrawerSheet(
                        modifier = Modifier.fillMaxSize(),
                        drawerShape = RectangleShape,
                        drawerContainerColor = Color.Black
                    ) {

                        SearchingPage(drawerState = drawerState, navController = navController)

                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {


                    Scaffold(
                        topBar = {
                            CommuTopAppBar(text = "프로필", navController, viewModel) {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        },
                        bottomBar = { MyBottomNavigation(navController) },
                        content = {
                            Column(modifier = Modifier.padding(it)) {
                                ProfilePage(viewModel, navController)
                            }
                        }
                    )
                }
            })
    }
}
