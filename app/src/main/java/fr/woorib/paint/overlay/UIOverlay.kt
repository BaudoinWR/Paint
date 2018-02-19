package fr.woorib.paint.overlay

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import fr.woorib.paint.R

class UIOverlay(private val width: Int, resources : Resources) : ImageOverlay {
    private val arrow = BitmapFactory.decodeResource(resources, R.drawable.arrow)
    private var rect : Rect

    init {
        rect = Rect(width - arrow.width, 0, width, arrow.height)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(arrow, (width - arrow.width).toFloat(), 0.toFloat(), null)
    }

    override fun update(touched: Boolean, touchedX: Int, touchedY: Int): OverlayReturnEnum {
        if (rect.contains(touchedX, touchedY)) {
            return OverlayReturnEnum.RESTART
        }
        return OverlayReturnEnum.DEFAULT
    }
}