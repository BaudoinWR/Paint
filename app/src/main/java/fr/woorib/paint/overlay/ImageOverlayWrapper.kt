package fr.woorib.paint.overlay

import android.content.res.Resources
import android.graphics.Canvas

class ImageOverlayWrapper(private val width: Int, private val height: Int, private val resources: Resources) : ImageOverlay {

    private var imageOverlay : ImageOverlay = ImageHider(width, height)

    override fun draw(canvas: Canvas) {
        imageOverlay.draw(canvas)
    }

    override fun update(touched: Boolean, touchedX: Int, touchedY: Int) : OverlayReturnEnum {
        var done = imageOverlay.update(touched, touchedX, touchedY)
        if (done == OverlayReturnEnum.CLEAR) {
            imageOverlay = UIOverlay(width, resources)
            done = OverlayReturnEnum.DEFAULT
        }
        return done
    }
}