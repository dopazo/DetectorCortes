package com.example.diego.DetectorCortes

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.text.TextUtils.replace
import android.util.Log
import com.example.diego.DetectorCortes.R.id.listaLugares
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*

class NotificationService : Service() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //conectar a firebase
        var database = FirebaseDatabase.getInstance()
        //TODO: como hacer que guarde previamente el uid para usar con la app cerrada :(
        val duenoDispositivo = FirebaseAuth.getInstance().uid
        var myRef = database.getReference("/Devices/$duenoDispositivo")

        val readPath = myRef
        readPath.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var hayCorte = 0
                var corteEn = ArrayList<String>()
                corteEn.removeAll(corteEn)
                val children = snapshot!!.children
                children.forEach {
                    val key = it.key.toString()
                    val estado = it.child("Estado_Corte_Energia").value.toString()
                    val lugar = it.child("Lugar").value.toString()
                    val numero = it.child("Telefono").value.toString()
                    //val time = it.child("Timestamp").value.toString()

                    if(estado == "OFF" || estado == "false"){
                        hayCorte = 1
                        corteEn.add(lugar)
                    }
                    Log.d("corteEn------------->", corteEn.toString())
                }
                if(hayCorte == 1){
                    showNotification(corteEn)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })

        return super.onStartCommand(intent, flags, startId)
    }
    public fun showNotification(corteEn: ArrayList<String>)
    {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val corteEnComas = corteEn.toString()
                .replace(",", ", ")
                .replace("[", "")
                .replace("]", "")
                .trim()           //remove trailing spaces from partially initialized arrays
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder = Notification.Builder(this)
                .setContentTitle("DetectorCortes")
                .setContentText("Ha ocurrido un corte de energia en " + corteEnComas)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
        //.setSound(soundUri);

        notificationManager.notify(1234, builder.build())
    }
}
