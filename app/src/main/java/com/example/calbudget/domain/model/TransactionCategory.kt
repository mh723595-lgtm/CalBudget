package com.example.calbudget.domain.model

import androidx.compose.ui.graphics.Color

// Model pure Kotlin untuk kategori
// Tidak disimpan di DB — hardcoded di CategoryManager
data class TransactionCategory(
    val id: String,           // Unique key, dipakai untuk filter
    val label: String,        // Nama tampilan: "Makan & Minum"
    val emoji: String,        // Icon emoji: "🍔"
    val color: Color,         // Warna background chip
    val type: CategoryType    // INCOME, EXPENSE, atau BOTH
)

enum class CategoryType {
    INCOME,   // Hanya untuk pemasukan
    EXPENSE,  // Hanya untuk pengeluaran
    BOTH      // Bisa keduanya (misal: "Lainnya")
}