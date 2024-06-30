package com.example.hobbyapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hobbyapp.global.Global
import com.example.hobbyapp.model.News
import com.example.hobbyapp.model.local.NewsDao
import com.example.hobbyapp.model.local.NewsEntity
import com.example.hobbyapp.util.Mapper.listMapper
import com.example.hobbyapp.util.Mapper.mapper
import com.example.hobbyapp.util.Mapper.mapperRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch


class NewsViewModel(application: Application, private val newsDao: NewsDao):AndroidViewModel(application) {
    val newsLD = MutableLiveData<News>()
    private val newsTemp = MutableLiveData<NewsEntity?>()
    val listNewsLD = MutableLiveData<List<News>>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null
    fun refresh(){
        queue = Volley.newRequestQueue(getApplication())
        val url = "${Global.baseUrl}/getallnews.php"
        loadingLD.value = true
        val stringRequest = StringRequest(
            Request.Method.GET, url, {
                val sType = object : TypeToken<List<News>>(){}.type
                val result = Gson().fromJson<List<News>>(it,sType)
                Log.d("news", it.toString())

                result.forEach{insertNews(it)}
                loadingLD.value = false

            },{
                loadingLD.value = false
                Log.d("students", it.toString())
            }
        )
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    fun getNews(id:Int){
        getDetailNews(id)
        if (newsTemp.value == null){
            queue = Volley.newRequestQueue(getApplication())
            val url = "${Global.baseUrl}/getnews.php?id=$id"
            val stringRequest = StringRequest(
                Request.Method.GET, url, {
                Log.d("news", it.toString())
                val result = Gson().fromJson(it, News::class.java)
                newsLD.value = result
            },{
                Log.d("news", it.toString())
            }
        )
            stringRequest.tag = TAG
            queue?.add(stringRequest)
        } else {
            newsLD.value = newsTemp.value!!.mapper()
        }

    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }

    private fun getLocalAllNews(){
        viewModelScope.launch {
            val news = newsDao.getAllNews()
            listNewsLD.value = news.listMapper()
        }

    }

    private fun getDetailNews(id: Int){
        viewModelScope.launch {
            val news = newsDao.getNewsById(id)
            newsTemp.value = news
        }

    }

    private fun insertNews(news: News) {
        viewModelScope.launch {
            newsDao.insert(news.mapperRequest())
        }

        getLocalAllNews();
    }
}