package com.lokile.applibrariesdemo

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.lokile.applibraries.base.AppBaseFragment
import com.lokile.applibraries.utils.viewBindings.viewBinding
import com.lokile.applibrariesdemo.databinding.FragmentFirstBinding

class FirstFragment : AppBaseFragment() {
    val binding by viewBinding(FragmentFirstBinding::inflate)
    override fun setupView(savedInstanceState: Bundle?) {
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }
}