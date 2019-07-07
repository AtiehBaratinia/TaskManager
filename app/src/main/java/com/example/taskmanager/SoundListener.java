package com.example.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class SoundListener implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    final AudioManager audioManager;
    SeekBar seekBar;
    ImageView volumeButton;
    public SoundListener(Activity activity){
        AlertDialog.Builder settingDialog = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.sound_setting, null);
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        ImageView playButton = view.findViewById(R.id.play_view);
        ImageView pauseButton = view.findViewById(R.id.pause_view);
        volumeButton = view.findViewById(R.id.volume_view);
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setOnSeekBarChangeListener(this);
        playButton.setOnClickListener(this);
        volumeButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        settingDialog.setView(view);
        AlertDialog dialog = settingDialog.create();
        dialog.show();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        }, 0, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_view:
                if (!navigationDrawer.mplayer.isPlaying()) {
                    navigationDrawer.mplayer.start();
                }
                break;
            case R.id.pause_view:
                if (navigationDrawer.mplayer.isPlaying()){
                    navigationDrawer.mplayer.pause();
                }
                break;
            case R.id.volume_view:
                if (seekBar.getProgress() > 0){
                    volumeButton.setBackgroundColor(Color.GRAY);
                    seekBar.setProgress(0);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                }else{
                    volumeButton.setBackgroundColor(Color.WHITE);
                    seekBar.setProgress(2);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 2, 0);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
