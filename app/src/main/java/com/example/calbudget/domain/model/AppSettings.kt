package com.example.calbudget.domain.model

// Model pure Kotlin — tidak ada dependency Android/Room
// Ini yang di-observe ViewModel dan Theme
data class AppSettings(
    val isDarkMode: Boolean = false,
    val currency: Currency = Currency.IDR,
    val username: String = "",
    val isNotificationEnabled: Boolean = true,
    val language: String = "id"
)

// Enum currency — mudah tambah mata uang baru nanti
enum class Currency(
    val code: String,
    val symbol: String,
    val displayName: String
) {
    IDR("IDR", "Rp", "Rupiah Indonesia"),
    USD("USD", "$", "US Dollar"),
    SGD("SGD", "S$", "Singapore Dollar"),
    MYR("MYR", "RM", "Malaysian Ringgit"),
    EUR("EUR", "€", "Euro")
}