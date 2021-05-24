package com.example.playmusicapp;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;


public class AudioPlayer extends AppCompatActivity {
    ImageView btnPlayPause, btnPre, btnNext, btnShuffle, btnRepeat, imageThumbnail;
    TextView tv_songName, tv_artistName, tv_timeTruoc, tv_timeSau;
    SeekBar audioSeek;

    String sName;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

    Thread updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player);


        btnPlayPause = findViewById(R.id.button_playpause);
        btnPre = findViewById(R.id.button_previous);
        btnNext = findViewById(R.id.button_next);

        imageThumbnail = findViewById(R.id.imageview_thumbnail);

        tv_songName = findViewById(R.id.tv_songName);
        tv_timeTruoc = findViewById(R.id.tv_timeTruoc);
        tv_timeSau = findViewById(R.id.tv_timeSau);

        audioSeek = findViewById(R.id.seekBarAudio);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 99);
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);
        tv_songName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).getAbsolutePath());
        sName = mySongs.get(position).getName();
        tv_songName.setText(sName);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                }else{
                    btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sName = mySongs.get(position).getName();
                tv_songName.setText(sName);
                mediaPlayer.start();
                btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                startAnimation(imageThumbnail);

            }
        });
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());

                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sName = mySongs.get(position).getName();
                tv_songName.setText(sName);
                mediaPlayer.start();
                btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                startAnimation(imageThumbnail);

            }
        });

        //Cập nhật seek bar
        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition < totalDuration){
                    try{
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        audioSeek.setProgress(currentPosition);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        audioSeek.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        audioSeek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.teal_700), PorterDuff.Mode.MULTIPLY);
        audioSeek.getThumb().setColorFilter(getResources().getColor(R.color.teal_700), PorterDuff.Mode.SRC_IN);

        audioSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        tv_timeSau.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                tv_timeTruoc.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnNext.performClick();
            }
        });






    }



    //Xoay tròn ảnh bài hát
    public void startAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageThumbnail, "rotation", 0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time += min + ":";
        if (sec < 10){
            time += "0";
        }
        time += sec;
        return time;
    }


}