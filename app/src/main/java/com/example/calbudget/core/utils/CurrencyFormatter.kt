package com.example.calbudget.core.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    private val rupiahFormat = NumberFormat.getCurrencyInstance(
        Locale("id", "ID")  // Indonesia locale
    )

    // Format angka ke Rupiah: 150000 → "Rp150.000"
    fun formatRupiah(amount: Double): String {
        return rupiahFormat.format(amount)
            .replace("Rp", "Rp")  // Pastikan format konsisten
            .replace(",00", "")   // Hapus desimal yang tidak perlu
    }

    // Format singkat: 1500000 → "Rp1,5jt"
    fun formatRupiahShort(amount: Double): String {
        return when {
            amount >= 1_000_000_000 -> "Rp${String.format("%.1f", amount / 1_000_000_000)}M"
            amount >= 1_000_000 -> "Rp${String.format("%.1f", amount / 1_000_000)}jt"
            amount >= 1_000 -> "Rp${String.format("%.0f", amount / 1_000)}rb"
            else -> formatRupiah(amount)
        }
    }

    // Parse String input ke Double: "150.000" → 150000.0
    fun parseAmount(input: String): Double {
        return input.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    }
}