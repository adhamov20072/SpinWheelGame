package com.aimardon.spingame

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

internal class WheelView : View {
    private var range = RectF()
    private var archPaint: Paint? = null
    private var textPaint: Paint? = null
    private var padding = 0
    private var radius = 0
    private var center = 0
    private var mWheelBackground = 0
    private var mImagePadding = 0
    private var mWheelItems: List<WheelItem>? = null
    private var mOnLuckyWheelReachTheTarget: OnLuckyWheelReachTheTarget? = null
    private var onRotationListener: OnRotationListener? = null
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    private fun initComponents() {
        //arc paint object
        archPaint = Paint()
        archPaint!!.isAntiAlias = true
        archPaint!!.isDither = true
        //text paint object
        textPaint = Paint()
        textPaint!!.color = Color.WHITE
        textPaint!!.isAntiAlias = true
        textPaint!!.isDither = true
        textPaint!!.textSize = 30f
        //rect rang of the arc
        range = RectF(
            padding.toFloat(),
            padding.toFloat(),
            (padding + radius).toFloat(),
            (padding + radius).toFloat()
        )
    }
    private fun getAngleOfIndexTarget(target: Int): Float {
        return (360 / mWheelItems!!.size * target).toFloat()
    }
    fun setWheelBackgroundWheel(wheelBackground: Int) {
        mWheelBackground = wheelBackground
        invalidate()
    }
    fun setItemsImagePadding(imagePadding: Int) {
        mImagePadding = imagePadding
        invalidate()
    }
    fun setWheelListener(onLuckyWheelReachTheTarget: OnLuckyWheelReachTheTarget?) {
        mOnLuckyWheelReachTheTarget = onLuckyWheelReachTheTarget
    }
    fun addWheelItems(wheelItems: List<WheelItem?>?) {
        mWheelItems = wheelItems as List<WheelItem>?
        invalidate()
    }
    private fun drawWheelBackground(canvas: Canvas) {
        val backgroundPainter = Paint()
        backgroundPainter.isAntiAlias = true
        backgroundPainter.isDither = true
        backgroundPainter.color = mWheelBackground
        canvas.drawCircle(center.toFloat(), center.toFloat(), center.toFloat(), backgroundPainter)
    }
    private fun drawImage(canvas: Canvas, tempAngle: Float, bitmap: Bitmap) {
        //get every arc img width and angle
        val imgWidth = radius / mWheelItems!!.size - mImagePadding
        val angle = ((tempAngle + 360 / mWheelItems!!.size / 2) * Math.PI / 180).toFloat()
        //calculate x and y
        val x = (center + radius / 2 / 2 * cos(angle.toDouble())).toInt()
        val y = (center + radius / 2 / 2 * sin(angle.toDouble())).toInt()
        //create arc to draw
        val rect = Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2)
        //rotate main bitmap
        val px = rect.exactCenterX()
        val py = rect.exactCenterY()
        val matrix = Matrix()
        matrix.postTranslate((-bitmap.width / 2).toFloat(), (-bitmap.height / 2).toFloat())
        matrix.postRotate(tempAngle + 120)
        matrix.postTranslate(px, py)
        canvas.drawBitmap(
            bitmap,
            matrix,
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        )}
    private fun drawText(canvas: Canvas, tempAngle: Float, sweepAngle: Float, text: String) {
        val path = Path()
        path.addArc(range, tempAngle, sweepAngle)
        val textWidth = textPaint!!.measureText(text)
        val hOffset = (radius * Math.PI / mWheelItems!!.size / 2 - textWidth / 2).toInt()
        val vOffset = radius / 2 / 3 - 3
        canvas.drawTextOnPath(text, path, hOffset.toFloat(), vOffset.toFloat(), textPaint!!)
    }
    fun rotateWheelToTarget(target: Int,animationTime:Int) {
        val wheelItemCenter = 270 - getAngleOfIndexTarget(target) + 360 / mWheelItems!!.size / 2
        animate().setInterpolator(DecelerateInterpolator())
            .setDuration(animationTime.toLong())
            .rotation(360 * 15 + wheelItemCenter)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    if (mOnLuckyWheelReachTheTarget != null) {
                        mOnLuckyWheelReachTheTarget!!.onReachTarget()
                    }
                    if (onRotationListener != null) {
                        onRotationListener!!.onFinishRotation()
                    }
                    clearAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            .start()
    }
    fun resetRotationLocationToZeroAngle(target: Int,animationTime: Int) {
        animate().setDuration(0)
            .rotation(0f).setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    rotateWheelToTarget(target, animationTime)
                    clearAnimation()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWheelBackground(canvas)
        initComponents()
        var tempAngle = 0f
        val sweepAngle = (360 / mWheelItems!!.size).toFloat()
        for (i in mWheelItems!!.indices) {
            archPaint!!.color = mWheelItems!![i].color
            canvas.drawArc(range, tempAngle, sweepAngle, true, archPaint!!)
            if (mWheelItems!![i].bitmap!==null){
            drawImage(
                canvas, tempAngle, mWheelItems!![i].bitmap!!)
            }
            drawText(
                canvas,
                tempAngle,
                sweepAngle,
                (if (mWheelItems!![i].text == null) "" else mWheelItems!![i].text)!!
            )

            tempAngle += sweepAngle
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = min(measuredWidth, measuredHeight)
        radius = width - padding * 2
        center = width / 2
        setMeasuredDimension(width, width)
    }
    fun setOnRotationListener(onRotationListener: LuckyWheel) {
        this.onRotationListener = onRotationListener
    }
}