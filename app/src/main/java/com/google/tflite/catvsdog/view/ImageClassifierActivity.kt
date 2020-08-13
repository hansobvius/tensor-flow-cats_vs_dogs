package com.google.tflite.catvsdog.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.databinding.ActivityImageClassifierBinding

class ImageClassifierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageClassifierBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ImageClassifierActivity, R.layout.activity_image_classifier
        )
        binding.apply{
            navController = findNavController(R.id.nav_host_fragment)
            navController.let{
                NavigationUI.setupActionBarWithNavController(this@ImageClassifierActivity, it, this.drawer)
                NavigationUI.setupWithNavController(this.navigationView, it)
                appBarConfiguration = AppBarConfiguration(navController.graph, this.drawer)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController, appBarConfiguration)
}
