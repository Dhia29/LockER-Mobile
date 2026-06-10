package com.example.lockermobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.domain.model.UserRole
import com.example.lockermobile.core.ui.theme.LockERMobileTheme
import com.example.lockermobile.presentation.navigation.NavGraph
import com.example.lockermobile.presentation.navigation.Screen
import com.example.lockermobile.presentation.components.BottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LockERMobileTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                val userRole by sessionManager.userRole.collectAsState(initial = null)

                val showBottomBar = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.JobApplications.route,
                    Screen.AdminDashboard.route,
                    Screen.AdminUsers.route,
                    Screen.AdminReports.route,
                    Screen.EmployerDashboard.route,
                    Screen.EmployerJobs.route,
                    Screen.EmployerCandidates.route,
                    Screen.Community.route,
                    Screen.Chat.route,
                    Screen.Profile.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar && userRole != null) {
                            BottomNavigationBar(
                                navController = navController,
                                role = userRole!!
                            )
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }
}
