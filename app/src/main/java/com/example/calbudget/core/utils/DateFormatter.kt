package com.example.calbudget.core.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    // Format: "Senin, 21 Mei 2026"
    fun formatFull(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    // Format: "21 Mei 2026"
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    // Format: "21 Mei"
    fun formatShort(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    // Format: "14:30"
    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        return sdf.format(Date(timestamp))
    }

    // Greeting dinamis berdasarkan jam
    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..10 -> "Selamat pagi"
            in 11..14 -> "Selamat siang"
            in 15..17 -> "Selamat sore"
            else -> "Selamat malam"
        }
    }

    // Relative time: "Barusan", "2 jam lalu", "Kemarin", dll
    fun formatRelative(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val minutes = diff / 60_000
        val hours = diff / 3_600_000
        val days = diff / 86_400_000

        return when {
            minutes < 1 -> "Barusan"
            minutes < 60 -> "$minutes menit lalu"
            hours < 24 -> "$hours jam lalu"
            days == 1L -> "Kemarin"
            days < 7 -> "$days hari lalu"
            else -> formatShort(timestamp)
        }
    }
}