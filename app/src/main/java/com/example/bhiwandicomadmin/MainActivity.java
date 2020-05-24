package com.example.bhiwandicomadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity
{

   TextInputLayout  id,pwd;
   private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.id);
        pwd = findViewById(R.id.pwd);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i = id.getEditText().getText().toString();
                String pass = pwd.getEditText().getText().toString();
                if (i.equals("123") && pass.equals("123"))
                {
                    Intent intent=new Intent(MainActivity.this,AddAdminStoreToDatabaseActivity.class);
                    startActivity(intent);
                }
                else
                {
                   error();
                }
            }
        });
    }

    private void error() {
        Toast.makeText(this,"Invalid Credentials",Toast.LENGTH_LONG).show();
    }
}

