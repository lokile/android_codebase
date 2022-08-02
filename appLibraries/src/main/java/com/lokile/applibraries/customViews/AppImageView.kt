package com.lokile.applibraries.customViews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.lokile.applibraries.R
import com.lokile.applibraries.utils.changeColor
import com.lokile.applibraries.utils.getColorRessource

class AppImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet? = null) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(
                attrs, R.styleable.AppImageView, 0, 0
            )
            try {
                changeColor(
                    context, ta.getColor(
                        R.styleable.AppImageView_initColor,
                        context.getColorRessource(R.color.transparent)
                    ), false
                )
            } finally {
                ta.recycle()
            }
        }
    }
}