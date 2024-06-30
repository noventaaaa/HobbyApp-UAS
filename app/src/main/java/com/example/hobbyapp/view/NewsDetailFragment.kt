package com.example.hobbyapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hobbyapp.R
import com.example.hobbyapp.databinding.FragmentNewsDetailBinding
import com.example.hobbyapp.global.DatabaseBuilder
import com.example.hobbyapp.global.NewsClickHandler
import com.example.hobbyapp.util.loadImage
import com.example.hobbyapp.viewmodel.NewsViewModel
import com.example.hobbyapp.viewmodel.NewsViewModelFactory

class NewsDetailFragment : Fragment(), NewsClickHandler {
    private lateinit var binding:FragmentNewsDetailBinding
    private lateinit var viewModel: NewsViewModel
    private var currentPage = 0
    private var maxPage = 0

    private var content: List<String> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail,container,false)
        binding.clickHandler = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsDao = DatabaseBuilder.getInstance(this.requireActivity().applicationContext).newsDao()
        val viewModelFactory = NewsViewModelFactory(this.requireActivity().application, newsDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        val newsId = NewsDetailFragmentArgs.fromBundle(requireArguments()).newsid
        viewModel.getNews(newsId)

        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            if(it!=null){
                Log.d("get news", it.toString())
                binding.imageViewNewsCard.loadImage(it.imgUrl,binding.progressImageCard)
                binding.news = it
//                binding.textTitleCard.text = it.title
//                binding.textAuthorCard.text = it.author
                val paragrapPerPage = 3
                content = splitNewsIntoPages(it.content!!, paragrapPerPage)
                maxPage = content.size

                currentPage = 1
                binding.textPage.text = "$currentPage/$maxPage"
                binding.buttonPrev.isEnabled = false;
                if (maxPage == 1){
                    binding.buttonNext.isEnabled = false
                }
                binding.textContent.text = content[currentPage - 1]
            }
        })

        binding.buttonPrev.setOnClickListener {
            currentPage -= 1
            if (currentPage == 1){
                binding.buttonPrev.isEnabled = false
            }
            if (currentPage < maxPage){
                binding.buttonNext.isEnabled = true
            }
            binding.textContent.text = content[currentPage - 1]
            binding.textPage.text = "$currentPage/$maxPage"
        }
    }

    private fun splitNewsIntoPages(news: String, paragraphsPerPage: Int): List<String> {
        val paragraphs = news.trim().split("\r\n\r\n")
        val numPages = paragraphs.size / paragraphsPerPage + if (paragraphs.size % paragraphsPerPage != 0) 1 else 0

        val pages = arrayListOf<String>()
        for (i in 0 until numPages) {
            val startIdx = i * paragraphsPerPage
            val endIdx = minOf((i + 1) * paragraphsPerPage, paragraphs.size)
            val pageParagraphs = paragraphs.subList(startIdx, endIdx)
            pages.add(pageParagraphs.joinToString("\r\n\r\n"))
        }

        return pages
    }

    override fun onNewsClick(view: View) {
        currentPage += 1
        if (currentPage == maxPage){

            binding.buttonNext.isEnabled = false
        }
        if (currentPage > 1){
            binding.buttonPrev.isEnabled = true
        }
        binding.textContent.text = content[currentPage - 1]
        binding.textPage.text = "$currentPage/$maxPage"
    }
}