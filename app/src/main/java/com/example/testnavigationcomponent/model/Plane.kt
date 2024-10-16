package com.example.testnavigationcomponent.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.testnavigationcomponent.R
import com.example.testnavigationcomponent.helper.screenHeight
import com.example.testnavigationcomponent.helper.screenWidth

data class Plane (
    var x: Float = 0f,
    var y: Float = 0f,
    var bitmapPlane: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val width: Int = 120,
    val height: Int = 120
) {

    fun init(resources: Resources) {
        x = ((screenWidth - width) / 2).toFloat()
        y = (screenHeight * 2 / 3 - height / 2).toFloat()
        bitmapPlane = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.ic_plane),
            width,
            height,
            true
        )
    }
}