//have used code on DJI official page , which is a tutorial about how to implements a app to control their aircraft
//https://developer.dji.com/mobile-sdk/documentation/android-tutorials/ActivationAndBinding.html
package com.dji.GRPDemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;



import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dji.common.flightcontroller.FlightControllerState;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.useraccount.UserAccountState;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.flightcontroller.FlightController;
import dji.common.error.DJIError;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.useraccount.UserAccountManager;

import static java.lang.Thread.sleep;

public class MapActivity extends FragmentActivity implements View.OnClickListener, OnMapClickListener {

    protected static final String TAG = "MapActivity";

    volatile private int infor = -1;
    private MapView mapView;
    private AMap aMap;

    private Button locate, add, clear;
    private Button config, upload, start, stop;

    private Button web;

    private boolean isAdd = false;
    private boolean isWeb = false;

    private double droneLocationLat = 181, droneLocationLng = 181;
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    private Marker droneMarker = null;

    private float altitude = 100.0f;
    private float mSpeed = 10.0f;

    private List<Waypoint> waypointList = new ArrayList<>();

    public static WaypointMission.Builder waypointMissionBuilder;
    private FlightController mFlightController;
    private WaypointMissionOperator instance;
    private WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;

    @Override
    protected void onResume() {
        super.onResume();
        initFlightController();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        removeListener();
        super.onDestroy();
    }

    /**
     * @Description : RETURN Button RESPONSE FUNCTION
     */
    public void onReturn(View view) {
        Log.d(TAG, "onReturn");
        this.finish();
    }

    private void setResultToToast(final String string) {
        MapActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MapActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {

        locate = (Button) findViewById(R.id.locate);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        config = (Button) findViewById(R.id.config);
        upload = (Button) findViewById(R.id.upload);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        config.setOnClickListener(this);
        upload.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        web = (Button) findViewById(R.id.web);
        web.setOnClickListener(this);
    }

    private void initMapView() {

        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);// add the listener for click for amap object
        }

