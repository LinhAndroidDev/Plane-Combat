package com.example.testnavigationcomponent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("Class View: ", this::class.java.simpleName)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.navHostContainer)
        return navController?.navigateUp() ?: false || super.onSupportNavigateUp()
    }
}