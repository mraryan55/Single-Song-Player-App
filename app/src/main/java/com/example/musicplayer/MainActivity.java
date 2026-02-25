package com.example.musicplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button start, pause; // Play aur Pause button ka reference
    private SeekBar seekBar; // SeekBar ka reference
    private MediaPlayer mediaPlayer; // MediaPlayer object jo song play karega

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Edge-to-Edge layout enable kar raha hai

        setContentView(R.layout.activity_main); // XML layout ko set kar raha hai

        // FindViewById se UI elements ko initialize kar rahe hain
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        seekBar = findViewById(R.id.seekBar);

        // MediaPlayer se audio file load kar rahe hain
        mediaPlayer = MediaPlayer.create(this, R.raw.victory); // victory.mp3 ko load kar raha hai

        // Set the SeekBar's max value to the duration of the audio file
        seekBar.setMax(mediaPlayer.getDuration());

        // PLAY button par click listener lagaya
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Song Play", Toast.LENGTH_SHORT).show();
                mediaPlayer.start(); // Audio start karne ka command
                updateSeekBar(); // SeekBar update karna
            }
        });

        // PAUSE button par click listener lagaya
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Song Paused", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause(); // Audio pause karne ka command
            }
        });

        // SeekBar par change listener lagaya
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) { // Agar user manually SeekBar change kare
                    mediaPlayer.seekTo(progress); // Audio position change karna
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Thread to update SeekBar position while the audio is playing
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition()); // SeekBar ko update karna
                        }
                        Thread.sleep(100); // Delay for a short time to update SeekBar smoothly
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer ko release karna jab activity destroy ho
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Method to update SeekBar position in the UI thread
    private void updateSeekBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        });
    }
}

