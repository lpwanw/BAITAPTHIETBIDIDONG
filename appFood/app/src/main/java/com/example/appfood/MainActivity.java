package com.example.appfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnorder;
    RadioButton bigsize;
    RadioButton smallsize;
    RadioButton cay;
    RadioButton notcay;
    RadioButton com;
    RadioButton soup;
    RadioButton khoai;
    RadioButton spagetti;
    RadioButton pepsi, sting,fanta,mirida;



    private static final String TAG =MainActivity.class.getSimpleName() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        order();
        // nut location
        ImageView location= findViewById(R.id.imglocation);
        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/V7vox1WudhYNZDxG8"));

                startActivity(intent);
            }
        });

    }


    private  void order(){
//        CheckBox bigsize,smallsize,cay,notcay,com,soup,khoai,spagetti,pepsi, sting,fanta,mirida;
        btnorder = (Button) findViewById(R.id.btnorder);
        bigsize =(RadioButton) findViewById(R.id.sizeB);
        smallsize =(RadioButton) findViewById(R.id.sizeB5);
        notcay =(RadioButton) findViewById(R.id.NotSpicy);
        cay =(RadioButton) findViewById(R.id.Spicy);
        com =(RadioButton) findViewById(R.id.rice);
        soup =(RadioButton) findViewById(R.id.soup);
        khoai =(RadioButton) findViewById(R.id.chips);
        spagetti =(RadioButton) findViewById(R.id.spaghetti);
        pepsi =(RadioButton) findViewById(R.id.pep);
        sting =(RadioButton) findViewById(R.id.sting);
        fanta =(RadioButton) findViewById(R.id.fanta);
        mirida =(RadioButton) findViewById(R.id.mil);
        pepsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sting.setChecked(false);
                fanta.setChecked(false);
                mirida.setChecked(false);
            }
        });
        sting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pepsi.setChecked(false);
                fanta.setChecked(false);
                mirida.setChecked(false);
            }
        });
        fanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pepsi.setChecked(false);
                sting.setChecked(false);
                mirida.setChecked(false);
            }
        });
        mirida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pepsi.setChecked(false);
                sting.setChecked(false);
                fanta.setChecked(false);
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soup.setChecked(false);
                khoai.setChecked(false);
                spagetti.setChecked(false);
            }
        });
        soup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.setChecked(false);
                khoai.setChecked(false);
                spagetti.setChecked(false);
            }
        });
        khoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soup.setChecked(false);
                com.setChecked(false);
                spagetti.setChecked(false);
            }
        });
        spagetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soup.setChecked(false);
                com.setChecked(false);
                khoai.setChecked(false);
            }
        });
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),dathang.class);
                ArrayList<String> sizes =new ArrayList<String>();
                int f1=0,f2=0,f3=0,f4=0,f5=0;
                if (bigsize.isChecked()){
                   sizes.add(bigsize.getText().toString());
                   f1=1;
                }
                if (smallsize.isChecked()){
                    sizes.add(smallsize.getText().toString());
                    f1=1;
                }
                ArrayList<String> types =new ArrayList<String>();
                if (notcay.isChecked()){
                    types.add(notcay.getText().toString());
                    f2=1;
                }
                if (cay.isChecked()){
                    f2=1;
                    types.add(cay.getText().toString());
                }

//                Filling
                ArrayList<String> fillings =new ArrayList<String>();
                 if (com.isChecked()){
                     f3=1;
                    fillings.add(com.getText().toString());
                 }
                if (soup.isChecked()){
                    f3=1;
                    fillings.add(soup.getText().toString());
                }
                if (khoai.isChecked()){
                    f3=1;
                    fillings.add(khoai.getText().toString());
                }
                if (spagetti.isChecked()){
                    f3=1;
                    fillings.add(spagetti.getText().toString());
                }
//              Drink
                ArrayList<String> drinks =new ArrayList<String>();
                if (pepsi.isChecked()){
                    f4=1;
                    drinks.add(pepsi.getText().toString());
                }
                if (sting.isChecked()){
                    f4=1;
                    drinks.add(sting.getText().toString());
                }
                if (fanta.isChecked()){
                    f4=1;
                    drinks.add(fanta.getText().toString());
                }
                if (mirida.isChecked()){
                    f4=1;
                    drinks.add(mirida.getText().toString());
                }
                i.putExtra("sizes",  sizes);
                i.putExtra("types",  types);
                i.putExtra("fillings",  fillings);
                i.putExtra("drinks",  drinks);
                startActivity(i);
            }
        });






    }



}