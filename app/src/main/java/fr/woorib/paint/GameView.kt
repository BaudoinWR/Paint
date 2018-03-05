package fr.woorib.paint

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.*
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import fr.woorib.paint.overlay.ImageOverlay
import fr.woorib.paint.overlay.ImageOverlayWrapper
import fr.woorib.paint.overlay.OverlayReturnEnum

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private lateinit var thread: GameThread
    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels
    lateinit var image : Bitmap
    private lateinit var imageOverlay: ImageOverlay
    private var touched: Boolean = false
    private var touchedX: Int = 0
    private var touchedY: Int = 0

    private var doInit = true

    init {
        id = R.id.gameViewId

        // add callback
        holder.addCallback(this)

        ImageSelector.init(context)
    }

    private fun initImage(newImage : Bitmap) {
        image = newImage
        val max = Math.max(screenWidth, screenHeight)
        val min = Math.min(screenWidth, screenHeight)
        if (image.width > image.height) {
            screenWidth = max
            screenHeight = min
            image = Bitmap.createScaledBitmap(image, screenWidth, screenHeight, false)
            (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            screenWidth = min
            screenHeight = max
            image = Bitmap.createScaledBitmap(image, screenWidth, screenHeight, false)
            (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
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
        // instantiate the game thread
        thread = GameThread(holder, this)
        if (doInit) {
            initImage(ImageSelector.select(resources))
        } else {
            doInit = true
        }

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
            OverlayReturnEnum.RESTART -> initImage(ImageSelector.select(resources))
            OverlayReturnEnum.CLOSE -> (context as Activity).finish()
            OverlayReturnEnum.CONFIG -> (context as Activity).startActivity(Intent(context, SettingsActivity::class.java))
            else -> Unit
        }
        //reset touch to avoid applying touches to previous position when returning to view
        touchedX = -1
        touchedY = -1
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

    override fun onSaveInstanceState(): Parcelable {

        val onSaveInstanceState = super.onSaveInstanceState()
        val imageSavedState = ImageSavedState(onSaveInstanceState)
        imageSavedState.image = image
        return imageSavedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is ImageSavedState) {
            super.onRestoreInstanceState(state.superState)
            initImage(state.image)
            doInit = false
        } else {
            super.onRestoreInstanceState(state)
        }

    }
}