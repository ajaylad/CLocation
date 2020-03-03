package com.ajay.clocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;


//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity implements TextToSpeech.OnInitListener,GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final String LOG_TAG = "ExampleApp";
    TextView t1;
    private ArrayList<Place> res;
    private String phurl;
    private ArrayList<String> ph;
    String type;
    String str;
    private ArrayList<Place> resultList = null;
    private String contactList = null;
    private GestureDetector gestureDetector;
    TextToSpeech speaker;
    private static final int SWIPE_THRESHOLD =100 ;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    int pos = 0;

    private ArrayList<String> hosp = new ArrayList<String>(Arrays.asList("Navkar Eye Care Centre 33/34 Kapol Niwas, S V Road, New Putalibai, Vile Parle West (West), Mumbai", "HBT Medical College And Dr. R N Cooper Municipal General Hospital U 15, Bhaktivedanta Swami Road, JVPD Scheme, Juhu, Mumbai", "Advanced Multispeciality Hospital CMPH Medical College Campus, Natakar Gadkari Marg, Gulmohar Road, Irla, Vile Parle West, Mumbai", "Sunridges specialty Hospital 17 Navyug , V. L, Vaikunthlal Mehta Road, JVPD Scheme, Vile Parle, W", "The Vission Eye Center Near IndusInd Bank, 101 Hiralaya Apartment, Ashok Nagar, Near IndusInd Bank, North South Road Number 10, JVPD Scheme, Mumbai", "KLS Memorial Hospital Irla, Vile Parle (West), Mumbai", "RichFeel Trichology Center Shop No. 1, Ground Floor, Shree Mangal, Society, 167, SV Road, Vile Parle West, Mumbai", "Dr Ashit Sheth, Dr Mohit Shah (Unlimited Potentialities) 701 Leela Apartments, Opp Sony mony, Vile Parle West, Mumbai", "Centre for Sight Eye Hospital Mumbai B101, Dhruv, Opp. Gagandeep ,Main Road, Juhu Lane, Gulmohar Road, MHADA Colony, Vile Parle West, Mumbai", "Saaol Heart Center opp. Marritt Hotel, Rohit Building, Ground Floor J. W., opp. Marritt Hotel, Juhu, Mumbai", "CritiCare Hospital J.V.P.D Plot Number 38/39, Main, Gulmohar Road, Andheri West, Mumbai", "Lotus Eye Hospital Bus Terminus, 13th, North South Road, opposite Juhu, Vithal Nagar, Juhu, Mumbai", "dr R N cooper Unnamed Road, JVPD Scheme, Vile Parle West, Mumbai"));
    private ArrayList<String> bank = new ArrayList<String>(Arrays.asList("Saraswat Bank Ground Floor, Madhusagar, North South Road Number 13, Near, Juhu Church Road, Juhu, Mumbai", "State Bank Of India New Francis Chawl, Irla Society Road, Tank Lank, Vile Parle (W, Indira Nagar, Vile Parle West, Mumbai", "ICICI BANK ATM S. V. Road, Irla, Vile Parle (W), Mumbai", "UCO Bank Vile Pare East Branch Plot3/21Jvpds, Juhu Ns Road 10, Al-Nemat Bldg Juhu, Vile Parle, Mumbai", "Oriental Bank of Commerce 66, Guru Darshan, 1, North South Road Number 5, JVPD Scheme, Vile Parle West, Mumbai", "Bank of Maharashtra Usha Pravin Gandhi Marg Presidency Society, Nutan Laxmi Society, Vile Parle West, Mumbai", "ICICI Bank Radha Kunj, Azadnagar, North South Road Number 1, JVPD Scheme, Vile Parle, W", "Indian Overseas Bank Juhu Branch N South Road Number 9, JVPD Scheme, Juhu, Mumbai", "Axis Bank ATM Bhanu Soda Pub Ghatkopar Opposite Sarvodaya Hospital", "HDFC Bank 30, Navyog SOC Krishna Kunj, VL Mehta Rd, Juhu, Vile Parle West, Mumbai", "Axis Bank Ground Floor, Vardhman, V, Vaikunthlal Mehta Road, JVPD Scheme, Vile Parle West, Mumbai", "Kotak Mahindra Bank Plot No.10, Maitri, North South Road Number 10, Nutan Laxmi Colony, Juhu, Mumbai"));
    private ArrayList<String> restaurants = new ArrayList<String> (Arrays.asList("The Butler and the Bayleaf Hotel Kings international ,1st floor ,5 juhu Tara Road Above Alfredos ,opp st josephs church, Juhu, Mumbai", "Hotel Canara Hari Vilas Bar WING A, SV Road, Indira Nagar, Prem Nagar, Vile Parle West, Mumbai", "Ice n Rolls 26, North South Road Number 1, Navyug Society, Swastik Society, Juhu, Mumbai", "Kailash Parbat Shop no. 4/5, Hazrabai House, Alfa Market, Irla Society Road, Vile Parle West, Mumbai", "Juhu Club Millennium 1, Gulmohar Road, Juhu, Mumbai", "Tibbs Frankie VM Road, Opposite Options Mall, Sahakari Bhandar, Vile Parle West, Airport Area, Juhu, Mumbai", "Naturals Ice Creams Shop 1 & 2, Royal Chambers, Opposite Juhu Club Millenium, JVPD Scheme, Vile Parle West, Mumbai", "Sharma Chaat Bhandar 15, Amrapali shopping centre, and, Vaikunthlal Mehta Road, JVPD Scheme, Juhu, Mumbai", "Orbango Juice (Juhu) Opposite Options Mall, VL Mehta Road, Juhu, Yamuna Nagar, Nehru Nagar, Airport Area, Juhu, Mumbai", "True Tramm Trunk First Floor, Juhu Vaishali Shopping Complex ,VL Mehta Road , J.V.P.D. Scheme, Next To Options Showroom, Juhu, Mumbai", "Harish Lunch Home 115, SV Road, Irla, Vile Parle West, Mumbai", "Hotel Ashwini No. 5, Vaikunthlal Mehta Road, Vile Parle West, Mumbai", "Utsav Restaurant V.M. Road, Near Sahakari Bhandar, Mithila, Juhu, Vile Parle West, Mumbai", "Gattu'S Chinese Shop No. 3, Kadri Park Building, Iria, Irla, Vile Parle West, Mumbai", "Med Meals Nanavati Hospital, S.V. Road, :shoeb@medmeals, E-mail, Vile Parle West, Mumbai"));
    private ArrayList<String> tn ;
    private ArrayList<String> tp ;
    private ArrayList<String> police = new ArrayList<String>(Arrays.asList("Juhu Police Station V M Road, Near Kalaniketan, Vile Parle West, Yamuna Nagar, Nehru Nagar, Juhu, Mumbai"));
    private ArrayList<String> hospnum = new ArrayList<String> (Arrays.asList("022 2624 0304", "022 2620 7254","022 2621 3500","022 6115 5600","098676 56060","022 6171 3939","079000 82222","022 2671 0606","022 2628 3610","093220 94948","022 6775 6600","022 2620 7352","022 2620 7254"));
    private ArrayList<String> banknum = new ArrayList<String>(Arrays.asList("022 2611 9121","1800 425 3800","022 3366 7777","1800 103 0123","1800 180 1235","022 2620 7654","022 3366 7777","1800 425 4445","092432 34663","075739 19585","092432 34663","1860 266 2666"));
    private ArrayList<String> resnum = new ArrayList<String>(Arrays.asList("091675 70591","022 2670 8566","090228 17788","022 2628 8169","022 6684 2002","022 2611 8787","090762 92362","099203 26548","022 6555 0705","077389 93360","022 2671 5758","084220 00520","022 2611 1244","086551 10777","098673 39172"));
    private ArrayList<String> polnum = new ArrayList<String>(Arrays.asList("022 2618 3856"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.gestureDetector = new GestureDetector(this,this);
        gestureDetector.setOnDoubleTapListener(this);
        speaker =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.ENGLISH);
                    speaker.speak("Swipe left and right to search for" + type,TextToSpeech.QUEUE_ADD,null);
                    speaker.speak("Double tap to call",TextToSpeech.QUEUE_ADD,null);
                }

            }
        });

        Bundle intent = getIntent().getExtras();
        str = intent.getString("url");
        type = intent.getString("type");
