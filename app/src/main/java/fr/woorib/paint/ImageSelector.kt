package fr.woorib.paint

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.lang.reflect.Field
import java.util.*

class ImageSelector {
    companion object {
        private val filtered = R.raw::class.java.fields.filter { x -> try {x.getInt(R.raw::class);  true } catch (e: Throwable) {false}}

        fun select(resources: Resources) : Bitmap{
            return if(possibles == null || possibles!!.isEmpty()) {
                BitmapFactory.decodeResource(resources, randomResource(filtered), null)
            } else {
                BitmapFactory.decodeFile(randomFile())
            }
        }

        private fun randomFile(): String? {
            return (possibles!![Random().nextInt(possibles!!.size)])
        }

        private fun randomResource(filtered: List<Field>): Int {
            return filtered[Random().nextInt(filtered.size)].getInt(R.drawable::class)
        }

        var possibles: List<String>? = null

    }

}