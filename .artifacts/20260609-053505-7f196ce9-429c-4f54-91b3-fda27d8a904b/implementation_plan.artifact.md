# Implementation Plan - Role-Based Restructuring

Restructure LockER Mobile to separate functionality between Administrator, Employer, and Job Seeker roles using independent navigation graphs and role-specific UI components.

## User Review Required

- **Navigation Flow**: Does the proposed redirection from Splash to role-specific Home routes match your expectations?
- **Shared Components**: The `ChatPage` and `CommunityPage` will remain shared but will eventually need role-specific logic (e.g., moderation tools for Admins). This plan focuses on the initial separation.

## Proposed Changes

### [Presentation - Navigation & Routing]

Restructure the navigation system to support role-based redirection and independent bottom bars.

#### [Screen.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/navigation/Screen.kt)
- Add role-specific routes: `AdminHome`, `EmployerHome`, `JobSeekerHome`.
- Add sub-routes for each role (e.g., `AdminUsers`, `EmployerJobs`, `JobSeekerApplications`).

#### [BottomNavigationBar.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/components/BottomNavigationBar.kt)
- Refactor to accept `UserRole` and dynamically generate items.
- Define `AdminNavItem`, `EmployerNavItem`, and `JobSeekerNavItem` sealed classes.

#### [NavGraph.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/navigation/NavGraph.kt)
- Update `NavGraph` to handle redirection based on `UserRole`.
- Implement logic to switch between role-specific start destinations.

---

### [Presentation - Role-Specific Packages]

Move and create screens under organized role-based packages.

#### [NEW] [AdminDashboard.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/admin/AdminDashboard.kt)
- Create a new dashboard for Administrators focused on platform metrics and moderation.

#### [NEW] [EmployerDashboard.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/employer/EmployerDashboard.kt)
- Create a new dashboard for Employers focused on job management and applicant tracking.

#### [Home - Moved](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/jobseeker/JobSeekerHome.kt)
- Rename and move `HomePage.kt` to `presentation/jobseeker/JobSeekerHome.kt` as it is primarily for job seekers.

---

### [Core - Logic & Auth]

#### [MainActivity.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/MainActivity.kt)
- Update `showBottomBar` logic to be role-aware.
- Pass the current user's role to the `BottomNavigationBar`.

#### [SplashViewModel.kt](file:///D:/Tugas Kuliah/PEABEH/LockERmobile/app/src/main/java/com/example/lockermobile/presentation/auth/SplashViewModel.kt)
- Update `checkSession` to set `startDestination` based on `UserRole` (e.g., `admin_home`, `employer_home`, `jobseeker_home`).

---

## Verification Plan

### Automated Tests
- Since this is a structural refactor, I will verify via compilation and UI testing.
- Command: `./gradlew assembleDebug` (to ensure no broken references).

### Manual Verification
1. **Login as Admin**: Verify redirection to Admin Dashboard and Admin Bottom Bar.
2. **Login as Employer**: Verify redirection to Employer Dashboard and Employer Bottom Bar.
3. **Login as Job Seeker**: Verify redirection to Job Seeker Home and Job Seeker Bottom Bar.
4. **Logout/Login Cycle**: Ensure roles are correctly preserved and navigated after session changes.
5. **Route Protection**: Attempt to access `/admin_home` as a Job Seeker and verify it fails (handled by `NavGraph` logic).
