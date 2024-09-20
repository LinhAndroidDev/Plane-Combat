package com.example.testnavigationcomponent

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testnavigationcomponent.page_curl.PageCurlView

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pageCurlView: PageCurlView? = activity?.findViewById(R.id.pagerCurl)

        Log.e("Class View: ", this::class.java.simpleName)

//        object : CountDownTimer(2000, 2000) {
//            override fun onTick(p0: Long) {
//
//            }
//
//            override fun onFinish() {
//                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
//            }
//
//        }.start()
    }
}