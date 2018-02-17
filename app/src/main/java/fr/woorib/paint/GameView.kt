package fr.woorib.paint

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    private var image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.img_20180213_195209, null), screenWidth, screenHeight, false)
    private var bp = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)
    private val c = Canvas(bp)

    private var touched: Boolean = false
    private var touched_x: Int = 0
    private var touched_y: Int = 0

    init {
        image = ImageSelecter.select(resources, screenWidth, screenHeight)
        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)
        val p = Paint()
        p.color = Color.BLUE
        c.drawRect(0.toFloat(),0.toFloat(),screenWidth.toFloat(), screenHeight.toFloat(), p)
        //c.drawBitmap(image, 0.toFloat(), 0.toFloat(), null)
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
        if (touched) {
            println("Drawage")
            val p = Paint()
            p.alpha = 0
            p.strokeJoin = Paint.Join.ROUND
            p.strokeCap = Paint.Cap.ROUND
            p.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            p.isAntiAlias = true
            c.drawCircle(touched_x.toFloat(), touched_y.toFloat(), 50.toFloat(), p)
        }
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(image, 0.toFloat(), 0.toFloat(), null)

        canvas.drawBitmap(bp, 0.toFloat(), 0.toFloat(), null)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when ever there is a touch on the screen,
        // we can get the position of touch
        // which we may use it for tracking some of the game objects
        touched_x = event.x.toInt()
        touched_y = event.y.toInt()

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