package com.example.lockermobile.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseManager {

    private const val SUPABASE_URL = "https://skdelezmotmbjydxznsz.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNrZGVsZXptb3RtYmp5ZHh6bnN6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODEyNjkyMTksImV4cCI6MjA5Njg0NTIxOX0.uXNh1VjCw0DaaMrFbapFLLhvpM29taE8H-ZRzKA05Jg"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        install(Realtime)
    }
}
