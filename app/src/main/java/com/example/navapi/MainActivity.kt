package com.example.navapi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.location.LocationProvider
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.kakao.kakaonavi.KakaoNaviParams
import com.kakao.kakaonavi.KakaoNaviService
import com.kakao.kakaonavi.Location
import com.kakao.kakaonavi.NaviOptions
import com.kakao.kakaonavi.options.CoordType
import com.kakao.util.helper.Utility.getPackageInfo
import kotlinx.android.synthetic.main.activity_main.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Api 호출
        btnTest.setOnClickListener {
            mapApp()

            Log.d("API ", "Calling Success")
        }



        // 해시 키
        var key = getKeyHash(this)
        Log.d("key", key)

        btnGPS.setOnClickListener{
            testApp()
        }
    }
    fun findLoad(){
        var url = "daummaps://route?sp=37.537229,127.005515&ep=37.4979502,127.0276368&by=CAR"

        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);

    }

    fun callAPI() {
        Log.d("test", "TEST")

        Toast.makeText(this@MainActivity, "TEST!", Toast.LENGTH_SHORT).show()
    }

    fun mapApp() {
        val mapView = MapView(this)
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        val mapViewContainer = map_view as ViewGroup

        mapViewContainer.addView(mapView)

        val marker = MapPOIItem()
        marker.itemName = "TEST"
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633)
        marker.markerType = MapPOIItem.MarkerType.BluePin
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker)

        val marker2 = MapPOIItem()
        marker2.itemName = "TEST"
        marker2.tag = 0
        marker2.mapPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00657633)
        marker2.markerType = MapPOIItem.MarkerType.BluePin
        marker2.selectedMarkerType = MapPOIItem.MarkerType.RedPin
        mapView.addPOIItem(marker2)

    }


    fun getKeyHash(context: Context?): String? {
        val packageInfo: PackageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES)
            ?: return null
        for (signature in packageInfo.signatures) {
            try {
                Log.d("test", "TEST33")
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {

                Log.w("tag", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
        return null
    }

    fun testApp() {
        val viaList: MutableList<Location> = ArrayList<Location>()
//        viaList.add(Location.newBuilder("서서울호수공원", 126.8322289016308, 37.528495607451205).build())

        val builder = KakaoNaviParams.newBuilder(
            Location.newBuilder(
                "카카오 판교오피스",
                127.10821222694533,
                37.40205604363057
            ).build()
        ).setNaviOptions(
            NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setStartX(126.5).setStartY(35.2).build()
        ).setViaList(viaList as List<Location>?)

        KakaoNaviService.getInstance().navigate(this@MainActivity, builder.build())
    }


}
