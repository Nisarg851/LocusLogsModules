package com.example.geofencing;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.geofencing.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    Polyline radiusLine;
    double earthRadius = 6378100.0;
    double ratioForMeterToLatLanConversion = 69/earthRadius;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        double latitude = -34, longitude = 151;
        LatLng sydney = new LatLng(latitude,longitude);
        Marker pos = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // Setting initial radius to 500m
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15.5f));
        double initialRadius = 500;

        Circle fence = mMap.addCircle(new CircleOptions()
                                        .center(sydney)
                                        .radius(initialRadius)
                                        .fillColor(Color.argb(55,255,0,0))
                                        .strokeColor(Color.BLACK)
                                    );

        radiusLine = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(latitude,longitude))
                            .add(new LatLng(latitude,longitude+(initialRadius*ratioForMeterToLatLanConversion)))
                            .width(3f));

//        mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(latitude,longitude+(initialRadius/2*ratioForMeterToLatLanConversion)))
//                            .title(new String().valueOf(initialRadius))
//                            .flat(true));

        // Changing the radius of fence according to zooming level
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                double zoomLevel = mMap.getCameraPosition().zoom;
                // equation to calculate meters from zoom level
                int maxZoomLevel = 22;
                int offset = 11;    // to keep little space between screen-size edge and fence.
                                    // if its 0, than diameter of fence would be equal to width of screen
                double newRadius = maxZoomLevel*(Math.pow(2,(maxZoomLevel-(zoomLevel+1))))/2 - offset;
                fence.setRadius(newRadius);
                pos.setSnippet("Fence radius: "+ new String().valueOf(newRadius));
                radiusLine.remove();
                radiusLine = mMap.addPolyline(new PolylineOptions()
                                                .add(new LatLng(latitude,longitude))
                                                .add(new LatLng(latitude,longitude+(newRadius*ratioForMeterToLatLanConversion)))
                                                .width(3f));
            }
        });
    }
}