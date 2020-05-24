package com.example.bhiwandicomadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisteredUsersActivity extends AppCompatActivity {


    DatabaseReference usersRef;
    RecyclerView usersRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_users);


        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRecView = (RecyclerView) findViewById(R.id.users_reclist);

        usersRecView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        usersRecView.setLayoutManager(linearLayoutManager);

        //RECYCLER VIEW FOR Dietecians UPDATE(CLOSE)
        //startListen();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startListen();
    }

    private void startListen()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").limitToLast(50);
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,Users.class).build();
        FirebaseRecyclerAdapter<Users, FeedbackViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, FeedbackViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull FeedbackViewHolder holder, final int position, @NonNull Users model)
            {
                //final String PostKey = getRef(position).getKey();
                holder.setNamee(model.getNamee());
                holder.setPhonee(model.getPhonee());
                holder.setGenderr(model.getGenderr());
                holder.setEmaill(model.getEmaill());
                holder.setImagee(model.getImagee());

            }

            @NonNull
            @Override
            public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout,parent,false);
                FeedbackViewHolder holder = new FeedbackViewHolder(view);
                return holder;
            }
        };
        usersRecView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    /*And this is the static class*/
    public static class FeedbackViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public FeedbackViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setPhonee(String phone)
        {
            TextView phonee = (TextView) mView.findViewById(R.id.phoneno);
            phonee.setText(phone);
        }

        public void setNamee(String name)
        {
            TextView namee = (TextView) mView.findViewById(R.id.name);
            namee.setText(name);
        }


        public void setEmaill(String email)
        {
            TextView emaill= (TextView) mView.findViewById(R.id.email);
            emaill.setText(email);
        }

        public void setGenderr(String gender)
        {
            TextView genderr= (TextView) mView.findViewById(R.id.gender);
            genderr.setText(gender);
        }

        public void setImagee(String image )
        {
            CircleImageView usersImage = (CircleImageView) mView.findViewById(R.id.profilephoto);
            Picasso.get().load(image).placeholder(R.drawable.profilephoto).into(usersImage);
        }
    }

}
