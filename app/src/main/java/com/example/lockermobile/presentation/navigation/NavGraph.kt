package com.example.lockermobile.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lockermobile.presentation.auth.*
import com.example.lockermobile.presentation.jobseeker.JobSeekerHome
import com.example.lockermobile.presentation.jobseeker.JobApplicationsPage
import com.example.lockermobile.presentation.admin.AdminDashboard
import com.example.lockermobile.presentation.admin.AdminUsersScreen
import com.example.lockermobile.presentation.admin.AdminReportsScreen
import com.example.lockermobile.presentation.community.CommunityPage
import com.example.lockermobile.presentation.chat.ChatPage
import com.example.lockermobile.presentation.chat.ChatDetailScreen
import com.example.lockermobile.presentation.profile.ProfilePage
import com.example.lockermobile.presentation.profile.SettingsPage
import com.example.lockermobile.presentation.home.NotificationsPage
import com.example.lockermobile.presentation.jobs.JobDetailPage

import com.example.lockermobile.domain.model.UserRole
import com.example.lockermobile.core.data.SessionManager
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues(),
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val startDestination by splashViewModel.startDestination.collectAsState()
    val isReady by splashViewModel.isReady.collectAsState()
    
    // Role protection - in a real app this might be handled via a higher-level state
    // but for this cleanup we'll add basic checks in the composables.

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        // Auth Flow
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToLogin = {})
            
            LaunchedEffect(isReady, startDestination) {
                if (isReady && startDestination != null) {
                    navController.navigate(startDestination!!) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            
            LoginPage(
                navController = navController,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {}
            )

            LaunchedEffect(state.isLoginSuccess) {
                if (state.isLoginSuccess) {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            
            RegisterPage(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {}
            )

            LaunchedEffect(state.isRegisterSuccess) {
                if (state.isRegisterSuccess) {
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Admin Flow
        composable(Screen.AdminDashboard.route) {
            AdminDashboard(navController)
        }
        composable(Screen.AdminUsers.route) {
            AdminUsersScreen()
        }
        composable(Screen.AdminReports.route) {
            AdminReportsScreen()
        }

        // Job Seeker Flow
        composable(Screen.Home.route) {
            JobSeekerHome(
                onJobClick = { jobId ->
                    navController.navigate(Screen.JobDetail.createRoute(jobId))
                },
                onNotificationClick = {
                    navController.navigate(Screen.Notifications.route)
                }
            )
        }
        composable(Screen.JobApplications.route) {
            JobApplicationsPage()
        }

        // Shared Flow
        composable(Screen.Community.route) {
            CommunityPage()
        }
        composable(Screen.Chat.route) {
            ChatPage(
                onConversationClick = { chatId ->
                    navController.navigate(Screen.ChatDetail.createRoute(chatId))
                }
            )
        }
        composable(Screen.ChatDetail.route) {
            ChatDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfilePage(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsPage(
                navController = navController,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Notifications.route) {
            NotificationsPage(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.SavedJobs.route) {
            // Reusing Home for now or create a filtered list
            JobSeekerHome(
                onJobClick = { /* TODO */ },
                onNotificationClick = {
                    navController.navigate(Screen.Notifications.route)
                }
            )
        }
        composable(Screen.JobDetail.route) {
            JobDetailPage(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
