package com.example.scicalc

object MatrixUtil {

    fun add(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        val n = a.size; val m = a[0].size
        return Array(n) { i -> DoubleArray(m) { j -> a[i][j] + b[i][j] } }
    }

    fun subtract(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        val n = a.size; val m = a[0].size
        return Array(n) { i -> DoubleArray(m) { j -> a[i][j] - b[i][j] } }
    }

    fun multiply(a: Array<DoubleArray>, b: Array<DoubleArray>): Array<DoubleArray> {
        val n = a.size; val k = a[0].size; val m = b[0].size
        if (k != b.size) throw IllegalArgumentException("Incompatible dimensions for multiplication")
        return Array(n) { i ->
            DoubleArray(m) { j ->
                var sum = 0.0
                for (t in 0 until k) sum += a[i][t] * b[t][j]
                sum
            }
        }
    }

    fun transpose(a: Array<DoubleArray>): Array<DoubleArray> {
        val n = a.size; val m = a[0].size
        return Array(m) { i -> DoubleArray(n) { j -> a[j][i] } }
    }

    fun determinant(a: Array<DoubleArray>): Double {
        val n = a.size
        if (n == 1) return a[0][0]
        if (n == 2) return a[0][0] * a[1][1] - a[0][1] * a[1][0]
        var det = 0.0
        for (col in 0 until n) {
            val sign = if (col % 2 == 0) 1.0 else -1.0
            det += sign * a[0][col] * determinant(minor(a, 0, col))
        }
        return det
    }

    private fun minor(a: Array<DoubleArray>, rowToRemove: Int, colToRemove: Int): Array<DoubleArray> {
        val n = a.size
        val result = Array(n - 1) { DoubleArray(n - 1) }
        var ri = 0
        for (i in 0 until n) {
            if (i == rowToRemove) continue
            var ci = 0
            for (j in 0 until n) {
                if (j == colToRemove) continue
                result[ri][ci] = a[i][j]
                ci++
            }
            ri++
        }
        return result
    }

    fun inverse(a: Array<DoubleArray>): Array<DoubleArray> {
        val n = a.size
        val det = determinant(a)
        if (Math.abs(det) < 1e-12) throw ArithmeticException("Matrix is singular (determinant = 0), no inverse exists")

        if (n == 1) return arrayOf(doubleArrayOf(1.0 / a[0][0]))

        // Cofactor / adjugate method
        val cofactors = Array(n) { i ->
            DoubleArray(n) { j ->
                val sign = if ((i + j) % 2 == 0) 1.0 else -1.0
                sign * determinant(minor(a, i, j))
            }
        }
        val adjugate = transpose(cofactors)
        return Array(n) { i -> DoubleArray(n) { j -> adjugate[i][j] / det } }
    }

    fun format(a: Array<DoubleArray>): String {
        val sb = StringBuilder()
        for (row in a) {
            sb.append(row.joinToString("\t") { v ->
                if (v == v.toLong().toDouble()) v.toLong().toString()
                else String.format("%.4f", v)
            })
            sb.append("\n")
        }
        return sb.toString().trim()
    }
}
