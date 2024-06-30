package com.example.hobbyapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hobbyapp.databinding.NewsListCardBinding
import com.example.hobbyapp.model.News
import com.example.hobbyapp.util.loadImage

class  NewsListAdapter(private val newsList:ArrayList<News>):RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {
    class NewsViewHolder(var binding: NewsListCardBinding): RecyclerView.ViewHolder(binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsListCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        val categoryAdapter = CategoryAdapter(news.category)
        with(holder.binding){
            textTitleCard.text = news.title
            textAuthorCard.text = "@ ${news.author}"
            textPreview.text = news.preview
            imageViewNewsCard.loadImage(news.imgUrl, progressImageCard)

            val layoutManager = LinearLayoutManager(rvCategory.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvCategory.layoutManager = layoutManager
            rvCategory.adapter = categoryAdapter
            buttonRead.setOnClickListener {
                val action = NewsListFragmentDirections.actionNewsDetail(news.id)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNewsList(newestNewsList: List<News>){
        newsList.clear()
        newsList.addAll(newestNewsList)
        notifyDataSetChanged()
    }
}