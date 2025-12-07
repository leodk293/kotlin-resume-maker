package com.resumemaker.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.resumemaker.app.data.models.CV
import com.resumemaker.app.data.repository.CVRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CVViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CVRepository(application.applicationContext)
    
    private val _allCVs = MutableStateFlow<List<CV>>(emptyList())
    val allCVs: StateFlow<List<CV>> = _allCVs.asStateFlow()

    init {
        loadCVs()
    }

    fun loadCVs() {
        viewModelScope.launch {
            _allCVs.value = repository.getAllCVs()
        }
    }

    suspend fun getCVById(id: String): CV? {
        return repository.getCVById(id)
    }

    fun addCV(cv: CV) {
        viewModelScope.launch {
            repository.saveCV(cv)
            loadCVs()
        }
    }

    fun updateCV(cv: CV) {
        viewModelScope.launch {
            repository.updateCV(cv)
            loadCVs()
        }
    }

    fun deleteCV(cv: CV) {
        viewModelScope.launch {
            repository.deleteCV(cv.id)
            loadCVs()
        }
    }
}

