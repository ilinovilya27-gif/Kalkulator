package com.example.kalkulator

import android.content.Context
import java.io.File

open class HistoryRepository(context: Context?) {
    private val historyFile = File(context?.filesDir, "history.txt")
    private val maxHistorySize = 20

    open fun addRecord(expression: String, result: String) {
        val newRecord = "$expression = $result"
        val currentHistory = getHistory().toMutableList()

        currentHistory.add(0, newRecord)
        while (currentHistory.size > maxHistorySize) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }

        try {
            historyFile.writeText(currentHistory.joinToString("\n"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun getHistory(): List<String> {
        if (!historyFile.exists()) return emptyList()
        return try {
            val content = historyFile.readText()
            if (content.isBlank()) emptyList() else content.split("\n")
        } catch (e: Exception) {
            emptyList()
        }
    }

    open fun clearHistory() {
        try {
            if (historyFile.exists()) historyFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}