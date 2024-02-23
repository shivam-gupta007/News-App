package com.shivamgupta.newsapp.presentation.binding_adapters

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.shivamgupta.newsapp.R
import com.shivamgupta.newsapp.util.Constants
import com.shivamgupta.newsapp.util.DateUtils
import com.shivamgupta.newsapp.util.getDrawableResById
import com.shivamgupta.newsapp.util.truncateStringWithDots

@BindingAdapter("visible")
fun bindIsVisible(view: View, shouldShow: Boolean){
    view.isVisible = shouldShow
}

@BindingAdapter("maxLengthToShow","textValue")
fun bindHandleTextLength(textView: TextView, maxLengthToShow: Int, textValue: String) {
    textView.text = textValue.truncateStringWithDots(maxLengthToShow)
}

@BindingAdapter("formatAsDate")
fun bindFormatAsDate(textView: TextView, text: String?){
    if(!text.isNullOrEmpty()) {
        textView.text = DateUtils.getFormattedDate(
            currentFormat = Constants.API_DATE_FORMAT,
            desiredFormat = Constants.UI_DATE_FORMAT,
            dateText = text
        )
    }
}

@BindingAdapter("isRefreshing")
fun bindIsRefreshing(
    swipeToRefreshLayout: SwipeRefreshLayout,
    isLoading: Boolean,
){
    swipeToRefreshLayout.isRefreshing = isLoading
}

@BindingAdapter("toggleBookmarkIcon")
fun bindToggleBookmarkIcon(
    button: MaterialButton,
    isBookmarked: Boolean
){
    val filledBookmarkIcon = button.getDrawableResById(R.drawable.ic_fill_bookmark)
    val blankBookmarkIcon = button.getDrawableResById(R.drawable.ic_bookmark)
    if(isBookmarked) {
        button.icon = filledBookmarkIcon
        button.tag = Constants.BOOKMARKED_TAG
    } else {
        button.icon = blankBookmarkIcon
        button.tag = Constants.UN_BOOKMARKED_TAG
    }
}