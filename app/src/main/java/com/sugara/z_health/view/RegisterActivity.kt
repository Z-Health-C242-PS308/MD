package com.sugara.z_health.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.sugara.z_health.R
import com.sugara.z_health.data.model.User
import com.sugara.z_health.databinding.ActivityRegisterBinding
import com.sugara.z_health.viewmodel.RegisterViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var register : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etDate.setOnClickListener {
            hideKeyboard()
            Log.d("RegisterActivity", "onCreate: etDate clicked")
            Handler(Looper.getMainLooper()).postDelayed({
                showDatePickerDialog()
            }, 200) // 200 milliseconds delay
        }

        registerViewModel = obtainViewModel(this)


        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
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
            }else if(password.isEmpty()){
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.setBackgroundResource(R.drawable.input_error)
            } else if(confirmPassword.isEmpty()){
                binding.etConfirmpassword.error = "Konfirmasi password tidak boleh kosong"
                binding.etConfirmpassword.setBackgroundResource(R.drawable.input_error)
            } else {
                if (password != confirmPassword) {
                    binding.etConfirmpassword.error = "Konfirmasi password tidak sama dengan password"
                    binding.etConfirmpassword.setBackgroundResource(R.drawable.input_error)
                } else {
                    register = User(
                        fullname = name,
                        email = email,
                        password = password,
                        birthdate = birthdate,
                        profileImg = "d_avatar_10.png",
                        confirmPass = confirmPassword
                    )
                    registerViewModel.register(register)
                }
            }

        }

        registerViewModel.isLoading.observe(this) { isLoading ->
            //set text button register to spinner and disable button
            if (isLoading) {
                binding.btnRegister.text = "Loading..."
                binding.btnRegister.isEnabled = false
            } else {
                //set text button register to register and enable button
                binding.btnRegister.text = "Register"
                binding.btnRegister.isEnabled = true
            }
        }

        registerViewModel.response.observe(this) { response ->
            val builder = AlertDialog.Builder(this)
            builder.setMessage(response.message)
            builder.setCancelable(false)

            if (response.isSuccess) {
                builder.setTitle("Success")
                builder.setPositiveButton("OK") { dialog, _ ->
                    startActivity(Intent(this, LoginActivity::class.java))
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

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etDate.windowToken, 0)
    }


    private fun obtainViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(RegisterViewModel::class.java)
    }
}