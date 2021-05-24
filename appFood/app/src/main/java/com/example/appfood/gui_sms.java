package com.example.appfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class gui_sms extends AppCompatActivity {

    EditText phone,message;
    Button btnsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gui_sms);
        show();

        btnsend = findViewById(R.id.btn_send);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sendsms();
            }
        });

    }


    public void show(){
        phone = findViewById(R.id.editText_phoneNumber);
        message=findViewById(R.id.editText_message);
        phone.setEnabled(false);

        Intent intent=getIntent();

        String txtsize= intent.getStringExtra("sizes");
        String txttype= intent.getStringExtra("types");
        String txtfilling= intent.getStringExtra("fillings");
        String txtdrink= intent.getStringExtra("drinks");

        message.setText("Size: " + txtsize + "\n" +
                "type: " + txttype + "\n" +
                "filling: " + txtfilling + "\n" +
                "drink: " + txtdrink + "\n");




    }

        public void sendsms(){
            btnsend = findViewById(R.id.btn_send);
            String phonenumber= phone.getText().toString();
            String sms=message.getText().toString();
            Uri uri = Uri.parse("smsto:" + phonenumber);
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
            smsIntent.putExtra("sms_body", sms);
            startActivity(smsIntent);





        }


}