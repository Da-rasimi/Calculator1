package com.example.scicalc

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class MatrixFragment : Fragment(R.layout.fragment_matrix) {

    private var size = 2
    private lateinit var gridA: GridLayout
    private lateinit var gridB: GridLayout
    private lateinit var tvResult: TextView

    private val operations = listOf(
        "A + B", "A − B", "A × B", "Transpose A", "Determinant A", "Inverse A"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridA = view.findViewById(R.id.gridMatrixA)
        gridB = view.findViewById(R.id.gridMatrixB)
        tvResult = view.findViewById(R.id.tvMatrixResult)

        val spinnerSize = view.findViewById<Spinner>(R.id.spinnerSize)
        val spinnerOp = view.findViewById<Spinner>(R.id.spinnerOperation)
        val btnCompute = view.findViewById<Button>(R.id.btnCompute)

        spinnerSize.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            listOf("2 x 2", "3 x 3", "4 x 4")
        )
        spinnerOp.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, operations
        )

        buildGrids(size)

        spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                size = position + 2
                buildGrids(size)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnCompute.setOnClickListener {
            try {
                val a = readMatrix(gridA, size)
                val opIndex = spinnerOp.selectedItemPosition
                val result: Array<DoubleArray> = when (opIndex) {
                    0 -> MatrixUtil.add(a, readMatrix(gridB, size))
                    1 -> MatrixUtil.subtract(a, readMatrix(gridB, size))
                    2 -> MatrixUtil.multiply(a, readMatrix(gridB, size))
                    3 -> MatrixUtil.transpose(a)
                    4 -> arrayOf(doubleArrayOf(MatrixUtil.determinant(a)))
                    5 -> MatrixUtil.inverse(a)
                    else -> a
                }
                tvResult.text = MatrixUtil.format(result)
            } catch (e: Exception) {
                tvResult.text = "Error: ${e.message}"
            }
        }
    }

    private fun buildGrids(n: Int) {
        gridA.removeAllViews()
        gridB.removeAllViews()
        gridA.columnCount = n
        gridB.columnCount = n
        for (i in 0 until n * n) {
            gridA.addView(makeCell())
            gridB.addView(makeCell())
        }
    }

    private fun makeCell(): EditText {
        val et = EditText(requireContext())
        et.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        et.setText("0")
        et.setTextColor(0xFFFFFFFF.toInt())
        et.setBackgroundColor(0xFF1F1F35.toInt())
        val params = GridLayout.LayoutParams()
        params.width = 130
        params.height = 110
        params.setMargins(4, 4, 4, 4)
        et.layoutParams = params
        et.gravity = android.view.Gravity.CENTER
        return et
    }

    private fun readMatrix(grid: GridLayout, n: Int): Array<DoubleArray> {
        val result = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0 until n) {
                val index = i * n + j
                val et = grid.getChildAt(index) as EditText
                val text = et.text.toString()
                result[i][j] = if (text.isBlank()) 0.0 else text.toDouble()
            }
        }
        return result
    }
}
