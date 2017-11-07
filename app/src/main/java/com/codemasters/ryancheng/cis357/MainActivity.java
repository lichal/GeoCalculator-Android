package com.codemasters.ryancheng.cis357;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;

import com.codemasters.ryancheng.cis357.dummy.HistoryContent;

import org.joda.time.DateTime;

import java.text.DecimalFormat;


/**
 *
 */
public class MainActivity extends AppCompatActivity {
    public static final int ACTIVITY_SETTINGS = 1;
    public static final int HISTORY_RESULT = 5;

    private EditText latP1;
    private EditText latP2;
    private EditText longP1;
    private EditText longP2;
    private Button clearButton;
    private Button calcButton;
    private TextView distanceLabel;
    private TextView bearingLabel;
    private String distUnit;
    private String bearUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate stuff...
        latP1 = (EditText) findViewById(R.id.latP1);
        latP2 = (EditText) findViewById(R.id.latP2);
        longP1 = (EditText) findViewById(R.id.longP1);
        longP2 = (EditText) findViewById(R.id.longP2);
        clearButton = (Button) findViewById(R.id.clearButton);
        calcButton = (Button) findViewById(R.id.calcButton);
        distanceLabel = (TextView) findViewById(R.id.distanceLabel);
        bearingLabel = (TextView) findViewById(R.id.bearingLabel);
        distUnit = "Kilometers";
        bearUnit = "Degrees";

        clearButton.setOnClickListener(v -> {
            distanceLabel.setText("Distance: ");
            bearingLabel.setText("Bearing: ");
            longP1.setText("");
            longP2.setText("");
            latP1.setText("");
            latP2.setText("");
            hideKeyboard(this);
        });

        calcButton.setOnClickListener(v -> {
            // remember the calculation.
            calculate();
            HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(latP1.getText().toString(),
                    longP1.getText().toString(), latP2.getText().toString(), longP2.getText().toString(), DateTime.now());
            HistoryContent.addItem(item);
        });
    }

    // This Code is From:
    // https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ACTIVITY_SETTINGS) {
            this.distUnit = data.getStringExtra("bearingUnits");
            this.bearUnit = data.getStringExtra("distanceUnits");
            calculate();
        } else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.latP1.setText(vals[0]);
            this.longP1.setText(vals[1]);
            this.latP2.setText(vals[2]);
            this.longP2.setText(vals[3]);
            this.calculate();  // code that updates the calcs.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings_menu222) {
            Intent intent = new Intent(MainActivity.this,
                    Settings.class);
            intent.putExtra("distPass", distUnit);
            intent.putExtra("bearPass", bearUnit);
            startActivityForResult(intent, ACTIVITY_SETTINGS );
            return true;
        } else if(item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT );
            return true;
        }
        return false;
    }


    private void calculate(){
        String latP1Text = latP1.getText().toString();
        String latP2Text = latP2.getText().toString();
        String longP1Text = longP1.getText().toString();
        String longP2Text = longP2.getText().toString();
        if (!latP1Text.equals("") && !latP2Text.equals("") && !longP1Text.equals("") && !longP2Text.equals("")) {
            Double startLatitude0 = Double.parseDouble(latP1Text);
            Double startLongitude0 = Double.parseDouble(longP1Text);
            Double endLatitude0 = Double.parseDouble(latP2Text);
            Double endLongitude0 = Double.parseDouble(longP2Text);
            double startLatitude = startLatitude0.doubleValue();
            double startLongitude = startLongitude0.doubleValue();
            double endLatitude = endLatitude0.doubleValue();
            double endLongitude = endLongitude0.doubleValue();
            float results[] = new float[3];
            Location one = new Location("CoolMysteriousProvider");
            try {
                one.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
                double computedDistance = results[0];
                double initialBearing = results[1];
                double finalBearing = results[2];
                long j2 = Math.round(100 * computedDistance / 1000);
                computedDistance = computedDistance / 1000;
                long j = Math.round(100 * finalBearing);
                finalBearing = j / 100.0;
                DecimalFormat nf = new DecimalFormat("#0.00");

                if(distUnit.equals("Miles")){
                    computedDistance = computedDistance / 1.609344;
                }
                if(bearUnit.equals("Mils")){
                    finalBearing = finalBearing * (160/9);
                }

                distanceLabel.setText("Distance: " + nf.format(computedDistance) + " " + distUnit);
                bearingLabel.setText("Bearing: " + nf.format(finalBearing) + " " + bearUnit);
                hideKeyboard(this);


            } catch (IllegalArgumentException m) {
            }
        }
    }
}

