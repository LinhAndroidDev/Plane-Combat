package com.example.testnavigationcomponent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View

class GameScreen @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val plane: Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_plane), 120, 120, true)
    private var xPlane = (screenWidth - plane.width) / 2
    private var yPlane = screenHeight * 2 / 3 - plane.height / 2
    private val bullet: Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_bullet), 40, 40, true)
    private val handler: Handler = Handler(Looper.getMainLooper())
    // Danh sách lưu trữ các vị trí của các viên đạn
    val bullets = mutableListOf<Pair<Float, Float>>()
    private val enemy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_enemy), 100, 100, true)
    val enemys = mutableListOf<Pair<Float, Float>>()
    private var isTouchPlane = false
    private var touchX = 0f
    private var touchY = 0f
    var isPlaying = true

    init {
        drawEnemy()

        // Vòng lặp tạo đạn mới
        handler.post(object : Runnable {
            override fun run() {
                if (isPlaying) {
                    val xBullet = xPlane + plane.width/2 - bullet.width / 2
                    val yBullet = yPlane - bullet.height
                    bullets.add(Pair(xBullet.toFloat(), yBullet.toFloat()))
                    handler.postDelayed(this, 100) // Tạo đạn mỗi 200 ms
                }
            }
        })

        startFrame()
//        startEnemy()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(plane, xPlane.toFloat(), yPlane.toFloat(), null)

        // Vẽ lại tất cả các viên đạn trong danh sách
        for (bulletPos in bullets) {
            val (x, y) = bulletPos
            canvas.drawBitmap(bullet, x, y, null)
        }

        for (enemyPos in enemys) {
            val (x, y) = enemyPos
            canvas.drawBitmap(enemy, x, y, null)
        }

    }

    private fun startFrame() {
        // Choreographer cho việc di chuyển đạn
        val choreographer = Choreographer.getInstance()
        choreographer.postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                // Vẽ lại tất cả các viên đạn trong danh sách
                if(isPlaying) {
                    var isCollide = false
                    var needRemove = Pair(0, 0)
                    for (i in bullets.indices) {
                        val (x, y) = bullets[i]
                        bullets[i] = Pair(x, y - 20)
                        for(j in enemys.indices) {
                            val xEnemy = enemys[j].first
                            val yEnemy = enemys[j].second
                            if(x >= xEnemy && x <= xEnemy + enemy.width && y >= yEnemy && y <= yEnemy + enemy.height) {
                                isCollide = true
                                needRemove = Pair(i, j)
                                break
                            }
                        }
                    }

                    if(isCollide) {
                        bullets.removeAt(needRemove.first)
                        enemys.removeAt(needRemove.second)
                    }

                    // Loại bỏ các viên đạn đã ra khỏi màn hình
                    bullets.removeAll { it.second + bullet.height < 0 }

                    for (i in enemys.indices) {
                        val (x, y) = enemys[i]
                        enemys[i] = Pair(x, y + 3) // Di chuyển enemy theo hướng xuống dưới
                    }

                    // Loại bỏ các enemy đã ra khỏi màn hình
                    enemys.removeAll { it.second > screenHeight }

                    invalidate() // Vẽ lại canvas
                    // Đăng ký lại callback cho khung hình tiếp theo
                }
                choreographer.postFrameCallback(this)
            }
        })
    }

    private fun drawEnemy() {
        //Draw Row 1
        for(i in 1 until 6) {
            enemys.add(Pair((screenWidth / 6 * i - enemy.width / 2).toFloat(), - enemy.height.toFloat()))
        }

        //Draw Row 2
        for(i in 1 until 5) {
            enemys.add(Pair((screenWidth / 5 * i - enemy.width / 2).toFloat(), - enemy.height.toFloat() * 2.5f))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Lấy tọa độ chạm
        touchX = event.x
        touchY = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(touchX >= xPlane && touchX <= xPlane + plane.width && touchY >= yPlane && touchY <= yPlane + plane.height) {
                    isTouchPlane = true
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                isTouchPlane = false
            }

            MotionEvent.ACTION_MOVE -> {
                if(isTouchPlane) {
                    xPlane = (touchX - (plane.width / 2)).toInt()
                    yPlane = (touchY - (plane.height / 2)).toInt()

                    if (xPlane < 0) {
                        xPlane = 0
                    } else if (xPlane > screenWidth - plane.width) {
                        xPlane = screenWidth - plane.width
                    }

                    if (yPlane < 0) {
                        yPlane = 0
                    } else if (yPlane > screenHeight - plane.height) {
                        yPlane = screenHeight - plane.height
                    }
                    invalidate() // Vẽ lại canvas
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }
}