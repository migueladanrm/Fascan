package com.kmr.fascan.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kmr.fascan.viewmodels.HomeViewModel

object ViewModelUtils {

    fun provideLoginViewModelFactory(): LoginViewModelFactory {
        return LoginViewModelFactory()
    }

}

class LoginViewModelFactory :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}