package com.example.lockermobile.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lockermobile.domain.model.UserRole
import com.example.lockermobile.presentation.navigation.Screen
import com.example.lockermobile.core.ui.theme.Primary

sealed class BottomNavItem(val title: String, val icon: ImageVector, val screen: Screen) {
    // Admin Items
    object AdminDashboard : BottomNavItem("Dashboard", Icons.Default.Dashboard, Screen.AdminDashboard)
    object AdminUsers : BottomNavItem("Users", Icons.Default.Group, Screen.AdminUsers)
    object AdminReports : BottomNavItem("Reports", Icons.Default.Assessment, Screen.AdminReports)
    
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .height(72.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(28.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.screen.route
                val tint by animateColorAsState(if (isSelected) Primary else Color.Gray.copy(alpha = 0.6f))
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
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
                ) {
                    Icon(
                        imageVector = item.icon, 
                        contentDescription = item.title,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        color = tint,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
