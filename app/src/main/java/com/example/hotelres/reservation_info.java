package com.example.hotelres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class reservation_info extends AppCompatActivity {

    private Button gohome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_info);

        gohome = findViewById(R.id.home);

        setuphome();

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        String checkIn = intent.getStringExtra("checkIn");
        String checkOut = intent.getStringExtra("checkOut");
        String roomType = intent.getStringExtra("roomType");
        String country = intent.getStringExtra("country");
        String hotel = intent.getStringExtra("hotel");

        // Display the data on the UI
        TextView textView = findViewById(R.id.textView);
        textView.setText("User Name: " + name + "\n" +
                "Selected Country: " + country + "\n" +
                "Selected Hotel: " + hotel +
                "Address: " + address + "\n" +
                "Room Type: " + roomType + "\n" +
                "Check-In Date: " + checkIn + "\n" +
                "Check-Out Date: " + checkOut + "\n");
    }

    private void setuphome() {
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newhome = new Intent(reservation_info.this, get_started.class);
                startActivity(newhome);
            }
        });
    }
}
