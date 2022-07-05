package com.example.mask_info_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mask_info_kotlin.databinding.ActivityMainBinding
import com.example.mask_info_kotlin.model.Store

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels() // 코틀린 확장함수임.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = StoreAdapter()
        binding.recyclerView.apply {
            this.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }

        model.apply {
            // 옵저버 익명객체를 람다식으로
            itemLiveData.observe(this@MainActivity, Observer {
                adapter.updateItems(it)
            })

            loadingLiveData.observe(this@MainActivity, Observer {
                if (it) {
                    binding.progressBar.visibility =View.VISIBLE
                } else {
                    binding.progressBar.visibility =View.GONE
                }
                //binding.progressBar.visibility=if(it) View.VISIBLE else View.GONE
            })
        }

        //model.fetchStoreInfo()
    }
}