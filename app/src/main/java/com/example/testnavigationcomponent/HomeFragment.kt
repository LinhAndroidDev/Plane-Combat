package com.example.testnavigationcomponent

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

enum class ActionMove {
    NONE,
    MOVE
}

class HomeFragment : Fragment() {
    private var item1: Button? = null
    private var item2: Button? = null
    private var item3: Button? = null
    private var item4: Button? = null
    private var item5: Button? = null
    private var item6: Button? = null
    private var item7: Button? = null
    private var item8: Button? = null
    private var item9: Button? = null
    private var gridItems: List<Button?> = listOf()
    private var itemActionMove: Button? = null
    private var itemMove: Button? = null
    private var actionMove: ActionMove = ActionMove.NONE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            item1 = findViewById(R.id.item1)
            item2 = findViewById(R.id.item2)
            item3 = findViewById(R.id.item3)
            item4 = findViewById(R.id.item4)
            item5 = findViewById(R.id.item5)
            item6 = findViewById(R.id.item6)
            item7 = findViewById(R.id.item7)
            item8 = findViewById(R.id.item8)
            item9 = findViewById(R.id.item9)
            itemActionMove = findViewById(R.id.itemActionMove)
            itemMove = findViewById(R.id.itemMove)

            gridItems = listOf(item1, item2, item3, item4, item5, item6, item7, item8, item9)

            gridItems.forEach {
                it?.setOnClickListener { view ->
                    view?.handleActionMove()
                }
            }

            itemActionMove?.setOnClickListener {
                actionMove = if(actionMove == ActionMove.NONE) {
                    ActionMove.MOVE
                } else {
                    ActionMove.NONE
                }
            }
        }
    }

    private fun View.handleActionMove() {
        when (actionMove) {
            ActionMove.NONE -> {

            }

            ActionMove.MOVE -> {
                itemMove?.isVisible = true
                itemMove?.x = itemActionMove?.x ?: 0f
                itemMove?.y = itemActionMove?.y ?: 0f
                actionMove = ActionMove.NONE
                moveView(itemMove!!, this)
            }
        }
    }

    private fun moveView(it1: View, it2: View) {
        // Lấy vị trí của view thứ hai
        val location = IntArray(2)
        it2.getLocationOnScreen(location)
        val x2 = location[0].toFloat()
        val y2 = location[1].toFloat()

// Di chuyển view thứ nhất đến vị trí của view thứ hai trong 0.3 giây
        it1.animate()
            .x(x2)
            .y(y2)
            .setDuration(700) // 0.3 giây = 300 ms
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    it2.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary))
                    itemMove?.isVisible = false
                }
            })
            .start()
    }
}