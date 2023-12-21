package com.example.pierdeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pierdeloapp.domain.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import io.github.muddz.styleabletoast.StyleableToast;

public class AddAddressActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mCity;
    private EditText mAddress;
    private EditText mNumber;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Address userAddress = new Address();
    Button mAddAddressBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        progressBar = findViewById(R.id.progressBar);
        mName = findViewById(R.id.ad_name);
        mCity = findViewById(R.id.ad_city);
        mAddress = findViewById(R.id.ad_address);
        mNumber = findViewById(R.id.ad_phone);
        mAddAddressBtn = findViewById(R.id.ad_add_address);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);

        activityHeader.setText(getString(R.string.add_address_title));
        backActivity.setOnClickListener(v -> finish());

        mAddAddressBtn.setOnClickListener(v -> {
            String name = mName.getText().toString();
            String city = mCity.getText().toString();
            String address = mAddress.getText().toString();
            String number = mNumber.getText().toString();

            userAddress.setName(name);
            userAddress.setCity(city);
            userAddress.setAddress(address);
            userAddress.setPhoneNumber(number);
            Map<String, Object> addressMap = new HashMap<>();
            addressMap.put("address", userAddress);

            if(userAddress!=null){
                mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(userAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                StyleableToast.makeText(AddAddressActivity.this, getString(R.string.add_address_activity), Toast.LENGTH_SHORT, R.style.infoToast).show();
                                Intent intent = new Intent(AddAddressActivity.this,AddressActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                StyleableToast.makeText(AddAddressActivity.this, getString(R.string.add_address_activity_error), Toast.LENGTH_SHORT, R.style.errorToast).show();
                            }
                            progressBar.setVisibility(View.VISIBLE);
                        });
            }
        });
    }
}
