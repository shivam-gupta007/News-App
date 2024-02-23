package com.shivamgupta.newsapp.presentation.search_news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shivamgupta.newsapp.databinding.LayoutPagerLoaderBinding

class PagerLoaderAdapter : LoadStateAdapter<PagerLoaderAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: LayoutPagerLoaderBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState){
            binding.progressBar.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(binding = LayoutPagerLoaderBinding.inflate(layoutInflater,parent,false))
    }
}