package com.mastercoding.myapplication.controller;

import android.content.Context;
import android.media.MediaPlayer;

import com.mastercoding.myapplication.R;

public class MusicController {
    private static MusicController instance;
    public MediaPlayer mediaPlayer;


    public static synchronized MusicController getInstance(Context context) {
        // If instance does not exist, we will create it
        if (instance == null) {
            instance = new MusicController(context);
        }
        return instance;
    }
    private MusicController(Context context) {
        SoundPlayer(context);
    }
    private void SoundPlayer(Context context){
        mediaPlayer = MediaPlayer.create(context, R.raw.betterdaymusic);
        mediaPlayer.setVolume(0.5f, 0.5f);
        mediaPlayer.setLooping(true);
    }


    public void startMusic() {
        mediaPlayer.start();
    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }

}
