package com.example.pierdeloapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pierdeloapp.R;
import com.example.pierdeloapp.domain.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import io.github.muddz.styleabletoast.StyleableToast;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private RadioButton mSelectedRadioBtutton;
    Context applicationContext;
    List<Address> mAddressList;
    SelectedAddress selectedAddress;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    public AddressAdapter(Context applicationContext, List<Address> mAddressList, SelectedAddress selectedAddress ) {
        this.applicationContext=applicationContext;
        this.mAddressList=mAddressList;
        this.selectedAddress=selectedAddress;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.mPhoneNumber.setText(mAddressList.get(position).getPhoneNumber());
        holder.mUserName.setText(mAddressList.get(position).getName());
        holder.mCityName.setText(mAddressList.get(position).getCity());
        holder.mAddress.setText(mAddressList.get(position).getAddress());

        holder.removeItem.setOnClickListener(v -> firebaseFirestore.collection("User")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("Address")
                .document(mAddressList.get(position).getDocId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mAddressList.remove(mAddressList.get(position));
                        notifyDataSetChanged();
                        StyleableToast.makeText(applicationContext, applicationContext.getString(R.string.toast_address_deleted), Toast.LENGTH_SHORT, R.style.infoToast).show();
                    } else {
                        Log.d("Delete Error", task.getException().getMessage());
                        StyleableToast.makeText(applicationContext, applicationContext.getString(R.string.toast_address_deleted_error), Toast.LENGTH_SHORT, R.style.errorToast).show();
                    }
                }));

        holder.mRadio.setOnClickListener(v -> {
                for (Address address : mAddressList) {
                    address.setSelected(false);
                }
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    mAddressList.get(adapterPosition).setSelected(true);
                    if (mSelectedRadioBtutton != null) {
                        mSelectedRadioBtutton.setChecked(false);
                    }
                    mSelectedRadioBtutton = (RadioButton) v;
                    mSelectedRadioBtutton.setChecked(true);
                    selectedAddress.setAddress(mAddressList.get(adapterPosition).getAddress());
                }
            });
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mCityName;
        private TextView mAddress;
        private TextView mUserName;
        private TextView mPhoneNumber;
        private RadioButton mRadio;
        private ImageView removeItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCityName = itemView.findViewById(R.id.address_cityName);
            mAddress = itemView.findViewById(R.id.address_locationName);
            mUserName = itemView.findViewById(R.id.address_userName);
            mPhoneNumber = itemView.findViewById(R.id.address_userPhone);
            mRadio=itemView.findViewById(R.id.select_address);
            removeItem = itemView.findViewById(R.id.remove_card);
        }
    }
    public interface SelectedAddress{
        public void setAddress(String s);
    }
}
