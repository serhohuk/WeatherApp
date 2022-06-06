package com.serhohuk.weatherapp.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun showKeyboard(theView: View) {
        val context = theView.context
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(theView, 0)
    }


    fun hideKeyboard(theView: View) {
        val context = theView.context
        val service = context.getSystemService(Context.INPUT_METHOD_SERVICE)
        val imm = service as InputMethodManager
        imm?.hideSoftInputFromWindow(theView.windowToken, 0)
    }

    fun hideKeyboard(context: Context?) {
        // close keyboard, use activity context, not application context
        if (context == null) return
        val view = (context as Activity).window.currentFocus
        if (view != null) {
            KeyboardUtils.hideKeyboard(view)
        }
    }
}
