package fr.woorib.paint

import android.content.Context

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileWriter
import java.lang.reflect.Field
import java.util.*
class ImageSelector {
    companion object {
        private val filtered = R.raw::class.java.fields.filter { x -> try {x.getInt(R.raw::class);  true } catch (e: Throwable) {false}}
        private var possibles: MutableList<String>? = null
        private lateinit var context : Context
        private var counter = Random().nextInt(50)

        fun select(resources: Resources) : Bitmap{
            return if(possibles == null || possibles!!.isEmpty()) {
                BitmapFactory.decodeResource(resources, randomResource(filtered), null)
            } else {
                BitmapFactory.decodeFile(randomFile())
            }
        }

        private fun randomFile(): String? {
            return (possibles!![counter++ % possibles!!.size])
        }

        private fun randomResource(filtered: List<Field>): Int {
            return filtered[counter++ % filtered.size].getInt(R.drawable::class)
        }

        fun init(context: Context) {
            this.context = context
            val file = File(context.filesDir, "selectedPictures")
            if (!file.exists()) {
                file.createNewFile()
            }
            val scanner = Scanner(file)
            possibles = mutableListOf()
            while (scanner.hasNextLine()) {
                val element = scanner.nextLine()
                if ("" != element.trim()) {
                    possibles!!.add(element)
                }
            }
            scanner.close()
        }

        fun selectPictures(map: List<String>) {
            val fileWriter = FileWriter(File(context.filesDir, "selectedPictures"))
            for (path in map) {
                fileWriter.write(path)
                fileWriter.write("\n")
            }
            fileWriter.close()
            init(context)
        }

    }

}