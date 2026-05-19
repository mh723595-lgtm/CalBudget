package com.example.calbudget.domain.model

// Pure Kotlin data class — TIDAK ada import dari Room/Android
// Ini yang dipakai oleh ViewModel dan Compose UI
data class Transaction(
    val id: String,
    val title: String,
    val amount: Double,
    val type: TransactionType,     // INCOME atau EXPENSE
    val category: String,
    val note: String,
    val date: Long,                // Simpan sebagai timestamp (Long) — mudah difilter
    val createdAt: Long = System.currentTimeMillis()
)

// Enum untuk tipe transaksi
// Kenapa enum? Type-safe, tidak bisa salah ketik "incone" dll
enum class TransactionType {
    INCOME,    // Pemasukan
    EXPENSE    // Pengeluaran
}