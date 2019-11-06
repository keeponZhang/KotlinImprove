package com.bennyhuo.github.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bennyhuo.github.R;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View fl = findViewById(R.id.fragmentContainer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View viewById = findViewById(R.id.usernameView);
        Log.e("TAG", "Main2Activity onCreate viewById:"+viewById+"  fl="+fl);
    }
}
