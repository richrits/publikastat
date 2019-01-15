package com.skripsi.android.publikasiapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.skripsi.android.publikasiapp.activity.MainActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkUtil {
    public NetworkUtil() {
    }
//        public boolean isOnline1() {
//        try {
//            int timeoutMs = 1500;
//            Socket sock = new Socket();
//            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
//
//            sock.connect(sockaddr, timeoutMs);
//            sock.close();
//
//            return true;
//        } catch (IOException e) { return false; }
//    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
