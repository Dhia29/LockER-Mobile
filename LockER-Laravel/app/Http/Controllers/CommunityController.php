<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class CommunityController extends Controller
{
    public function index()
    {
        // For now, return an empty array or dummy data if the database isn't ready
        $posts = \DB::table('community_posts')->get();
        return response()->json($posts);
    }
