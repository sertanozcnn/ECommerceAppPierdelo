package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.pierdeloapp.adapter.AddressAdapter;
import com.example.pierdeloapp.domain.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress{

    private ProgressBar progressBar;
    private List<Address> mAddressList;
    private AddressAdapter mAddressAdapter;
    RecyclerView mAddressRecyclerView;
    Button mAddAddress;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    String address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);

        progressBar = findViewById(R.id.progressBar);
        mAddressRecyclerView=findViewById(R.id.address_recycler);
        mAddAddress=findViewById(R.id.add_address_btn);
        mAuth=FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();
        mAddressList=new ArrayList<>();
        mAddressAdapter=new AddressAdapter(getApplicationContext(),mAddressList, this);
        mAddressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAddressRecyclerView.setAdapter(mAddressAdapter);

        activityHeader.setText(getString(R.string.address_title));
        backActivity.setOnClickListener(v -> finish());
        progressBar.setVisibility(View.VISIBLE);

        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        for (DocumentChange doc : task.getResult().getDocumentChanges()) {
                            String documentId = doc.getDocument().getId();
                            Address userAddress = doc.getDocument().toObject(Address.class);
                            userAddress.setDocId(documentId);
                            mAddressList.add(userAddress);
                        }
                        mAddressAdapter.notifyDataSetChanged();
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });

        mAddAddress.setOnClickListener(v -> {
            Intent intent=new Intent(AddressActivity.this,AddAddressActivity.class);
            startActivity(intent);
        });
    }

    public void setAddress(String s) {
        Log.d("AddressAdapter", "setAddress: " + s);
        address = s;
    }
}