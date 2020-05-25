package com.example.bhiwandicomadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStores extends AppCompatActivity {

    DatabaseReference storesRef;
    RecyclerView storesRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);

        storesRef = FirebaseDatabase.getInstance().getReference().child("Store");

        storesRecView = (RecyclerView) findViewById(R.id.stores_reclist);

        storesRecView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        storesRecView.setLayoutManager(linearLayoutManager);

        //RECYCLER VIEW FOR Dietecians UPDATE(CLOSE)
        //startListen();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startListen();
    }

    private void startListen() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Store").limitToLast(50);
        FirebaseRecyclerOptions<Stores> options = new FirebaseRecyclerOptions.Builder<Stores>().setQuery(query, Stores.class).build();
        FirebaseRecyclerAdapter<Stores, FeedbackViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Stores, ViewStores.FeedbackViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, final int position, @NonNull final Stores model) {
                //final String PostKey = getRef(position).getKey();
                holder.setShopNamee(model.getShopNamee());
                holder.setOwnerNamee(model.getOwnerNamee());
                holder.setFromTimee(model.getFromTimee());
                holder.setToTimee(model.getToTimee());
                holder.setShopAdresss(model.getShopAddresss());
                holder.setImagee(model.getImagee());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]{
                                "Edit",
                                "Delete",
                                "Add Tag"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewStores.this);
                        builder.setTitle("Select the Option");
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(ViewStores.this, ModifyStoreActivity.class);
                                    intent.putExtra("Shopname", model.getShopNamee());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    storesRef.child(model.getShopNamee()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ViewStores.this, "Store Removed!", Toast.LENGTH_SHORT).show();
                                                        /*Intent home = new Intent(getActivity(),HomeFragment.class);
                                                        startActivity(home);*/
                                            }
                                        }
                                    });
                                }
                                if (i==2) {
                                    Toast.makeText(ViewStores.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stores_layout, parent, false);
                FeedbackViewHolder holder = new FeedbackViewHolder(view);
                return holder;
            }
        };
        storesRecView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    /*And this is the static class*/
    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

       /* public void setPhonee(String phone)
        {
            TextView phonee = (TextView) mView.findViewById(R.id.phoneno);
            phonee.setText(phone);
        }*/

        public void setShopNamee(String shopName) {
            TextView snamee = (TextView) mView.findViewById(R.id.store_name);
            snamee.setText(shopName);
        }

        public void setOwnerNamee(String ownerName) {
            TextView onamee = (TextView) mView.findViewById(R.id.owner_name);
            onamee.setText(ownerName);
        }

        public void setFromTimee(String fromTime) {
            TextView ftime = (TextView) mView.findViewById(R.id.fromtime);
            ftime.setText(fromTime);
        }

        public void setToTimee(String toTime) {
            TextView ttime = (TextView) mView.findViewById(R.id.totime);
            ttime.setText(toTime);
        }

        public void setShopAdresss(String shopAdress) {
            TextView saddress = (TextView) mView.findViewById(R.id.address);
            saddress.setText(shopAdress);
        }


        public void setImagee(String image) {
            ImageView storeImage = (ImageView) mView.findViewById(R.id.store_image);
            Picasso.get().load(image).placeholder(R.drawable.storeback).into(storeImage);
        }
    }

}

