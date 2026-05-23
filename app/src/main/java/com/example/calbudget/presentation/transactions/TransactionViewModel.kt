package com.example.calbudget.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.data.repository.TransactionRepository
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel  // Wajib untuk ViewModel dengan Hilt
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    // =========================================
    // STATE — private MutableStateFlow (hanya ViewModel yang bisa ubah)
    //         public StateFlow (UI hanya bisa baca)
    // =========================================

    private val _uiState = MutableStateFlow<TransactionUiState>(TransactionUiState.Loading)
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    private val _addTransactionState = MutableStateFlow(AddTransactionUiState())
    val addTransactionState: StateFlow<AddTransactionUiState> = _addTransactionState.asStateFlow()

    // ✨ BARU: Filter state
    private val _filterState = MutableStateFlow(TransactionFilterState())
    val filterState: StateFlow<TransactionFilterState> = _filterState.asStateFlow()

    // =========================================
    // INIT — langsung load data saat ViewModel dibuat
    // =========================================

    init {
        loadTransactions()
    }

    // =========================================
    // LOAD DATA
    // =========================================

    private fun loadTransactions() {
        viewModelScope.launch {
            // Combine 3 Flow sekaligus — update UI kalau salah satu berubah
            combine(
                repository.getAllTransactions(),
                repository.getTotalIncome(),
                repository.getTotalExpense(),
                _filterState
            ) { transactions, income, expense, filter ->

                // Terapkan filter
                val filtered = transactions
                    .filter { tx ->
                        // Filter by type
                        filter.selectedType == null || tx.type == filter.selectedType
                    }
                    .filter { tx ->
                        // Filter by category ID
                        filter.selectedCategoryId == null ||
                                tx.category == filter.selectedCategoryId
                    }

                Triple(transactions, income, expense)
            }
                .onStart { _uiState.value = TransactionUiState.Loading }
                .catch { e ->
                    _uiState.value = TransactionUiState.Error(
                        e.message ?: "Terjadi kesalahan"
                    )
                }
                .collect { (transactions, income, expense) ->
                    _uiState.value = if (transactions.isEmpty()) {
                        TransactionUiState.Empty
                    } else {
                        TransactionUiState.Success(
                            transactions = transactions,
                            totalIncome = income,
                            totalExpense = expense
                        )
                    }
                }
        }
    }

    // =========================================
    // FILTER EVENTS ✨ BARU
    // =========================================

    fun onTypeFilterChange(type: TransactionType?) {
        _filterState.update {
            it.copy(
                selectedType = type,
                selectedCategoryId = null   // reset kategori saat tipe berubah
            )
        }
    }

    fun onCategoryFilterChange(categoryId: String?) {
        _filterState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun resetFilter() {
        _filterState.value = TransactionFilterState()
    }

    // =========================================
    // ADD TRANSACTION FORM EVENTS
    // =========================================

    fun onTitleChange(value: String) {
        _addTransactionState.update { it.copy(title = value, titleError = null) }
    }

    fun onAmountChange(value: String) {
        // Hanya izinkan angka dan titik desimal
        val filtered = value.filter { it.isDigit() || it == '.' }
        _addTransactionState.update { it.copy(amount = filtered, amountError = null) }
    }

    fun onTypeChange(type: TransactionType) {
        // Reset kategori ke default type baru
        val defaultCategory = CategoryManager.getDefaultCategory(type)
        _addTransactionState.update {
            it.copy(
                selectedType = type,
                selectedCategoryId = defaultCategory.id
            )
        }
    }

    fun onCategoryChange(category: String) {
        _addTransactionState.update { it.copy(selectedCategoryId = category) }
    }

    fun onNoteChange(value: String) {
        _addTransactionState.update { it.copy(note = value) }
    }

    fun onDateChange(date: Long) {
        _addTransactionState.update { it.copy(date = date) }
    }

    // =========================================
    // SAVE TRANSACTION — dengan validasi
    // =========================================

    fun saveTransaction() {
        val state = _addTransactionState.value

        // Validasi input
        val titleError = if (state.title.isBlank()) "Judul tidak boleh kosong" else null
        val amountError = when {
            state.amount.isBlank() -> "Nominal tidak boleh kosong"
            state.amount.toDoubleOrNull() == null -> "Nominal tidak valid"
            state.amount.toDouble() <= 0 -> "Nominal harus lebih dari 0"
            else -> null
        }

        // Kalau ada error, update state dan STOP
        if (titleError != null || amountError != null) {
            _addTransactionState.update {
                it.copy(titleError = titleError, amountError = amountError)
            }
            return
        }

        // Simpan ke database
        viewModelScope.launch {
            _addTransactionState.update { it.copy(isLoading = true) }

            val transaction = Transaction(
                id = UUID.randomUUID().toString(),
                title = state.title.trim(),
                amount = state.amount.toDouble(),
                type = state.selectedType,
                category = state.selectedCategoryId,
                note = state.note.trim(),
                date = state.date,
                createdAt = System.currentTimeMillis()
            )

            val result = repository.addTransaction(transaction)

            result
                .onSuccess {
                    _addTransactionState.value = AddTransactionUiState(isSuccess = true)
                }
                .onFailure { e ->
                    _addTransactionState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Gagal menyimpan transaksi"
                        )
                    }
                }
        }
    }

    // =========================================
    // DELETE TRANSACTION
    // =========================================

    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            repository.deleteTransaction(transactionId)
            // Flow di loadTransactions() otomatis update UI
            // tidak perlu trigger manual!
        }
    }

    // =========================================
    // UPDATE TRANSACTION
    // =========================================

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    // Reset form setelah navigasi
    fun resetAddTransactionState() {
        _addTransactionState.value = AddTransactionUiState()
    }

    // Error message sudah ditampilkan — clear
    fun clearError() {
        _addTransactionState.update { it.copy(errorMessage = null) }
    }
}