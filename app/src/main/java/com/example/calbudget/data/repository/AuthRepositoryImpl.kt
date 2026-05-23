package com.example.calbudget.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.example.calbudget.domain.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    // callbackFlow = convert Firebase listener ke Kotlin Flow
    // Setiap kali auth state berubah (login/logout), UI otomatis update
    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }
        firebaseAuth.addAuthStateListener(listener)
        // Cleanup saat Flow selesai di-collect
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override fun getCurrentUserOnce(): User? {
        return firebaseAuth.currentUser?.toDomain()
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            val user = result.user?.toDomain()
                ?: return Result.failure(Exception("Login gagal"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(parseFirebaseError(e))
        }
    }

    override suspend fun registerWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            // Update display name setelah register
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            result.user?.updateProfile(profileUpdate)?.await()

            val user = result.user?.toDomain()?.copy(displayName = displayName)
                ?: return Result.failure(Exception("Register gagal"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(parseFirebaseError(e))
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user?.toDomain()
                ?: return Result.failure(Exception("Google login gagal"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(parseFirebaseError(e))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(parseFirebaseError(e))
        }
    }

    // =========================================
    // HELPERS
    // =========================================

    // Convert FirebaseUser → domain User
    private fun FirebaseUser.toDomain() = User(
        uid = uid,
        email = email ?: "",
        displayName = displayName ?: email?.substringBefore("@") ?: "User",
        photoUrl = photoUrl?.toString()
    )

    // Parse error Firebase → pesan yang user-friendly
    // Firebase error code pakai format "ERROR_XXX"
    private fun parseFirebaseError(e: Exception): Exception {
        val message = when {
            e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ||
                    e.message?.contains("wrong-password") == true ->
                "Email atau password salah"

            e.message?.contains("EMAIL_EXISTS") == true ||
                    e.message?.contains("email-already-in-use") == true ->
                "Email sudah terdaftar"

            e.message?.contains("INVALID_EMAIL") == true ||
                    e.message?.contains("invalid-email") == true ->
                "Format email tidak valid"

            e.message?.contains("WEAK_PASSWORD") == true ||
                    e.message?.contains("weak-password") == true ->
                "Password minimal 6 karakter"

            e.message?.contains("USER_NOT_FOUND") == true ||
                    e.message?.contains("user-not-found") == true ->
                "Akun tidak ditemukan"

            e.message?.contains("NETWORK_ERROR") == true ||
                    e.message?.contains("network") == true ->
                "Tidak ada koneksi internet"

            e.message?.contains("TOO_MANY_REQUESTS") == true ->
                "Terlalu banyak percobaan. Coba lagi nanti."

            else -> "Terjadi kesalahan: ${e.message}"
        }
        return Exception(message)
    }
}