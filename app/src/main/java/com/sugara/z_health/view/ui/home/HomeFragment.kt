package com.sugara.z_health.view.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sugara.z_health.data.model.JournalItem
import com.sugara.z_health.databinding.FragmentHomeBinding
import com.sugara.z_health.utils.EmojiValueFormatter
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.view.FormJournalActivity
import com.sugara.z_health.view.ui.rekomendasi.RekomendasiActivity
import com.sugara.z_health.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var userId : String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        barChart = binding.barChart
        pieChart = binding.pieChart
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvName.text = user.fullname
            val encodedUrl = Helper().encodeUrl(user.profileImg ?: "")
            Glide.with(requireContext())
                .load("$encodedUrl")
                .apply(RequestOptions())
                .into(binding.civAvatar)
            userId = user.userId ?: ""
            homeViewModel.getLatestJournal(userId)
            homeViewModel.getLatestWeekJournal(userId)
        }

        binding.btnIsiJurnal.setOnClickListener {
            val intent = Intent(requireContext(), FormJournalActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.latestJournal.observe(viewLifecycleOwner) { journal ->
            if (journal != null) {
                //chek apakah tanggal hari ini sudah diisi jurnal atau belum
                val today = Helper().getCurrentDate()
                val journalDate = Helper().convertToDateFormat(journal.journal?.created ?: "")
                if (today == journalDate) {
                    binding.journalEmpty.visibility = View.GONE
                    binding.journalContent.visibility = View.VISIBLE

                    binding.tvBelajar.text = Helper().convertToDecimal(journal.journal?.waktuBelajar).toString() + " Jam" ?: "-"
                    binding.tvEkstrakurikuler.text = Helper().convertToDecimal(journal.journal?.waktuBelajarTambahan).toString() + " Jam" ?: "-"
                    binding.tvSosial.text = Helper().convertToDecimal(journal.journal?.aktivitasSosial).toString() + " Jam" ?: "-"
                    binding.tvFisik.text = Helper().convertToDecimal(journal.journal?.aktivitasFisik).toString() + " Jam" ?: "-"
                    binding.tvTidur.text = Helper().convertToDecimal(journal.journal?.waktuTidur).toString() + " Jam" ?: "-"
                } else {
                    binding.journalEmpty.visibility = View.VISIBLE
                    binding.journalContent.visibility = View.GONE
                }

                binding.recomendationContent.visibility = View.VISIBLE
                binding.loadingRecomendation.visibility = View.GONE
                binding.recomendationEmpty.visibility = View.GONE
                binding.tvRecommendation.text = journal.journal?.predict?.data?.recommendations?.get(0)?.title
                    ?: "-"
                binding.tvRecommendationDesc.text = journal.journal?.predict?.data?.recommendations?.get(0)?.description
                    ?: "-"
                binding.ivRecomendation.setImageDrawable(Helper().getDrawableResource(requireContext(), journal.journal?.predict?.data?.recommendations?.get(0)?.icon ?: ""))

            }else{
                binding.journalEmpty.visibility = View.VISIBLE
                binding.journalContent.visibility = View.GONE
                binding.recomendationEmpty.visibility = View.VISIBLE
                binding.recomendationContent.visibility = View.GONE
            }
        }

        homeViewModel.isLoadingJournal.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingJournal.visibility = View.VISIBLE
                binding.loadingRecomendation.visibility = View.VISIBLE
                binding.journalEmpty.visibility = View.GONE
                binding.journalContent.visibility = View.GONE
            } else {
                binding.loadingJournal.visibility = View.GONE
                binding.loadingRecomendation.visibility = View.GONE
            }
        }


        homeViewModel.isLoadingWeekJournal.observe(viewLifecycleOwner) { isLoading ->
            Log.d("isLoading", "isLoading weekly : $isLoading")
            if (isLoading) {
                binding.loadingMood.visibility = View.VISIBLE
            } else {
                binding.loadingMood.visibility = View.GONE
            }
        }

        homeViewModel.latestWeekJournal.observe(viewLifecycleOwner) { journalWeek ->
            Log.d("journalWeek", "journalWeek : $journalWeek")
            if (!journalWeek.journal?.isNullOrEmpty()!!) {
                binding.moodEmpty.visibility = View.GONE
                binding.moodChart.visibility = View.VISIBLE
                val listPredict = ArrayList<JournalItem>()
                journalWeek.journal?.forEach() {
                    if (it != null) {
                        listPredict.add(it ?: JournalItem())
                    }
                }
                setupBarChart(listPredict)
                setupPieChart(listPredict)
            }else{
                binding.moodEmpty.visibility = View.VISIBLE
                binding.moodChart.visibility = View.GONE
            }
        }


        binding.recomendationContent.setOnClickListener {
            val intent = Intent(requireContext(), RekomendasiActivity::class.java)
            startActivity(intent)
        }




    }

    private fun obtainViewModel(activity: AppCompatActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }


    private fun setupBarChart(listData: ArrayList<JournalItem>) {
        listData.reverse()
        val entries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        val labels = listData.map { Helper().convertToDay(it.created ?: "") }

        for (i in listData.indices) {
            val moodValue = listData[i].predict?.data?.predictedStress?.score?.toFloat() ?: 0f
            // Add a full bar with gray color
            entries.add(BarEntry(i.toFloat(), 10f))
            colors.add(Color.LTGRAY)
            // Add the actual mood value with the corresponding color
            entries.add(BarEntry(i.toFloat(), moodValue))
            val stressLevel = listData[i].predict?.data?.predictedStress?.stressLevel ?: ""
            colors.add(Helper().generateColor(stressLevel))
        }

        val dataSet = BarDataSet(entries, "Mood Tracker")
        dataSet.colors = colors
        dataSet.valueFormatter = EmojiValueFormatter(colors)
        dataSet.valueTextSize = 16f

        val data = BarData(dataSet)
        barChart.data = data

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.axisLeft.setDrawLabels(false) // Disable left y-axis labels
        barChart.axisRight.setDrawLabels(false) // Disable right y-axis labels
        barChart.axisLeft.axisMaximum = 11f // Set max value for left y-axis
        barChart.axisRight.axisMaximum = 11f // Set max value for right y-axis
        barChart.axisLeft.axisMinimum = 0f // Set min value for left y-axis
        barChart.axisRight.axisMinimum = 0f // Set min value for right y-axis
        barChart.axisLeft.setDrawAxisLine(false) // Remove left y-axis line
        barChart.axisRight.setDrawAxisLine(false) // Remove right y-axis line
        barChart.setFitBars(true) // Ensure bars fit within the chart
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.invalidate() // refresh
    }
    private fun setupPieChart(listData: ArrayList<JournalItem>) {
        val entries = ArrayList<PieEntry>()
        var low = 0
        var moderate = 0
        var high = 0

        for (item in listData) {
            val stressLevel = item.predict?.data?.predictedStress?.stressLevel ?: ""
            when (stressLevel) {
                "Low" -> low++
                "Moderate" -> moderate++
                "High" -> high++
            }
        }

        entries.add(PieEntry(low.toFloat(), ""))
        entries.add(PieEntry(moderate.toFloat(), ""))
        entries.add(PieEntry(high.toFloat(), ""))

        val dataSet = PieDataSet(entries, "Stress Level")
        dataSet.colors = listOf(Color.GREEN, Color.YELLOW, Color.RED)
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }
}
