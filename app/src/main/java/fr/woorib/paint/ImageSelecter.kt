package fr.woorib.paint

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.lang.reflect.Field
import java.util.*

class ImageSelecter {
    companion object {
        fun select(resources: Resources, screenWidth: Int, screenHeight: Int) : Bitmap{
            val f = R.drawable::class.java.fields
            val filtered = f.filter { x -> try {x.getInt(R.drawable::class);  true } catch (e: Exception) {false}}

            return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, randomResource(filtered), null), screenWidth, screenHeight, false)

        }

        private fun randomResource(filtered: List<Field>): Int {
            var resource: Int
            do {
                resource = filtered[Random().nextInt(filtered.size)].getInt(R.drawable::class)
            } while (resource == R.drawable.arrow)
            return resource
        }

    }

}