package com.example.scicalc

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class StatsCombinatoricsFragment : Fragment(R.layout.fragment_stats_combinatorics) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etN = view.findViewById<EditText>(R.id.etN)
        val etR = view.findViewById<EditText>(R.id.etR)
        val tvCombResult = view.findViewById<TextView>(R.id.tvCombinatoricsResult)

        view.findViewById<Button>(R.id.btnPermutation).setOnClickListener {
            try {
                val n = etN.text.toString().toInt()
                val r = etR.text.toString().toInt()
                tvCombResult.text = "P(${n},${r}) = ${formatBig(StatsUtil.permutation(n, r))}"
            } catch (e: Exception) {
                tvCombResult.text = "Error: ${e.message}"
            }
        }

        view.findViewById<Button>(R.id.btnCombination).setOnClickListener {
            try {
                val n = etN.text.toString().toInt()
                val r = etR.text.toString().toInt()
                tvCombResult.text = "C(${n},${r}) = ${formatBig(StatsUtil.combination(n, r))}"
            } catch (e: Exception) {
                tvCombResult.text = "Error: ${e.message}"
            }
        }

        view.findViewById<Button>(R.id.btnFactorial).setOnClickListener {
            try {
                val n = etN.text.toString().toInt()
                tvCombResult.text = "${n}! = ${formatBig(StatsUtil.factorial(n))}"
            } catch (e: Exception) {
                tvCombResult.text = "Error: ${e.message}"
            }
        }

        val etDataSet = view.findViewById<EditText>(R.id.etDataSet)
        val tvStatsResult = view.findViewById<TextView>(R.id.tvStatsResult)

        view.findViewById<Button>(R.id.btnComputeStats).setOnClickListener {
            try {
                val data = etDataSet.text.toString()
                    .split(",", " ", "\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { it.toDouble() }

                val s = StatsUtil.summarize(data)
                tvStatsResult.text = buildString {
                    appendLine("Count   : ${s.count}")
                    appendLine("Sum     : ${fmt(s.sum)}")
                    appendLine("Mean    : ${fmt(s.mean)}")
                    appendLine("Median  : ${fmt(s.median)}")
                    appendLine("Mode    : ${if (s.mode.isEmpty()) "none" else s.mode.joinToString(", ") { fmt(it) }}")
                    appendLine("Min     : ${fmt(s.min)}")
                    appendLine("Max     : ${fmt(s.max)}")
                    appendLine("Range   : ${fmt(s.range)}")
                    appendLine("Variance: ${fmt(s.variance)}")
                    append("Std Dev : ${fmt(s.stdDev)}")
                }
            } catch (e: Exception) {
                tvStatsResult.text = "Error: ${e.message}"
            }
        }
    }

    private fun fmt(v: Double): String {
        return if (v == v.toLong().toDouble()) v.toLong().toString()
        else String.format("%.4f", v)
    }

    private fun formatBig(v: Double): String {
        return if (v == v.toLong().toDouble() && v < 1e15) v.toLong().toString()
        else String.format("%.4e", v)
    }
}
