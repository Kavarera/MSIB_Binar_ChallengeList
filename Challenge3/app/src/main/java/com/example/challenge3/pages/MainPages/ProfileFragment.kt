package com.example.challenge3.pages.MainPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.challenge3.R
import com.example.challenge3.databinding.FragmentProfileBinding
import com.example.challenge3.util.viewmodels.ProfileVIewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val viewModel:ProfileVIewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        viewModel.getUserData()


        viewModel.User.observe(viewLifecycleOwner){
            binding.etUsernameProfile.setText(it?.username)
            binding.etTeleponProfile.setText(it?.telepon)
        }

        binding.btnEditProfile.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_profileEditActivity)
        }

        binding.btnSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            android.os.Process.killProcess(android.os.Process.myPid())
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserData()
    }
}