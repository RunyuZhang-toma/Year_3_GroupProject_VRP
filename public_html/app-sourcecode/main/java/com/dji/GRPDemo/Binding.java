//have used code on DJI official page , which is a tutorial about how to implements a app to control their aircraft
//https://developer.dji.com/mobile-sdk/documentation/android-tutorials/ActivationAndBinding.html
package com.dji.GRPDemo;

import android.Manifest;
import android.os.Build;


import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dji.common.error.DJIError;
import dji.common.realname.AircraftBindingState;
import dji.common.realname.AppActivationState;
import dji.common.useraccount.UserAccountState;
import dji.common.util.CommonCallbacks;
import dji.sdk.realname.AppActivationManager;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.useraccount.UserAccountManager;
import dji.sdk.useraccount.a;

public class Binding extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    protected Button loginBtn;
    protected Button logoutBtn;
    protected TextView bindingStateTV;
    protected TextView appActivationStateTV;
//    protected TextView useraccountActivationStateTV;

    private AppActivationManager appActivationManager;
    private UserAccountManager useraccountManager;
    private AppActivationState.AppActivationStateListener activationStateListener;
    private AircraftBindingState.AircraftBindingStateListener bindingStateListener;
    private UserAccountManager.UserAccountStateChangeListener userAccountStateChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }

        setContentView(R.layout.activity_binding);
        initUI();
        initData();
    }

    private void initData(){
        setUpListener();

        appActivationManager = DJISDKManager.getInstance().getAppActivationManager();
        useraccountManager = UserAccountManager.getInstance();

        if (appActivationManager != null) {
            appActivationManager.addAppActivationStateListener(activationStateListener);
            appActivationManager.addAircraftBindingStateListener(bindingStateListener);
            useraccountManager.addUserAccountStateChangeListener(userAccountStateChangeListener);

            Binding.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appActivationStateTV.setText("" + appActivationManager.getAppActivationState());
                    bindingStateTV.setText("" + appActivationManager.getAircraftBindingState());
//                    useraccountActivationStateTV.setText("" + useraccountManager.getUserAccountState());
                }
            });
        }
    }

    private void setUpListener() {
        // Example of Listener
        activationStateListener = new AppActivationState.AppActivationStateListener() {
            @Override
            public void onUpdate(final AppActivationState appActivationState) {
                Binding.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        appActivationStateTV.setText("" + appActivationState);
                    }
                });
            }
        };

        bindingStateListener = new AircraftBindingState.AircraftBindingStateListener() {

            @Override
            public void onUpdate(final AircraftBindingState bindingState) {
                Binding.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindingStateTV.setText("" + bindingState);
                    }
                });
            }
        };

//        userAccountStateChangeListener = new UserAccountManager.UserAccountStateChangeListener() {
//
//            @Override
//            public void onUserAccountStateChanged(final UserAccountState userAccountState, a a) {
//                Binding.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        useraccountActivationStateTV.setText("" + userAccountState);
//                    }
//                });
//            }
//        };
    }

    private void tearDownListener() {
        if (activationStateListener != null) {
            appActivationManager.removeAppActivationStateListener(activationStateListener);
            Binding.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appActivationStateTV.setText("Unknown");
                }
            });
        }
        if (bindingStateListener !=null) {
            appActivationManager.removeAircraftBindingStateListener(bindingStateListener);
            Binding.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bindingStateTV.setText("Unknown");
                }
            });
        }

//        if (userAccountStateChangeListener !=null) {
//            useraccountManager.removeUserAccountStateChangeListener(userAccountStateChangeListener);
//            Binding.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    useraccountActivationStateTV.setText("Unknown");
//                }
//            });
//        }

    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        setUpListener();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
    }

    public void onReturn(View view){
        Log.e(TAG, "onReturn");
        this.finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        tearDownListener();
        super.onDestroy();
    }

    private void initUI(){

        bindingStateTV =  findViewById(R.id.tv_binding_state_info);
        appActivationStateTV =  findViewById(R.id.tv_activation_state_info);
//        useraccountActivationStateTV = findViewById(R.id.tv_useraccount_state_info);
        loginBtn =  findViewById(R.id.btn_login);
        logoutBtn =  findViewById(R.id.btn_logout);
        loginBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

    }

    private void loginAccount(){

        UserAccountManager.getInstance().logIntoDJIUserAccount(this,
                new CommonCallbacks.CompletionCallbackWith<UserAccountState>() {
                    @Override
                    public void onSuccess(final UserAccountState userAccountState) {

                        showToast("Login Success");
                    }
                    @Override
                    public void onFailure(DJIError error) {
                        showToast("Login Error:"
                                + error.getDescription());
                    }
                });

    }

    private void logoutAccount(){
        UserAccountManager.getInstance().logoutOfDJIUserAccount(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (null == error) {
                    showToast("Logout Success");
                } else {
                    showToast("Logout Error:"
                            + error.getDescription());
                }
            }
        });
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(Binding.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:{
                loginAccount();
                break;
            }
            case R.id.btn_logout:{
                logoutAccount();
                break;
            }
            default:
                break;
        }
    }
}

