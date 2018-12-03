package com.example.diego.DetectorCortes

import android.app.Notification
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.diego.DetectorCortes"
    private val description = "Test Notification"

    private var editLugar: EditText? = null
    private var editNumero: EditText? = null
    private var button: Button? = null
    private var lugar = ""
    private var numero = ""

    companion object {
        val TAG = "ChatLog"
    }

    private lateinit var colorAdapter: ColorAdapter

    //metodo que se ejecuta al iniciar la aplicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Add Device"

        //conectar a firebase
        val duenoDispositivo = FirebaseAuth.getInstance().uid
        if (duenoDispositivo == null) return
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("/Devices/$duenoDispositivo")

        //se asocian las variables a su objeto en la pantalla
        //"R." para buscar en la pantalla según tengo entendido
        editLugar = findViewById(R.id.editText_Lugar) as EditText
        editNumero = findViewById(R.id.editText_Numero) as EditText
        button = findViewById(R.id.button_Agregar) as Button
        button!!.setOnClickListener(this)
        editLugar?.addTextChangedListener(this)
        editNumero?.addTextChangedListener(this)

        val arreglo = ArrayList<Dispositivo>() //de la clase que creare
        colorAdapter = ColorAdapter(applicationContext, android.R.layout.simple_list_item_1,  arreglo)
        //READ DATA FROM FIREBASE
        val readPath = myRef//
        readPath.addValueEventListener(object : ValueEventListener{
            val corteEn = ArrayList<String>()
            override fun onDataChange(snapshot: DataSnapshot) {
                var hayCorte = 0
                val children = snapshot!!.children
                colorAdapter!!.clear()
                corteEn.clear()
                children.forEach {
                    val key = it.key.toString()
                    val estado = it.child("Estado_Corte_Energia").value.toString()
                    val lugar = it.child("Lugar").value.toString()
                    val numero = it.child("Telefono").value.toString()
                    //val time = it.child("Timestamp").value.toString()

                    //Toast.makeText(applicationContext, time, Toast.LENGTH_SHORT).show()
                    val dispositivo = Dispositivo(key, estado, lugar, numero) // y timestamp
                    if(estado == "OFF" || estado == "false"){
                        hayCorte = 1
                        corteEn.add(lugar)
                    }
                    colorAdapter!!.add(dispositivo)
                    //Log.d("------------->", key)
                }
                listaLugares!!.adapter = colorAdapter
                Log.d("hayCorte------------->", hayCorte.toString())
                if(hayCorte == 1){
                    //showNotification(corteEn)
                    //lo cambié, mejor que HomeActivity sea el encargado
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
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

    //interface que se ejecuta despues de que ingresamos algo al campo de texto
    override fun afterTextChanged(s: Editable?) {

    }
    //interface que se ejecuta antes de que ingresamos algo al campo de texto
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    //interface que se ejecuta al momento de que ingresamos algo al campo de texto
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        lugar = editLugar?.text.toString()
        if(lugar?.equals("") ?: ("" === null)){
            editLugar!!.inputType
        }

        //Esto hará que se muestre una mini notificacion en pantalla
        //Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show()
    }

    //AL CLICKEAR EL BOTON AGREGAR
    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.button_Agregar -> sendNewDevice()
        }
    }
    private fun sendNewDevice(){
        //si hay dato vacio, se hace "focus" para indicar que lo llenee
        lugar = editLugar?.text.toString()
        numero = editNumero?.text.toString()
        if(lugar?.equals("") ?: ("" === null)){
            editLugar!!.requestFocus()
        }else{
            if (numero?.equals("") ?: ("" === null)){
                editNumero!!.requestFocus()
            }else{
                //conectar a firebase
                val duenoDispositivo = FirebaseAuth.getInstance().uid
                if (duenoDispositivo == null) return

                val reference = FirebaseDatabase.getInstance().getReference("/Devices/$duenoDispositivo")

                val textLugar = editText_Lugar.text.toString()
                val textNumero = editText_Numero.text.toString()
                val key = reference.push().key
                Log.d(TAG, textLugar)
                Log.d(TAG, textNumero)
                Log.d(TAG, key)

                reference!!.child(key.toString()).child("Lugar").setValue(textLugar)
                reference!!.child(key.toString()).child("Telefono").setValue(textNumero)
                reference!!.child(key.toString()).child("Estado_Corte_Energia").setValue("true")
                reference!!.child(key.toString()).child("Timestamp").setValue(System.currentTimeMillis())
                //timestamp system.currentmillis
                Toast.makeText(this, "Ingresando dispositivo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
