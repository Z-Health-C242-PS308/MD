package com.sugara.z_health.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sugara.z_health.data.model.JournalsItem
import com.sugara.z_health.databinding.ItemRecomendationBinding
import com.sugara.z_health.utils.Helper


class RecomendationAdapter : ListAdapter<JournalsItem, RecomendationAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecomendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)

        holder.bind(review)
    }
    class MyViewHolder(val binding: ItemRecomendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: JournalsItem){
            binding.tvTitle.text = journal.predict?.data?.recommendations?.get(0)?.title ?: "-"
            binding.tvDesc.text = journal.predict?.data?.recommendations?.get(0)?.description ?: "-"
            binding.ivIconRecomendation.setImageDrawable(Helper().getDrawableResource(itemView.context, journal.predict?.data?.recommendations?.get(0)?.icon ?: ""))
            binding.tvDate.text = Helper().convertDateIndo(journal.created ?: "")
            binding.tvStress.text = "Stress : " + journal.predict?.data?.predictedStress?.stressLevel ?: "-"
            binding.tvScore.text = "Score : " + journal.predict?.data?.predictedStress?.score.toString() ?: "-"
            binding.tvStress.setTextColor(Helper().getColorStress(itemView.context, journal.predict?.data?.predictedStress?.stressLevel ?: ""))
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JournalsItem>() {
            override fun areItemsTheSame(oldItem: JournalsItem, newItem: JournalsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: JournalsItem, newItem: JournalsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}