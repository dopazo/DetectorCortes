package com.example.diego.DetectorCortes

import android.content.Context
import android.graphics.Color
import android.os.Build.ID
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import android.widget.TextView
import com.example.diego.DetectorCortes.R.id.editText_Lugar
import com.example.diego.DetectorCortes.R.id.editText_Numero

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    //inicializacion de variables, junto con su tipo de variable
    //terminar el tipo en "?" para permitir que este vacia o null

    private var editLugar: EditText? = null
    private var editNumero: EditText? = null
    private var button: Button? = null
    private var lugar = ""
    private var Numero = ""


    //clase con esto
    internal var keyLv: Array<String>? = null
    internal var lugarLv: Array<String>? = null
    internal var NumeroLv: Array<String>? = null
    internal var estadoLv: Array<String>? = null

    //conectar a firebase
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Devices")

    companion object {
        val TAG = "ChatLog"
    }

    private lateinit var colorAdapter: ColorAdapter

    //metodo que se ejecuta al iniciar la aplicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //se asocian las variables a su objeto en la pantalla
        //"R." para buscar en la pantalla según tengo entendido
        //"!!" significa que debe tener un valor obligatoriamente (no puede estar null)
        editLugar = findViewById(R.id.editText_Lugar) as EditText
        editNumero = findViewById(R.id.editText_Numero) as EditText
        button = findViewById(R.id.button_Agregar) as Button
        button!!.setOnClickListener(this)
        editLugar?.addTextChangedListener(this)
        editNumero?.addTextChangedListener(this)


        // ACA EL ADAPTER
        val arreglo = ArrayList<Dispositivo>() //de la clase que creare
        colorAdapter = ColorAdapter(applicationContext, android.R.layout.simple_list_item_1,  arreglo)
        //colorAdapter.setAlternateColor(getColor())
        //READ DATA FROM FIREBASE
        val readPath = myRef//.
        readPath.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val children = snapshot!!.children
                colorAdapter!!.clear()
                children.forEach {
                    val key = it.key.toString()
                    val estado = it.child("Estado_Corte_Energia").value.toString()
                    val lugar = it.child("Lugar").value.toString()
                    val numero = it.child("Telefono").value.toString()

                    val dispositivo = Dispositivo(key, estado, lugar, numero)

                    colorAdapter!!.add(dispositivo)
                    Log.d("------------->", key)
                }
                listaLugares!!.adapter = colorAdapter
            }

        })

        //addTextChangedListener(this) <- muestra mini notificacion
    }

    inner class Dispositivo {

        var key:String
         var estado:String
         var lugar:String
         var numero:String

        constructor(key:String, estado:String, lugar:String, numero:String): super() {
            this.key = key
            this.estado = estado
            this.lugar = lugar
            this.numero = numero
        }
    }

    class ColorAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val entities: ArrayList<Dispositivo>):
            ArrayAdapter<Dispositivo>(context, layoutResource, entities) { //clase

        var color:Int = 0

        fun setAlternateColor(color:Int) {
            this.color = color
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //explicitar campo a usar
            val view = super.getView(position, convertView, parent)
            val textView = view.findViewById<View>(android.R.id.text1) as TextView
            val dispositivo = this.entities.get(position)
            textView.text = dispositivo.key + " - " + dispositivo.estado
            textView.setTextColor(Color.BLUE)
            /*if (position%2 == 0) {
                textView.setBackgroundColor(this.color)
            }*/
            return view
        }
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
            R.id.button_Agregar -> operacion()
        }
    }
    private fun operacion(){
        //si hay dato vacio, se hace "focus" para indicar que lo llenee
        lugar = editLugar?.text.toString()
        Numero = editNumero?.text.toString()
        if(lugar?.equals("") ?: ("" === null)){
            editLugar!!.requestFocus()
        }else{
            if (Numero?.equals("") ?: ("" === null)){
                editNumero!!.requestFocus()
            }else{
                val textLugar = editText_Lugar.text.toString()
                val textNumero = editText_Numero.text.toString()
                val key = myRef.push().key
                Log.d(TAG, textLugar)
                Log.d(TAG, textNumero)
                Log.d(TAG, key)

                myRef!!.child(key.toString()).child("Lugar").setValue(textLugar)
                myRef!!.child(key.toString()).child("Telefono").setValue(textNumero)
                myRef!!.child(key.toString()).child("Estado_Corte_Energia").setValue("ON")
                myRef!!.child(key.toString()).child("Timestamp").setValue(System.currentTimeMillis())
                //timestamp system.currentmillis
                Toast.makeText(this, "subiendo a firebase", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
