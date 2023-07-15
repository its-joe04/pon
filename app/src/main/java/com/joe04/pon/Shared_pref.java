package com.joe04.pon;

import android.content.Context;
import android.content.SharedPreferences;

    public class Shared_pref {
        SharedPreferences sharedPreferences;
        public Shared_pref(Context context){
            sharedPreferences = context.getSharedPreferences("serverInfo" , Context.MODE_PRIVATE);
        }
        public void saveServerInfo(String serverIp , int port){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("serverIp" , serverIp);
            editor.putInt("port" , port);
            editor.apply();
        }


        public String getServerIp(){
            return sharedPreferences.getString("serverIp" , "");
        }
        int port;
        public int getPort(){
            return sharedPreferences.getInt("port" , port);
        }

    }

