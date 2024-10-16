package com.example.testnavigationcomponent.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.testnavigationcomponent.R

enum class TypeBullet {
    STRAIGHT, CROSS, REVERSE_CROSS
}

data class Bullet(
    var x: Float = 0f,
    var y: Float = 0f,
    var bitmapBullet: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val width: Int = 40,
    val height: Int = 40,
    var bulletType: TypeBullet = TypeBullet.STRAIGHT
) {
    fun init(resources: Resources, plane: Plane) {
        x = plane.x + plane.width / 2 - width / 2
        y = plane.y
        bitmapBullet = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.ic_bullet),
            width,
            height,
            true
        )
    }

    fun setTypeBullet(type: TypeBullet) {
        bulletType = type
    }
}