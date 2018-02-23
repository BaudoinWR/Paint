package fr.woorib.paint

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.lang.reflect.Field
import java.util.*

class ImageSelecter {
    companion object {
        fun select(resources: Resources) : Bitmap{
            val f = R.raw::class.java.fields
            val filtered = f.filter { x -> try {x.getInt(R.raw::class);  true } catch (e: Throwable) {false}}
            return BitmapFactory.decodeResource(resources, randomResource(filtered), null)
        }

        private fun randomResource(filtered: List<Field>): Int {
            return filtered[Random().nextInt(filtered.size)].getInt(R.drawable::class)
        }

    }

}