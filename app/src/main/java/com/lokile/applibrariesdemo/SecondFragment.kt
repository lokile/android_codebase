package com.lokile.applibrariesdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lokile.applibraries.base.AppBaseFragment
import com.lokile.applibrariesdemo.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : AppBaseFragment<FragmentSecondBinding>() {
    override fun setupView(savedInstanceState: Bundle?) {
        binding?.buttonSecond?.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onCreateViewBinding(layoutInflater: LayoutInflater) = FragmentSecondBinding.inflate(layoutInflater)
}