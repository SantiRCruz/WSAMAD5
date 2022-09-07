package com.example.wsamad5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wsamad5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        animateIn()
    }

    private fun animateIn() {
        binding.imgLogo.animate().alpha(0f).translationY(-300f).scaleX(0f).scaleY(0f).setDuration(0).withEndAction {
                binding.imgName.animate().alpha(0f).translationY(-100f).setDuration(0)
                    binding.imgLogo.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f).setDuration(400).withEndAction {
                        binding.imgName.animate().alpha(1f).translationY(0f).setDuration(400).withEndAction {
                            val i = Intent(this@MainActivity,LoginActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    }
            }
    }
}