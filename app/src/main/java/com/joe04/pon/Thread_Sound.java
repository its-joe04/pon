package com.joe04.pon;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Thread_Sound implements Runnable{

    private String s_ipAddress;
    private int s_port;
    private boolean s_killServer = false;

    public Thread_Sound(String s_ipAddress , int s_port){
        this.s_ipAddress = s_ipAddress;
        this.s_port = s_port;
    }

    public void killConnection(){
        s_killServer = true;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedInputStream bufferedInputStream = null;
        try{
            socket = new Socket(s_ipAddress , s_port);
        } catch (UnknownHostException e) {
            killConnection();
            e.printStackTrace();
        } catch (IOException e) {
            killConnection();
            e.printStackTrace();
        } catch (SecurityException e){
            killConnection();
            e.printStackTrace();
        }

        if (false == s_killServer) {
            try {
                bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            } catch (UnsupportedEncodingException e) {
                killConnection();
                e.printStackTrace();
            } catch (IOException e) {
                killConnection();
                e.printStackTrace();
            }
        }


        final int sampleRate = 48000;

        int musicLength = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, musicLength,
                AudioTrack.MODE_STREAM);
        audioTrack.play();

        byte[] audioBuffer = new byte[musicLength * 8];

        while (false == s_killServer) {
            try {
                int sizeRead = bufferedInputStream.read(audioBuffer, 0, musicLength * 8);
                int sizeWrite = audioTrack.write(audioBuffer, 0, sizeRead);
                if (sizeWrite == AudioTrack.ERROR_INVALID_OPERATION) {
                    sizeWrite = 0;
                }
                if (sizeWrite == AudioTrack.ERROR_BAD_VALUE) {
                    sizeWrite = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        audioTrack.stop();
        socket = null;
        bufferedInputStream = null;
    }
}
