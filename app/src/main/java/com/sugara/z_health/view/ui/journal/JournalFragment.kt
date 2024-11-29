package com.sugara.z_health.view.ui.journal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugara.z_health.adapter.JournalAdapter
import com.sugara.z_health.databinding.FragmentJournalBinding

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val journalViewModel =
            ViewModelProvider(this).get(JournalViewModel::class.java)

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


        //create dummy data for journal
        val journalList = ArrayList<JournalItem>()
        journalList.add(JournalItem(2, 3, 4, 5, 6))
        journalList.add(JournalItem(3, 4, 5, 6, 7))
        journalList.add(JournalItem(4, 5, 6, 7, 8))
        journalList.add(JournalItem(5, 6, 7, 8, 9))
        journalList.add(JournalItem(6, 7, 8, 9, 10))
        journalList.add(JournalItem(7, 8, 9, 10, 11))

        //set adapter
        val adapter = JournalAdapter()
        adapter.submitList(journalList)
        binding.rvJournal.setHasFixedSize(true)
        binding.rvJournal.isNestedScrollingEnabled = false
        binding.rvJournal.adapter = adapter


        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvJournal.layoutManager = layoutManager
    }
}