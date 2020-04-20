package com.example.perpustakaandigital.network

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityStatus {
    companion object{
        fun isConnected(context: Context?): Boolean{
            val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val connection = manager.activeNetworkInfo
            return connection != null && connection.isConnected
        }
    }
}