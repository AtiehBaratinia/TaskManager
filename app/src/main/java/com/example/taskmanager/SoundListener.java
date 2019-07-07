package com.example.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class SoundListener implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    final AudioManager audioManager;

    public SoundListener(Activity activity){
        AlertDialog.Builder settingDialog = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.sound_setting, null);
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        ImageView playButton = view.findViewById(R.id.play_view);
        ImageView pauseButton = view.findViewById(R.id.pause_view);
        ImageView volumeButton = view.findViewById(R.id.volume_view);
        SeekBar seekBar = view.findViewById(R.id.seekbar);
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setOnSeekBarChangeListener(this);
        playButton.setOnClickListener(this);
        volumeButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        settingDialog.setView(view);
        AlertDialog dialog = settingDialog.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_view:
                break;
            case R.id.pause_view:
                break;
            case R.id.volume_view:
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
