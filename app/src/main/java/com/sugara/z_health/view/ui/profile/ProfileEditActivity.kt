package com.sugara.z_health.view.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sugara.z_health.databinding.ActivityProfileEditBinding
import com.sugara.z_health.viewmodel.ViewModelFactory

@Suppress("DEPRECATION")
class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var viewModel: ProfileEditViewModel
    private var imageUri: Uri? = null
    private val IMAGE_PICK_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(application).create(ProfileEditViewModel::class.java)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this, Observer { isLoading ->
            showLoading(isLoading)
        })

        viewModel.response.observe(this, Observer { response ->
            showToast(response.message)
            if (response.isSuccess) {
                finish() // Close activity after successful update
            }
        })

        viewModel.getSession().observe(this, Observer { user ->
            binding.etFullname.setText(user.fullname)
            binding.etEmail.setText(user.email)
        })
    }

    private fun setupListeners() {
        binding.btnSelectImage.setOnClickListener {
            selectImage()

            binding.civProfileImage.setImageURI(imageUri)

            if (imageUri == null) {
                showToast("Please select a profile image")
                return@setOnClickListener
            }
        }



        binding.btnSaveChanges.setOnClickListener {
            val fullName = binding.etFullname.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Please fill all fields")
                return@setOnClickListener
            }

            viewModel.getSession().observe(this) { user ->
                val userId = user.userId ?: ""
                val token = user.token ?: ""
                viewModel.updateProfile(userId, token, fullName, email, password, imageUri)
            }
        }

        binding.tvCancel.setOnClickListener {
            finish()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.civProfileImage.setImageURI(imageUri)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
