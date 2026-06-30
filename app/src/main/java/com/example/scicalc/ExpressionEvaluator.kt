package com.example.scicalc

import kotlin.math.*

/**
 * Recursive-descent expression evaluator.
 * Supports: + - * / ^ % parentheses, unary minus,
 * functions: sin cos tan asin acos atan sinh cosh tanh
 *            log ln sqrt abs exp
 * Constants: pi, e
 * Degrees mode toggle for trig functions.
 */
class ExpressionEvaluator(private val degreesMode: Boolean = true) {

    private lateinit var tokens: List<String>
    private var pos = 0

    fun evaluate(expression: String): Double {
        tokens = tokenize(expression)
        pos = 0
        val result = parseExpression()
        if (pos != tokens.size) throw IllegalArgumentException("Unexpected token: ${tokens.getOrNull(pos)}")
        return result
    }

    private fun tokenize(expr: String): List<String> {
        val cleaned = expr.replace(" ", "")
        val result = mutableListOf<String>()
        var i = 0
        while (i < cleaned.length) {
            val c = cleaned[i]
            when {
                c.isDigit() || c == '.' -> {
                    val start = i
                    while (i < cleaned.length && (cleaned[i].isDigit() || cleaned[i] == '.')) i++
                    result.add(cleaned.substring(start, i))
                    continue
                }
                c.isLetter() -> {
                    val start = i
                    while (i < cleaned.length && cleaned[i].isLetter()) i++
                    result.add(cleaned.substring(start, i))
                    continue
                }
                c in "+-*/^%()" -> {
                    result.add(c.toString())
                    i++
                }
                else -> throw IllegalArgumentException("Invalid character: $c")
            }
        }
        return result
    }

    private fun peek(): String? = tokens.getOrNull(pos)

    private fun parseExpression(): Double {
        var value = parseTerm()
        while (peek() == "+" || peek() == "-") {
            val op = tokens[pos]; pos++
            val rhs = parseTerm()
            value = if (op == "+") value + rhs else value - rhs
        }
        return value
    }

    private fun parseTerm(): Double {
        var value = parseFactor()
        while (peek() == "*" || peek() == "/" || peek() == "%") {
            val op = tokens[pos]; pos++
            val rhs = parseFactor()
            value = when (op) {
                "*" -> value * rhs
                "/" -> value / rhs
                else -> value % rhs
            }
        }
        return value
    }

    private fun parseFactor(): Double {
        // handles unary minus/plus
        if (peek() == "-") { pos++; return -parseFactor() }
        if (peek() == "+") { pos++; return parseFactor() }
        return parsePower()
    }

    private fun parsePower(): Double {
        val base = parseAtom()
        if (peek() == "^") {
            pos++
            val exponent = parseFactor() // right-associative
            return base.pow(exponent)
        }
        return base
    }

    private fun parseAtom(): Double {
        val token = peek() ?: throw IllegalArgumentException("Unexpected end of expression")

        if (token == "(") {
            pos++
            val value = parseExpression()
            if (peek() != ")") throw IllegalArgumentException("Expected ')'")
            pos++
            return value
        }

        if (token[0].isDigit() || token[0] == '.') {
            pos++
            return token.toDouble()
        }

        if (token[0].isLetter()) {
            pos++
            return when (token.lowercase()) {
                "pi" -> PI
                "e" -> E
                "sin" -> applyTrig(::sin)
                "cos" -> applyTrig(::cos)
                "tan" -> applyTrig(::tan)
                "asin" -> toDegreesIfNeeded(asin(parseParenArg()))
                "acos" -> toDegreesIfNeeded(acos(parseParenArg()))
                "atan" -> toDegreesIfNeeded(atan(parseParenArg()))
                "sinh" -> sinh(parseParenArg())
                "cosh" -> cosh(parseParenArg())
                "tanh" -> tanh(parseParenArg())
                "log" -> log10(parseParenArg())
                "ln" -> ln(parseParenArg())
                "sqrt" -> sqrt(parseParenArg())
                "abs" -> abs(parseParenArg())
                "exp" -> exp(parseParenArg())
                else -> throw IllegalArgumentException("Unknown function/constant: $token")
            }
        }

        throw IllegalArgumentException("Unexpected token: $token")
    }

    private fun parseParenArg(): Double {
        if (peek() != "(") throw IllegalArgumentException("Expected '(' after function name")
        pos++
        val value = parseExpression()
        if (peek() != ")") throw IllegalArgumentException("Expected ')'")
        pos++
        return value
    }

    private fun applyTrig(fn: (Double) -> Double): Double {
        val arg = parseParenArg()
        val radians = if (degreesMode) Math.toRadians(arg) else arg
        return fn(radians)
    }

    private fun toDegreesIfNeeded(radResult: Double): Double {
        return if (degreesMode) Math.toDegrees(radResult) else radResult
    }
}
