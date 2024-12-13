package com.sugara.z_health.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sugara.z_health.R
import com.sugara.z_health.data.model.Journal
import com.sugara.z_health.databinding.ActivityFormJournalBinding
import com.sugara.z_health.viewmodel.FormJournalViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory

class FormJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormJournalBinding
    private lateinit var formJournalViewModel: FormJournalViewModel
    private lateinit var userId : String
    private lateinit var loadingDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        formJournalViewModel = obtainViewModel(this)
        formJournalViewModel.getSession().observe(this) { session ->
            userId = session.userId.toString()
        }

        binding.btnSubmit.setOnClickListener {
            val jam_belajar = binding.etJamBelajar.text.toString()
            val jam_belajar_tambahan = binding.etJamEkstrakurikuler.text.toString()
            val jam_sosial = binding.etJamSosial.text.toString()
            val jam_aktifitas_fisik = binding.etJamFisik.text.toString()
            val jam_tidur = binding.etJamTidur.text.toString()
            val catatan = binding.etCatatan.text.toString()

            if(jam_belajar.isEmpty()){
                binding.etJamBelajar.error = "Jam belajar tidak boleh kosong"
                binding.etJamBelajar.setBackgroundResource(R.drawable.input_error)
            } else if(jam_belajar_tambahan.isEmpty()){
                binding.etJamEkstrakurikuler.error = "Jam belajar tambahan tidak boleh kosong"
                binding.etJamEkstrakurikuler.setBackgroundResource(R.drawable.input_error)
            } else if(jam_sosial.isEmpty()){
                binding.etJamSosial.error = "Jam sosial tidak boleh kosong"
                binding.etJamSosial.setBackgroundResource(R.drawable.input_error)
            } else if(jam_aktifitas_fisik.isEmpty()){
                binding.etJamFisik.error = "Jam aktifitas fisik tidak boleh kosong"
                binding.etJamFisik.setBackgroundResource(R.drawable.input_error)
            } else if(jam_tidur.isEmpty()){
                binding.etJamTidur.error = "Jam tidur tidak boleh kosong"
                binding.etJamTidur.setBackgroundResource(R.drawable.input_error)
            } else if(catatan.isEmpty()){
                binding.etCatatan.error = "Catatan tidak boleh kosong"
                binding.etCatatan.setBackgroundResource(R.drawable.input_error)
            } else {
                val formJournal = Journal(
                    userId = userId,
                    waktuBelajar = jam_belajar.toDouble(),
                    waktuBelajarTambahan = jam_belajar_tambahan.toDouble(),
                    aktivitasSosial = jam_sosial.toDouble(),
                    aktivitasFisik = jam_aktifitas_fisik.toDouble(),
                    waktuTidur = jam_tidur.toDouble(),
                    jurnalHarian = catatan

                )
                formJournalViewModel.insertJournal(formJournal)
            }
        }

        formJournalViewModel.response.observe(this) { response ->
            val builder = AlertDialog.Builder(this)
            builder.setMessage(response.message)
            builder.setCancelable(false)

            if (response.isSuccess) {
                builder.setTitle("Success")
                builder.setPositiveButton("OK") { dialog, _ ->
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            } else {
                builder.setTitle("Error")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        formJournalViewModel.isLoading.observe(this) { isLoading ->
            //set text button register to spinner and disable button
            if (isLoading) {
                showLoadingDialog()
                binding.btnSubmit.text = "Loading..."
                binding.btnSubmit.isEnabled = false
            } else {
                dismissLoadingDialog()
                binding.btnSubmit.text = "Simpan"
                binding.btnSubmit.isEnabled = true
            }
        }


    }

    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }


    private fun dismissLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FormJournalViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FormJournalViewModel::class.java)
    }
}