package com.example.kalkulator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    private val fakeHistory = object : HistoryRepository(null as android.content.Context?) {
        override fun addRecord(expression: String, result: String) {}
    }

    private val engine = CalculatorEngine(fakeHistory)

    @Test
    fun testAddition() {
        assertEquals(4.0, engine.calculate("2+2"), 0.001)
    }

    @Test
    fun testMultiplicationPriority() {
        assertEquals(6.0, engine.calculate("2+2*2"), 0.001)
    }

    @Test
    fun testDivisionByZero() {
        try {
            engine.calculate("5/0")
        } catch (e: Exception) {
            assertEquals("Деление на ноль", e.message)
        }
    }

    @Test
    fun testSubtraction() {
        assertEquals(2.0, engine.calculate("5-3"), 0.001)
    }
}