package com.sugara.z_health.view.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sugara.z_health.databinding.FragmentProfileBinding
import com.sugara.z_health.utils.Helper
import com.sugara.z_health.view.LoginActivity
import com.sugara.z_health.viewmodel.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
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
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvName.text = user.fullname
            binding.tvEmail.text = user.email
            val encodedUrl = Helper().encodeUrl(user.profileImg ?: "")
            Glide.with(requireContext())
                .load(encodedUrl)
                .apply(RequestOptions())
                .into(binding.civAvatarprofile)
        }

        binding.btnEditt.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileEditActivity::class.java))
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): ProfileViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(ProfileViewModel::class.java)
    }
}