//        Double latitude = intent.getDouble("lat",0);
//        Double longitude = intent.getDouble("lng",0);
//        Double latitude = 18.9442135;
        Double latitude = 19.1071059;
//        Double longitude = 72.8332599;
        Double longitude = 72.8349187;

        t1 = findViewById(R.id.textView);

        Log.d("sizeeeeeeeeeeeeee", hosp.size() +" " + hospnum.size());
//        t2 = findViewById(R.id.textView2);

//        t2.setText(Double.toString(latitude) + " " + Double.toString(longitude));

        try {
            res = new Search().execute().get();
            Log.d("finalRes", ""+res);
//            for (int i=0;i< res.size();i++){
//                String pid = res.get(i).place_id;
//                phurl = getPhurl(pid);
//                Log.d("in Contact", "in contact");
//                try {
//                    ph.add(new Contact().execute().get());
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            Log.d("final",""+res.get(0).name);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("check", ""+res);

        switch (type){
            case "bank":
                tn = bank;
                tp=banknum;
                break;
            case "restaurant":
                tn = restaurants;
                tp = resnum;
                break;
            case "police":
                tn=police;
                tp = polnum;
                break;
            case "hospital":
                tn=hosp;
                tp=hospnum;
                break;
        }



        t1.setText(tn.get(pos));

        Log.d("places",""+resultList);
        Log.d("resplaces","");

//        res = search(url);

        Log.d("phhhh",""+ph);
        Log.d("resu", ""+ res);
    }

    private String getPhurl(String place_id){
        StringBuilder googleUrl =new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googleUrl.append("place_id=" + place_id);
        googleUrl.append("&fields=formatted_phone_number");
        googleUrl.append("&key="+"AIzaSyBnWlXSKsbqeTwgnGbMp0V7OqIMT6I08u0");
        return googleUrl.toString();
    }
    @Override
    public void onInit(int status) {

    }

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

        String number = tp.get(pos);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        startActivity(callIntent);

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
        speaker.speak(tn.get(pos),TextToSpeech.QUEUE_FLUSH,null,"");
        speaker.speak("Double tap to call",TextToSpeech.QUEUE_ADD,null,"");
        speaker.speak("Long press to repeat instructions",TextToSpeech.QUEUE_ADD,null,"");
        speaker.speak("To go back swipe down",TextToSpeech.QUEUE_ADD,null,"");
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

        pos--;
        if (pos < 0){
            pos = tn.size()-1;
        }
        t1.setText(tn.get(pos));
        String spk = tn.get(pos);
        speaker.speak(spk,TextToSpeech.QUEUE_FLUSH,null,"");
