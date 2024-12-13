package com.sugara.z_health.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sugara.z_health.R
import com.sugara.z_health.data.model.ProfileModel
import com.sugara.z_health.data.model.User
import com.sugara.z_health.databinding.ActivityEditProfileBinding
import com.sugara.z_health.databinding.ActivityHistoryRecomendationBinding
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.view.ui.journal.JournalViewModel
import com.sugara.z_health.view.ui.profile.ProfileFragment
import com.sugara.z_health.view.ui.profile.ProfileViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private  lateinit var userId : String
    private lateinit var token : String
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private  lateinit var profile : ProfileModel
    private lateinit var loadingDialog: AlertDialog

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCamera.setOnClickListener {
            selectImage()

        }

        binding.etDate.setOnClickListener {
            hideKeyboard()
            Handler(Looper.getMainLooper()).postDelayed({
                showDatePickerDialog()
            }, 200) // 200 milliseconds delay
        }

        profileViewModel = obtainViewModel(this)
        profileViewModel.getSession().observe(this) { user ->
            binding.etFullname.setText(user.fullname)
            binding.etEmail.setText(user.email)
            binding.etDate.setText(user.birthdate.toString())
            val encodedUrl = Helper().encodeUrl(user.profileImg ?: "")
            Glide.with(this)
                .load("$encodedUrl")
                .apply(RequestOptions())
                .into(binding.civAvatar)
            userId = user.userId ?: ""
            token = user.token ?: ""

        }

        binding.btnUpdate.setOnClickListener {
            val name = binding.etFullname.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmpassword.text.toString()
            val birthdate = binding.etDate.text.toString()

            if(name.isEmpty()){
                binding.etFullname.error = "Nama tidak boleh kosong"
                binding.etFullname.setBackgroundResource(R.drawable.input_error)
            } else if(email.isEmpty()){
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.setBackgroundResource(R.drawable.input_error)
            }else if(birthdate.isEmpty()) {
                binding.etDate.error = "Tanggal lahir tidak boleh kosong"
                binding.etDate.setBackgroundResource(R.drawable.input_error)
            } else {
                if (password != confirmPassword) {
                    binding.etConfirmpassword.error = "Konfirmasi password tidak sama dengan password"
                    binding.etConfirmpassword.setBackgroundResource(R.drawable.input_error)
                } else {
                    profile = ProfileModel(
                        fullname = name,
                        email = email,
                        password = password,
                        birthdate = birthdate,
                        profileImg = imageUri ?: Uri.parse(""),
                        confirmPass = confirmPassword,
                        token = token,
                        userId = userId
                    )
                    profileViewModel.update(profile)
                }
            }
        }


        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                imageUri = result.data?.data
                val mimeType = contentResolver.getType(imageUri!!)
                if (mimeType != null && mimeType.startsWith("image/")) {
                    binding.civAvatar.setImageURI(imageUri)
                } else {
                    imageUri = null
                    Toast.makeText(this, "Selected file is not an image", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Failed to pick image", Toast.LENGTH_SHORT).show()
            }
        }


        profileViewModel.isLoading.observe(this) { isLoading ->
            //set text button register to spinner and disable button
            if (isLoading) {
                showLoadingDialog()
                binding.btnUpdate.text = "Loading..."
                binding.btnUpdate.isEnabled = false
            } else {
                dismissLoadingDialog()
                //set text button register to register and enable button
                binding.btnUpdate.text = "UPDATE PROFILE"
                binding.btnUpdate.isEnabled = true
            }
        }

        profileViewModel.response.observe(this) { response ->
            val builder = AlertDialog.Builder(this)
            builder.setMessage(response.message)
            builder.setCancelable(false)

            if (response.isSuccess) {
                builder.setTitle("Success")
                builder.setPositiveButton("OK") { dialog, _ ->
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("showProfileFragment", true)
                    startActivity(intent)
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val selectedDate = "$selectedYear-$formattedMonth-$formattedDay"
                binding.etDate.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }


    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etDate.windowToken, 0)
    }


    private fun obtainViewModel(activity: AppCompatActivity): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ProfileViewModel::class.java)
    }
}