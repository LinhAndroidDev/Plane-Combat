package com.example.testnavigationcomponent

/**
 * Created By Nguyen Huu Linh on 2024/10/16
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import com.example.testnavigationcomponent.helper.screenHeight
import com.example.testnavigationcomponent.helper.screenWidth
import com.example.testnavigationcomponent.model.Bullet
import com.example.testnavigationcomponent.model.Enemy
import com.example.testnavigationcomponent.model.Plane
import com.example.testnavigationcomponent.model.TypeBullet
import kotlin.math.tan

/**
 * This class create a game screen that contains a plane, bullets and enemies
 * All items are drawn by canvas, they actives by update frame
 * Each frame is updated by Choreographer, this help the game run smoothly
 */

class GameScreen @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val plane = Plane().apply { init(resources) }
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val bullets = mutableListOf<Bullet>()
    private val enemys = mutableListOf<Enemy>()
    private var isTouchPlane = false // Kiểm tra xem người chơi có đang chạm vào máy bay không
    private var touchX = 0f
    private var touchY = 0f
    var isPlaying = true
    var timeDelay = 0f
    private val bloodPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    init {
        drawEnemy()

        // Vòng lặp tạo đạn mới
        handler.post(object : Runnable {
            override fun run() {
                if (isPlaying) {
                    val bullet = Bullet().apply { init(resources, plane) }
//                    val bullet1 = Bullet().apply {
//                        init(resources, plane)
//                        setTypeBullet(TypeBullet.CROSS)
//                    }
//                    val bullet2 = Bullet().apply {
//                        init(resources, plane)
//                        setTypeBullet(TypeBullet.REVERSE_CROSS)
//                    }
                    bullets.add(bullet)
//                    bullets.add(bullet1)
//                    bullets.add(bullet2)
                    if(enemys.isEmpty() && timeDelay < 3000f) {
                        timeDelay += 100f * (3000f / (100f * 30f))
                    }
                    if(timeDelay == 3000f) {
                        drawEnemy()
                        timeDelay = 0f
                    }
                    handler.postDelayed(this, 100) // Tạo đạn mỗi 200 ms
                }
            }
        })

        startFrame()
    }

    /**
     * Draw and update all state of items in here
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(plane.bitmapPlane, plane.x, plane.y, null)

        // Vẽ lại tất cả các viên đạn trong danh sách
        for (bulletPos in bullets) {
            canvas.drawBitmap(bulletPos.bitmapBullet, bulletPos.x, bulletPos.y, null)
        }

        // Vẽ lại tất cả các enemy trong danh sách
        if(enemys.isNotEmpty()) {
            for (enemyPos in enemys) {
                canvas.drawBitmap(enemyPos.bitmapEnemy, enemyPos.x, enemyPos.y, null)
                val rect = RectF(enemyPos.x, enemyPos.y + enemyPos.height + 5, enemyPos.x + enemyPos.width / 5 * enemyPos.blood, enemyPos.y + enemyPos.height + 17)
                canvas.drawRect(rect, bloodPaint)
            }
        }

    }

    /**
     * This function is used to process each frame during gameplay
     */
    private fun startFrame() {
        var lastFrameTime = System.nanoTime()

        // Choreographer cho việc di chuyển đạn
        val choreographer = Choreographer.getInstance()
        choreographer.postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if(isPlaying) {
                    // Tính thời gian trôi qua từ lần cuối
                    val deltaTime = (frameTimeNanos - lastFrameTime) / 1_000_000 // Convert từ nano giây sang milli giây
                    lastFrameTime = frameTimeNanos
                    handlerMoveBullet(deltaTime)
                    handlerMoveEnemy(deltaTime)
                    invalidate() // Vẽ lại canvas
                }
                // Đăng ký lại callback cho khung hình tiếp theo
                choreographer.postFrameCallback(this)
            }
        })
    }

    /**
     * This function is used to handle the movement of the enemy and remove the enemy if it is out of the screen
     */
    private fun handlerMoveEnemy(deltaTime: Long) {
        for (i in enemys.indices) {
            enemys[i].y += (3 * deltaTime / 16.67).toFloat() // Di chuyển enemy theo hướng xuống dưới
        }

        // Loại bỏ các enemy đã ra khỏi màn hình
        enemys.removeAll { it.y > screenHeight }
    }

    /**
     * This function is used to handle the movement of the bullet and remove the bullet
     * if it is out of the screen, check collide between bullet and enemy
     */
    private fun handlerMoveBullet(deltaTime: Long) {
        var isCollide = false
        var needRemove = Pair(0, 0)
        val speedBullet = 10
//        val angleDegrees = 20
//        val angleRadians = Math.toRadians(angleDegrees.toDouble()) // Chuyển từ độ sang radian
//        val tanValue = tan(angleRadians).toFloat()
        for (i in bullets.indices) {
            bullets[i].y -= (speedBullet * deltaTime / 16.67).toFloat()
//            when(bullets[i].bulletType) {
//                TypeBullet.CROSS -> {
//                    bullets[i].x += (speedBullet * deltaTime / 16.67 * tanValue).toFloat()
//                }
//
//                TypeBullet.REVERSE_CROSS -> {
//                    bullets[i].x += (speedBullet * deltaTime / 16.67 * tanValue * -1).toFloat()
//                }
//
//                else -> {}
//            }
            val xBullet = bullets[i].x
            val yBullet = bullets[i].y
            for(j in enemys.indices) {
                val xEnemy = enemys[j].x
                val yEnemy = enemys[j].y
                val widthEnemy = enemys[j].width
                val heightEnemy = enemys[j].height

                // Kiểm tra va chạm giữa đạn và enemy
                if(xBullet >= xEnemy && xBullet <= xEnemy + widthEnemy && yBullet >= yEnemy && yBullet <= yEnemy + heightEnemy) {
                    isCollide = true
                    enemys[j].blood--
                    needRemove = Pair(i, j)
                    break
                }
            }
        }

        //Nếu có va chạm thì loại bỏ viên đạn và enemy
        if(isCollide) {
            bullets.removeAt(needRemove.first)
        }

        // Loại bỏ enemy nếu máu bằng 0
        enemys.removeAll { it.blood == 0 }

        // Loại bỏ các viên đạn đã ra khỏi màn hình
        bullets.removeAll { it.y + it.height < 0 }
    }

    /**
     * This function is used to draw the enemy by row
     * Each row we will draw by get screenWidth / number of enemy in row
     * So the enemy will equally spaced in the row
     */
    private fun drawEnemy() {
        // Danh sách chứa thông tin các hàng (số lượng enemy, hệ số độ cao)
        val rowData = listOf(
            Pair(6, 1f),   // Hàng 1: 6 enemy, y = - height
            Pair(5, 2.5f), // Hàng 2: 5 enemy, y = - height * 2.5
            Pair(6, 4f),   // Hàng 3: 6 enemy, y = - height * 4
            Pair(5, 5.5f)  // Hàng 4: 5 enemy, y = - height * 5.5
        )

        for ((columns, heightMultiplier) in rowData) {
            for (i in 1..columns) {
                val enemy = Enemy().apply {
                    init(resources, (screenWidth / (columns + 1) * i - width / 2).toFloat(), - height * heightMultiplier)
                }
                enemys.add(enemy)
            }
        }
    }

    /**
     * Handler events when player touch on screen to move the plane
     * so that the plane moves according to the user's finger
     * if plane move out of screen, set plane position to the edge of screen
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Lấy tọa độ chạm
        touchX = event.x
        touchY = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(touchX >= plane.x && touchX <= plane.x + plane.width && touchY >= plane.y && touchY <= plane.y + plane.height) {
                    isTouchPlane = true
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                isTouchPlane = false
            }

            MotionEvent.ACTION_MOVE -> {
                if(isTouchPlane) {
                    plane.x = touchX - (plane.width / 2)
                    plane.y = touchY - (plane.height / 2)

                    if (plane.x < 0) {
                        plane.x = 0f
                    } else if (plane.x > screenWidth - plane.width) {
                        plane.x = (screenWidth - plane.width).toFloat()
                    }

                    if (plane.y < 0) {
                        plane.y = 0f
                    } else if (plane.y > screenHeight - plane.height) {
                        plane.y = (screenHeight - plane.height).toFloat()
                    }
                    invalidate() // Vẽ lại canvas
                    return true
                }
            }
        }

        return super.onTouchEvent(event)
    }
}