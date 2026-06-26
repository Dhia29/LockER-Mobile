<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\ProfileController;
use App\Http\Controllers\JobController;
use App\Http\Controllers\ApplicationController;
use App\Http\Controllers\CommunityController;
use App\Http\Controllers\AdminController;
use App\Http\Controllers\SupportTicketController;

// Public Routes
Route::post('/register', [AuthController::class, 'register']);
Route::post('/login', [AuthController::class, 'login']);
Route::post('/forgot-password', [AuthController::class, 'forgotPassword']);
Route::post('/reset-password', [AuthController::class, 'resetPassword']);

Route::post('/verify-email', [AuthController::class, 'verifyEmail']);
Route::post('/resend-verification', [AuthController::class, 'resendVerification']);

// Protected Routes
Route::middleware('auth:sanctum')->group(function () {
    // Auth
    Route::get('/user', [AuthController::class, 'user']);
    Route::post('/logout', [AuthController::class, 'logout']);

    // Profile & CV
    Route::get('/profile', [ProfileController::class, 'show']);
    Route::put('/profile', [ProfileController::class, 'update']);
    Route::post('/profile/avatar', [ProfileController::class, 'uploadAvatar']);
    Route::post('/profile/upload-cv', [ProfileController::class, 'uploadCv']);
    Route::delete('/profile/delete-cv', [ProfileController::class, 'deleteCv']);

    // Jobs
    Route::get('/jobs', [JobController::class, 'index']);
    Route::get('/jobs/{id}', [JobController::class, 'show']);
    Route::post('/jobs/{id}/apply', [JobController::class, 'apply']);
    Route::post('/jobs/{id}/save', [JobController::class, 'saveJob']);

    // Applications
    Route::get('/applications', [ApplicationController::class, 'index']);

    // Community
    Route::get('/community/posts', [CommunityController::class, 'index']);
    Route::post('/community/posts', [CommunityController::class, 'store']);
    Route::put('/community/posts/{id}', [CommunityController::class, 'update']);
    Route::delete('/community/posts/{id}', [CommunityController::class, 'destroy']);
    Route::post('/community/posts/{id}/like', [CommunityController::class, 'like']);
    Route::get('/community/posts/{id}/comments', [CommunityController::class, 'comments']);
    Route::post('/community/posts/{id}/comments', [CommunityController::class, 'storeComment']);
    Route::delete('/community/comments/{id}', [CommunityController::class, 'destroyComment']);

    // Reporting
    Route::post('/post-reports', [CommunityController::class, 'reportPost']);
    Route::post('/community-reports', [CommunityController::class, 'reportComment']);

    // Support Tickets
    Route::get('/support-tickets', [SupportTicketController::class, 'index']);
    Route::post('/support-tickets', [SupportTicketController::class, 'store']);
    Route::get('/support-tickets/{id}', [SupportTicketController::class, 'show']);

    // Notifications
    Route::get('/notifications', [ProfileController::class, 'notifications']);
    Route::post('/notifications/{id}/read', [ProfileController::class, 'readNotification']);

    // Admin (Should probably have an admin middleware, but grouping here for now)
    Route::get('/admin/stats', [AdminController::class, 'stats']);
    Route::get('/admin/users', [AdminController::class, 'users']);
    Route::get('/admin/reports', [AdminController::class, 'reports']);
    Route::post('/admin/reports/{id}/resolve', [AdminController::class, 'resolveReport']);
});
