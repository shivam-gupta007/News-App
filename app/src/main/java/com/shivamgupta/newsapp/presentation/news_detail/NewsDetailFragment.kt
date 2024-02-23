package com.shivamgupta.newsapp.presentation.news_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.databinding.FragmentNewsDetailBinding
import com.shivamgupta.newsapp.domain.models.News
import com.shivamgupta.newsapp.presentation.bookmark_news.BookmarkNewsViewModel
import com.shivamgupta.newsapp.util.ChromeCustomTabsUtil
import com.shivamgupta.newsapp.util.Constants
import com.shivamgupta.newsapp.util.getDrawableResById
import com.shivamgupta.newsapp.util.toast
import kotlinx.coroutines.launch


class NewsDetailFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailBinding
    private val args: NewsDetailFragmentArgs by navArgs()
    private val bookmarkNewsViewModel by activityViewModels<BookmarkNewsViewModel>()
    private val viewModel by activityViewModels<NewsDetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_news_detail,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

     private fun setupViews() {
        viewModel.clearUiState()
        if(args.navigateFromBookmarkedScreen){
            viewModel.updateNewsDetailState(args.newsItem)
        } else {
            viewModel.mapBookmarkStatusOfNewsByTitle(args.newsItem)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collect { currentUiState ->
                currentUiState.newsDetail?.let {
                    binding.newsDetailItem = it
                    setupViewArticleAction(newsDetails = it)
                    setupBookmarkNewsAction(newsDetails = it)
                    setupShareNewsAction(newsDetails = it)
                }
            }
        }
    }

    private fun setupShareNewsAction(newsDetails: News) {
        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getString(R.string.news_sharing_message,newsDetails.title,newsDetails.articleUrl))
            }

            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_news)))
        }
    }

    private fun setupBookmarkNewsAction(newsDetails: News) {
        binding.bookmarkButton.apply {
            val filledBookmarkIcon = getDrawableResById(R.drawable.ic_fill_bookmark)
            val blankBookmarkIcon = getDrawableResById(R.drawable.ic_bookmark)

            setOnClickListener {
                if (tag == Constants.BOOKMARKED_TAG) {
                    icon = blankBookmarkIcon
                    tag = Constants.UN_BOOKMARKED_TAG
                } else {
                    icon = filledBookmarkIcon
                    tag = Constants.BOOKMARKED_TAG
                }

                val isBookmarked = tag == Constants.BOOKMARKED_TAG
                handleNewsBookmark(isBookmarked, newsDetails)
            }
        }
    }

    private fun handleNewsBookmark(isBookmarked: Boolean, newsDetails: News) {
        val bookmarkStatus = getString(
            R.string.news_bookmark_status,
            if (isBookmarked) "saved to Bookmarks" else "removed from Bookmarks"
        )
        toast(text = bookmarkStatus)

        if(isBookmarked){
            bookmarkNewsViewModel.addNews(newsDetails.copy(isBookmarked = true))
        } else {
            newsDetails.id?.let { newsId ->
                bookmarkNewsViewModel.deleteBookmarkedNews(newsId)
            }
        }

        viewModel.updateBookmarkStatusState(isBookmarked)
    }

    private fun setupViewArticleAction(newsDetails: News) {
        binding.viewFullArticleButton.setOnClickListener {
            if(newsDetails.isArticleUrlValid()){
                context?.let {
                    ChromeCustomTabsUtil.openWebLinkInChromeTab(
                        context = it,
                        url = newsDetails.articleUrl
                    )
                }
            }
        }
    }
}