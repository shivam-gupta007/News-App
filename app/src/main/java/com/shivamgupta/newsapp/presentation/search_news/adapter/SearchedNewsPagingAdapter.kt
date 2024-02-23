package com.shivamgupta.newsapp.presentation.search_news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.LayoutNewsCardBinding
import com.shivamgupta.newsapp.domain.models.News

class SearchedNewsPagingAdapter(
    private val onNewsClicked: (News) -> Unit
) : PagingDataAdapter<News, SearchedNewsPagingAdapter.SearchedNewsViewHolder>(COMPARATOR){
    inner class SearchedNewsViewHolder(
        private val binding: LayoutNewsCardBinding
    ): ViewHolder(binding.root){
        fun bind(newsItem: News){
            binding.newsItem = newsItem
            binding.root.setOnClickListener {
                onNewsClicked(newsItem)
            }
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(
        holder: SearchedNewsViewHolder,
        position: Int
    ) {
        getItem(position)?.let { news ->
            holder.bind(news)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchedNewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchedNewsViewHolder(
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.layout_news_card,
                parent,
                false
            )
        )
    }

    companion object {
        val COMPARATOR = object: DiffUtil.ItemCallback<News>(){
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }

        }
    }
}