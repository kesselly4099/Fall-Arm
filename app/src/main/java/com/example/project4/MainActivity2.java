//package com.example.project4;
//import com.google.android.gms.tasks.CancellationTokenSource;
//import com.google.android.gms.location.LocationRequest;
//
//
//import android.Manifest;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import com.android.volley.Request;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class MainActivity2 extends AppCompatActivity implements SensorEventListener {
//
//    private static final long TWO_HOURS_IN_MILLIS = 7200000; // Two hours in milliseconds
//    private static final int LOCATION_REQUEST_CODE = 1;
//    private static final String PREFERENCE_NAME = "your_preference_name";
//
//    private SensorManager sensorManager;
//    private FusedLocationProviderClient fusedLocationClient;
//    private long lastDataSentTime = 0;
//
//    private TextView accelerometerXTextView, accelerometerYTextView, accelerometerZTextView;
//    private TextView gyroscopeXTextView, gyroscopeYTextView, gyroscopeZTextView;
//    private TextView locationText1;
//    private Geocoder geocoder;
//
//
//
//
//    private SharedPreferences sharedPref;
//    private String patientName, patientId;
//    private double latitude, longitude;
//
//
//    private Handler handler;
//    private Runnable sendDataRunnable;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//
//        geocoder = new Geocoder(this, Locale.getDefault());
//        initializeSharedPrefs();
//        initializeSensors();
//        initializeTextViews();
//        initializeLocationClient();
//        initializeHandler();
//
//        requestLocationPermissionIfNeeded();
//        handler.postDelayed(sendDataRunnable, TWO_HOURS_IN_MILLIS);
//
//
//
//        getLastLocation();
//
//        handler.postDelayed(sendDataRunnable, TWO_HOURS_IN_MILLIS);
//        requestLocationPermissionIfNeeded();
//        getLastLocation();
//    }
//
//
//
//
//
//
//    private void initializeSharedPrefs() {
//        sharedPref = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//        patientName = sharedPref.getString("patient_name_key", "Default Name222");
//        patientId = sharedPref.getString("patient_id_key", "Default ID");
//        latitude = Double.parseDouble(sharedPref.getString("latitude_key", "0.0"));
//        longitude = Double.parseDouble(sharedPref.getString("longitude_key", "0.0"));
//    }
//
//    private void initializeSensors() {
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        registerSensor(Sensor.TYPE_ACCELEROMETER);
//        registerSensor(Sensor.TYPE_GYROSCOPE);
//    }
//
//    private void initializeTextViews() {
//        accelerometerXTextView = findViewById(R.id.accelerometerX);
//        accelerometerYTextView = findViewById(R.id.accelerometerY);
//        accelerometerZTextView = findViewById(R.id.accelerometerZ);
//        gyroscopeXTextView = findViewById(R.id.gyroscopeX);
//        gyroscopeYTextView = findViewById(R.id.gyroscopeY);
//        gyroscopeZTextView = findViewById(R.id.gyroscopeZ);
//        locationText1 = findViewById(R.id.locationText);
//    }
//
//    private void initializeLocationClient() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//    }
//
//    private void initializeHandler() {
//        sendDataRunnable = new Runnable() {
//            @Override
//            public void run() {
//                getLastLocation();
//                sendDataToServer(patientName, patientId, latitude, longitude, new float[]{0, 0, 0},);
//                handler.postDelayed(this, TWO_HOURS_IN_MILLIS);
//            }
//        };
//        handler = new Handler();
//    }
//
//    private void registerSensor(int sensorType) {
//        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
//        if (sensor != null) {
//            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }
//
//
//
//
//    private boolean detectFall(float[] sensorValues) {
//
//
//
//        return sum(sensorValues) > 5.500000; // Example threshold condition
//    }
//    private float sum(float[] values) {
//        float total = 0;
//        for (float value : values) {
//            total += value;
//        }
//        return total;
//    }
//
//
//
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        // Get the sensor values
//        float[] sensorValues = event.values;
//
//
//        // Access sensor
//        int sensorType = event.sensor.getType();
//        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
//            // Update accelerometer
//            accelerometerXTextView.setText("Accelerometer X: " + event.values[0]);
//            accelerometerYTextView.setText("Accelerometer Y: " + event.values[1]);
//            accelerometerZTextView.setText("Accelerometer Z: " + event.values[2]);
//        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
//            // Update gyroscope
//            gyroscopeXTextView.setText("Gyroscope X: " + event.values[0]);
//            gyroscopeYTextView.setText("Gyroscope Y: " + event.values[1]);
//            gyroscopeZTextView.setText("Gyroscope Z: " + event.values[2]);
//        }
//
//        if (detectFall(event.values)) {
//            sendDataToServer(patientName, patientId, latitude, longitude, sensorValues, address  );
//            lastDataSentTime = System.currentTimeMillis();
//        } else {
//            long currentTime = System.currentTimeMillis();
//            if (currentTime - lastDataSentTime >= 2 * 60 * 60 * 1000) { // 2 hours in milliseconds
//                sendDataToServer(String patientName, String patientId, double latitude, double longitude, String address, float[] sensorValues);
//                lastDataSentTime = currentTime;
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            } else {
//                locationText1.setText("Location permissions denied.");
//            }
//        }
//    }
//
//    private boolean checkLocationPermissions() {
//        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
////    public void updateUIWithLocation(Location location) {
////        try {
////            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
////            if (addresses != null && !addresses.isEmpty()) {
////                Address address = addresses.get(0);
////                locationText1.setText("Address: " + address.getAddressLine(0));
////            } else {
////                locationText1.setText("Unable to get address");
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////            locationText1.setText("Failed to get address");
////        }
////    }
////
////
////
////
////
////
////
////
////
////    public void getLastLocation() {
////        if (checkLocationPermissions()) {
////            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
////            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
////                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
////                        @Override
////                        public void onSuccess(Location location) {
////                            if (location != null) {
////                              updateUIWithLocation(location);
////                            }
////                        }
////                    });
////        } else {
////            requestLocationPermissionIfNeeded();
////        }
////    }
////
////
////
////
////    private void sendDataToServer(String patientName, String patientId, double address, double longitude, float[] sensorValues) {
////
////        String url = "http://10.0.2.2:5000/endpoint";
////
////        JSONObject jsonBody = new JSONObject();
////        try {
////
////            jsonBody.put("patientName", patientName);
////            jsonBody.put("patientId", patientId);
////            jsonBody.put("address",  address);
////            jsonBody.put("longitude", longitude);
////            jsonBody.put("sensorValue1", sensorValues[0]);
////            jsonBody.put("sensorValue2", sensorValues[1]);
////            jsonBody.put("sensorValue3", sensorValues[2]);
////            jsonBody.put("sensorValue4", sensorValues[0]);
////            jsonBody.put("sensorValue5", sensorValues[1]);
////            jsonBody.put("sensorValue6", sensorValues[2]);
////            jsonBody.put("time", getCurrentTime());
////
////            // ... continue for other sensor values
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////
////        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
////                response -> {
////                    // Handle success
////                }, error -> {
////            // Handle error
////        });
////
////        // Add the request to the RequestQueue.
////        Volley.newRequestQueue(this).add(jsonObjectRequest);
////    }
//
//
//    public String updateUIWithLocation(Location location) {
//        String addressString = "Unknown Location";
//        try {
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//                addressString = "Address: " + address.getAddressLine(0);
//                locationText1.setText(addressString);
//            } else {
//                locationText1.setText("Unable to get address");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            locationText1.setText("Failed to get address");
//        }
//        return addressString;
//    }
//
//    public void getLastLocation() {
//        if (checkLocationPermissions()) {
//            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
//            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                String currentAddress = updateUIWithLocation(location);
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                                sendDataToServer(patientName, patientId, latitude, longitude, currentAddress, new float[]{0, 0, 0});
//                            }
//                        }
//                    });
//        } else {
//            requestLocationPermissionIfNeeded();
//        }
//    }
//
//    private void sendDataToServer(String patientName, String patientId, double latitude, double longitude, String address, float[] sensorValues) {
//        String url = "http://10.0.2.2:5000/endpoint";
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("patientName", patientName);
//            jsonBody.put("patientId", patientId);
//            jsonBody.put("latitude", latitude);
//            jsonBody.put("longitude", longitude);
//            jsonBody.put("address", address);
//            jsonBody.put("sensorValue1", sensorValues[0]);
//            jsonBody.put("sensorValue2", sensorValues[1]);
//            jsonBody.put("sensorValue3", sensorValues[2]);
//            jsonBody.put("time", getCurrentTime());
//
//            // ... [Continue adding other sensor values as needed]
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
//                response -> {
//                    // Handle success
//                }, error -> {
//            // Handle error
//        });
//
//        Volley.newRequestQueue(this).add(jsonObjectRequest);
//    }
//
//
//
//
//    private String getCurrentTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        return dateFormat.format(new Date());
//    }
//
//    private void requestLocationPermissionIfNeeded() {
//        if (!checkLocationPermissions()) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        sensorManager.unregisterListener(this);
//    }
//}
//
//
//


package com.example.project4;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {

    private static final long TWO_HOURS_IN_MILLIS = 7200000;
    private static final int LOCATION_REQUEST_CODE = 1;
    private static final String PREFERENCE_NAME = "your_preference_name";

    private SensorManager sensorManager;
    private FusedLocationProviderClient fusedLocationClient;
    private long lastDataSentTime = 0;
    private TextView accelerometerXTextView, accelerometerYTextView, accelerometerZTextView;
    private TextView gyroscopeXTextView, gyroscopeYTextView, gyroscopeZTextView;
    private TextView locationText1;
    private Geocoder geocoder;
    private SharedPreferences sharedPref;
    private String patientName, patientId;
    private double latitude, longitude;
    private Handler handler;
    private Runnable sendDataRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        geocoder = new Geocoder(this, Locale.getDefault());
        initializeSharedPrefs();
        initializeSensors();
        initializeTextViews();
        initializeLocationClient();
        initializeHandler();
        requestLocationPermissionIfNeeded();
        handler.postDelayed(sendDataRunnable, TWO_HOURS_IN_MILLIS);
        getLastLocation();
    }

    private void initializeSharedPrefs() {
        sharedPref = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        patientName = sharedPref.getString("patient_name_key", "Default Name222");
        patientId = sharedPref.getString("patient_id_key", "Default ID");
        latitude = Double.parseDouble(sharedPref.getString("latitude_key", "0.0"));
        longitude = Double.parseDouble(sharedPref.getString("longitude_key", "0.0"));
    }

    private void initializeSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensor(Sensor.TYPE_GYROSCOPE);
    }

    private void registerSensor(int sensorType) {
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void initializeTextViews() {
        accelerometerXTextView = findViewById(R.id.accelerometerX);
        accelerometerYTextView = findViewById(R.id.accelerometerY);
        accelerometerZTextView = findViewById(R.id.accelerometerZ);
        gyroscopeXTextView = findViewById(R.id.gyroscopeX);
        gyroscopeYTextView = findViewById(R.id.gyroscopeY);
        gyroscopeZTextView = findViewById(R.id.gyroscopeZ);
        locationText1 = findViewById(R.id.locationText);
    }

    private void initializeLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initializeHandler() {
        sendDataRunnable = new Runnable() {
            @Override
            public void run() {
                getLastLocation();
                sendDataToServer(patientName, patientId, latitude, longitude, "", new float[]{0, 0, 0});
                handler.postDelayed(this, TWO_HOURS_IN_MILLIS);
            }
        };
        handler = new Handler();
    }

    private boolean detectFall(float[] sensorValues) {
        return sum(sensorValues) > 14.800000;
    }

    private float sum(float[] values) {
        float total = 0;
        for (float value : values) {
            total += value;
        }
        return total;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] sensorValues = event.values;
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            accelerometerXTextView.setText("Accelerometer X: " + event.values[0]);
            accelerometerYTextView.setText("Accelerometer Y: " + event.values[1]);
            accelerometerZTextView.setText("Accelerometer Z: " + event.values[2]);
        } else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            gyroscopeXTextView.setText("Gyroscope X: " + event.values[0]);
            gyroscopeYTextView.setText("Gyroscope Y: " + event.values[1]);
            gyroscopeZTextView.setText("Gyroscope Z: " + event.values[2]);
        }

        if (detectFall(event.values)) {
            sendDataToServer(patientName, patientId, latitude, longitude, "", sensorValues);
            lastDataSentTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - lastDataSentTime >= TWO_HOURS_IN_MILLIS) {
            sendDataToServer(patientName, patientId, latitude, longitude, "", sensorValues);
            lastDataSentTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                locationText1.setText("Location permissions denied.");
            }
        }
    }

    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public String updateUIWithLocation(Location location) {
        String addressString = "Unknown Location";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                addressString = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        locationText1.setText(addressString);
        return addressString;
    }

    private void requestLocationPermissionIfNeeded() {
        if (!checkLocationPermissions()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    private void getLastLocation() {
        if (checkLocationPermissions()) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        String address = updateUIWithLocation(location);
                        sendDataToServer(patientName, patientId, latitude, longitude, address, new float[]{0, 0, 0});
                    }
                }
            });
        }
    }

    private void sendDataToServer(String patientName, String patientId, double latitude, double longitude, String locationName, float[] sensorValues) {
        String url = "http://10.0.2.2:5000/endpoint";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String currentDateAndTime = sdf.format(new Date());

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("patient_name", patientName);
            jsonRequest.put("patient_id", patientId);
            jsonRequest.put("timestamp", currentDateAndTime);
            jsonRequest.put("latitude", latitude);
            jsonRequest.put("longitude", longitude);
            jsonRequest.put("location_name", locationName);
            jsonRequest.put("accelerometer_value", sensorValues[0]);
            jsonRequest.put("gyroscope_value", sensorValues[1]);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, response -> {
                // Handle the response
            }, error -> {
                // Handle error
            });

            Volley.newRequestQueue(this).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && sendDataRunnable != null) {
            handler.removeCallbacks(sendDataRunnable);
        }
    }
}

