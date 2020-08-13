package com.google.tflite.catvsdog.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@MainActivity, R.layout.activity_main
        )
        binding.apply{
            navController = findNavController(R.id.nav_host_fragment)
            navController.let{
                NavigationUI.setupActionBarWithNavController(this@MainActivity, it, this.drawer)
                NavigationUI.setupWithNavController(this.navigationView, it)
                appBarConfiguration = AppBarConfiguration(navController.graph, this.drawer)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController, appBarConfiguration)
}
