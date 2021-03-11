package raum.muchbeer.customviewktx.canvas

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import raum.muchbeer.customviewktx.R


private const val STROKE_WIDTH = 12f // has to be float
class MyCanvasView(context: Context) : View(context) {

 /*   private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap*/

    private lateinit var frame: Rect

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    // Set up the paint with which to draw.
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }
//with a Path object to store the path that is being drawn when following the user's touch on the screen
    private var path = Path()
    // Path representing the drawing so far
    private val drawing = Path()
    // Path representing what's currently being drawn
    private val curPath = Path()


    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY =0f


    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
      /*  extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)*/

        val inset = 40
        frame = Rect(inset, inset, width-inset, height-inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
      //  canvas.drawBitmap(extraBitmap, 0, 0, null)
        canvas?.apply {
            //0, 0 is at the top corner of the canvas map
          //  drawBitmap(extraBitmap, 0,0, null)
            // Draw the drawing so far
            canvas.drawPath(drawing, paint)
            // Draw any current squiggle
            canvas.drawPath(curPath, paint)
            drawRect(frame, paint)


        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        motionTouchEventX = event!!.x
        motionTouchEventY= event!!.y

        when(event!!.action) {
            MotionEvent.ACTION_DOWN ->touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchRemove()
        }
        return true
    }


    private fun touchRemove() {
        // Add the current path to the drawing so far
        drawing.addPath(curPath)
// Rewind the current path for the next touch
        curPath.reset()
    }

    private fun touchMove() {

        //calculate the distance that has been moved (dx, dy)
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        //if the movement is better enough to be drawn then add to the path
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
                //quadTo draw smooth and avoid corners that always use lineTo
            drawing.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
           // extraCanvas.drawPath(path, paint)

        }
        invalidate()
    }

    private fun touchStart() {
        drawing.reset()
        drawing.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY

    }
}