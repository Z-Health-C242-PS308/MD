package com.sugara.z_health.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sugara.z_health.R
import com.sugara.z_health.data.model.User
import com.sugara.z_health.data.model.UserLogin
import com.sugara.z_health.databinding.ActivityLoginBinding
import com.sugara.z_health.viewmodel.LoginViewModel
import com.sugara.z_health.viewmodel.RegisterViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var login : UserLogin
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = obtainViewModel(this)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if(email.isEmpty()){
                binding.etEmail.error = "Email tidak boleh kosong"
                binding.etEmail.setBackgroundResource(R.drawable.input_error)
            } else if(password.isEmpty()){
                binding.etPassword.error = "Password tidak boleh kosong"
                binding.etPassword.setBackgroundResource(R.drawable.input_error)
            } else {
                login = UserLogin(
                    email = email,
                    password = password
                )
                loginViewModel.login(login)
            }

        }


        loginViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoadingDialog()
                binding.btnLogin.text = "Loading..."
                binding.btnLogin.isEnabled = false
            } else {
                dismissLoadingDialog()
                //set text button register to register and enable button
                binding.btnLogin.text = "Login"
                binding.btnLogin.isEnabled = true
            }
        }

        loginViewModel.response.observe(this) { response ->
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


    }

    private fun obtainViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(LoginViewModel::class.java)
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

}