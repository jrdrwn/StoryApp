package com.dicoding.picodiploma.loginwithanimation.customview

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
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

        if (inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS + 1) {
            validateEmail(text)
        } else if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD + 1) {
            validatePassword(text)
        }
    }

    private fun validateEmail(text: CharSequence?) {
        if (!text.toString().contains("@") || !text.toString().contains(".")) {
            setError("Email tidak valid", null)
        } else {
            error = null
        }
    }

    private fun validatePassword(text: CharSequence?) {
        if (text.toString().length < 8) {
            setError("Password tidak boleh kurang dari 8 karakter", null)
        } else {
            error = null
        }
    }
}