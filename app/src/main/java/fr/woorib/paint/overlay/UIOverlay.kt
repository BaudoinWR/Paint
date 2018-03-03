package fr.woorib.paint.overlay

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import fr.woorib.paint.R

class UIOverlay(private val width: Int, private val height: Int, resources: Resources) : ImageOverlay {
    private val arrow = BitmapFactory.decodeResource(resources, R.drawable.arrow)
    private val close = BitmapFactory.decodeResource(resources, R.drawable.close)
    private val options = BitmapFactory.decodeResource(resources, R.drawable.options)
    private var nextRect: Rect
    private var closeRect: Rect
    private var optionsRect: Rect


    init {
        nextRect = Rect(width - arrow.width, (height - arrow.height) / 2, width, (height / 2 + arrow.height))
        closeRect = Rect(0, 0, close.width, close.height)
        optionsRect = Rect(width - options.width, 0, width, options.height)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(arrow, (width - arrow.width).toFloat(), ((height - arrow.height).toFloat() / 2), null)
        canvas.drawBitmap(close, 0.toFloat(), 0.toFloat(), null)
        canvas.drawBitmap(options, (width - options.width).toFloat(), 0.toFloat(), null)
    }

    override fun update(touched: Boolean, touchedX: Int, touchedY: Int): OverlayReturnEnum {
        if (!touched) {
            return OverlayReturnEnum.DEFAULT
        }
        if (nextRect.contains(touchedX, touchedY)) {
            return OverlayReturnEnum.RESTART
        }
        if (closeRect.contains(touchedX, touchedY)) {
            return OverlayReturnEnum.CLOSE
        }
        if (optionsRect.contains(touchedX, touchedY)) {
            return OverlayReturnEnum.CONFIG
        }
        return OverlayReturnEnum.DEFAULT
    }
}