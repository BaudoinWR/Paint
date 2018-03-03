package fr.woorib.paint.overlay

import android.graphics.Canvas

class ConfigOverlay : ImageOverlay {
    override fun draw(canvas: Canvas) {
    }

    override fun update(touched: Boolean, touchedX: Int, touchedY: Int): OverlayReturnEnum {
        return OverlayReturnEnum.DEFAULT
    }
}