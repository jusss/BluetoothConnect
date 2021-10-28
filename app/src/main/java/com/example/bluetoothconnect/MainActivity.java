package com.example.bluetoothconnect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String screenSize = "1280x720";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout ll = (LinearLayout)findViewById(R.id.choose_target);

        ArrayList<Button> buttons = new ArrayList<Button>();

        for (BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()){
            buttons.add(new Button(this));
            buttons.get(buttons.size() - 1).setText(device.getName());
            buttons.get(buttons.size() - 1).setAllCaps(false);
            buttons.get(buttons.size() - 1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> name = new ArrayList<>();
                    name.add(device.getName());
                    name.add(screenSize);
                    Intent intent = new Intent(getApplicationContext(), KeyboardActivity.class);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
            });
            ll.addView(buttons.get(buttons.size() - 1));
        }

        LinearLayout ls = (LinearLayout)findViewById(R.id.choose_size);

        CheckBox size1 = new CheckBox(this);
        size1.setText("1280x720");

        CheckBox size2 = new CheckBox(this);
        size2.setText("2340x1080");

        CheckBox size3 = new CheckBox(this);
        size3.setText("1280x720 large");

        CheckBox size5 = new CheckBox(this);
        size5.setText("otg keyboard");

        ls.addView(size1);
        ls.addView(size2);
        ls.addView(size3);
        ls.addView(size5);

        size1.setChecked(true);

        size1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setChecked(true);
                size2.setChecked(false);
                size3.setChecked(false);
                size5.setChecked(false);
                screenSize = "1280x720";
            }
        });


        size2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setChecked(false);
                size2.setChecked(true);
                size3.setChecked(false);
                size5.setChecked(false);
                screenSize = "2340x1080";
            }
        });

        size3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setChecked(false);
                size2.setChecked(false);
                size3.setChecked(true);
                size5.setChecked(false);
                screenSize = "1280x720_large";
            }
        });

        size5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setChecked(false);
                size2.setChecked(false);
                size3.setChecked(false);
                size5.setChecked(true);
                screenSize = "otg_keyboard";
            }
        });
    }
}