package com.example.lockermobile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lockermobile.core.ui.components.GlassCard
import com.example.lockermobile.core.ui.components.GlassSection
import com.example.lockermobile.core.ui.theme.*
import com.example.lockermobile.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 8.dp).background(Color.White.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                
                SettingsGroup(title = "Personal Account") {
                    SettingItem(title = "Update Profile", icon = Icons.Default.Person, onClick = { navController.navigate(Screen.Profile.route) })
                    SettingItem(title = "Security & Password", icon = Icons.Default.Security, onClick = { /* TODO */ })
                    SettingItem(title = "Push Notifications", icon = Icons.Default.Notifications, onClick = { /* TODO */ })
                }

                SettingsGroup(title = "App Preferences") {
                    SettingItem(title = "App Language", icon = Icons.Default.Language, onClick = { /* TODO */ })
                    SettingItem(title = "Privacy Policy", icon = Icons.Default.PrivacyTip, onClick = { /* TODO */ })
                }

                SettingsGroup(title = "More Info") {
                    SettingItem(title = "Help & Feedback", icon = Icons.Default.Help, onClick = { /* TODO */ })
                    SettingItem(title = "About Version", icon = Icons.Default.Info, onClick = { /* TODO */ })
                }

                Spacer(modifier = Modifier.height(24.dp))

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.logout(onLogout) },
                    alpha = 0.5f
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Error)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Sign Out", fontWeight = FontWeight.ExtraBold, color = Error, style = MaterialTheme.typography.titleMedium)
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title, 
            style = MaterialTheme.typography.labelLarge, 
            fontWeight = FontWeight.ExtraBold,
            color = Primary,
            modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
        )
        GlassCard(modifier = Modifier.fillMaxWidth(), alpha = 0.3f) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        Icon(
            Icons.Default.ChevronRight, 
            contentDescription = null, 
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}
