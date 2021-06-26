package com.example.remotejoystick.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.lang.Math.min
import kotlin.math.*


class Joystick @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): View(context, attrs, defStyleAttr){
    // Initialize Paint only once, will be reused in every draw cycle.
    // Rendering is done many times a second and rendering code should be minimal for user-responsiveness.

        private val paint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.parseColor("#FFC107")
            isAntiAlias = true
        }
        private val basePaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            color = Color.parseColor("#000000")
            isAntiAlias = true
        }

        private var radius: Float = 0f
        private var center: PointF = PointF()
        private var baseRadius: Float = 0f
        private var baseCenter: PointF = PointF()
        var onChange : (Float,Float)->Unit= { _, _ ->}//default behaviour does nothing

        // calculate positions and sizes here, not when drawing
        override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
            radius = 0.15f* min(width, height).toFloat()
            center = PointF(width/2.0f, height/2.0f)
            baseRadius = 0.35f* min(width, height).toFloat()
            baseCenter = PointF(width/2.0f, height/2.0f)
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawCircle(baseCenter.x, baseCenter.y, baseRadius, basePaint)
            canvas.drawCircle(center.x, center.y, radius, paint)
        }
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            if (event == null) {
                return true
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchMove(event.x, event.y)//same as actionMove
                MotionEvent.ACTION_MOVE -> touchMove(event.x, event.y)
                MotionEvent.ACTION_UP -> touchMove(baseCenter.x, baseCenter.y)//reset the joystick placement
            }
            return true
        }
        private fun touchMove(x: Float, y: Float){
            //Update positions and properties of drawn items
            center = if(distance(x,y,baseCenter.x,baseCenter.y)>baseRadius){//make sure the joystick stays within bounds(inside the base)
                //calculate new position within the bounds
                val ratio = baseRadius / distance(x,y,baseCenter.x,baseCenter.y)
                val newX = baseCenter.x + (x - baseCenter.x)*ratio
                val newY = baseCenter.y + (y - baseCenter.y)*ratio
                PointF(newX,newY)
            } else{//joystick is inside the base area
                PointF(x,y)
            }
            //default behaviour is to do nothing, possible override to add functionality(parameters are deviations from the base's center, relative to the base radius)
            onChange((center.x-baseCenter.x)/baseRadius,-(center.y-baseCenter.y)/baseRadius)
            // render again the screen
            invalidate()
        }

    // calculate distance between two points
    private fun distance(x: Float, y: Float, x1: Float, y1: Float): Float {
        return sqrt((x - x1).toDouble().pow(2.0) + (y - y1).toDouble().pow(2.0)).toFloat()
    }
}