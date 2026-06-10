package com.example.lockermobile.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lockermobile.domain.model.UserRole
import com.example.lockermobile.presentation.navigation.Screen

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screen: Screen) {
    // Admin Items
    object AdminDashboard : BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.AdminDashboard)
    object AdminUsers : BottomNavItem("Users", Icons.Default.Group, Screen.AdminUsers)
    object AdminReports : BottomNavItem("Reports", Icons.Default.Assessment, Screen.AdminReports)
    
    // Employer Items
    object EmployerDashboard : BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.EmployerDashboard)
    object EmployerJobs : BottomNavItem("Jobs", Icons.Default.Work, Screen.EmployerJobs)
    object EmployerCandidates : BottomNavItem("Candidates", Icons.Default.People, Screen.EmployerCandidates)
    
    // Job Seeker Items
    object Home : BottomNavItem("Home", Icons.Default.Home, Screen.Home)
    object Applications : BottomNavItem("Apps", Icons.Default.Assignment, Screen.JobApplications)
    
    // Shared Items
    object Community : BottomNavItem("Community", Icons.Default.Groups, Screen.Community)
    object Chat : BottomNavItem("Chat", Icons.AutoMirrored.Filled.Chat, Screen.Chat)
    object Profile : BottomNavItem("Profile", Icons.Default.Person, Screen.Profile)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    role: UserRole
) {
    val items = when (role) {
        UserRole.ADMIN -> listOf(
            BottomNavItem.AdminDashboard,
            BottomNavItem.AdminUsers,
            BottomNavItem.AdminReports,
            BottomNavItem.Community,
            BottomNavItem.Profile
        )
        UserRole.EMPLOYER -> listOf(
            BottomNavItem.EmployerDashboard,
            BottomNavItem.EmployerJobs,
            BottomNavItem.EmployerCandidates,
            BottomNavItem.Community,
            BottomNavItem.Profile
        )
        UserRole.JOB_SEEKER -> listOf(
            BottomNavItem.Home,
            BottomNavItem.Applications,
            BottomNavItem.Community,
            BottomNavItem.Chat,
            BottomNavItem.Profile
        )
    }
    
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = isSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(items.first().screen.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
