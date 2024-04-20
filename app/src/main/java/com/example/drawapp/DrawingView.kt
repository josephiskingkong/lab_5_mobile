package com.example.drawapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mDrawPath: Path? = null
    private var mDrawingBitmap: Bitmap? = null
    private var mBackgroundBitmap: Bitmap? = null
    private var mDrawPaint: Paint = Paint()
    private var mCanvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    private var mCanvas: Canvas? = null
    private val mPaths = mutableListOf<Pair<Path, Paint>>()

    init {
        setupPaint()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun setupPaint() {
        mDrawPaint.isAntiAlias = true
        mDrawPaint.isDither = true
        mDrawPaint.style = Paint.Style.STROKE
        mDrawPaint.strokeJoin = Paint.Join.ROUND
        mDrawPaint.strokeCap = Paint.Cap.ROUND
        mDrawPaint.strokeWidth = 20f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mDrawingBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mDrawingBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mBackgroundBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        canvas.drawBitmap(mDrawingBitmap!!, 0f, 0f, mCanvasPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath = Path().apply {
                    moveTo(touchX, touchY)
                }
                mPaths.add(Pair(mDrawPath!!, Paint(mDrawPaint)))
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath?.lineTo(touchX, touchY)
                mCanvas?.drawPath(mDrawPath!!, mDrawPaint)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mCanvas?.drawPath(mDrawPath!!, mDrawPaint)
                mDrawPath = null
                invalidate()
            }
            else -> return false
        }
        return true
    }

    fun changeColor(color: Int) {
        mDrawPaint.color = color
        mDrawPaint.xfermode = null
    }

    fun useEraser() {
        mDrawPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun changeBrushSize(size: Float) {
        mDrawPaint.strokeWidth = size
    }

    fun getCurrentColor(): Int {
        return mDrawPaint.color
    }


    fun setBackgroundImage(bitmap: Bitmap) {
        mBackgroundBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        invalidate()
    }
}
