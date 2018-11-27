package com.ukyoo.v2client.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TabWidget
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.ukyoo.v2client.R


class BadgeView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) :
    AppCompatTextView(mContext, attrs, defStyle) {

    /**
     * @return Returns true if view is hidden on badge value 0 or null;
     */
    /**
     * @param hideOnNull the hideOnNull to set
     */
    var isHideOnNull = true
        set(hideOnNull) {
            field = hideOnNull
            text = text
        }

    val badgeCount: Int?
        get() {
            if (text == null) {
                return null
            }

            val text = text.toString()
            try {
                return Integer.parseInt(text)
            } catch (e: NumberFormatException) {
                return null
            }

        }

    var badgeGravity: Int
        get() {
            val params = layoutParams as FrameLayout.LayoutParams
            return params.gravity
        }
        set(gravity) {
            val params = layoutParams as FrameLayout.LayoutParams
            params.gravity = gravity
            layoutParams = params
        }

    val badgeMargin: IntArray
        get() {
            val params = layoutParams as FrameLayout.LayoutParams
            return intArrayOf(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin)
        }

    init {

        init()
    }

    private fun init() {
        if (layoutParams !is FrameLayout.LayoutParams) {
            val layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT or Gravity.TOP
            )
            setLayoutParams(layoutParams)
        }

        // set default font
        setTextColor(Color.WHITE)
        typeface = Typeface.DEFAULT_BOLD
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setPadding(dip2Px(5f), dip2Px(1f), dip2Px(5f), dip2Px(1f))

        // set default background
        setBackground(9, ContextCompat.getColor(mContext, R.color.colorAccent))

        gravity = Gravity.CENTER

        // default values
        isHideOnNull = true
        setBadgeCount(0)
    }

    fun setBackground(dipRadius: Int, badgeColor: Int) {
        val radius = dip2Px(dipRadius.toFloat())
        val radiusArray = floatArrayOf(
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat()
        )

        val roundRect = RoundRectShape(radiusArray, null, null)
        val bgDrawable = ShapeDrawable(roundRect)
        bgDrawable.paint.color = badgeColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = bgDrawable
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.TextView#setText(java.lang.CharSequence, android.widget.TextView.BufferType)
     */
    override fun setText(text: CharSequence?, type: TextView.BufferType) {
        if (isHideOnNull && (text == null || text.toString().equals("0", ignoreCase = true))) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
        }
        super.setText(text, type)
    }

    fun setBadgeCount(count: Int) {
        text = count.toString()
    }

    fun setBadgeMargin(dipMargin: Int) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin)
    }

    fun setBadgeMargin(leftDipMargin: Int, topDipMargin: Int, rightDipMargin: Int, bottomDipMargin: Int) {
        val params = layoutParams as FrameLayout.LayoutParams
        params.leftMargin = dip2Px(leftDipMargin.toFloat())
        params.topMargin = dip2Px(topDipMargin.toFloat())
        params.rightMargin = dip2Px(rightDipMargin.toFloat())
        params.bottomMargin = dip2Px(bottomDipMargin.toFloat())
        layoutParams = params
    }

    fun incrementBadgeCount(increment: Int) {
        val count = badgeCount
        if (count == null) {
            setBadgeCount(increment)
        } else {
            setBadgeCount(increment + count)
        }
    }

    fun decrementBadgeCount(decrement: Int) {
        incrementBadgeCount(-decrement)
    }

    /*
     * Attach the BadgeView to the TabWidget
     *
     * @param target the TabWidget to attach the BadgeView
     *
     * @param tabIndex index of the tab
     */
    fun setTargetView(target: TabWidget, tabIndex: Int) {
        val tabView = target.getChildTabViewAt(tabIndex)
        setTargetView(tabView)
    }

    /*
     * Attach the BadgeView to the target view
     *
     * @param target the view to attach the BadgeView
     */
    fun setTargetView(target: View?) {
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }

        if (target == null) {
            return
        }

        if (target.parent is FrameLayout) {
            (target.parent as FrameLayout).addView(this)

        } else if (target.parent is ViewGroup) {
            // use a new Framelayout container for adding badge
            val parentContainer = target.parent as ViewGroup
            val groupIndex = parentContainer.indexOfChild(target)
            parentContainer.removeView(target)

            val badgeContainer = FrameLayout(context)
            val parentLayoutParams = target.layoutParams

            badgeContainer.layoutParams = parentLayoutParams
            target.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams)
            badgeContainer.addView(target)

            badgeContainer.addView(this)
        } else if (target.parent == null) {
            Log.e(javaClass.simpleName, "ParentView is needed")
        }

    }

    /*
     * converts dip to px
     */
    private fun dip2Px(dip: Float): Int {
        return (dip * context.resources.displayMetrics.density + 0.5f).toInt()
    }
}
