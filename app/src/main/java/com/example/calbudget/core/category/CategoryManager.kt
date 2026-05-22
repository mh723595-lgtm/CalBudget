package com.example.calbudget.core.category

import androidx.compose.ui.graphics.Color
import com.example.calbudget.domain.model.CategoryType
import com.example.calbudget.domain.model.TransactionCategory
import com.example.calbudget.domain.model.TransactionType

object CategoryManager {

    // =========================================
    // SEMUA KATEGORI — definisi lengkap
    // =========================================
    val allCategories: List<TransactionCategory> = listOf(

        // ===== PEMASUKAN =====
        TransactionCategory(
            id = "salary",
            label = "Gaji",
            emoji = "💼",
            color = Color(0xFF4CAF50),
            type = CategoryType.INCOME
        ),
        TransactionCategory(
            id = "business",
            label = "Bisnis",
            emoji = "🏪",
            color = Color(0xFF2196F3),
            type = CategoryType.INCOME
        ),
        TransactionCategory(
            id = "investment",
            label = "Investasi",
            emoji = "📈",
            color = Color(0xFF9C27B0),
            type = CategoryType.INCOME
        ),
        TransactionCategory(
            id = "freelance",
            label = "Freelance",
            emoji = "💻",
            color = Color(0xFF00BCD4),
            type = CategoryType.INCOME
        ),
        TransactionCategory(
            id = "gift_income",
            label = "Hadiah",
            emoji = "🎁",
            color = Color(0xFFE91E63),
            type = CategoryType.INCOME
        ),
        TransactionCategory(
            id = "bonus",
            label = "Bonus",
            emoji = "🎉",
            color = Color(0xFFFF9800),
            type = CategoryType.INCOME
        ),
        TransactionCategory(
            id = "rental",
            label = "Sewa",
            emoji = "🏠",
            color = Color(0xFF795548),
            type = CategoryType.INCOME
        ),

        // ===== PENGELUARAN =====
        TransactionCategory(
            id = "food",
            label = "Makan & Minum",
            emoji = "🍔",
            color = Color(0xFFFF5722),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "transport",
            label = "Transportasi",
            emoji = "🚗",
            color = Color(0xFF607D8B),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "shopping",
            label = "Belanja",
            emoji = "🛍️",
            color = Color(0xFFE91E63),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "housing",
            label = "Rumah",
            emoji = "🏡",
            color = Color(0xFF795548),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "health",
            label = "Kesehatan",
            emoji = "💊",
            color = Color(0xFFF44336),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "education",
            label = "Pendidikan",
            emoji = "🎓",
            color = Color(0xFF3F51B5),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "entertainment",
            label = "Hiburan",
            emoji = "🎮",
            color = Color(0xFF9C27B0),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "bills",
            label = "Tagihan",
            emoji = "📄",
            color = Color(0xFF607D8B),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "beauty",
            label = "Kecantikan",
            emoji = "💄",
            color = Color(0xFFE91E63),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "sport",
            label = "Olahraga",
            emoji = "⚽",
            color = Color(0xFF4CAF50),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "vacation",
            label = "Liburan",
            emoji = "✈️",
            color = Color(0xFF00BCD4),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "social",
            label = "Sosial",
            emoji = "🤝",
            color = Color(0xFFFF9800),
            type = CategoryType.EXPENSE
        ),
        TransactionCategory(
            id = "subscription",
            label = "Langganan",
            emoji = "📱",
            color = Color(0xFF2196F3),
            type = CategoryType.EXPENSE
        ),

        // ===== KEDUANYA =====
        TransactionCategory(
            id = "other",
            label = "Lainnya",
            emoji = "💳",
            color = Color(0xFF9E9E9E),
            type = CategoryType.BOTH
        )
    )

    // =========================================
    // HELPER FUNCTIONS
    // =========================================

    // Ambil kategori sesuai tipe transaksi
    fun getCategoriesForType(type: TransactionType): List<TransactionCategory> {
        return allCategories.filter { category ->
            when (type) {
                TransactionType.INCOME ->
                    category.type == CategoryType.INCOME || category.type == CategoryType.BOTH
                TransactionType.EXPENSE ->
                    category.type == CategoryType.EXPENSE || category.type == CategoryType.BOTH
            }
        }
    }

    // Cari kategori by ID — return default kalau tidak ketemu
    fun getCategoryById(id: String): TransactionCategory {
        return allCategories.find { it.id == id } ?: allCategories.last()
    }

    // Default kategori untuk tiap tipe
    fun getDefaultCategory(type: TransactionType): TransactionCategory {
        return when (type) {
            TransactionType.INCOME -> allCategories.first { it.id == "salary" }
            TransactionType.EXPENSE -> allCategories.first { it.id == "food" }
        }
    }
}