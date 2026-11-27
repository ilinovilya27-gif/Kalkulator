package com.example.kalkulator

class CalculatorEngine(private val historyRepository: HistoryRepository) {

    fun calculate(expression: String): Double {
        val cleanExpression = expression.replace(" ", "")
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace(Regex("(\\d)\\("), "$1*(")
            .replace(Regex("\\)(\\d)"), ")*$1")

        if (cleanExpression.isEmpty()) throw IllegalArgumentException("Пустое выражение")

        try {
            val result = evaluate(cleanExpression)
            val formattedResult = formatResult(result)
            historyRepository.addRecord(cleanExpression, formattedResult)
            return result
        } catch (e: ArithmeticException) {
            throw e
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка в выражении")
        }
    }

    fun formatResult(result: Double): String {
        return if (result % 1.0 == 0.0) {
            result.toLong().toString()
        } else {
            String.format("%.4f", result).trimEnd('0').trimEnd('.')
        }
    }

    private fun evaluate(str: String): Double {
        var i = str.length - 1
        var depth = 0

        while (i >= 0) {
            val char = str[i]
            if (char == ')') depth++
            if (char == '(') depth--

            if (depth == 0 && (char == '+' || char == '-')) {
                if (i == 0) {
                    i--
                    continue
                }

                val prevChar = str[i - 1]
                if (prevChar == '+' || prevChar == '-' || prevChar == '*' || prevChar == '/') {
                    i--
                    continue
                }

                val left = evaluate(str.substring(0, i))
                val right = evaluate(str.substring(i + 1))
                return if (char == '+') left + right else left - right
            }
            i--
        }

        i = str.length - 1
        depth = 0
        while (i >= 0) {
            val char = str[i]
            if (char == ')') depth++
            if (char == '(') depth--

            if (depth == 0 && (char == '*' || char == '/')) {
                val left = evaluate(str.substring(0, i))
                val right = evaluate(str.substring(i + 1))
                if (char == '/' && right == 0.0) throw ArithmeticException("Деление на ноль")
                return if (char == '*') left * right else left / right
            }
            i--
        }

        if (str.startsWith("(") && str.endsWith(")")) {
            return evaluate(str.substring(1, str.length - 1))
        }

        return str.toDouble()
    }
}