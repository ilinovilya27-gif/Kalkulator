package com.example.kalkulator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private lateinit var engine: CalculatorEngine
    private var currentExpression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val currentTheme = prefs.getString("theme", "dark")

        when (currentTheme) {
            "light" -> setTheme(R.style.Theme_Kalkulator_Light)
            else -> setTheme(R.style.Theme_Kalkulator)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        engine = CalculatorEngine(HistoryRepository(this))

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnDot, R.id.btnOpenBracket, R.id.btnCloseBracket
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { view ->
                val button = view as Button
                currentExpression += button.text
                tvExpression.text = currentExpression
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            try {
                val result = engine.calculate(currentExpression)
                val formattedResult = engine.formatResult(result)

                tvResult.text = formattedResult
                currentExpression = formattedResult
                tvExpression.text = currentExpression

            } catch (e: Exception) {
                tvResult.text = e.message
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            currentExpression = ""
            tvExpression.text = ""
            tvResult.text = "0"
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            if (currentExpression.isNotEmpty()) {
                currentExpression = currentExpression.dropLast(1)
                tvExpression.text = currentExpression
            }
        }

        findViewById<Button>(R.id.btnToggleTheme).setOnClickListener {
            val newTheme = if (currentTheme == "dark") "light" else "dark"
            prefs.edit().putString("theme", newTheme).apply()
            recreate()
        }

        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            showHistoryDialog()
        }
    }

    private fun showHistoryDialog() {
        val historyRepo = HistoryRepository(this)
        val historyList = historyRepo.getHistory()

        if (historyList.isEmpty()) {
            tvResult.text = "История пуста"
        } else {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("История вычислений")
            val historyText = historyList.joinToString("\n")
            builder.setMessage(historyText)

            builder.setPositiveButton("Очистить") { _, _ ->
                historyRepo.clearHistory()
                tvResult.text = "История очищена"
            }
            builder.setNegativeButton("Закрыть", null)
            builder.show()
        }
    }
}