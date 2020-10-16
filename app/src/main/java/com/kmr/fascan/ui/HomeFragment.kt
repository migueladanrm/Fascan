package com.kmr.fascan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kmr.fascan.databinding.HomeFragmentBinding
import com.kmr.fascan.utils.ViewModelUtils
import com.kmr.fascan.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelUtils.provideLoginViewModelFactory()
    }
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

}