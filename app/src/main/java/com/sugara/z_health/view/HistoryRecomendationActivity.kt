package com.sugara.z_health.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugara.z_health.R
import com.sugara.z_health.adapter.JournalAdapter
import com.sugara.z_health.adapter.RecomendationAdapter
import com.sugara.z_health.data.model.JournalsItem
import com.sugara.z_health.databinding.ActivityHistoryRecomendationBinding
import com.sugara.z_health.databinding.ActivityHomeBinding
import com.sugara.z_health.databinding.FragmentJournalBinding
import com.sugara.z_health.view.ui.journal.JournalViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory

class HistoryRecomendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryRecomendationBinding
    private lateinit var journalViewModel: JournalViewModel
    private  lateinit var userId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryRecomendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        journalViewModel = obtainViewModel(this)

        journalViewModel.getSession().observe(this) { user ->
            userId = user.userId ?: ""
            journalViewModel.getJournals(userId)
        }

        journalViewModel.listJournals.observe(this) { listJournals ->
            if(listJournals.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
            setListJournals(listJournals)
        }

        journalViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecomendation.layoutManager = layoutManager
    }


    private fun obtainViewModel(activity: AppCompatActivity): JournalViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(JournalViewModel::class.java)
    }



    private fun setListJournals(listEvents: List<JournalsItem>) {
        val adapter = RecomendationAdapter()
        adapter.submitList(listEvents)
        binding.rvRecomendation.setHasFixedSize(true)
        binding.rvRecomendation.isNestedScrollingEnabled = true
        binding.rvRecomendation.adapter = adapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}