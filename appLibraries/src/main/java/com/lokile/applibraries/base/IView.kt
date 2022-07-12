package com.lokile.applibraries.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

interface IView{
    fun setupView(savedInstanceState: Bundle?)
}