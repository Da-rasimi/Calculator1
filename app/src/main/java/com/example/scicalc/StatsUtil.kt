package com.example.scicalc

import kotlin.math.pow
import kotlin.math.sqrt

object StatsUtil {

    fun factorial(n: Int): Double {
        if (n < 0) throw IllegalArgumentException("n must be non-negative")
        var result = 1.0
        for (i in 2..n) result *= i
        return result
    }

    fun permutation(n: Int, r: Int): Double {
        if (r > n || r < 0 || n < 0) throw IllegalArgumentException("Require 0 <= r <= n")
        var result = 1.0
        for (i in 0 until r) result *= (n - i)
        return result
    }

    fun combination(n: Int, r: Int): Double {
        if (r > n || r < 0 || n < 0) throw IllegalArgumentException("Require 0 <= r <= n")
        return permutation(n, r) / factorial(r)
    }

    data class Summary(
        val count: Int,
        val sum: Double,
        val mean: Double,
        val median: Double,
        val mode: List<Double>,
        val range: Double,
        val variance: Double,
        val stdDev: Double,
        val min: Double,
        val max: Double
    )

    fun summarize(data: List<Double>): Summary {
        if (data.isEmpty()) throw IllegalArgumentException("Data set is empty")
        val sorted = data.sorted()
        val n = data.size
        val sum = data.sum()
        val mean = sum / n
        val median = if (n % 2 == 0) (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0 else sorted[n / 2]
        val variance = data.sumOf { (it - mean).pow(2) } / n
        val stdDev = sqrt(variance)
        val freq = data.groupingBy { it }.eachCount()
        val maxFreq = freq.values.maxOrNull() ?: 0
        val mode = if (maxFreq > 1) freq.filterValues { it == maxFreq }.keys.sorted() else emptyList()

        return Summary(
            count = n,
            sum = sum,
            mean = mean,
            median = median,
            mode = mode,
            range = sorted.last() - sorted.first(),
            variance = variance,
            stdDev = stdDev,
            min = sorted.first(),
            max = sorted.last()
        )
    }
}
