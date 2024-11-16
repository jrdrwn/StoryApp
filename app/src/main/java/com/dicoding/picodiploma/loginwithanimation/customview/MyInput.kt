package com.dicoding.picodiploma.loginwithanimation.customview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.material.textfield.TextInputEditText

class MyInput @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        when (inputType) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + 1 -> {
                validateEmail(text)
            }

            InputType.TYPE_TEXT_VARIATION_PASSWORD + 1 -> {
                validatePassword(text)
            }

            InputType.TYPE_CLASS_TEXT -> {
                validateText(text)
            }
        }
    }

    private fun validateText(text: CharSequence?) {
        if (text.toString().isEmpty()) {
            setError(context.getString(R.string.name_validation_msg), null)
        } else {
            error = null
        }
    }

    private fun validateEmail(text: CharSequence?) {
        if (!text.toString().contains("@") || !text.toString().contains(".")) {
            setError(context.getString(R.string.email_validation_msg), null)
        } else {
            error = null
        }
    }

    private fun validatePassword(text: CharSequence?) {
        if (text.toString().length < 8) {
            setError(context.getString(R.string.pass_validation_msg), null)
        } else {
            error = null
        }
    }
}