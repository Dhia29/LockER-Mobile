<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class AuthController extends Controller
{
    public function register(Request $request)
    {
        try {
            $request->validate([
                'name' => 'required|string|max:255',
                'email' => 'required|string|email|max:255|unique:users',
                'password' => 'required|string|min:8',
                'role' => 'nullable|string'
            ]);

            $user = User::create([
                'email' => $request->email,
                'password_hash' => Hash::make($request->password),
                'role' => $request->role ?? 'job_seeker' // Default role
            ]);

            // Create associated profile
            if (($request->role ?? 'job_seeker') === 'job_seeker') {
                DB::table('job_seeker_profiles')->insert([
                    'id' => Str::uuid()->toString(),
                    'user_id' => $user->id,
                    'nama_lengkap' => $request->name,
                    'headline' => '',
                    'lokasi' => '',
                    'posisi_saat_ini' => '',
                    'avatar_url' => '',
                    'wa_number' => '',
                    'pendidikan' => '[]',
                    'pengalaman' => '[]',
                    'skill' => '[]',
                    'updated_at' => now(),
                ]);
            }

            $token = $user->createToken('auth_token')->plainTextToken;

            // Re-attach name to response to match Android's UserAuthDto
            $user->name = $request->name;

            return response()->json([
                'user' => $user,
                'token' => $token,
            ], 201);
        } catch (\Illuminate\Validation\ValidationException $e) {
            return response()->json([
                'message' => $e->validator->errors()->first()
            ], 400);
        } catch (\Exception $e) {
            \Log::error('Registration Error: ' . $e->getMessage() . ' Trace: ' . $e->getTraceAsString());
            return response()->json(['message' => 'Registration failed: ' . $e->getMessage()], 500);
        }
    }

    public function login(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'password' => 'required',
        ]);

        $user = User::where('email', $request->email)->first();

        if (!$user) {
            return response()->json(['message' => 'Invalid login credentials'], 401);
        }

        // Fallback for older plaintext passwords in the database
        $isValidPassword = false;
        if (str_starts_with($user->password_hash, '$2y$')) {
            $isValidPassword = Hash::check($request->password, $user->password_hash);
        } else {
            $isValidPassword = ($request->password === $user->password_hash);
            
            // Upgrade plaintext password to Bcrypt hash for security
            if ($isValidPassword) {
                $user->password_hash = Hash::make($request->password);
                $user->save();
            }
        }

        if (!$isValidPassword) {
            return response()->json([
                'message' => 'Invalid login credentials'
            ], 401);
        }

        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'user' => $user,
            'token' => $token,
        ], 200);
    }

    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();

        return response()->json([
            'message' => 'Logged out successfully'
        ], 200);
    }

    public function verifyEmail(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'code' => 'required|string'
        ]);
        
        $user = User::where('email', $request->email)->first();
        if (!$user) {
            return response()->json(['message' => 'User not found'], 404);
        }
        
        // If code is somehow null in db but they pass something
        if (!$user->verification_code || $user->verification_code !== $request->code) {
            // For testing purposes, if they pass "123456", let them pass
            if ($request->code !== '123456') {
                return response()->json(['message' => 'Invalid verification code'], 400);
            }
        }
        
        if ($user->verification_expires_at && now()->isAfter($user->verification_expires_at)) {
            if ($request->code !== '123456') {
                return response()->json(['message' => 'Verification code expired'], 400);
            }
        }
        
        $user->email_verified_at = now();
        $user->verification_code = null;
        $user->verification_expires_at = null;
        $user->status = 'active';
        $user->save();
        
        return response()->json(['message' => 'Email verified successfully'], 200);
    }

    public function resendVerification(Request $request)
    {
        $request->validate(['email' => 'required|email']);
        $user = User::where('email', $request->email)->first();
        
        if (!$user) {
            return response()->json(['message' => 'User not found'], 404);
        }
        
        $code = sprintf("%06d", mt_rand(1, 999999));
        $user->verification_code = $code;
        $user->verification_expires_at = now()->addMinutes(10);
        $user->save();
        
        // In a real app, send email here. 
        // For testing, we just simulate success.
        return response()->json(['message' => 'Code sent'], 200);
    }

    public function user(Request $request)
    {
        return response()->json($request->user());
    }
}
