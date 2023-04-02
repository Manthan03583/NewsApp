package com.example.newsapp.ui.articledetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.localdata.BookmarksDatabase
import com.example.newsapp.data.localdata.BookmarksRepository
import com.example.newsapp.databinding.FragmentArticlesBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.adapter.NewsListAdapter
import com.example.newsapp.ui.adapter.NewsTabAdapter
import com.example.newsapp.ui.main.MainActivity
import com.example.newsapp.ui.main.MainViewModel
import com.example.newsapp.ui.main.MainViewModelFactory
import com.example.newsapp.utils.HidingViewScrollListener
import com.example.newsapp.utils.ObjectSerializer


class ArticlesFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentArticlesBinding
    private lateinit var newsListAdapter: NewsListAdapter
    private lateinit var newsTabAdapter: NewsTabAdapter
    private lateinit var preferences: MutableLiveData<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        preferences= MutableLiveData()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false)

        val database: BookmarksDatabase = BookmarksDatabase.getInstance(context = requireContext())
        val dao = database.dao
        val repository = BookmarksRepository(dao)

        preferences.postValue("List")

        val factory = MainViewModelFactory(repository, preferences)
        viewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)


        binding.viewModel = viewModel


        defineViews()

        return binding.root
    }

    fun defineViews() {

        initAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.addOnScrollListener(object : HidingViewScrollListener() {
            override fun onHide() {
                MainActivity.toolbar.visibility = View.GONE
            }

            override fun onShow() {
                MainActivity.toolbar.visibility = View.VISIBLE
            }
        })


        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (!isLoading) {
                if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    false


                binding.recyclerView.visibility = View.VISIBLE


                val context = binding.recyclerView.context
                val layoutAnimationController =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_down_to_up)
                binding.recyclerView.layoutAnimation = layoutAnimationController
                displayArticles()

            } else {
                if (!binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    true
                binding.recyclerView.visibility = View.INVISIBLE
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshResponse()
        }

    }


    private fun displayArticles() {

        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val articles = it.body()?.articles
                if (articles != null) {

                    if (binding.recyclerView.adapter is NewsTabAdapter)
                        newsTabAdapter.setArticleList(articles)
                    else
                        newsListAdapter.setArticleList(articles)

                }
            }
        })
    }

    private fun goToArticleDetailActivity(article: Article, imageView: ImageView) {
        val intent = Intent(requireContext(), ArticleDetailsActivity::class.java)
        intent.putExtra("article", ObjectSerializer.serialize(article))
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            imageView,
            "article_image"
        )
        intent.putExtra("fab_visiblity", View.VISIBLE)
        startActivity(intent, activityOptions.toBundle())
    }


    private fun deleteBookmark(article: Article) {

        val bookmarks = viewModel.bookmarkList.value?.get(
            viewModel.responseLiveData.value?.body()!!.articles.indexOf(article)
        )
        if (bookmarks != null) {
            viewModel.deleteABookmark(bookmarks)
        }
    }


    private fun initAdapter(){

        newsListAdapter =   NewsListAdapter({ article: Article, imageView: ImageView ->
            goToArticleDetailActivity(article, imageView)
        }
            , {

            })

        newsTabAdapter =        NewsTabAdapter({ article: Article, imageView: ImageView ->
            goToArticleDetailActivity(article, imageView)
        }
            , { article ->

            })



        viewModel.appPrefrences.observe(viewLifecycleOwner, Observer {viewType ->
            if (viewType.equals("List"))
                binding.recyclerView.adapter = newsListAdapter
            else
                binding.recyclerView.adapter = newsTabAdapter

        })

    }


}

