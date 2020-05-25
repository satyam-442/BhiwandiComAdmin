package com.example.bhiwandicomadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

public class ModifyStoreActivity extends AppCompatActivity {

    DatabaseReference storeRef, adminRef;
    String currentUserId, gender, shopName;
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
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_store);

        storeRef = FirebaseDatabase.getInstance().getReference().child("Store");
        shopName = getIntent().getStringExtra("Shopname");

        adminRef = FirebaseDatabase.getInstance().getReference().child("Admins");

        storagePicRef = FirebaseStorage.getInstance().getReference().child("Store Logos");
        loadingBar = new ProgressDialog(this);

        registerOwnerName = findViewById(R.id.registerOwnerName);
        registerOwnerPhone = findViewById(R.id.registerOwnerPhone);
        registerShopName = findViewById(R.id.registerShopName);
        registerShopAddress = findViewById(R.id.registerShopAddress);
        registerPassword = findViewById(R.id.registerPassword);
        tapFromDailog = findViewById(R.id.tapFromDailog);
        selectFromTime = findViewById(R.id.selectFromTime);
        tapToDailog = findViewById(R.id.tapToDailog);
        selectToTime = findViewById(R.id.selectToTime);

        setupProfileImage = findViewById(R.id.setupProfileImage);
        setupSelectImage = findViewById(R.id.setupSelectImage);

        getShopDetails(shopName);

        tapFromDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ModifyStoreActivity.this,
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

        tapToDailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        ModifyStoreActivity.this,
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

        //CODE FOR SELECTION OF IMAGE
        setupSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(5, 2)
                        .start(ModifyStoreActivity.this);
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

    private void getShopDetails(String shopName) {
        storeRef.child(shopName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Stores stores = dataSnapshot.getValue(Stores.class);

                    registerOwnerName.getEditText().setText(stores.getOwnerNamee());
                    registerShopName.getEditText().setText(stores.getShopNamee());
                    registerOwnerPhone.getEditText().setText(stores.getOwnerPhonee());
                    registerShopAddress.getEditText().setText(stores.getShopAddresss());
                    registerPassword.getEditText().setText(stores.getPasswordd());
                    selectFromTime.setText(stores.getFromTimee());
                    selectToTime.setText(stores.getToTimee());
                    Picasso.get().load(stores.getImagee()).into(setupProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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
            startActivity(new Intent(ModifyStoreActivity.this, AddAdminStoreToDatabaseActivity.class));
            finish();
        }
    }

    private void SaveDetailsToDB() {
        final String name = registerOwnerName.getEditText().getText().toString();
        final String phone = registerOwnerPhone.getEditText().getText().toString();
        final String shopname = registerShopName.getEditText().getText().toString();
        final String shopaddress = registerShopAddress.getEditText().getText().toString();
        final String password = registerPassword.getEditText().getText().toString();
        final String fromTime = selectFromTime.getText().toString();
        final String toTime = selectToTime.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Phone is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(shopname)) {
            Toast.makeText(this, "Shop Name is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(shopaddress)) {
            Toast.makeText(this, "Shop Address is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is empty.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(fromTime)) {
            Toast.makeText(this, "Select from time.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(toTime)) {
            Toast.makeText(this, "Select to time.", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setMessage("please wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (imageUri != null) {
                final StorageReference fileref = storagePicRef.child(shopname + ".jpg");
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
            userMap.put("OwnerPhone", phone);
            userMap.put("ShopName", shopname);
            userMap.put("ShopAddress", shopaddress);
            userMap.put("fromTime", fromTime);
            userMap.put("toTime", toTime);
            userMap.put("Password", password);
            userMap.put("tag", "tag");

            //userMap.put("uid",currentUserId);
            /*userMap.put("image",myUrl);*/
            storeRef.child(shopName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ModifyStoreActivity.this, "Data Updated!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

}
