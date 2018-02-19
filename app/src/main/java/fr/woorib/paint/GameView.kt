package fr.woorib.paint

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import fr.woorib.paint.overlay.ImageOverlay
import fr.woorib.paint.overlay.ImageOverlayWrapper
import fr.woorib.paint.overlay.OverlayReturnEnum

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    private lateinit var image : Bitmap
    private lateinit var imageOverlay: ImageOverlay
    private var touched: Boolean = false
    private var touchedX: Int = 0
    private var touchedY: Int = 0

    init {
        initImage()
        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)

    }

    private fun initImage() {
        image = ImageSelecter.select(resources, screenWidth, screenHeight)
        imageOverlay = ImageOverlayWrapper(screenWidth, screenHeight, resources)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?){
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        // start the game thread
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update() {
        when (imageOverlay.update(touched, touchedX, touchedY)) {
            OverlayReturnEnum.RESTART -> initImage()
            OverlayReturnEnum.CLOSE -> (context as Activity).finish()
            else -> Unit
        }
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(image, 0.toFloat(), 0.toFloat(), null)
        imageOverlay.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when ever there is a touch on the screen,
        // we can get the position of touch
        // which we may use it for tracking some of the game objects
        touchedX = event.x.toInt()
        touchedY = event.y.toInt()

        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> touched = true
            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false
        }
        return true
    }

}