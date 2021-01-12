package com.developerdesk9.pokemongame

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDate


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var oldLocation :Location? =null

    var powerColleted: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        addPockemon()

        if (Build.VERSION.SDK_INT>= 23){
            checkLocationPermission()

        }
        else{
            getUserLocation()
        }

    }

    val REQCODE = 123

    fun checkLocationPermission(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQCODE)
        }
        else{
            getUserLocation()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQCODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) getUserLocation()
                else Toast.makeText(this, "Request Denied", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getUserLocation(){
        var myLocation = MylocationListner()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocation)
        var myThread = myThread()
        myThread.start()
//        myThread.stop()

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    // Get user Location class


    var location :Location? = null

    inner class MylocationListner() : LocationListener {

        init {
            location = Location("Start")
            location!!.latitude = 0.00
            location!!.longitude = 0.00
        }

        override fun onLocationChanged(loc: Location) {
            location = loc

        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    }

    inner class myThread() : Thread() {

        init {
            oldLocation = Location("Start")
            oldLocation!!.latitude = 0.00
            oldLocation!!.longitude = 0.00
        }

        override fun run() {

            while (true){

                if (oldLocation!!.distanceTo(location)==0f){
                    continue
                }
                oldLocation=location

                try {

                    runOnUiThread{
                        // Add a marker in Sydney and move the camera
                        mMap.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet("My current location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        )


                        for (i in 0..listofPockemon.size-1){

                            var items = listofPockemon[i]
                            if (!items.isCatched){
                                val pockemoncoordinates = LatLng(items.location!!.latitude, items.location!!.longitude)

//                                Toast.makeText(applicationContext,location!!.distanceTo(items.location).toString(),Toast.LENGTH_SHORT).show()
//                                Log.d("difference",location!!.distanceTo(items.location).toString())


                                if (location!!.distanceTo(items.location)<100){
                                    items.isCatched=true
                                    listofPockemon[i]=items
                                    powerColleted+= items.power!!
                                    Toast.makeText(applicationContext,"Pockemon Collected having power ${items.power}",Toast.LENGTH_SHORT).show()
                                    continue
                                }

                                mMap.addMarker(MarkerOptions()
                                        .position(pockemoncoordinates)
                                        .title(items.title,)
                                        .snippet(items.des+" Power of Pockemon= ${items.power}")
                                        .icon(BitmapDescriptorFactory.fromResource(items.image!!))
                                )


                            }

                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f))

                    }

                    sleep(1000)

                }catch (ex: Exception){}


            } // while loop ended here

        }
    }

    var listofPockemon = ArrayList<Pockemon>()

    fun addPockemon(){
        listofPockemon.add(Pockemon("Pockemon Ball","BBD Lucknow",10.00,81.0590,26.8887,false,R.drawable.pokeballsmall))
        listofPockemon.add(Pockemon("Turtle","IITT Naya Raipur",40.00,21.1285,81.7662,false,R.drawable.bulbasaur))
        listofPockemon.add(Pockemon("Dragon","IIT Kanpur",70.00,26.5123,80.2329,false,R.drawable.charmander))
        listofPockemon.add(Pockemon("Square Turtle","IIT Kharagpur",555.00,22.3145,87.3091,false,R.drawable.squirtle))

        listofPockemon.add(Pockemon("Pockemon Ball","Takshashila, IIM Rd,",10.00,11.2933,75.8731,false,R.drawable.pokeballsmall))
        listofPockemon.add(Pockemon("Turtle","IIT Gandhinagar",40.00,23.2115,72.6842,false,R.drawable.bulbasaur))
        listofPockemon.add(Pockemon("Dragon","Indian Institute Of Technology, Chenna",70.00,13.0064,80.2426,false,R.drawable.charmander))
        listofPockemon.add(Pockemon("Square Turtle","NIT Agartala, Paschim Barjalai, Tripura ",555.00,23.8409,91.4214,false,R.drawable.squirtle))

        listofPockemon.add(Pockemon("Square Turtle","IIT Jammu",555.00,32.8036,74.8962,false,R.drawable.squirtle))

    }
}