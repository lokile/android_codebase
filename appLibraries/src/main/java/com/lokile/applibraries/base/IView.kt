package com.lokile.applibraries.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

interface IView<T : ViewBinding> {
    fun setupView(savedInstanceState: Bundle?)
    fun onCreateViewBinding(layoutInflater: LayoutInflater): T
}