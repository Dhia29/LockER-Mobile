package com.example.lockermobile.presentation.jobseeker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lockermobile.domain.model.Job
import com.example.lockermobile.domain.model.User
import com.example.lockermobile.core.ui.components.GlassCard
import com.example.lockermobile.core.ui.components.GlassSection
import com.example.lockermobile.core.ui.components.AvatarImage
import com.example.lockermobile.core.ui.theme.*
import com.example.lockermobile.presentation.home.HomeViewModel

@Composable
fun JobSeekerHome(
    onJobClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                user = state.user,
                searchQuery = state.searchQuery,
                onSearchChange = viewModel::onSearchQueryChange,
                onNotificationClick = onNotificationClick
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
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f),
                            Color.White.copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CategorySection(
                    selectedCategory = state.selectedCategory,
                    onCategoryClick = viewModel::onCategoryChange
                )
                
                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Primary)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            GlassSection(title = "Featured Roles") {
                                // Reusing item for simplicity or could have a horizontal pager
                            }
                        }
                        
                        item {
                            Text(
                                text = "Recommended Jobs",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                            )
                        }
                        
                        items(state.jobs) { job ->
                            JobItem(job = job, onClick = { onJobClick(job.id) })
                        }
                        
                        item { Spacer(modifier = Modifier.height(100.dp)) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    user: User?,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onNotificationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hello,",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${user?.name?.split(" ")?.firstOrNull() ?: "User"}!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Primary
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.background(Color.White.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Notifications, 
                        contentDescription = "Notifications",
                        tint = Primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // User Avatar
                AvatarImage(
                    model = user?.profilePicture,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(2.dp, Primary.copy(alpha = 0.2f), CircleShape),
                    placeholderIcon = Icons.Default.Person
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Find your dream career...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Primary) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White.copy(alpha = 0.7f),
                focusedBorderColor = Primary.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun CategorySection(
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    val categories = listOf("All", "Software Development", "Design", "Management", "Marketing", "Finance")
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier.clickable { onCategoryClick(category) },
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) Primary else Color.White.copy(alpha = 0.5f),
                contentColor = if (isSelected) OnPrimary else OnSurfaceVariant,
                border = if (!isSelected) BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)) else null,
                tonalElevation = if (isSelected) 4.dp else 0.dp
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun JobItem(job: Job, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        cornerRadius = 24.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarImage(
                model = job.logoUrl,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholderIcon = Icons.Default.Work,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = job.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = job.companyName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn, 
                        contentDescription = null, 
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = job.location,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = job.type,
                        style = MaterialTheme.typography.labelSmall,
                        color = Success,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            IconButton(onClick = { /* Save */ }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder, 
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}
