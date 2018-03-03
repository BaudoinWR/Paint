package fr.woorib.paint

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.widget.ImageButton
import android.widget.Toast
import com.darsh.multipleimageselect.activities.AlbumSelectActivity
import com.darsh.multipleimageselect.helpers.Constants
import com.darsh.multipleimageselect.models.Image


class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)

        findViewById<ImageButton>(R.id.imageButton01)
                .setOnClickListener({
                    val intent = Intent(this, AlbumSelectActivity::class.java)
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        println("MULTIPLE")
                    }
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), PICK_IMAGE_MULTIPLE)
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
                    && null != data) {
                // Get the Image from data
                val parcelableArrayListExtra = data.getParcelableArrayListExtra<Image>(Constants.INTENT_EXTRA_IMAGES)
                ImageSelector.possibles = parcelableArrayListExtra.map { it.path }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val PICK_IMAGE_MULTIPLE = 1
    }
}
