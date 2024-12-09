package com.example.hotelres;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class room_details extends Fragment {

    private TextView roomDetailsTextView;

    public room_details() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_details, container, false);
        roomDetailsTextView = view.findViewById(R.id.roomDetailsTextView);
        return view;
    }

    public void updateRoomDetails(String details) {
        roomDetailsTextView.setText(details);
    }
}
