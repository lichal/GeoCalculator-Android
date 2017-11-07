package com.codemasters.ryancheng.cis357;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends AppCompatActivity {
    private String distSelection;
    private String bearSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner dist = (Spinner) findViewById(R.id.distanceSelect);
        Spinner bear = (Spinner) findViewById(R.id.bearingSelect);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.distance_units,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dist.setAdapter(adapter);
        dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.bearing_units,
                android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bear.setAdapter(adapter2);
        bear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bearSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent catchMain = getIntent();
        if(catchMain.hasExtra("distPass")) {
            if (catchMain.getStringExtra("distPass").equals("Kilometers")) {
                dist.setSelection(0);
            } else {
                dist.setSelection(1);
            }
        }
        if(catchMain.hasExtra("bearPass")) {
            if (catchMain.getStringExtra("bearPass").equals("Degrees")) {
                bear.setSelection(0);
            } else {
                bear.setSelection(1);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("distSelection", distSelection);
                intent.putExtra("bearSelection", bearSelection);
                setResult(MainActivity.ACTIVITY_SETTINGS, intent);
                finish();
            }
        });

    }

}






