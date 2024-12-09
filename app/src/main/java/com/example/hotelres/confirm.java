package com.example.hotelres;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class confirm extends AppCompatActivity {

    private RadioGroup roomTypeRadioGroup;
    private room_details roomDetailsFragment;
    private FragmentManager fragmentManager;
    private EditText customerName, customerAddress, checkInDate, checkOutDate;
    private Button confirmReservationButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        this.setTitle("Confirm your Booking");

        roomTypeRadioGroup = findViewById(R.id.roomTypeRadioGroup);
        fragmentManager = getSupportFragmentManager();
        customerName = findViewById(R.id.cname);
        customerAddress = findViewById(R.id.cadd);
        checkInDate = findViewById(R.id.indate);
        checkOutDate = findViewById(R.id.outdate);
        confirmReservationButton = findViewById(R.id.confirm);

        roomDetailsFragment = new room_details();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.roomDetailsFragmentContainer, roomDetailsFragment);
        fragmentTransaction.commit();

        databaseReference = FirebaseDatabase.getInstance().getReference("Reservations");

        roomTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String details = "";
                switch (checkedId) {
                    case R.id.single:
                        details = "Economic Single rooms consist usually of one room and include a set of necessary furniture and appliances." +
                                "The area ranges from 12-15 m2 to 20-23 m2 and contains a single bed, a width of 90 cm, a wardrobe, desk," +
                                "television, often have air conditioning, telephone, writing utensils, refrigerator and kettle.";
                        break;
                    case R.id.two:
                        details = "A double room is a hotel room with a double bed that can accommodate two people." +
                                "Double rooms come in different sizes, and the room size can affect the room rate.";
                        break;
                    case R.id.deluxe:
                        details = "Deluxe rooms, are modern decorated, can accommodate up to 2 persons," +
                                "totally soundproofed and equipped with high tech comforts such as high speed internet.";
                        break;
                }
                roomDetailsFragment.updateRoomDetails(details);
            }
        });

        confirmReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReservation();
            }
        });
    }

    private void confirmReservation() {
        String name = customerName.getText().toString().trim();
        String address = customerAddress.getText().toString().trim();
        String checkIn = checkInDate.getText().toString().trim();
        String checkOut = checkOutDate.getText().toString().trim();
        int selectedRoomId = roomTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRoomButton = findViewById(selectedRoomId);
        String roomType = selectedRoomButton.getText().toString();

        if (name.isEmpty() || address.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty() || roomType.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get country and hotel from the previous activity (page2)
        Intent intent = getIntent();
        String country = intent.getStringExtra("country");
        String hotel = intent.getStringExtra("hotel");

        // Generate a unique ID for each reservation
        String reservationId = databaseReference.push().getKey();

        // Create a reservation object
        Reservation reservation = new Reservation(reservationId, name, address, checkIn, checkOut, roomType, country, hotel);

        // Save the reservation to Firebase
        databaseReference.child(reservationId).setValue(reservation)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(confirm.this, "Hotel booking confirmed", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to another activity or perform other actions
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(confirm.this, "Failed to confirm booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Intent page3Intent = new Intent(confirm.this, reservation_info.class);
        page3Intent.putExtra("name", name);
        page3Intent.putExtra("address", address);
        page3Intent.putExtra("checkIn", checkIn);
        page3Intent.putExtra("checkOut", checkOut);
        page3Intent.putExtra("roomType", roomType);
        page3Intent.putExtra("country", country);
        page3Intent.putExtra("hotel", hotel);
        startActivity(page3Intent);
    }

    // Define a Reservation class to structure the data
    public static class Reservation {
        public String id;
        public String name;
        public String address;
        public String checkInDate;
        public String checkOutDate;
        public String roomType;
        public String country;
        public String hotel;

        public Reservation() {

        }

        public Reservation(String id, String name, String address, String checkInDate, String checkOutDate, String roomType, String country, String hotel) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.roomType = roomType;
            this.country = country;
            this.hotel = hotel;
        }
    }
}
