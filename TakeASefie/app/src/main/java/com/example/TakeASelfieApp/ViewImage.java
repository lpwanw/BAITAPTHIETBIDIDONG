package com.example.TakeASelfieApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ViewImage extends Activity {
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Intent i = getIntent();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        File f = new File(i.getStringExtra("FilePath"));

        BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);

        imgView = (ImageView)findViewById(R.id.detail);
        imgView.setImageBitmap(bitmap);
    }
}
