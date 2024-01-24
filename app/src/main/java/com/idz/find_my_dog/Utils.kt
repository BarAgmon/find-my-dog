package com.idz.find_my_dog

import android.content.Context
import android.widget.Toast
import java.util.UUID

object Utils {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun getUniqueID(): String {
        return UUID.randomUUID().toString()
    }
}
