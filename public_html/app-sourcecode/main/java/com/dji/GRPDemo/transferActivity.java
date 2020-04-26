package com.dji.GRPDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;


public class transferActivity extends AppCompatActivity {

    Button login;
    Button connection;
    Button UX;
    Button camera;
    Button mapconnection;
    Button mapview;
    Button testm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        login = (Button)findViewById(R.id.binding);
//        connection = (Button)findViewById(R.id.connection);
        UX = (Button)findViewById(R.id.UXview);
//        camera = (Button)findViewById(R.id.camera);
//        mapconnection = (Button)findViewById(R.id.mapconnection);
        mapview = (Button)findViewById(R.id.mapview);
//        testm = (Button)findViewById(R.id.testphpp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(transferActivity.this,Binding.class);
                startActivity(intent);
            }
        });

//        connection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(transferActivity.this, ConnectionActivity.class);
//                startActivity(intent);
//            }
//        });

        UX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(transferActivity.this,UXActivity.class);
                startActivity(intent);
            }
        });

//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(transferActivity.this,CameraActivity.class);
//                startActivity(intent);
//            }
//        });

//        mapconnection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(transferActivity.this,MapconnectionActivity.class);
//                startActivity(intent);
//            }
//        });

        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(transferActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });

//       testm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(transferActivity.this,testphp.class);
//                startActivity(intent);
//            }
//        });

    }
}
