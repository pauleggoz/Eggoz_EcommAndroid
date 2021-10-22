package com.eggoz.ecommerce.utils

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Validation {
    companion object {
        fun emptyField(data: TextInputEditText, error: TextInputLayout, message: String): Boolean {
            if (data.text.toString().isEmpty()) {
                error.error = message
                return false
            } else {
                error.isErrorEnabled = false
                return true
            }

        }

        fun FieldWithLength(
            data: TextInputEditText,
            error: TextInputLayout,
            message: String,
            length: Int
        ): Boolean {
            if (data.text.toString().isEmpty()) {
                emptyField(data, error, "Input Field can't be Empty")
                return false
            } else if (data.text.toString().length < length) {
                error.error = message
                return false
            } else {
                error.isErrorEnabled = false
                return true
            }
        }

        fun emailField(
            data: TextInputEditText,
            error: TextInputLayout,
            emptymessage: String,
            message: String
        ): Boolean {
            if (data.text.toString().isEmpty()) {
                error.error = emptymessage
                return false
            } else {
                error.isErrorEnabled = false
                return if (android.util.Patterns.EMAIL_ADDRESS.matcher(data.text.toString()).matches()) {

                    error.isErrorEnabled = false
                    true
                } else {
                    error.error = message
                    false
                }
            }

        }


    }
}
