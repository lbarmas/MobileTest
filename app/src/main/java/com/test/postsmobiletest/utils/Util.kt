package com.test.postsmobiletest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity

 class Util {
    companion object{
        fun isNetworkConnected(context: Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =  connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities.also {
                if (it != null){
                    if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                        return true
                    else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        return true
                    }
                }
            }
            return false
        }
    }
}