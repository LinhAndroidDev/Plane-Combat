package com.example.testnavigationcomponent

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class Plane (
    var x: Int = 0,
    var y: Int = 0,
    var bitmapPlane: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    var width: Int = 0,
    var height: Int = 0
) {

    fun init(resources: Resources) {
        width = 120
        height = 120
        x = (screenWidth - width) / 2
        y = screenHeight * 2 / 3 - height / 2
        bitmapPlane = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.ic_plane),
            width,
            height,
            true
        )
    }
}