package com.example.lockermobile.presentation.employer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lockermobile.core.ui.theme.*
import com.example.lockermobile.presentation.admin.StatCard
import com.example.lockermobile.presentation.admin.ActionCard

import androidx.navigation.NavController
import com.example.lockermobile.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerDashboard(
    navController: NavController,
    viewModel: EmployerDashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Employer Dashboard", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Recruitment Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        StatCard(
                            title = "Active Jobs",
                            value = state.activeJobs.toString(),
                            icon = Icons.Default.WorkOutline,
                            color = Primary
                        )
                    }
                    item {
                        StatCard(
                            title = "Total Candidates",
                            value = state.totalCandidates.toString(),
                            icon = Icons.Default.PeopleOutline,
                            color = Success
                        )
                    }
                    item {
                        StatCard(
                            title = "Pending Apps",
                            value = state.pendingApplications.toString(),
                            icon = Icons.Default.HourglassEmpty,
                            color = Color(0xFFF59E0B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Management",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        title = "My Job Posts",
                        icon = Icons.Default.List,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Screen.EmployerJobs.route) }
                    )
                    ActionCard(
                        title = "Post New Job",
                        icon = Icons.Default.AddBusiness,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Screen.PostJob.route) }
                    )
                }
            }
        }
    }
}
