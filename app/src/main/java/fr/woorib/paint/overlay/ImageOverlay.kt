package fr.woorib.paint.overlay

import android.graphics.Canvas

interface ImageOverlay {
    /**
     * Draws into the overlay onto the canvas
     */
    fun draw(canvas: Canvas)

    /**
     * Reacts to touch on the screen
     * Returns true if it's ready for the caller to switch to a new overlay
     */
    fun update(touched: Boolean, touchedX: Int, touchedY: Int) : Boolean
}