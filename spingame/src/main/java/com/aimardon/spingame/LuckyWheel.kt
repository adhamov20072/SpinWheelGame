package com.aimardon.spingame

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.AttrRes
import kotlin.math.abs


class LuckyWheel : FrameLayout, OnTouchListener, OnRotationListener {
    private var wheelView: WheelView? = null
    private var arrow: ImageView? = null
    private var animationTime: Int? = null
    private var target = -1
    private var isRotate = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initComponent()
        applyAttribute(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initComponent()
        applyAttribute(attrs)
    }

    private fun initComponent() {
        inflate(context, R.layout.lucky_wheel_layout, this)
        setOnTouchListener(this)
        wheelView = findViewById(R.id.wv_main_wheel)
        wheelView?.setOnRotationListener(this)
        arrow = findViewById(R.id.iv_arrow)
    }


    fun addWheelItems(wheelItems: MutableList<WheelItem?>) {
        wheelView?.addWheelItems(wheelItems)
    }

    fun applyAttribute(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LuckyWheel, 0, 0)
        try {
            val backgroundColor =
                typedArray.getColor(R.styleable.LuckyWheel_background_color, Color.GREEN)
            val arrowImage =
                typedArray.getResourceId(R.styleable.LuckyWheel_arrow_image, R.drawable.arrow)
            val imagePadding =
                typedArray.getDimensionPixelSize(R.styleable.LuckyWheel_image_padding, 0)
            wheelView?.setWheelBackgroundWheel(backgroundColor)
            wheelView?.setItemsImagePadding(imagePadding)
            arrow!!.setImageResource(arrowImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        typedArray.recycle()
    }


    fun setLuckyWheelReachTheTarget(onLuckyWheelReachTheTarget: OnLuckyWheelReachTheTarget?) {
        wheelView?.setWheelListener(onLuckyWheelReachTheTarget)
    }
    fun setTarget(target: Int): Int {
        this.target = target
        return target
    }
    fun rotateWheelTo(number: Int,animationTime:Int) {
        isRotate = true
        return wheelView!!.resetRotationLocationToZeroAngle(number,animationTime)
    }

    val SWIPE_DISTANCE_THRESHOLD = 100
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    var dx = 0f
    var dy = 0f
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (target < 0 || isRotate) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                y2 = event.y
                dx = x2 - x1
                dy = y2 - y1
                if (abs(dx) > abs(dy)) {
                    if (dx < 0 && abs(dx) > SWIPE_DISTANCE_THRESHOLD) animationTime?.let { rotateWheelTo(target,it) }
                } else {
                    if (dy > 0 && abs(dy) > SWIPE_DISTANCE_THRESHOLD) animationTime?.let { rotateWheelTo(target, animationTime = it) }
                }
            }
            else -> return true
        }
        return true
    }

    override fun onFinishRotation() {
        isRotate = false
    }
}