package com.echoist.linkedout.gps

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.echoist.linkedout.viewModels.WritingViewModel
import javax.inject.Inject

//gps 위치권한을 받아오는 Class
class RequestPermissionsUtil @Inject constructor(
    private val context: Context,
    private val viewModel: WritingViewModel

){

private fun stringToLatitude(latitudeStr: String): String {
    val lat = latitudeStr.substring(0,6)
    return if (latitudeStr.toDouble() > 0) "$lat˚E"
    else "$lat˚W"
}

private fun stringToLongitude(longitudeStr: String): String {
    val lon = longitudeStr.substring(0,7)
    return if (longitudeStr.toDouble() > 0) "$lon˚N"
    else "$lon˚S"
}


    val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    // 위치 정보를 수신하는 리스너
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경되었을 때 실행되는 코드

            Log.d("Location_Lat_Lon", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
            // 이후 여기에서 추가적인 작업을 수행할 수 있습니다.
            locationManager.removeUpdates(this) //메모리 낭비 방지를위해 리스너가 호출될때 한번 빼고는 업데이트를 하지않는다. 실시간x
            viewModel.longitude = stringToLongitude(location.longitude.toString())
            viewModel.latitute = stringToLatitude(location.latitude.toString())
            Log.d("Location_Lat_Lon", "Latitude: ${viewModel.longitude}, Longitude: ${viewModel.latitute}")


        }



        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // 위치 공급자의 상태가 변경될 때 실행되는 코드
        }

        override fun onProviderEnabled(provider: String) {
            // 위치 공급자가 사용 가능한 상태가 될 때 실행되는 코드
        }

        override fun onProviderDisabled(provider: String) {
            // 위치 공급자가 사용 불가능한 상태가 될 때 실행되는 코드
        }
    }
    // 추가적으로 사용할 위치 정보 요청 코드
    private val REQUEST_LOCATION = 1

    // 추가적으로 사용할 위치 정보 요청 함수
    @SuppressLint("MissingPermission")
    fun RequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates( //NETWORK_PROVIDER과 GPS_PROVIDER를 사용할 수 있다.
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener

            )
        }
    }

    /** 위치 권한 SDK 버전 29 이상**/
    @RequiresApi(Build.VERSION_CODES.Q)
    private val permissionsLocationUpApi29Impl = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    /** 위치 권한 SDK 버전 29 이하**/
    @TargetApi(Build.VERSION_CODES.P)
    private val permissionsLocationDownApi29Impl = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

        /** 위치정보 권한 요청**/
        fun RequestLocation() {
            if (Build.VERSION.SDK_INT >= 29) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permissionsLocationUpApi29Impl[0]
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        context,
                        permissionsLocationUpApi29Impl[1]
                    ) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(
                        context,
                        permissionsLocationUpApi29Impl[2]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        permissionsLocationUpApi29Impl,
                        REQUEST_LOCATION
                    )
                }
            } else {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permissionsLocationDownApi29Impl[0]
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        context,
                        permissionsLocationDownApi29Impl[1]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        permissionsLocationDownApi29Impl,
                        REQUEST_LOCATION
                    )
                }
            }
        }

        /**위치권한 허용 여부 검사**/
        fun isLocationPermitted(): Boolean {
            if (Build.VERSION.SDK_INT >= 29) {
                for (perm in permissionsLocationUpApi29Impl) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            perm
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            } else {
                for (perm in permissionsLocationDownApi29Impl) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            perm
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }

            return true
        }



}