package com.idz.find_my_dog

import android.content.Context
import android.widget.Toast

object Utils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
