package com.example.lockermobile.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lockermobile.core.ui.theme.*
import com.example.lockermobile.core.ui.components.AvatarImage
import com.example.lockermobile.core.ui.components.GlassCard
import com.example.lockermobile.core.ui.components.GlassButton
import com.example.lockermobile.core.ui.components.GlassSection
import com.example.lockermobile.domain.model.Experience
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    onLogout: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var showBioDialog by remember { mutableStateOf(false) }
    var showSkillDialog by remember { mutableStateOf(false) }
    var showExpDialog by remember { mutableStateOf(false) }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                val inputStream = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                bytes?.let { viewModel.onProfilePictureSelect(it) }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.ExtraBold) },
                actions = {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.background(Color.White.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Primary)
                    }
                    IconButton(
                        onClick = { viewModel.logout(onLogout) },
                        modifier = Modifier.padding(start = 8.dp).background(Color.White.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Error)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
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
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (state.user != null) {
                val user = state.user!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 32.dp,
                        alpha = 0.4f
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box {
                                AvatarImage(
                                    model = user.profilePicture,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, Color.White, CircleShape)
                                        .clickable { photoLauncher.launch("image/*") },
                                    placeholderIcon = Icons.Default.AddAPhoto
                                )
                                IconButton(
                                    onClick = { photoLauncher.launch("image/*") },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(30.dp)
                                        .background(Primary, CircleShape)
                                        .border(2.dp, Color.White, CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit Photo",
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = user.role,
                                style = MaterialTheme.typography.titleMedium,
                                color = Primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = user.location, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ProfileStatItem(label = "Applied", value = "12")
                                ProfileStatItem(label = "Saved", value = "24")
                                ProfileStatItem(label = "Connections", value = "148")
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    GlassSection(title = "Professional Bio", onActionClick = { showBioDialog = true }, actionText = "Edit") {
                        GlassCard(modifier = Modifier.fillMaxWidth(), alpha = 0.25f) {
                            Text(
                                text = user.bio.ifBlank { "Describe your professional journey..." }, 
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (user.bio.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    GlassSection(title = "Core Skills", onActionClick = { showSkillDialog = true }, actionText = "Add") {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            user.skills.forEach { skill ->
                                FilterChip(
                                    selected = true,
                                    onClick = { viewModel.onSkillRemove(skill) },
                                    label = { Text(skill, fontWeight = FontWeight.Bold) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Primary.copy(alpha = 0.15f),
                                        selectedLabelColor = Primary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = true, borderColor = Primary.copy(alpha = 0.3f), selectedBorderColor = Primary),
                                    trailingIcon = { Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp)) }
                                )
                            }
                        }
                    }
                    
                    GlassSection(title = "Work Experience", onActionClick = { showExpDialog = true }, actionText = "Add") {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            user.experience.forEach { exp ->
                                GlassCard(modifier = Modifier.fillMaxWidth(), alpha = 0.25f) {
                                    Row(verticalAlignment = Alignment.Top) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = exp.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                                            Text(text = exp.company, color = Primary, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = "${exp.startDate} - ${exp.endDate}", 
                                                style = MaterialTheme.typography.labelMedium, 
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = exp.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                        }
                                        IconButton(onClick = { viewModel.onExperienceDelete(exp) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Error.copy(alpha = 0.6f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    if (showBioDialog) {
        var bioText by remember { mutableStateOf(state.user?.bio ?: "") }
        AlertDialog(
            onDismissRequest = { showBioDialog = false },
            title = { Text("Update Bio", fontWeight = FontWeight.ExtraBold) },
            text = {
                OutlinedTextField(
                    value = bioText,
                    onValueChange = { bioText = it },
                    placeholder = { Text("Bio...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            },
            confirmButton = {
                GlassButton(text = "Save", onClick = { viewModel.onBioUpdate(bioText); showBioDialog = false }, modifier = Modifier.width(100.dp))
            },
            dismissButton = {
                TextButton(onClick = { showBioDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showSkillDialog) {
        var skillText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showSkillDialog = false },
            title = { Text("Add New Skill", fontWeight = FontWeight.ExtraBold) },
            text = {
                OutlinedTextField(
                    value = skillText,
                    onValueChange = { skillText = it },
                    placeholder = { Text("e.g. Kotlin") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
            },
            confirmButton = {
                GlassButton(text = "Add", onClick = { viewModel.onSkillAdd(skillText); showSkillDialog = false }, modifier = Modifier.width(100.dp))
            },
            dismissButton = {
                TextButton(onClick = { showSkillDialog = false }) { Text("Done") }
            }
        )
    }

    if (showExpDialog) {
        var title by remember { mutableStateOf("") }
        var company by remember { mutableStateOf("") }
        var start by remember { mutableStateOf("") }
        var end by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showExpDialog = false },
            title = { Text("Add Experience", fontWeight = FontWeight.ExtraBold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Position") }, shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company") }, shape = RoundedCornerShape(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Start") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                        OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("End") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                    }
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.height(100.dp), shape = RoundedCornerShape(12.dp))
                }
            },
            confirmButton = {
                GlassButton(text = "Save", onClick = { viewModel.onExperienceAdd(Experience(title, company, start, end, desc)); showExpDialog = false }, modifier = Modifier.width(120.dp))
            },
            dismissButton = {
                TextButton(onClick = { showExpDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Primary)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
