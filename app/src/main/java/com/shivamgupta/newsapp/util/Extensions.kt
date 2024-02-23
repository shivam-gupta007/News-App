package com.shivamgupta.newsapp.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun showSnackBar(
    message: String, rootLayout: View, length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(
        rootLayout, message, length
    ).show()
}

fun View.showKeyboard() {
    if (this.requestFocus()) {
        val inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideKeyboard() {
    if (this.requestFocus()) {
        val inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken,0)
    }
}

fun String.truncateStringWithDots(
    maxLengthToShow: Int = Constants.VISIBLE_TEXT_LENGTH
): String {
    return if (this.length > maxLengthToShow) {
        this.substring(startIndex = 0, endIndex = maxLengthToShow).plus("...")
    } else {
        this
    }
}

var TextInputLayout.editTextValue: String
    set(value) {
        editText?.setText(value)
    } get() {
        return editText?.text?.toString().orEmpty()
    }

fun TextInputLayout.afterTextChanged(afterTextChanged: (String) -> Unit) {
    editText?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged(editable?.toString() ?: "")
        }
    })
}

fun Fragment.toast(
    text: String,
    length: Int = Toast.LENGTH_SHORT
) {
    activity?.let {
        Toast.makeText(it, text, length).show()
    }
}

fun View.getDrawableResById(
    @DrawableRes resId: Int
): Drawable? {
    return ResourcesCompat.getDrawable(
        resources,
        resId,
        null
    )
}

fun View.getColorResById(
    @ColorRes resId: Int
): Int {
    return ResourcesCompat.getColor(
        resources,
        resId,
        null
    )
}