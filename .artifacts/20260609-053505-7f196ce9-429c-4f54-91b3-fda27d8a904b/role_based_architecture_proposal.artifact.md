# Role-Based Architecture Proposal - LockER Mobile

This document outlines the proposed restructuring of the LockER Mobile application to support a robust role-based system (Administrator, Employer, Job Seeker).

## 1. Role-Feature Matrix

Based on the `product-backlog.md`, the following matrix defines the feature ownership:

| Feature ID | Feature Name | Administrator | Employer | Job Seeker |
| :--- | :--- | :---: | :---: | :---: |
| **Auth & Profile** | | | | |
| BK-01 | Admin Dashboard & Authentication | Yes | No | No |
| BK-02 | Registration & General Auth | Yes | Yes | Yes |
| BK-04 | Job Seeker Profile Management | No | No | Yes |
| EX-01 | Social Login (Google) | Yes | Yes | Yes |
| EX-06 | Profile File Management (CV/Cert) | No | No | Yes |
| **Job Management** | | | | |
| BK-03 | Job Posting Moderation | Yes | No | No |
| BK-05 | Job Posting (Create/Edit/Delete) | No | Yes | No |
| BK-06 | Job Application Submission | No | No | Yes |
| BK-07 | Job Search & Filter | No | No | Yes |
| BK-08 | Application Status Tracking | No | No | Yes |
| **Interaction** | | | | |
| BK-09 | Direct Messaging (Chat) | Yes (Support) | Yes | Yes |
| BK-10 | Professional Timeline/Feed | Yes (Mod) | Yes | Yes |
| EX-02 | Networking (Connections) | No | Yes | Yes |
| EX-03 | Community Groups | Yes (Mod) | Yes | Yes |
| EX-04 | Post Interactions (Like/Save/Report) | Yes (Mod) | Yes | Yes |
| EX-05 | Appointment & Chat Block | No | Yes | Yes |
| **System** | | | | |
| BK-11 | Help & Customer Service | Yes (Admin) | Yes (User) | Yes (User) |
| BK-12 | Notifications | Yes | Yes | Yes |

---

## 2. Navigation Architecture

The navigation will be restructured into independent graphs to ensure strict role separation and prevent unauthorized access.

### 2.1 Navigation Structure
- **RootNavGraph**: Handles the high-level transition between Auth and Role-specific graphs.
- **AuthGraph**: Splash, Login, Register.
- **AdminGraph**: Admin-specific flow (Dashboard, Moderation, User Mgmt).
- **EmployerGraph**: Employer-specific flow (Job Posting, Applicant Review).
- **JobSeekerGraph**: Job Seeker flow (Home/Discovery, Search, Applications).

### 2.2 Role-Based Routing
After a successful login or splash check, the `SplashViewModel` or a `SessionManager` will determine the user's role and trigger navigation to the corresponding graph.

---

## 3. Bottom Navigation Design

Each role will have a dedicated `BottomNavigationBar` with specific items.

| Role | Item 1 | Item 2 | Item 3 | Item 4 | Item 5 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Admin** | Dashboard | Users | Reports | Community | Profile |
| **Employer** | Dashboard | Jobs | Candidates | Community | Profile |
| **Job Seeker** | Home | Jobs | Applications | Community | Profile |

---

## 4. Screen Restructuring & Ownership

### 4.1 Categorization
Screens will be moved into role-specific packages: `com.example.lockermobile.presentation.[role]`.

| Category | Screens |
| :--- | :--- |
| **Shared** | Splash, Login, Register, Chat, Profile (Base), Settings |
| **Administrator** | AdminDashboard, UserMgmt, ReportMgmt, ContentModeration |
| **Employer** | EmployerDashboard, JobPosting, ApplicantReview, RecruitmentAnalytics |
| **Job Seeker** | Home (Discovery), JobSearch, ApplicationTracking, ResumeMgmt |

### 4.2 Shared Screen Adaptability
- **ProfilePage**: Will dynamically load different components/sections based on the user's role (e.g., Job Seeker sees CV section, Employer sees Company info).
- **CommunityPage**: Administrators will see "Moderate" or "Delete" buttons on posts.

---

## 5. Authorization & Security

1. **Route Protection**: The `NavGraph` will validate the current user's role before allowing navigation to role-specific destinations.
2. **UI Visibility**: Elements like "Post Job" FAB will be strictly role-bound.
3. **Session Interceptor**: A clean-architecture approach using a `GetUserRoleUseCase` to ensure all presentation logic is role-aware.

---

## 6. Implementation Plan

### Phase 1: Package Restructuring
1. Create packages: `presentation/admin`, `presentation/employer`, `presentation/jobseeker`, `presentation/shared`.
2. Move existing generic screens to their respective new locations.

### Phase 2: Navigation & Routes
1. Update `Screen.kt` to include all new role-specific routes.
2. Refactor `BottomNavigationBar.kt` to accept role as a parameter and display appropriate items.
3. Split `NavGraph.kt` into `AdminNavGraph`, `EmployerNavGraph`, and `JobSeekerNavGraph`.

### Phase 3: Role-Specific Dashboards
1. Implement `AdminDashboard` with stats (Users, Active Jobs, Reports).
2. Implement `EmployerDashboard` (Posted Jobs, Recent Applicants).
3. Update `JobSeekerHome` (Discovery, Saved Jobs).

### Phase 4: Authorization Logic
1. Update `SplashViewModel` to handle role-based redirection.
2. Implement `RoleGate` composables to wrap protected content.

---

## 7. User Review Required

- Are there any specific analytics or reports the Administrator should see immediately on the Dashboard?
- Should the `CommunityPage` be identical for all roles, or should Employers have a "Verified Employer" badge?
- Do we need a "Switch Role" feature (e.g., for testing) or are roles strictly permanent per account?
