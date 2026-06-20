package com.example.lockermobile.presentation.navigation

sealed class Screen(val route: String) {
    // Auth Routes
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    
    // Shared App Routes
    object Community : Screen("community")
    object Chat : Screen("chat")
    object ChatDetail : Screen("chat_detail/{chatId}") {
        fun createRoute(chatId: String) = "chat_detail/$chatId"
    }
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Notifications : Screen("notifications")
    
    // Admin Routes
    object AdminDashboard : Screen("admin_dashboard")
    object AdminUsers : Screen("admin_users")
    object AdminReports : Screen("admin_reports")
    
    // Job Seeker Routes
    object Home : Screen("home")
    object JobApplications : Screen("job_applications")
    object SavedJobs : Screen("saved_jobs")
    
    // Job Detail (Seeker focus)
    object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: String) = "job_detail/$jobId"
    }
}
