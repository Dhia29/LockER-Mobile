package com.example.lockermobile.presentation.employer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lockermobile.core.ui.components.LockerButton
import com.example.lockermobile.core.ui.components.LockerTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostJobScreen(
    onBackClick: () -> Unit,
    viewModel: PostJobViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post New Job", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LockerTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = "Job Title",
                modifier = Modifier.fillMaxWidth()
            )

            LockerTextField(
                value = state.category,
                onValueChange = viewModel::onCategoryChange,
                label = "Category (e.g. Software Development)",
                modifier = Modifier.fillMaxWidth()
            )

            Text("Job Type", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Full-time", "Part-time", "Remote", "Contract").forEach { type ->
                    FilterChip(
                        selected = state.type == type,
                        onClick = { viewModel.onTypeChange(type) },
                        label = { Text(type) }
                    )
                }
            }

            LockerTextField(
                value = state.location,
                onValueChange = viewModel::onLocationChange,
                label = "Location",
                modifier = Modifier.fillMaxWidth()
            )

            LockerTextField(
                value = state.salary,
                onValueChange = viewModel::onSalaryChange,
                label = "Salary Range",
                modifier = Modifier.fillMaxWidth()
            )

            LockerTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
                label = "Job Description",
                modifier = Modifier.fillMaxWidth()
            )

            if (state.error != null) {
                Text(text = state.error!!, color = MaterialTheme.colorScheme.error)
            }

            LockerButton(
                text = if (state.isLoading) "Posting..." else "Post Job",
                onClick = viewModel::postJob,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )
        }
    }
}
