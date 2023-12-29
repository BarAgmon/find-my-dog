package com.idz.lecture4_demo3

import android.content.Context
import java.util.regex.Pattern

object CredValidation {
    fun isRegisteredEmailValid(context: Context, userEmail: String): Boolean {
        if (userEmail.isEmpty()) {
            Utils.showToast(context, "Email is required")
            return false
        } else if (!Pattern.compile("^(.+)@(\\S+)$").matcher(userEmail).matches()) {
            Utils.showToast(context, "Bad email type")
            return false
        }

        return true
    }

    fun isNewPasswordValid(context: Context, password: String, confirmPassword: String): Boolean {
        if (password != "") {
            if (password == confirmPassword) {
                if (Pattern.compile("^.{0,5}$").matcher(password).matches()) {
                    Utils.showToast(context, "password is too short (at least 6)")
                    return false
                }
            } else {
                Utils.showToast(context, "Password and Confirm password are different")
                return false
            }
        } else {
            Utils.showToast(context, "Password is empty")
            return false
        }

        return true
    }

    fun isLoginCredValid(context: Context, userEmail: String, password: String): Boolean {
        if (userEmail == "" || password == "") {
            Utils.showToast(context, "email and password are required")
            return false
        } else if (!Pattern.compile("^(.+)@(\\S+)$").matcher(userEmail).matches()) {
            Utils.showToast(context, "not an email type")
            return false
        } else if (Pattern.compile("^.{0,5}$").matcher(password).matches()) {
            Utils.showToast(context, "password is too short (at least 6)")
            return false
        }

        return true
    }
}