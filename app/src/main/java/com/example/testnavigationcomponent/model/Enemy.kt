package com.example.testnavigationcomponent.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.testnavigationcomponent.R

data class Enemy(
    var x: Float = 0f,
    var y: Float = 0f,
    var bitmapEnemy: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val width: Int = 100,
    val height: Int = 100,
    var blood: Int = 5
) {
    fun init(resources: Resources, xPos: Float, yPos: Float) {
        x = xPos
        y = yPos
        bitmapEnemy = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.ic_enemy),
            width,
            height,
            true
        )
    }
}