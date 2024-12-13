package com.sugara.z_health.view.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sugara.z_health.databinding.FragmentProfileBinding
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.utils.UrlHelper
import com.sugara.z_health.view.EditProfileActivity
import com.sugara.z_health.view.LoginActivity
import com.sugara.z_health.viewmodel.MainViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = obtainViewModel(requireActivity() as AppCompatActivity)
        binding.btnLogout.setOnClickListener {
            profileViewModel.logout()
            val mIntent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(mIntent)
            requireActivity().finish()
        }
        binding.btnEdit.setOnClickListener {
            val mIntent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(mIntent)
        }

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvName.text = user.fullname
            binding.tvEmail.text = user.email
            val encodedUrl = Helper().encodeUrl(user.profileImg ?: "")
            Glide.with(requireContext())
                .load("$encodedUrl")
                .apply(RequestOptions())
                .into(binding.civAvatar)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ProfileViewModel::class.java)
    }
}