//        speaker.speak(tn.get(pos),TextToSpeech.QUEUE_FLUSH,null);
        speaker.speak("Double tap to call",TextToSpeech.QUEUE_ADD,null,"");
        speaker.speak("Long press to repeat instructions",TextToSpeech.QUEUE_ADD,null,"");
        speaker.speak("To go back swipe down",TextToSpeech.QUEUE_ADD,null,"");

    }

    private void onSwipeLeft() {

        pos++;
        if (pos >= tn.size()){
            pos = 0;
        }
        t1.setText(tn.get(pos));
        String spk = tn.get(pos);
        speaker.speak(spk,TextToSpeech.QUEUE_FLUSH,null,"");
//        speaker.speak(tn.get(pos),TextToSpeech.QUEUE_FLUSH,null);
        speaker.speak("Double tap to call",TextToSpeech.QUEUE_ADD,null,"");
        speaker.speak("Long press to repeat instructions",TextToSpeech.QUEUE_ADD,null,"");
        speaker.speak("To go back swipe down",TextToSpeech.QUEUE_ADD,null,"");

    }

    private void onSwipeBottom() {
        Intent back=new Intent(this,MainActivity.class);
        startActivity(back);
    }

    private void onSwipeTop() {
    }


    public class Contact extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection = null;
            StringBuilder jsondetails = new StringBuilder();

            try {
                StringBuilder sb = new StringBuilder();
                sb.append(phurl);
                URL url = new URL(sb.toString());
                Log.d("checkUrl", ""+url);
                connection = (HttpURLConnection) url.openConnection();
                try (InputStreamReader in = new InputStreamReader(connection.getInputStream())) {

                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsondetails.append(buff, 0, read);
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error processing Places API URL", e);
                return contactList;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to Places API", e);
                return contactList;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsondetails.toString());
                Log.d("phoneeeee", ""+ jsonObj);
                JSONArray predsJsonArray = jsonObj.getJSONArray("results");




                contactList =new String();
                Log.d("phoneeeee",""+contactList);
                Log.d("json",""+predsJsonArray );
                contactList = predsJsonArray.getJSONObject(0).getString("formatted_phone_number");;
//                contactList = predsJsonArray.getString("");
                // Extract the Place descriptions from the results
//                resultList = new ArrayList<Place>(predsJsonArray.length());
//                Log.d("resultList"," "+resultList);

//                for (int i = 0; i < predsJsonArray.length(); i++) {
//                    String a = predsJsonArray.getJSONObject(i).getString("reference");
//                    Place place = new Place();
//                    place.reference = predsJsonArray.getJSONObject(i).getString("reference");
//                    place.name = predsJsonArray.getJSONObject(i).getString("name");
//                    place.vicinity = predsJsonArray.getJSONObject(i).getString("vicinity");
//                    place.place_id = predsJsonArray.getJSONObject(i).getString("place_id");
//                    Log.d("inForName",predsJsonArray.getJSONObject(i).getString("name")+" " +place.vicinity);
//                    resultList.add(place);
//
//                }
                Log.d("places",""+contactList);
//
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error processing JSON results", e);
            }

            return contactList;
        }
    }






    public class Search extends AsyncTask<String,Void,ArrayList>{

        @Override
        protected ArrayList<Place> doInBackground(String... strings) {

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {

                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        jsonResults.append(buff, 0, read);
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error processing Places API URL", e);
                return resultList;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to Places API", e);
                return resultList;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("results");


                Log.d("results", ""+predsJsonArray);

                // Extract the Place descriptions from the results
                resultList = new ArrayList<Place>(predsJsonArray.length());
                Log.d("resultList"," "+resultList);

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    String a = predsJsonArray.getJSONObject(i).getString("reference");
                    Place place = new Place();
                    place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                    place.name = predsJsonArray.getJSONObject(i).getString("name");
                    place.vicinity = predsJsonArray.getJSONObject(i).getString("vicinity");
                    place.place_id = predsJsonArray.getJSONObject(i).getString("place_id");
                    Log.d("inForName",predsJsonArray.getJSONObject(i).getString("name")+" " +place.vicinity);
                    resultList.add(place);

                }
                Log.d("places",""+resultList);
//
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error processing JSON results", e);
            }

            return resultList;


//            return null;


        }
    }




