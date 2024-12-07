package com.sugara.z_health.view.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sugara.z_health.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        barChart = binding.barChart
        setupBarChart()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun setupBarChart() {
        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        val labels = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        // Example data
        val moodData = listOf(1, 1, 1, 1, 1, 1, 1)

        for (i in moodData.indices) {
            entries.add(BarEntry(i.toFloat(), moodData[i].toFloat()))
            if (moodData[i] > 0) {
                colors.add(Color.GREEN)
            } else {
                colors.add(Color.RED)
            }
        }

        val dataSet = BarDataSet(entries, "Mood Tracker")
        dataSet.colors = colors

        val data = BarData(dataSet)
        barChart.data = data

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.invalidate() // refresh
    }
}