package com.example.mask_info_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mask_info_kotlin.databinding.ActivityMainBinding
import com.example.mask_info_kotlin.model.Store
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.widget.Toast

import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels() // 코틀린 확장함수임.
    private lateinit var fusedLocationClient: FusedLocationProviderClient // 통합위치제공자
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                performAction()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check();
    }

    @SuppressLint("MissingPermission")
    private fun performAction() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.latitude=37.188
                location?.longitude=127.043
                model.location=location!!
                model.fetchStoreInfo()
            }

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
                supportActionBar!!.title = "마스크 재고 있는 곳: " + it.size
            })

            loadingLiveData.observe(this@MainActivity, Observer {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
                //binding.progressBar.visibility=if(it) View.VISIBLE else View.GONE
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_refresh -> {
                model.fetchStoreInfo()
                true
            }
            else -> true
        }
    }

}