//    public static ArrayList<Place> search(String str) {
//        ArrayList<Place> resultList = null;
//
//
//        HttpURLConnection conn = null;
//        StringBuilder jsonResults = new StringBuilder();
//        try {
//            StringBuilder sb = new StringBuilder();
//            sb.append(str);
//            URL url = new URL(sb.toString());
//            conn = (HttpURLConnection) url.openConnection();
//            try (InputStreamReader in = new InputStreamReader(conn.getInputStream())) {
//
//                int read;
//                char[] buff = new char[1024];
//                while ((read = in.read(buff)) != -1) {
//                    jsonResults.append(buff, 0, read);
//                }
//            }
//        } catch (MalformedURLException e) {
//            Log.e(LOG_TAG, "Error processing Places API URL", e);
//            return resultList;
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error connecting to Places API", e);
//            return resultList;
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//
//        try {
//            // Create a JSON object hierarchy from the results
//            JSONObject jsonObj = new JSONObject(jsonResults.toString());
//            JSONArray predsJsonArray = jsonObj.getJSONArray("results");
//
//
//            Log.d("results", ""+predsJsonArray);
//
//            // Extract the Place descriptions from the results
//            resultList = new ArrayList<Place>(predsJsonArray.length());
//            Log.d("msg"," "+resultList);
//
//            for (int i = 0; i < predsJsonArray.length(); i++) {
//                String a = predsJsonArray.getJSONObject(i).getString("reference");
//                Place place = new Place();
//                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
//                place.name = predsJsonArray.getJSONObject(i).getString("name");
//                resultList.add(place);
//
//            }
////            for (int i = 0; i < predsJsonArray.length(); i++) {
////                Place place = new Place();
////                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
////                place.name = predsJsonArray.getJSONObject(i).getString("name");
////                resultList.add(place);
////            }
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Error processing JSON results", e);
//        }
//
//        return resultList;
//    }





}
