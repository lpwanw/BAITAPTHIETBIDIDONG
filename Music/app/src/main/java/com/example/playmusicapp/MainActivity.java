package com.example.playmusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    String[] items;
    static final String PATH = Environment.DIRECTORY_MUSIC;
    public final int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_Songs);

        //check quyền đọc file
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            return;
        }else{
            displaySongs();
        }



    }

    //Check quyền đọc file
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                displaySongs();
            }
        }
    }

    //Đọc các file mp3 từ bộ nhớ
    public ArrayList<File> getSongs(File rootFolder){
        ArrayList<File> fileList = new ArrayList<>();
        File[] files = rootFolder.listFiles();
        Log.e("Size","Path: "+rootFolder.getAbsolutePath());
        for(File file:files){
            if (file.isDirectory() && file.isHidden()){
                fileList.addAll(getSongs(file));
            }else{
                if(file.getName().endsWith(".mp3")){
                    fileList.add(file);
                }
            }
        }
        return fileList;
//        if(file!=null)
//        for(File singleFile : files){
//            if (singleFile!=null&&singleFile.isDirectory() && !singleFile.isHidden()){
//                arrayList.addAll(getSongs(singleFile));
//            }else{
//                if (singleFile.getName().endsWith(".mp3")){
//                    arrayList.add(singleFile);
//                }
//            }
//        }
    }


    public void displaySongs(){
        final ArrayList<File> mySongs = getSongs(getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        Log.e("Size","Size: "+ mySongs.size());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "");
        }

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName = (String) listView.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), AudioPlayer.class)
                .putExtra("songs", mySongs)
                .putExtra("songname", songName)
                .putExtra("pos", position)
                );

            }
        });

    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView = getLayoutInflater().inflate(R.layout.song_item, null);
            TextView textSong = myView.findViewById(R.id.song_title);
            textSong.setSelected(true);
            textSong.setText(items[position]);

            return myView;
        }
    }




}