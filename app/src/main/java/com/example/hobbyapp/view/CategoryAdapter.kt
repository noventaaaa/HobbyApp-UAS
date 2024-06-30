package com.example.hobbyapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hobbyapp.databinding.ItemCategoryBinding

class  CategoryAdapter(private val categories:List<String>?):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(var binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories?.get(position)
        with(holder.binding){
            tvCategory.text = category
        }
    }

    override fun getItemCount(): Int = categories?.size ?: 0

}