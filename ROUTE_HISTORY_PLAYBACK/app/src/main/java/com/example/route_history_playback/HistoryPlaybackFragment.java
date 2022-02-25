package com.example.route_history_playback;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.MemoryFile;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;

public class HistoryPlaybackFragment extends Fragment implements OnMapReadyCallback {
    // DATA
    private double[][] route = SAMPLE_DATA.getRoute();
    int totalCoordInRoute = SAMPLE_DATA.getTotalCoordInRoute();
    private String[] timeStampData = SAMPLE_DATA.getTimeStampData();

    // MAP components
    MapView mapView;
    GoogleMap mMap;
    final private String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    Marker vehicle;
//    double[][] route;
    boolean userAction = false;
    private PolylineOptions polylineOptions;
    boolean setNewPolyLine = false;

    // SEEKBAR components
    private View view;
    private Button playButton, pauseButton;
    private SeekBar seekBar;
    private TextView timeStamp, dateStamp;
    private int progress = 0;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_playback, container, false);
        initiateGoogleMaps(savedInstanceState);
        return view;
    }

    private void initiateGoogleMaps(Bundle savedInstanceState){
        // MapView initialization
        Bundle mapViewBundle = null;
        if(savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if(mapViewBundle == null){
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        playButton = view.findViewById(R.id.playButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        timeStamp = view.findViewById(R.id.timeStamp);
        dateStamp = view.findViewById(R.id.dateStamp);
        seekBar = view.findViewById(R.id.seekBar);

        seekBar.setMax(route.length);

        playButton.setOnClickListener(playButtonView -> {
            playButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ++progress;
                    if(userAction)
                        setNewPolyLine=true;
                    userAction = false;
                    if(progress==totalCoordInRoute)
                        timer.cancel();
                    seekBar.setProgress(progress);
                }
            },500,250);
        });

        pauseButton.setOnClickListener(pauseButtonView -> {
            pauseButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.VISIBLE);
            timer.cancel();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressChange, boolean fromUser) {
                if(fromUser){
                    userAction = true;
                    pauseButton.callOnClick();
                }
                progress = progressChange;
                String date = timeStampData[progress].substring(0,10);
                String time = timeStampData[progress].substring(11);
                Log.i("Progress: ", "onProgressChanged: "+progress);
                timeStamp.setText(time);
                dateStamp.setText(date);
                moveVehicle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        vehicle = mMap.addMarker(createMarker(route[0][0],route[0][1],route[0][2]));
    }

    private MarkerOptions createMarker(double latitude, double longitude, double angle){
        LatLng latLng = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike));
        markerOptions.anchor(0.5f,0.5f);
        markerOptions.position(latLng);
        markerOptions.rotation((float) angle);
        markerOptions.title("your vehicle");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        polylineOptions = new PolylineOptions().add(new LatLng(route[0][0], route[0][1]));
        mMap.addPolyline(polylineOptions);
        mMap.setMinZoomPreference(13.5f);
        return markerOptions;
    }

    private void moveVehicle(int nextCoord){
        if(nextCoord == route.length)
            return;
        LatLng startCoordPoint = new LatLng(vehicle.getPosition().latitude, vehicle.getPosition().longitude);
        LatLng nextCoordPoint = new LatLng(route[nextCoord][0], route[nextCoord][1]);
        float angle = (float) route[nextCoord][2];
        vehicle.setRotation(angle);
        smoothTransition(startCoordPoint, nextCoordPoint);
    }

    private void smoothTransition(LatLng startCoordPoint, LatLng finalCoordPoint){
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 250;

        handler.post(new Runnable() {
            long elasped;
            float time, velocity;
            @Override
            public void run() {
                elasped = SystemClock.uptimeMillis() - start;
                time = elasped/durationInMs;
                velocity = interpolator.getInterpolation(time);
                LatLng prevPosition = new LatLng(vehicle.getPosition().latitude, vehicle.getPosition().longitude);
                LatLng currentPosition = new LatLng(
                        startCoordPoint.latitude*(1-time)+finalCoordPoint.latitude*time,
                        startCoordPoint.longitude*(1-time)+finalCoordPoint.longitude*time
                );
                vehicle.setPosition(currentPosition);
                if(setNewPolyLine){
                    polylineOptions = new PolylineOptions().add(new LatLng(vehicle.getPosition().latitude, vehicle.getPosition().longitude));
                    mMap.addPolyline(polylineOptions);
                    setNewPolyLine=false;
                }else if(!userAction) {
                    polylineOptions = polylineOptions.add(currentPosition).width(18f).color(0xf00000ff);
                    mMap.addPolyline(polylineOptions);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
                if(time<1){
                    handler.postDelayed(this, 20);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    /*
    * Oiginal data-set to use
    * status date-time-minute lat lng angle speed duration top_speed avg_speed distance data_count
        {
            {"Parked",	"17-12-2021 00:00:00",	21.186036,	72.822236,	315,	0,	2089,	0,	0,	0,	    3},
            {"Idle",   "17-12-2021 00:34:49",	21.186104,	72.822244,	219,    0,	59,	    0,	0,	0,	    1},
            {"Moving", "17-12-2021 00:35:48",	21.186258,	72.822151,	45,	    11,	10,	    11,	11,	0,	    1},
            {"Moving", "17-12-2021 00:35:58",	21.186467,	72.822516,	59,	    18,	10,	    18,	29,	0.044,	2},
            {"Moving", "17-12-2021 00:36:08",	21.186629,	72.822929,	73,	    10,	23,	    18,	13,	0.09,	3},
            {"Parked", "17-12-2021 00:36:31",	21.186662,	72.823044,	189,	0,	167,	0,	0,	0,	    2},
            {"Idle",   "17-12-2021 00:39:18",	21.186667,	72.823044,	160,	0,	47,	    0,	0,	0,	    1},
            {"Moving",  "17-12-2021 00:40:05",	21.186624,	72.822876,	250,	15,	10,	    15,	15,	0,	    1},
            {"Moving",  "17-12-2021 00:40:15",	21.186311,	72.822364,	234,	25,	10,	    25,	40,	0.063,	2},
            {"Moving",	"17-12-2021 00:40:25",	21.185933,	72.821733,	234,	30,	4,	30,	70,	0.141,	3},
            {"Moving",	"17-12-2021 00:40:29",	21.185689,	72.821489,	206,	33,	10,	33,	103,	0.178,	4},
            {"Moving",	"17-12-2021 00:40:39",	21.184876,	72.82112,	223,	30,	2,	33,	133,	0.276,	5},
            {"Moving",	"17-12-2021 00:40:41",	21.184807,	72.820991,	244,	28,	10,	33,	161,	0.291,	6},
            {"Moving",	"17-12-2021 00:40:51",	21.184611,	72.82012,	251,	16,	10,	33,	177,	0.384,	7},
            {"Moving",	"17-12-2021 00:41:01",	21.184344,	72.819587,	229,	23,	5,	33,	200,	0.447,	8},
            {"Moving",	"17-12-2021 00:41:06",	21.184047,	72.819382,	209,	27,	10,	33,	227,	0.486,	9},
            {"Moving",	"17-12-2021 00:41:16",	21.183516,	72.819053,	206,	22,	10,	33,	249,	0.554,	10},
            {"Moving",	"17-12-2021 00:41:26",	21.18302,   72.818884,	188,	20,	10,	33,	269,	0.612,	11},
            {"Moving",	"17-12-2021 00:41:36",	21.182402,	72.818773,	191,	28,	10,	33,	297,	0.682,	12},
            {"Moving",	"17-12-2021 00:41:46",	21.181551,	72.81868,	188,	37,	10,	37,	334,	0.777,	13},
            {"Moving",	"17-12-2021 00:41:56",	21.180844,	72.818582,	156,	15,	1,	37,	349,	0.856,	14},
            {"Moving",	"17-12-2021 00:41:57",	21.180816,	72.818596,	133,	18,	2,	37,	367,	0.859,	15},
            {"Moving",	"17-12-2021 00:41:59",	21.180778,	72.818689,	111,	24,	10,	37,	391,	0.87,	16},
            {"Moving",	"17-12-2021 00:42:09",	21.180669,	72.819542,	100,	34,	10,	37,	425,	0.959,	17},
        }
    * */
}