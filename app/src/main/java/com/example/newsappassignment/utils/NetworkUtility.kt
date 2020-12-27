package com.example.newsappassignment.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.newsappassignment.R
import java.net.InetAddress

class NetworkUtility {
    companion object {

       //to check if the device is connected ( Only for Android API version less than 21)
        fun internetCheck(c: Context): Boolean {
            val connectivityManager =
                c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
                .isConnectedOrConnecting
        }

        // to check if the Application is able to connect to API server.
        fun isAPIAvailable(c: Context): Boolean {
            return try {
                val ipAddr: InetAddress = InetAddress.getByName(c.getString(R.string.api_server))
                ipAddr.hostAddress != ""
            } catch (e: Exception) {
                false
            }
        }
    }
}