package fr.woorib.paint.overlay

import android.graphics.Canvas

class ImageOverlayWrapper(private val width: Int, private val height: Int) : ImageOverlay {

    private var imageOverlay : ImageOverlay = ImageHider(width, height)

    override fun draw(canvas: Canvas) {
        imageOverlay.draw(canvas)
    }

    override fun update(touched: Boolean, touchedX: Int, touchedY: Int) : Boolean {
        val done = imageOverlay.update(touched, touchedX, touchedY)
        if (imageOverlay is ImageHider && done) {
            imageOverlay = UIOverlay(width, height)
            return false
        }
        return done
    }
}