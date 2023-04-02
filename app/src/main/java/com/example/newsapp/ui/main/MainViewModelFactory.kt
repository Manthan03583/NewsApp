package com.example.newsapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.newsapp.data.localdata.BookmarksRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val repository: BookmarksRepository,
    private val preferences: MutableLiveData<String>): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository,preferences) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}