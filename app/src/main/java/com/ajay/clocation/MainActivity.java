package com.ajay.clocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener,GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private TextView latlng;
    private int ProximityRadius = 1000;
    double latitude,longitude;
    private GestureDetector gestureDetector;
    TextToSpeech speaker;
    private static final int SWIPE_THRESHOLD =100 ;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            getCurrentLocation();
        }else{
            Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gestureDetector = new GestureDetector(this,this);
        gestureDetector.setOnDoubleTapListener(this);
        speaker =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.ENGLISH);

                    String toSpeak = ", for banks swipe right, for police stations swipe bottom, for restaurants swipe left";

                    speaker.speak("To search for hospitals swipe top",TextToSpeech.QUEUE_ADD,null);
                    speaker.speak("To  search for banks swipe right",TextToSpeech.QUEUE_ADD,null);
                    speaker.speak("To  search for police stations swipe bottom",TextToSpeech.QUEUE_ADD,null);
                    speaker.speak("To  search for restaurants swipe left",TextToSpeech.QUEUE_ADD,null);
                    speaker.speak("To repeat instructions long press",TextToSpeech.QUEUE_ADD,null);


                }

            }
        });

        //latlng = findViewById(R.id.latlng);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }else {
            getCurrentLocation();
        }

        latlng = findViewById(R.id.tt);
        latlng.setText("Latitude: "+ latitude +"\n"+ "Longitude: "+longitude);



    }

    private void getCurrentLocation(){

        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size()>0){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
//                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
//                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            longitude =72.8349187;
                            latitude = 19.1071059;
                            Log.d("latitude"," " + latitude);
                            //latlng.setText(latitude + " " + longitude);
                            Toast.makeText(MainActivity.this,"Current location "+ latitude + " " +longitude,Toast.LENGTH_SHORT).show();
                            latlng = findViewById(R.id.tt);
                            latlng.setText("Latitude: "+ latitude +"\n"+ "Longitude: "+longitude);
                        }
                    }
                }, Looper.getMainLooper());

    }

    private String getUrl(double latitude,double longitude,String nearByplace){
        StringBuilder googleUrl =new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=" + latitude +","+longitude);
//        googleUrl.append("location=" + latitude +","+longitude);
        googleUrl.append("&radius="+ProximityRadius);
        googleUrl.append("&type="+nearByplace);
        googleUrl.append("&sensor=true");
        googleUrl.append("&key="+"AIzaSyBnWlXSKsbqeTwgnGbMp0V7OqIMT6I08u0");

        Log.d("GoogleActivity","url= "+googleUrl.toString());

        return googleUrl.toString();
    }


//    public void onClick(View view) {
//
//        String hospital= "hospital", police="police", restaurant="restaurant", bank="bank";
//        String url = "";
//        Intent intent=new Intent(this,Main2Activity.class);
//
//        switch (view.getId()){
//
//            case R.id.hospital:
//                Log.d("lat", " "+latitude);
//                url=getUrl(latitude,longitude,hospital);
//                Log.d("switch", "hosp");
//                break;
//
//            case R.id.police:
//                url=getUrl(latitude,longitude,police);
//                Log.d("switch", "police");
//
//                break;
//
//            case R.id.restaurant:
//                url=getUrl(latitude,longitude,restaurant);
//
//                break;
//
//            case R.id.bank:
//                url=getUrl(latitude,longitude,bank);
//
//                break;
//        }
//
//        Log.d("before","" + latitude);
//        intent.putExtra("url", url);
//        intent.putExtra("lat", latitude);
//        intent.putExtra("lng",longitude);
//        startActivity(intent);
//    }

    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        speaker.speak("To search for hospitals swipe top",TextToSpeech.QUEUE_ADD,null);
        speaker.speak("To  search for banks swipe right",TextToSpeech.QUEUE_ADD,null);
        speaker.speak("To  search for police stations swipe bottom",TextToSpeech.QUEUE_ADD,null);
        speaker.speak("To  search for restaurants swipe left",TextToSpeech.QUEUE_ADD,null);
        speaker.speak("To repeat instructions long press",TextToSpeech.QUEUE_ADD,null);



    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    result = true;
                }
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                result = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;


    }

    private void onSwipeRight() {
        String url = "";
        Intent intent=new Intent(this,Main2Activity.class);

        url=getUrl(latitude,longitude,"bank");


        intent.putExtra("type","bank");
        intent.putExtra("url", url);
        intent.putExtra("lat", latitude);
        intent.putExtra("lng",longitude);
        startActivity(intent);
    }

    private void onSwipeLeft() {
        String url = "";
        Intent intent=new Intent(this,Main2Activity.class);

        url=getUrl(latitude,longitude,"restaurant");


        intent.putExtra("type","restaurant");
        intent.putExtra("url", url);
        intent.putExtra("lat", latitude);
        intent.putExtra("lng",longitude);
        startActivity(intent);
    }

    private void onSwipeBottom() {
        String url = "";
        Intent intent=new Intent(this,Main2Activity.class);

        url=getUrl(latitude,longitude,"police");


        intent.putExtra("type","police");
        intent.putExtra("url", url);
        intent.putExtra("lat", latitude);
        intent.putExtra("lng",longitude);
        startActivity(intent);
    }

    private void onSwipeTop() {


        String url = "";
        Intent intent=new Intent(this,Main2Activity.class);

        url=getUrl(latitude,longitude,"hospital");


        intent.putExtra("type","hospital");
        intent.putExtra("url", url);
        intent.putExtra("lat", latitude);
        intent.putExtra("lng",longitude);
        startActivity(intent);

    }

    @Override
    public void onInit(int status) {

    }
}
