package com.example.appfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class dathang extends AppCompatActivity {

        TextView tvsize,tvtype,tvfilling,tvdrink;
        Button btnback,btnxacnhan;
        String txtsize,txttype,txtfilling,txtdrink;
         ArrayList<String> sizes =new ArrayList<String>();
         ArrayList<String> types =new ArrayList<String>();
             ArrayList<String> fillings =new ArrayList<String>();
            ArrayList<String> drinks =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dathang);
        ImageView location= findViewById(R.id.imglocation);

        Intent i=getIntent();
        sizes=i.getStringArrayListExtra("sizes");
        types=i.getStringArrayListExtra("types");
        fillings=i.getStringArrayListExtra("fillings");
        drinks=i.getStringArrayListExtra("drinks");

        txtsize="";
        for(String s:sizes){
            txtsize += s + "";
        }
        txttype="";
        for(String s:types){
            txttype += s + "";
        }
        txtfilling="";
        for(String s:fillings){
            txtfilling += s + "";
        }
        txtdrink="";
        for(String s:drinks){
            txtdrink += s + "";
        }
        tvsize=(TextView) findViewById(R.id.showsize);
        tvsize.setText(txtsize);
        tvtype=(TextView) findViewById(R.id.showtype);
        tvtype.setText(txttype);
        tvfilling=(TextView) findViewById(R.id.showfilling);
        tvfilling.setText(txtfilling);
        tvdrink=(TextView) findViewById(R.id.showdrink);
        tvdrink.setText(txtdrink);





        back();
        xacnhan();
//        location
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/V7vox1WudhYNZDxG8"));

                startActivity(intent);
            }
        });
    }
    private void back(){
        btnback=findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }
    private void xacnhan(){

        btnxacnhan = findViewById(R.id.btnxacnhan);
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),gui_sms.class);
                intent.putExtra("sizes",txtsize);
                intent.putExtra("types",txttype);
                intent.putExtra("fillings",txtfilling);
                intent.putExtra("drinks",txtdrink);
                startActivity(intent);
            }
        });





    }
}