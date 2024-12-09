package com.example.hotelres;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class select_hotel extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Spinner spinner;
    private Button nexthotel;
    private TextView weatherTextView;


    private Map<String, String[]> hotelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_hotel);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        spinner = findViewById(R.id.spinner);
        nexthotel = findViewById(R.id.selecthotel);
        weatherTextView = findViewById(R.id.weatherTextView);

        initializeHotelData();
        setupAutoCompleteTextView();
        setupnexthotel();
    }

    private void initializeHotelData() {
        hotelData = new HashMap<>();
        hotelData.put("Bangladesh", getResources().getStringArray(R.array.hotels_bangladesh));
        hotelData.put("USA", getResources().getStringArray(R.array.hotels_usa));
        hotelData.put("UK", getResources().getStringArray(R.array.hotels_uk));
        hotelData.put("India", getResources().getStringArray(R.array.hotels_india));
    }

    private void setupAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.country_locations));
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter hotels
                filterHotels(charSequence.toString());

                // Fetch weather information
                fetchWeather(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void filterHotels(String query) {
        if (query.isEmpty() || !hotelData.containsKey(query)) {
            spinner.setVisibility(View.GONE);
            return;
        }

        spinner.setVisibility(View.VISIBLE);

        String[] hotels = hotelData.get(query);
        List<String> filteredHotels = new ArrayList<>();
        for (String hotel : hotels) {
            filteredHotels.add(hotel);
        }

        ArrayAdapter<String> filteredAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filteredHotels);
        filteredAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filteredAdapter);
    }

    private void setupnexthotel() {
        nexthotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedHotel = (String) spinner.getSelectedItem();
                String selectedCountry = autoCompleteTextView.getText().toString();

                if (selectedHotel == null || selectedCountry.isEmpty()) {
                    Toast.makeText(select_hotel.this, "Please select a country and a hotel", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent confirmIntent = new Intent(select_hotel.this, confirm.class);
                confirmIntent.putExtra("country", selectedCountry);
                confirmIntent.putExtra("hotel", selectedHotel);
                startActivity(confirmIntent);
            }
        });
    }

    private void fetchWeather(String country) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q=" + country + "&appid=3d8645065d85b8f63b50d1e204c95e03&units=metric")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    select_hotel.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(responseData);
                                JSONArray weatherArray = json.getJSONArray("weather");
                                JSONObject weatherObject = weatherArray.getJSONObject(0);
                                String description = weatherObject.getString("description");

                                JSONObject mainObject = json.getJSONObject("main");
                                double temperature = mainObject.getDouble("temp");

                                String weatherInfo = String.format("Temperature: %.1f°C, Description: %s", temperature, description);
                                weatherTextView.setText(weatherInfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
