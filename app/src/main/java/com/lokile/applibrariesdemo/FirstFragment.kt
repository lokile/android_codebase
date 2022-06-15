package com.lokile.applibrariesdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lokile.applibraries.base.AppBaseFragment
import com.lokile.applibrariesdemo.databinding.FragmentFirstBinding

class FirstFragment : AppBaseFragment<FragmentFirstBinding>() {
    override fun setupView(savedInstanceState: Bundle?) {
        binding?.buttonFirst?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    override fun onCreateViewBinding(layoutInflater: LayoutInflater) = FragmentFirstBinding.inflate(layoutInflater)
}