package com.sugara.z_health.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sugara.z_health.view.ui.journal.JournalItem
import com.sugara.z_health.databinding.ItemJournalBinding


class JournalAdapter : ListAdapter<JournalItem, JournalAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
    class MyViewHolder(val binding: ItemJournalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: JournalItem){
            binding.tvStudyTime.text = ": ${journal.studyHours} hours"
            binding.tvExtracurricularTime.text = ": ${journal.extracurricularHours} hours"
            binding.tvSleepTime.text = ": ${journal.sleepHours} hours"
            binding.tvSocialTime.text = ": ${journal.socialHours} hours"
            binding.tvPhysicalActivityTime.text = ": ${journal.physicalActivityHours} hours"
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JournalItem>() {
            override fun areItemsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JournalItem, newItem: JournalItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}