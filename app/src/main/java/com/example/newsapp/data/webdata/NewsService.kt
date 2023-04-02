package com.example.newsapp.data.webdata

import com.example.newsapp.model.NewsItem
import retrofit2.Response
import retrofit2.http.GET

interface NewsService{

    @GET("/v2/top-headlines?country=in&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNews(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=business&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNewsOnBusiness(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=general&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNewsOnGeneral(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=health&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNewsOnHealth(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=technology&apiKey=")
    suspend fun getNewsOnTech(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=science&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNewsOnScience(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=entertainment&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNewsOnEntertainment(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=sports&apiKey=bdb1688209e3487cbdcfe76fad0c7dff")
    suspend fun getNewsOnSports(): Response<NewsItem>

}