package fr.woorib.paint.overlay

import android.graphics.*

class ImageOverlay(width: Int, height : Int) {
    private val bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private val c = Canvas(bp)
    private var transparencyIncreased = false

    private var clear = false

    /**
     * Initializing overlay as a hiding frame that needs to be swiped away
     */
    init {
        val p = Paint()
        p.color = Color.BLUE
        c.drawRect(0.toFloat(),0.toFloat(),width.toFloat(), height.toFloat(), p)
    }

    fun update(touched: Boolean, touchedX: Int, touchedY: Int) {
        if (touched && !clear) {
            clearPart(touchedX, touchedY)
            transparencyIncreased = true
        }
        if (!touched && transparencyIncreased) {
            transparencyIncreased = false
            checkClearPercentage()
        }
    }

    private fun clearPart(touchedX: Int, touchedY: Int) {
        val p = Paint()
        p.alpha = 0
        p.strokeJoin = Paint.Join.ROUND
        p.strokeCap = Paint.Cap.ROUND
        p.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        p.isAntiAlias = true
        c.drawCircle(touchedX.toFloat(), touchedY.toFloat(), 100.toFloat(), p)
    }

    private fun checkClearPercentage() {
        val pixels = IntArray(bp.width * bp.height)
        bp.getPixels(pixels, 0, bp.width, 0, 0, bp.width, bp.height)
        val totalTransparent = pixels.count { it == Color.TRANSPARENT }

        if ((totalTransparent.toFloat() / (bp.width * bp.height).toFloat()) > 0.75) {
            clear = true
        }
    }

    fun draw(canvas : Canvas) {
        if (!clear) {
            canvas.drawBitmap(bp, 0.toFloat(), 0.toFloat(), null)
        }
    }
}