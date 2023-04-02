package com.example.newsapp.ui.main

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.example.newsapp.R
import com.example.newsapp.data.localdata.Bookmarks
import com.example.newsapp.data.localdata.BookmarksRepository
import com.example.newsapp.model.NewsItem
import com.example.newsapp.data.repo.ArticleProvider
import com.example.newsapp.model.Article
import com.example.newsapp.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel(
    private val repository: BookmarksRepository,
    private val prefrences: MutableLiveData<String>
) : ViewModel(), Observable {
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    @Bindable
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val category: MutableLiveData<String> = MutableLiveData("Top Headlines")
    private val message : MutableLiveData<Event<String>> = MutableLiveData()

    val errorMessage
    get() = message

    var responseLiveData: MutableLiveData<Response<NewsItem>?> = MutableLiveData()

    var bookmarkList: LiveData<List<Bookmarks>> = repository.bookmarks

    val appPrefrences: MutableLiveData<String>
    get() = prefrences




    fun refreshResponse() {

        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = ArticleProvider().getNews(category.value!!)
                if (response.isSuccessful) {
                    responseLiveData.postValue(response)
                }
                else {
                    responseLiveData.postValue(null)
                    message.value = Event(response.errorBody().toString())
                }
            }catch (e: Exception){
                responseLiveData.postValue(null)
                message.value = Event(e.toString())
            }

            isLoading.value = false
        }

    }

    fun changeViewType(id: Int) {

        when (id) {
            R.id.radio_button1 -> {
                viewModelScope.launch {
                    prefrences.postValue("List")
                }
            }
            R.id.radio_button2 -> {
                viewModelScope.launch {
                    prefrences.postValue("Tab")
                }
            }
        }

        refreshResponse()
    }

    fun addABookmark(id: Int = 0, article: Article) {

        viewModelScope.launch {
            val bookMark = Bookmarks(id, article)
            repository.insertArticleIntoBookmarks(bookMark)
        }

    }

    fun deleteABookmark(bookmark: Bookmarks) {
        viewModelScope.launch {
            repository.deleteArticlefromBookmarks(bookmark)
        }
    }

    fun clearAllBookmarks() {
        viewModelScope.launch {
            repository.deleteALlArticleFromBookmarks()
        }
    }

}