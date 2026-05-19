package com.example.calbudget

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp = wajib untuk Hilt DI
// Hilt akan generate code DI di sini secara otomatis
@HiltAndroidApp
class CalBudgetApplication : Application()