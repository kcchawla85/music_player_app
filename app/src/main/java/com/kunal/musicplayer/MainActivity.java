package com.kunal.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button forward_btn, rewind_btn, play_btn, pause_btn;
    TextView time_text, title_txt;
    SeekBar seekbar;
    //Media player
    MediaPlayer mediaPlayer;
    // Handlers
    Handler handler = new Handler();
    //Variables
    double startTime=0;
    double finalTime=0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play_btn = findViewById(R.id.play);
        forward_btn = findViewById(R.id.forward);
        rewind_btn = findViewById(R.id.rewind);
        pause_btn = findViewById(R.id.pause);
        title_txt = findViewById(R.id.title_song);
        time_text = findViewById(R.id.minutes);
        seekbar = findViewById(R.id.seekBar);

        //Media player
        mediaPlayer = MediaPlayer.create(this, R.raw.sohne);
        title_txt.setText(getResources().getIdentifier("sohne", "raw", getPackageName()));
        seekbar.setClickable(false);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);

                } else {
                    Toast.makeText(MainActivity.this,
                            "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rewind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Can't Go Back!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


            private void PlayMusic() {
                mediaPlayer.start();
                finalTime=mediaPlayer.getDuration();
                startTime=mediaPlayer.getCurrentPosition();
                if(oneTimeOnly==0)
                {
                    seekbar.setMax((int)finalTime);
                    oneTimeOnly=1;

                }
                time_text.setText(String.format("%d min, %d sec ", TimeUnit.MILLISECONDS.toMinutes((long)finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
                seekbar.setProgress((int)startTime);
                handler.postDelayed(UpdateSongTime, 100);
            }
            //creating runnables
            private Runnable UpdateSongTime = new Runnable() {
                @Override
                public void run() {
                    startTime = mediaPlayer.getCurrentPosition();
                    time_text.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
                    ));


                    seekbar.setProgress((int)startTime);
                    handler.postDelayed(this, 10);
                }
            };
        }


