package com.example.calbudget.data.repository

import com.example.calbudget.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    // Cek user yang sedang login — null berarti belum login
    // Flow agar UI reaktif saat auth state berubah
    val currentUser: Flow<User?>

    // Ambil user sekali (bukan flow) — untuk cek awal
    fun getCurrentUserOnce(): User?

    suspend fun loginWithEmail(email: String, password: String): Result<User>

    suspend fun registerWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User>

    // Google Sign-In menggunakan idToken dari Google
    suspend fun loginWithGoogle(idToken: String): Result<User>

    suspend fun logout(): Result<Unit>

    suspend fun resetPassword(email: String): Result<Unit>
}