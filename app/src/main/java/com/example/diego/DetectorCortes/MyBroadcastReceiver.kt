package com.example.diego.DetectorCortes

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


class MyCustomBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action != null) {
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                val intent = Intent(context, NotificationService::class.java)
                if (context != null) {
                    context.startService(intent)
                }
                // TO-DO: Code to handle BOOT COMPLETED EVENT
                // TO-DO: I can start an service.. display a notification... start an activity
            }
        }
    }
}