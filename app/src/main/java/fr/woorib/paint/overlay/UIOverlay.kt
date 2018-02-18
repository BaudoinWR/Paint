package fr.woorib.paint.overlay

import android.graphics.Canvas

class UIOverlay(width: Int, height: Int) : ImageOverlay {
    override fun draw(canvas: Canvas) {
    }

    override fun update(touched: Boolean, touchedX: Int, touchedY: Int): Boolean {
        return true
    }
}