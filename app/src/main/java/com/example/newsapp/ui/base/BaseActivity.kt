package com.example.newsapp.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.newsapp.data.localdata.BookmarksDatabase
import com.example.newsapp.data.localdata.BookmarksRepository
import com.example.newsapp.ui.main.MainViewModelFactory

abstract class BaseActivity<binding : ViewDataBinding, viewModel : ViewModel, viewModelstoreOwner : ViewModelStoreOwner>() :
    AppCompatActivity() {

    abstract fun getViewBinding(): binding
    abstract fun getViewModel(): Class<viewModel>
    abstract fun getViewModelStoreOwner(): viewModelstoreOwner
    abstract fun getContext(): Context

    protected lateinit var binding: binding

    protected lateinit var viewModel: viewModel

    protected lateinit var prefrences: MutableLiveData<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        prefrences = MutableLiveData()
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        val database: BookmarksDatabase =
            BookmarksDatabase.getInstance(context = applicationContext)
        val dao = database.dao
        val repository = BookmarksRepository(dao)
        prefrences.postValue("List")
        val factory = MainViewModelFactory(repository, prefrences)
        viewModel = ViewModelProvider(getViewModelStoreOwner(), factory).get(getViewModel())
    }
}