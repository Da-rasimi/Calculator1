package com.example.scicalc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ScientificCalculatorFragment : Fragment(R.layout.fragment_scientific_calculator) {

    private var expression = StringBuilder()
    private var degreesMode = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvExpression = view.findViewById<TextView>(R.id.tvExpression)
        val tvResult = view.findViewById<TextView>(R.id.tvResult)
        val btnDegRad = view.findViewById<Button>(R.id.btnDegRad)

        fun refresh() { tvExpression.text = expression.toString() }
        fun appendToken(token: String) { expression.append(token); refresh() }

        btnDegRad.setOnClickListener {
            degreesMode = !degreesMode
            btnDegRad.text = if (degreesMode) "DEG" else "RAD"
        }

        val numberIds = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3",
            R.id.btn4 to "4", R.id.btn5 to "5", R.id.btn6 to "6", R.id.btn7 to "7",
            R.id.btn8 to "8", R.id.btn9 to "9", R.id.btnDot to "."
        )
        numberIds.forEach { (id, label) -> view.findViewById<Button>(id).setOnClickListener { appendToken(label) } }

        view.findViewById<Button>(R.id.btnPlus).setOnClickListener { appendToken("+") }
        view.findViewById<Button>(R.id.btnMinus).setOnClickListener { appendToken("-") }
        view.findViewById<Button>(R.id.btnMultiply).setOnClickListener { appendToken("*") }
        view.findViewById<Button>(R.id.btnDivide).setOnClickListener { appendToken("/") }
        view.findViewById<Button>(R.id.btnParenOpen).setOnClickListener { appendToken("(") }
        view.findViewById<Button>(R.id.btnParenClose).setOnClickListener { appendToken(")") }
        view.findViewById<Button>(R.id.btnPow).setOnClickListener { appendToken("^") }
        view.findViewById<Button>(R.id.btnPi).setOnClickListener { appendToken("pi") }
        view.findViewById<Button>(R.id.btnE).setOnClickListener { appendToken("e") }

        view.findViewById<Button>(R.id.btnSin).setOnClickListener { appendToken("sin(") }
        view.findViewById<Button>(R.id.btnCos).setOnClickListener { appendToken("cos(") }
        view.findViewById<Button>(R.id.btnTan).setOnClickListener { appendToken("tan(") }
        view.findViewById<Button>(R.id.btnSinh).setOnClickListener { appendToken("sinh(") }
        view.findViewById<Button>(R.id.btnCosh).setOnClickListener { appendToken("cosh(") }
        view.findViewById<Button>(R.id.btnTanh).setOnClickListener { appendToken("tanh(") }
        view.findViewById<Button>(R.id.btnLog).setOnClickListener { appendToken("log(") }
        view.findViewById<Button>(R.id.btnLn).setOnClickListener { appendToken("ln(") }
        view.findViewById<Button>(R.id.btnSqrt).setOnClickListener { appendToken("sqrt(") }
        view.findViewById<Button>(R.id.btnAbs).setOnClickListener { appendToken("abs(") }

        view.findViewById<Button>(R.id.btnClear).setOnClickListener {
            expression.clear(); tvResult.text = "0"; refresh()
        }
        view.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (expression.isNotEmpty()) { expression.deleteCharAt(expression.length - 1); refresh() }
        }
        view.findViewById<Button>(R.id.btnEquals).setOnClickListener {
            try {
                val result = ExpressionEvaluator(degreesMode).evaluate(expression.toString())
                tvResult.text = formatResult(result)
            } catch (e: Exception) {
                tvResult.text = "Error"
            }
        }
    }

    private fun formatResult(value: Double): String {
        return if (value == value.toLong().toDouble() && !value.isInfinite()) {
            value.toLong().toString()
        } else {
            String.format("%.8f", value).trimEnd('0').trimEnd('.')
        }
    }
}
