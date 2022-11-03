package com.aimardon.spingame

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.aimardon.spingame.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val exampleThread by lazy {
        ExampleThread()
    }
    var wheelItems: MutableList<WheelItem?>? = null
    val handler=Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        generateWheelItems()
        binding.lwv.addWheelItems(wheelItems!!)
        exampleThread.start()
    }
    inner class ExampleThread:Thread(){
        override fun run() {
            handler.post {
                var a: Int
                binding.start.setOnClickListener {
                    a = Random.nextInt(wheelItems!!.size)
                    binding.lwv.setTarget(a)
                    binding.lwv.rotateWheelTo(a, 200)
                binding.start.isClickable=false
                }
                    binding.lwv.setLuckyWheelReachTheTarget(object : OnLuckyWheelReachTheTarget {
                        override fun onReachTarget() {
                            binding.start.isClickable=true
                        }
                    })
            }
        }
    }
    private fun generateWheelItems() {
        wheelItems = ArrayList()
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#fc6c6c"), "100 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#00E6FF"), "0 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#F00E6F"), "30 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#00E6FF"), "6000 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#fc6c6c"), "9 $"
            )
        )
        (wheelItems as ArrayList<WheelItem?>).add(
            WheelItem(
                Color.parseColor("#00E6FF"), "20 $"
            )
        )
    }
}