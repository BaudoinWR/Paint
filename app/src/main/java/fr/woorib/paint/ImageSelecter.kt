package fr.woorib.paint

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class ImageSelecter {
    companion object {
        fun select(resources: Resources, screenWidth: Int, screenHeight: Int) : Bitmap{
            val f = R.drawable::class.java.fields
            val filtered = f.filter { x -> try {x.getInt(R.drawable::class);  true } catch (e: Exception) {false}}

            return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, filtered[Random().nextInt(filtered.size)].getInt(R.drawable::class), null), screenWidth, screenHeight, false)

        }
    }

}