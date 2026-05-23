package com.example.calbudget.domain.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null
)