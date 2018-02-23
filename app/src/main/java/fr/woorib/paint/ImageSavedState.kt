package fr.woorib.paint

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import android.view.View

class ImageSavedState : View.BaseSavedState {
    lateinit var image : Bitmap

    constructor(parcel: Parcel) : super(parcel)

    constructor(superState: Parcelable) : super(superState)

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageSavedState> {
        override fun createFromParcel(parcel: Parcel): ImageSavedState {
            return ImageSavedState(parcel)
        }

        override fun newArray(size: Int): Array<ImageSavedState?> {
            return arrayOfNulls(size)
        }
    }

}