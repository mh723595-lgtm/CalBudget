package com.example.calbudget.data.lokal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType

// @Entity = beritahu Room bahwa ini adalah tabel database
// tableName = nama tabel di SQLite
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey  // Primary key — unik untuk setiap row
    val id: String,
    val title: String,
    val amount: Double,
    val type: String,      // Simpan sebagai String ("INCOME"/"EXPENSE")
    val category: String,
    val note: String,
    val date: Long,
    val createdAt: Long
) {
    // Extension function: Entity → Domain Model
    // Dipanggil di Repository sebelum data diserahkan ke ViewModel
    fun toDomain(): Transaction = Transaction(
        id = id,
        title = title,
        amount = amount,
        type = TransactionType.valueOf(type),  // String → Enum
        category = category,
        note = note,
        date = date,
        createdAt = createdAt
    )
}

// Extension function: Domain Model → Entity
// Dipanggil di Repository sebelum data disimpan ke Room
fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    title = title,
    amount = amount,
    type = type.name,   // Enum → String
    category = category,
    note = note,
    date = date,
    createdAt = createdAt
)