package com.joe04.pon;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private boolean playState = false;
    Thread_Sound thread_sound = null;
    Shared_pref shared_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView tv_main_getDevice = findViewById(R.id.main_command1);
        tv_main_getDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("a", tv_main_getDevice.getText().toString());
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view, "copied to clipboard", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        });
        TextView tv_main_command = findViewById(R.id.main_command);
        tv_main_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("a", tv_main_command.getText().toString());
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view, "copied to clipboard", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();
            }
        });

        /*
         * toolbar
         */
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pulse over tcp/ip");
        setSupportActionBar(toolbar);

        /*
         * Lottie Animation
         */
        LottieAnimationView lottieAnimationView = findViewById(R.id.main_lottieAnimationView);
        lottieAnimationView.setAnimation(R.raw.notconnected);
        lottieAnimationView.setRepeatCount(Animation.INFINITE);


        ExtendedFloatingActionButton eFab_main_play = findViewById(R.id.main_btn_play);
        eFab_main_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playState == false) {
                    playState = true;
                    eFab_main_play.setText("Stop");
                    if (thread_sound != null) {
                        thread_sound.killConnection();
                        thread_sound = null;
                    }
                    TextInputEditText et_serverIp = findViewById(R.id.main_et_ip);
                    TextInputEditText et_serverPort = findViewById(R.id.main_et_port);
                    shared_pref = new Shared_pref(MainActivity.this);
                    shared_pref.saveServerInfo(et_serverIp.getText().toString(), Integer.valueOf(et_serverPort.getText().toString()));

                    if (et_serverPort.getText().length() <= 0 || et_serverPort == null) {
                        thread_sound = new Thread_Sound(et_serverIp.getText().toString(), 8000);
                    } else {
                        thread_sound = new Thread_Sound(et_serverIp.getText().toString(), Integer.valueOf(et_serverPort.getText().toString()));
                    }
                    new Thread(thread_sound).start();
                    lottieAnimationView.setAnimation(R.raw.streaming);
                    lottieAnimationView.playAnimation();
                } else {
                    playState = false;
                    eFab_main_play.setText("Play");
                    lottieAnimationView.setAnimation(R.raw.notconnected);
                    lottieAnimationView.playAnimation();
                    if (thread_sound != null) {
                        thread_sound.killConnection();
                        thread_sound = null;
                    }
                }

            }
        });


        /*
         * reload
         */
        shared_pref = new Shared_pref(MainActivity.this);
        TextInputEditText et_serverip = findViewById(R.id.main_et_ip);
        if (shared_pref.getServerIp().length() > 0) {
            et_serverip.setText(shared_pref.getServerIp());
        }
        TextInputEditText et_serverport = findViewById(R.id.main_et_port);
        if (shared_pref.getPort() > 0) {
            et_serverport.setText(Integer.valueOf(shared_pref.getPort()).toString());
        }

    }
}