package com.example.sync_front.ui.main

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sync_front.R
import com.example.sync_front.databinding.ActivityMainBinding
import android.Manifest
import android.location.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 허용되었을 때의 처리
            } else {
                // 권한이 거부되었을 때의 처리
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnv.setupWithNavController(navController)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }

        // 위치 권한을 확인하고 위치 정보를 받아오는 함수 호출
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한을 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        } else {
            // 위치 권한이 이미 허용된 경우
            setupLocationListener()
        }
    }

    private fun setupLocationListener() {
        // 로케이션 매니저 초기화
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // 로케이션 리스너 설정
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 위치가 변경될 때마다 실행
                val latitude = location.latitude
                val longitude = location.longitude
                Log.i("MY LOCATION", "위도 : $latitude")
                Log.i("MY LOCATION", "경도 : $longitude")
                // 원하는 위치 정보 사용 가능
                getAddressFromLocation(latitude, longitude)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 상태 변경 시 실행
            }

            override fun onProviderEnabled(provider: String) {
                // 위치 제공자가 활성화되었을 때 실행
            }

            override fun onProviderDisabled(provider: String) {
                // 위치 제공자가 비활성화되었을 때 실행
            }
        }

        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 허용된 경우 위치 업데이트 요청
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000,
                10f,
                locationListener
            )
        } else {
            // 위치 권한이 거부된 경우 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                100
            )
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        addresses?.let {
            if (it.isNotEmpty()) {
                val address = it[0]
                val city = address.locality  // 시
                val district = address.subLocality  // 구

                // city와 district를 UI에 표시하거나 사용
                Log.d("my log","City: $city, District: $district")
            } else {
                Log.d("my log", "출력불가")
            }
        }
    }

    private fun requestNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

}