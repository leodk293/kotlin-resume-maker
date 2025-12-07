package com.resumemaker.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.resumemaker.app.data.models.CV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CVRepository(private val context: Context? = null) {
    private val gson = Gson()
    private val prefsKey = "saved_cvs"
    
    // For demo purposes, using in-memory storage
    // In production, you'd use Room database
    private var cvs: MutableList<CV> = mutableListOf()
    
    private fun getSharedPreferences(): SharedPreferences? {
        return context?.getSharedPreferences("CV_APP_PREFS", Context.MODE_PRIVATE)
    }

    suspend fun getAllCVs(): List<CV> = withContext(Dispatchers.IO) {
        if (context != null) {
            loadFromPrefs()
        } else {
            cvs.toList()
        }
    }

    suspend fun getCVById(id: String): CV? = withContext(Dispatchers.IO) {
        val allCVs = if (context != null) {
            loadFromPrefs()
        } else {
            cvs.toList()
        }
        allCVs.find { it.id == id }
    }

    suspend fun saveCV(cv: CV) = withContext(Dispatchers.IO) {
        if (context != null) {
            val allCVs = loadFromPrefs().toMutableList()
            allCVs.add(cv)
            saveToPrefs(allCVs)
        } else {
            cvs.add(cv)
        }
    }

    suspend fun updateCV(cv: CV) = withContext(Dispatchers.IO) {
        if (context != null) {
            val allCVs = loadFromPrefs().toMutableList()
            val index = allCVs.indexOfFirst { it.id == cv.id }
            if (index != -1) {
                allCVs[index] = cv.copy(updatedAt = System.currentTimeMillis())
                saveToPrefs(allCVs)
            }
        } else {
            val index = cvs.indexOfFirst { it.id == cv.id }
            if (index != -1) {
                cvs[index] = cv.copy(updatedAt = System.currentTimeMillis())
            }
        }
    }

    suspend fun deleteCV(id: String) = withContext(Dispatchers.IO) {
        if (context != null) {
            val allCVs = loadFromPrefs().toMutableList()
            allCVs.removeAll { it.id == id }
            saveToPrefs(allCVs)
        } else {
            cvs.removeAll { it.id == id }
        }
    }

    private fun loadFromPrefs(): List<CV> {
        val prefs = getSharedPreferences() ?: return emptyList()
        val json = prefs.getString(prefsKey, null) ?: return emptyList()
        val type = object : TypeToken<List<CV>>() {}.type
        return try {
            gson.fromJson<List<CV>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveToPrefs(cvs: List<CV>) {
        val prefs = getSharedPreferences() ?: return
        val json = gson.toJson(cvs)
        prefs.edit().putString(prefsKey, json).apply()
    }
}

