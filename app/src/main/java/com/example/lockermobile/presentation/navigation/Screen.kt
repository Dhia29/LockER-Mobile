package com.example.lockermobile.presentation.navigation

sealed class Screen(val route: String) {
    // Auth Routes
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    
    // Shared App Routes
    object Community : Screen("community")
    object Chat : Screen("chat")
    object ChatDetail : Screen("chat_detail/{chatId}") {
        fun createRoute(chatId: String) = "chat_detail/$chatId"
    }
    object Profile : Screen("profile")
    
    // Admin Routes
    object AdminDashboard : Screen("admin_dashboard")
    object AdminUsers : Screen("admin_users")
    object AdminReports : Screen("admin_reports")
    
    // Employer Routes
    object EmployerDashboard : Screen("employer_dashboard")
    object EmployerJobs : Screen("employer_jobs")
    object EmployerCandidates : Screen("employer_candidates")
    object PostJob : Screen("post_job")
    
    // Job Seeker Routes
    object Home : Screen("home")
    object JobApplications : Screen("job_applications")
    
    // Job Detail (Shared/Seeker focus)
    object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: String) = "job_detail/$jobId"
    }
}
