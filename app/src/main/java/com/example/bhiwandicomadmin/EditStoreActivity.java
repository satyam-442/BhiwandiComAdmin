package com.example.bhiwandicomadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;
import java.util.HashMap;

public class EditStoreActivity extends AppCompatActivity {
    DatabaseReference storeRef;
    String Shopname;

    ProgressDialog loadingBar;
    TextInputLayout registerOwnerName, registerOwnerPhone, registerShopName, registerShopAddress, registerPassword;
    EditText selectFromTime, selectToTime;
    TextView tapFromDailog, tapToDailog;
    int fromHour, fromMinute, toHour, toMinute;

    ImageView setupProfileImage;
    Uri imageUri;
    StorageReference storagePicRef;
    String myUrl = "";
    String checker = "";
    StorageTask uploadTask;

    Button setupSelectImage, setupUploadImage,viewusers,btnstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);

        Shopname = getIntent().getStringExtra("Shopname");

        storeRef = FirebaseDatabase.getInstance().getReference().child("Store");

        storagePicRef = FirebaseStorage.getInstance().getReference().child("Store Logos");
        loadingBar = new ProgressDialog(this);

        registerOwnerName = findViewById(R.id.registerOwnerName);
        registerShopName = findViewById(R.id.registerShopName);
        registerShopAddress = findViewById(R.id.registerShopAddress);
        registerPassword = findViewById(R.id.registerPassword);

        tapFromDailog = findViewById(R.id.tapFromDailog);
        selectFromTime = findViewById(R.id.selectFromTime);

        getProductDetails(Shopname);

        tapFromDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditStoreActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                fromHour = hourOfDay;
                                fromMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, fromHour, fromMinute);
                                //selectFromTime.setText(DateFormat.format("hh:mm aa"));
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                selectFromTime.setText(df.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(fromHour, fromMinute);
                timePickerDialog.show();
            }
        });

        tapToDailog = findViewById(R.id.tapToDailog);
        selectToTime = findViewById(R.id.selectToTime);
        tapToDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EditStoreActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                toHour = hourOfDay;
                                toMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, toHour, toMinute);
                                //selectFromTime.setText(DateFormat.format("hh:mm aa"));
                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                selectToTime.setText(df.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(toHour, toMinute);
                timePickerDialog.show();
            }
        });

        setupProfileImage = findViewById(R.id.setupProfileImage);
        setupSelectImage = findViewById(R.id.setupSelectImage);

        //CODE FOR SELECTION OF IMAGE
        setupSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(5, 2)
                        .start(EditStoreActivity.this);
            }
        });

        setupUploadImage = findViewById(R.id.setupUploadImage);
        setupUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDetailsToDB();
            }
        });
    }

    private void getProductDetails(String shopname)
    {
        storeRef.child(shopname).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Stores product = dataSnapshot.getValue(Stores.class);

                    registerOwnerName.getEditText().setText(product.getOwnerNamee());
                    registerShopName.getEditText().setText(product.getShopNamee());
                    registerShopAddress.getEditText().setText(product.getShopAddresss());
                    selectFromTime.setText(product.getFromTimee());
                    selectToTime.setText(product.getToTimee());
                    Picasso.get().load(product.getImagee()).into(setupProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            setupProfileImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error! Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditStoreActivity.this, AddAdminStoreToDatabaseActivity.class));
            finish();
        }
    }

    private void SaveDetailsToDB() {
        final String name = registerOwnerName.getEditText().getText().toString();
        final String shopaddress = registerShopAddress.getEditText().getText().toString();
        final String fromTime = selectFromTime.getText().toString();
        final String toTime = selectToTime.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(shopaddress)) {
            Toast.makeText(this, "Shop Address is empty.", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setMessage("please wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (imageUri != null) {
                final StorageReference fileref = storagePicRef.child(Shopname + ".jpg");
                uploadTask = fileref.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return fileref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            HashMap<String, Object> userMapImg = new HashMap<String, Object>();
                            userMapImg.put("image", myUrl);
                            storeRef.updateChildren(userMapImg);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            }

            HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("OwnerName", name);
            userMap.put("ShopAddress", shopaddress);
            userMap.put("fromTime", fromTime);
            userMap.put("toTime", toTime);
            userMap.put("tag", "tag");

            //userMap.put("uid",currentUserId);
            /*userMap.put("image",myUrl);*/
            storeRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditStoreActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }
}