package com.example.testnavigationcomponent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testnavigationcomponent.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        binding.gameScreen.isPlaying = true
    }

    override fun onStop() {
        super.onStop()
        binding.gameScreen.isPlaying = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.gameScreen.isPlaying = false
    }
}