        LatLng shenzhen = new LatLng(29.800288995201665, 121.56115455871578);
        aMap.addMarker(new MarkerOptions().position(shenzhen).title("Marker in Shenzhen"));
        aMap.moveCamera(CameraUpdateFactory.newLatLng(shenzhen));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DJIDemoApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        initMapView();
        initUI();
        addListener();

    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            onProductConnectionChange();
        }
    };

    private void onProductConnectionChange() {
        initFlightController();
        loginAccount();
    }

    //maybe need to delete this
    private void loginAccount() {

        UserAccountManager.getInstance().logIntoDJIUserAccount(this,
                new CommonCallbacks.CompletionCallbackWith<UserAccountState>() {
                    @Override
                    public void onSuccess(final UserAccountState userAccountState) {
                        Log.e(TAG, "Login Success");
                    }

                    @Override
                    public void onFailure(DJIError error) {
                        setResultToToast("Login Error:"
                                + error.getDescription());
                    }
                });
    }

    private void initFlightController() {

        BaseProduct product = DJIDemoApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof Aircraft) {
                mFlightController = ((Aircraft) product).getFlightController();
            }
        }

        if (mFlightController != null) {

            mFlightController.setStateCallback(
                    new FlightControllerState.Callback() {
                        @Override
                        public void onUpdate(FlightControllerState
                                                     djiFlightControllerCurrentState) {
                            droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
                            droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
                            updateDroneLocation();
                        }
                    });

        }
    }

    //Add Listener for WaypointMissionOperator
    private void addListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().addListener(eventNotificationListener);
        }
    }

    private void removeListener() {
        if (getWaypointMissionOperator() != null) {
            getWaypointMissionOperator().removeListener(eventNotificationListener);
        }
    }

    private WaypointMissionOperatorListener eventNotificationListener = new WaypointMissionOperatorListener() {
        @Override
        public void onDownloadUpdate(WaypointMissionDownloadEvent downloadEvent) {

        }

        @Override
        public void onUploadUpdate(WaypointMissionUploadEvent uploadEvent) {

        }

        @Override
        public void onExecutionUpdate(WaypointMissionExecutionEvent executionEvent) {

        }

        @Override
        public void onExecutionStart() {

        }

        @Override
        public void onExecutionFinish(@Nullable final DJIError error) {
            setResultToToast("Execution finished: " + (error == null ? "Success!" : error.getDescription()));
        }
    };

    public WaypointMissionOperator getWaypointMissionOperator() {
        if (instance == null) {
            instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
        }
        return instance;
    }

    @Override
    public void onMapClick(LatLng point) {
        if (isAdd == true) {
            markWaypoint(point);
            Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, altitude);
            //Add Waypoints to Waypoint arraylist;
            if (waypointMissionBuilder != null) {
                waypointList.add(mWaypoint);
                waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
            } else {
                waypointMissionBuilder = new WaypointMission.Builder();
                waypointList.add(mWaypoint);
                waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
            }
        } else {
            setResultToToast("Cannot Add Waypoint");
        }
    }

    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }

    // Update the drone location based on states from MCU.
    private void updateDroneLocation() {

        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        //Create MarkerOptions object
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (droneMarker != null) {
                    droneMarker.remove();
                }

                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    droneMarker = aMap.addMarker(markerOptions);
                }
            }
        });
    }

    private void markWaypoint(LatLng point) {
        //Create MarkerOptions object
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = aMap.addMarker(markerOptions);
        mMarkers.put(mMarkers.size(), marker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locate: {
                updateDroneLocation();
                cameraUpdate(); // Locate the drone's place
                break;
            }
            case R.id.add: {
                enableDisableAdd();
                break;
            }
            case R.id.clear: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aMap.clear();
                    }

                });
                waypointList.clear();
                waypointMissionBuilder.waypointList(waypointList);
                updateDroneLocation();
                break;
            }
            case R.id.config: {
                showSettingDialog();
                break;
            }
            case R.id.upload: {
                uploadWayPointMission();
                break;
            }
            case R.id.start: {
                startWaypointMission();
                break;
            }
            case R.id.stop: {
                stopWaypointMission();
                break;
            }

            case R.id.web: {
                enableDisableweb();
                break;
            }
            default:
                break;
        }
    }

    private void cameraUpdate() {
        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
        float zoomlevel = (float) 18.0;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
        aMap.moveCamera(cu);

    }

    private void enableDisableAdd() {
        if (isAdd == false) {
            isAdd = true;
            add.setText("Exit");
        } else {
            isAdd = false;
            add.setText("Add");
        }
    }

    private void enableDisableweb() {

        if (isWeb == false) {
            isWeb = true;

            locate.setEnabled(false);
            add.setEnabled(false);
            clear.setEnabled(false);
            config.setEnabled(false);
            upload.setEnabled(false);
            start.setEnabled(false);
            stop.setEnabled(false);

            web.setText("Exit");
            getWebControlInfor();

    }else
    {
        isWeb = false;

        locate.setEnabled(true);
        add.setEnabled(true);
        clear.setEnabled(true);
        config.setEnabled(true);
        upload.setEnabled(true);
        start.setEnabled(true);
        stop.setEnabled(true);

        web.setText("Web Control");
    }

}

    private void RealTimelocation()
    {
        new Thread(new Runnable() {

            @Override

            public void run() {

                while(isWeb == true) {

                    try {
                        sleep(1000);
                        URL url = new URL("http://10.178.1.63/~Team201810/server/php/get_currentpoint.php");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setReadTimeout(5000);
                        httpURLConnection.setConnectTimeout(5000);
                        httpURLConnection.connect();

                        OutputStream out = httpURLConnection.getOutputStream();
                        JSONObject obj = new JSONObject();

                        obj.put("Lat", droneLocationLat);
                        obj.put("Lng", droneLocationLng);
                        Log.v("TAG", obj.toString());

                        String str = "Location=" + obj.toString();
                        out.write(str.getBytes());

                        if (httpURLConnection.getResponseCode() == 200) {
                            System.out.println("connected");
                            // httpURLConnection.getResponseCode();
                            InputStream is = httpURLConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                            String lines = reader.readLine();

                            System.out.println(lines);
                            System.out.println("finish");

                            is.close();
                            out.close();
                            httpURLConnection.disconnect();
                        }else
                        {
                            System.out.println("wrong");
                        }

                    } catch (Exception e) {
                        Log.getStackTraceString(e);
                    }

                }
            }

        }).start();

    }

    private void getWebControlInfor()
    {

        //while loop receive start infor
        onProductConnectionChange();
        cameraUpdate();

        RealTimelocation();


        take_off();








    }

    private void take_off() {

        new Thread(new Runnable() {

            @Override

            public void run() {

                while(isWeb == true) {

                    if(infor == -1) {

                    try {
                        sleep(10000);
                        URL url = new URL("http://10.178.1.63/~Team201810/server/php/Takeoff.php");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setReadTimeout(5000);
                        httpURLConnection.setConnectTimeout(5000);
                        httpURLConnection.connect();

                        OutputStream out = httpURLConnection.getOutputStream();

                        String str = "ready=1";

                        out.write(str.getBytes());

                        if (httpURLConnection.getResponseCode() == 200) {
                            System.out.println("connected");
                            // httpURLConnection.getResponseCode();
                            InputStream is = httpURLConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                            String lines = reader.readLine();
                            String lines;

                            StringBuffer sb = new StringBuffer("");
                            while ((lines = reader.readLine()) != null) {
                                lines = new String(lines.getBytes(), "utf-8");
                                sb.append(lines);
                            }

                            JSONObject root = new JSONObject(sb.toString());
                            infor = root.getInt("Infor");
                            System.out.println("infor is " + infor);


                            if (infor == 1) {
                                System.out.println("infor is " + infor);

                                JSONObject config = root.getJSONObject("Config");
                                setconfig(config);
                                JSONArray positionarray = root.getJSONArray("Positions");
                                setwaypoint(positionarray);


                                Log.v("TAG", sb.toString());
                            }


                            System.out.println(sb);
                            System.out.println("finish");

                            is.close();
                            out.close();
                            httpURLConnection.disconnect();
                        }else
                        {
                            System.out.println("wrong");
                        }

                    } catch (Exception e) {
                        Log.getStackTraceString(e);
                    }



                    }
                    else if (infor == 1)
                    {
                        infor = -1;
                        startWaypointMission();
                        System.out.printf("get 1");
                    }
                    else if(infor == 4)
                    {
                        infor = -1;
                        stopWaypointMission();
                    }

                }
            }

        }).start();


    }






    private void setconfig(JSONObject config)
    {
        try {

            if (config.getInt("Altitude") == 23){
                altitude = 20;
            } else if (config.getInt("Altitude") == 22){
                altitude = 50;
            } else if (config.getInt("Altitude") == 21){
                altitude = 80;
            }
            System.out.println("Altitude is " + config.getInt("Altitude"));

            if (config.getInt("Speed") == 13){
                mSpeed = 3.0f;
            } else if (config.getInt("Speed") == 12){
                mSpeed = 5.0f;
            } else if (config.getInt("Speed") == 11){
                mSpeed = 10.0f;
            }
            System.out.println("Speed is " + config.getInt("Speed"));

            if (config.getInt("FinishAction") == 30){
                mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
            } else if (config.getInt("FinishAction") == 31){
                mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
            } else if (config.getInt("FinishAction") == 33){
                mFinishedAction = WaypointMissionFinishedAction.AUTO_LAND;
            } else if (config.getInt("FinishAction") == 32){
                mFinishedAction = WaypointMissionFinishedAction.GO_FIRST_WAYPOINT;
            }
            System.out.println("FinishAction is " + config.getInt("FinishAction"));
            if (config.getInt("HeadingMode") == 43) {
                mHeadingMode = WaypointMissionHeadingMode.AUTO;
            } else if (config.getInt("HeadingMode") == 42) {
                mHeadingMode = WaypointMissionHeadingMode.USING_INITIAL_DIRECTION;
            } else if (config.getInt("HeadingMode") == 40) {
                mHeadingMode = WaypointMissionHeadingMode.CONTROL_BY_REMOTE_CONTROLLER;
            } else if (config.getInt("HeadingMode") == 41) {
                mHeadingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
            }
            System.out.println("HeadingMode is " + config.getInt("HeadingMode"));
            configWayPointMission();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setwaypoint(JSONArray array)
    {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject position = array.getJSONObject(i);

                LatLng point = new LatLng(position.getDouble("Lng"), position.getDouble("Lat"));

                System.out.println("Lat is " + position.getDouble("Lat")+ "Lng is " + position.getDouble("Lng"));

                markWaypoint(point);
                Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, altitude);
                //Add Waypoints to Waypoint arraylist;
                if (waypointMissionBuilder != null) {
                    waypointList.add(mWaypoint);
                    waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                }else
                {
                    waypointMissionBuilder = new WaypointMission.Builder();
                    waypointList.add(mWaypoint);
                    waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                }
             }

            uploadWayPointMission();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showSettingDialog(){
        LinearLayout wayPointSettings = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);

        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        RadioGroup speed_RG = (RadioGroup) wayPointSettings.findViewById(R.id.speed);
        RadioGroup actionAfterFinished_RG = (RadioGroup) wayPointSettings.findViewById(R.id.actionAfterFinished);
        RadioGroup heading_RG = (RadioGroup) wayPointSettings.findViewById(R.id.heading);

        speed_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lowSpeed){
                    mSpeed = 3.0f;
                } else if (checkedId == R.id.MidSpeed){
                    mSpeed = 5.0f;
                } else if (checkedId == R.id.HighSpeed){
                    mSpeed = 10.0f;
                }
            }

        });

        actionAfterFinished_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select finish action");
                if (checkedId == R.id.finishNone){
                    mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
                } else if (checkedId == R.id.finishGoHome){
                    mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
                } else if (checkedId == R.id.finishAutoLanding){
                    mFinishedAction = WaypointMissionFinishedAction.AUTO_LAND;
                } else if (checkedId == R.id.finishToFirst){
                    mFinishedAction = WaypointMissionFinishedAction.GO_FIRST_WAYPOINT;
                }
            }
        });

        heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select heading");

                if (checkedId == R.id.headingNext) {
                    mHeadingMode = WaypointMissionHeadingMode.AUTO;
                } else if (checkedId == R.id.headingInitDirec) {
                    mHeadingMode = WaypointMissionHeadingMode.USING_INITIAL_DIRECTION;
                } else if (checkedId == R.id.headingRC) {
                    mHeadingMode = WaypointMissionHeadingMode.CONTROL_BY_REMOTE_CONTROLLER;
                } else if (checkedId == R.id.headingWP) {
                    mHeadingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
                }
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                        String altitudeString = wpAltitude_TV.getText().toString();
                        altitude = Integer.parseInt(nulltoIntegerDefalt(altitudeString));
                        Log.e(TAG,"altitude "+altitude);
                        Log.e(TAG,"speed "+mSpeed);
                        Log.e(TAG, "mFinishedAction "+mFinishedAction);
                        Log.e(TAG, "mHeadingMode "+mHeadingMode);
                        configWayPointMission();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                })
                .create()
                .show();
    }

    String nulltoIntegerDefalt(String value){
        if(!isIntValue(value)) value="0";
        return value;
    }

    boolean isIntValue(String val)
    {
        try {
            val=val.replace(" ","");
            Integer.parseInt(val);
        } catch (Exception e) {return false;}
        return true;
    }

    private void configWayPointMission(){

        if (waypointMissionBuilder == null){

            waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

        }else
        {
            waypointMissionBuilder.finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(WaypointMissionFlightPathMode.NORMAL);

        }

        if (waypointMissionBuilder.getWaypointList().size() > 0){

            for (int i=0; i< waypointMissionBuilder.getWaypointList().size(); i++){
                waypointMissionBuilder.getWaypointList().get(i).altitude = altitude;
            }

            setResultToToast("Set Waypoint attitude successfully");
        }

        DJIError error = getWaypointMissionOperator().loadMission(waypointMissionBuilder.build());
        if (error == null) {
            setResultToToast("loadWaypoint succeeded");
        } else {
            setResultToToast("loadWaypoint failed " + error.getDescription());
        }

    }

    private void uploadWayPointMission(){

        getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    setResultToToast("Mission upload successfully!");
                } else {
                    setResultToToast("Mission upload failed, error: " + error.getDescription() + " retrying...");
                    getWaypointMissionOperator().retryUploadMission(null);
                }
            }
        });

    }

    private void startWaypointMission(){

        getWaypointMissionOperator().startMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }

    private void stopWaypointMission(){

        getWaypointMissionOperator().stopMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                setResultToToast("Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }

}