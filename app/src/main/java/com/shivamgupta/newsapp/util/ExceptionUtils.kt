package com.shivamgupta.newsapp.util

import android.content.Context
import com.shivamgupta.newsapp.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ExceptionUtils {
    fun getUiMessageOfException(throwable: Throwable, context: Context): String {
        return when (throwable) {
            is SocketTimeoutException -> context.getString(R.string.socket_timeout_error_msg)

            is IOException -> context.getString(R.string.io_exception_error_msg)

            is HttpException -> context.getString(R.string.http_exception_error_msg)

            is ApiException -> throwable.message.orEmpty()

            is EmptyResultException -> throwable.message.orEmpty()

            else -> context.getString(R.string.something_went_wrong_error)
        }
    }
}

class ApiException(errorMessage: String): Throwable(message = errorMessage)
class EmptyResultException(errorMessage: String): Throwable(message = errorMessage)