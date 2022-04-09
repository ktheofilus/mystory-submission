package com.example.myapplication.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.example.myapplication.R

class EmailET : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (s.toString().isEmpty()) error = resources.getString(R.string.error_empty)
                else if (s.toString().isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) error = resources.getString(R.string.error_invalidEmail)
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}