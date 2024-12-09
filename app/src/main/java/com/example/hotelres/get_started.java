package com.example.hotelres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class get_started extends AppCompatActivity {

    private TextView getStartedTextView;
    private ImageView arrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_started);

        getStartedTextView = findViewById(R.id.textView3);
        arrowImageView = findViewById(R.id.imageView2);

        View.OnClickListener getStartedClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to the second page
                Intent intent = new Intent(get_started.this, select_hotel.class);
                startActivity(intent);
            }
        };

        getStartedTextView.setOnClickListener(getStartedClickListener);
        arrowImageView.setOnClickListener(getStartedClickListener);
    }
}
