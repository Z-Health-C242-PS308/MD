package com.sugara.z_health.view.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sugara.z_health.adapter.JournalAdapter
import com.sugara.z_health.data.model.JournalsItem
import com.sugara.z_health.databinding.FragmentJournalBinding
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.view.ui.home.HomeViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var journalViewModel: JournalViewModel
    private  lateinit var userId : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        journalViewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        journalViewModel.getSession().observe(viewLifecycleOwner) { user ->
            userId = user.userId ?: ""
            journalViewModel.getJournals(userId)
        }

        journalViewModel.listJournals.observe(viewLifecycleOwner) { listJournals ->
            if(listJournals.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
            setListJournals(listJournals)
        }

        journalViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }


        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvJournal.layoutManager = layoutManager
    }


    private fun obtainViewModel(activity: AppCompatActivity): JournalViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(JournalViewModel::class.java)
    }


    private fun setListJournals(listEvents: List<JournalsItem>) {
        val adapter = JournalAdapter()
        adapter.submitList(listEvents)
        binding.rvJournal.setHasFixedSize(true)
        binding.rvJournal.isNestedScrollingEnabled = false
        binding.rvJournal.adapter = adapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}