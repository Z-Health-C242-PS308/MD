package com.sugara.z_health.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sugara.z_health.data.model.JournalsItem
import com.sugara.z_health.databinding.ItemJournalBinding
import com.sugara.z_health.utils.Helper


class JournalAdapter : ListAdapter<JournalsItem, JournalAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)

        holder.bind(review)
    }
    class MyViewHolder(val binding: ItemJournalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: JournalsItem){
            binding.tvBelajar.text = Helper().convertToDecimal(journal?.waktuBelajar).toString() + " Jam" ?: "-"
            binding.tvEkstrakurikuler.text = Helper().convertToDecimal(journal?.waktuBelajarTambahan).toString() + " Jam" ?: "-"
            binding.tvSosial.text = Helper().convertToDecimal(journal?.aktivitasSosial).toString() + " Jam" ?: "-"
            binding.tvFisik.text = Helper().convertToDecimal(journal?.aktivitasFisik).toString() + " Jam" ?: "-"
            binding.tvTidur.text = Helper().convertToDecimal(journal?.waktuTidur).toString() + " Jam" ?: "-"
            binding.tvCatatan.text = journal.jurnalHarian
            binding.tvTanggal.text = Helper().convertDateIndo(journal.created ?: "")
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