<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class ProfileController extends Controller
{
    public function show(Request $request)
    {
        // Get user with profile if you set up relations, otherwise just return user
        return response()->json($request->user());
    }

    public function update(Request $request)
    {
        $user = $request->user();
        $user->update($request->all());
        
        // Update job_seeker_profiles if needed
        $profileData = $request->only(['headline', 'lokasi', 'posisi_saat_ini', 'pendidikan', 'pengalaman', 'skill']);
        if (!empty($profileData)) {
            \DB::table('job_seeker_profiles')
                ->where('user_id', $user->id)
                ->update($profileData);
        }
        
        return response()->json(['message' => 'Profile updated']);
    }
