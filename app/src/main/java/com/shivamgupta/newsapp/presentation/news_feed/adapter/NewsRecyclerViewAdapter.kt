package com.shivamgupta.newsapp.presentation.news_feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.LayoutEmptyDataBinding
import com.shivamgupta.newsapp.databinding.LayoutNewsCardBinding
import com.shivamgupta.newsapp.domain.models.News

class NewsRecyclerViewAdapter(
    private val news: List<News>,
    private val onNewsClicked: (News) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    inner class NewsListViewHolder(
        private val binding: LayoutNewsCardBinding
    ) : ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                absoluteAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    onNewsClicked(news[position])
                }
            }
        }

        fun bind(news: News) {
            binding.newsItem = news
            binding.executePendingBindings()
        }
    }

    inner class NoDataViewHolder(
        private val binding: LayoutEmptyDataBinding
    ) : ViewHolder(binding.root) {
        fun bind(messageText: String) {
            binding.message = messageText
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            NEWS_DATA_VIEW_TYPE -> {
                NewsListViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_news_card,
                        parent, false
                    )
                )
            }

            NO_DATA_VIEW_TYPE -> {
                NoDataViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.layout_empty_data,
                        parent, false
                    )
                )
            }

            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun getItemCount(): Int {
        return if (news.isEmpty()) 1 else news.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (news.isEmpty()) NO_DATA_VIEW_TYPE else NEWS_DATA_VIEW_TYPE
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is NewsListViewHolder -> {
                holder.bind(news[position])
            }
            is NoDataViewHolder -> {
                holder.bind(messageText = "No news found")
            }
        }
    }

    companion object {
        private const val NO_DATA_VIEW_TYPE = 0
        private const val NEWS_DATA_VIEW_TYPE = 1
    }

}


