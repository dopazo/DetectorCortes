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

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    //inicializacion de variables, junto con su tipo de variable
    //terminar el tipo en "?" para permitir que este vacia o null

    private var editLugar: EditText? = null
    private var editNumero: EditText? = null
    private var button: Button? = null
    private var lugar = ""
    private var Numero = ""

    private var num = 20
    private var count = 1
    internal var keyLv: Array<String>? = null
    internal var lugarLv: Array<String>? = null
    internal var NumeroLv: Array<String>? = null
    internal var estadoLv: Array<String>? = null

    /*
    var ID = arrayOfNulls<String>(20)
    var estado = arrayOfNulls<String>(20)
    var textLugar = arrayOfNulls<String>(20)
    var textNumero = arrayOfNulls<String>(20)
    */

    //conectar a firebase
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("Devices")

    companion object {
        val TAG = "ChatLog"
    }
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

        keyLv = Array<String>(20, {""})
        lugarLv = Array<String>(20, {""})
        NumeroLv = Array<String>(20, {""})
        estadoLv = Array<String>(20, {""})

        //READ DATA FROM FIREBASE
        val readPath = myRef//.
        readPath.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot?) {
                val children = snapshot!!.children
                var largo = snapshot.getChildrenCount().toInt()
                num = largo
                children.forEach {
                    //println(it.toString())
                    val dataReaded = it.toString()
                    //Toast.makeText(applicationContext , dataReaded, Toast.LENGTH_SHORT).show()
                    var lugares: Array<String>
                    var estados: Array<String>
                    var keys: Array<String>
                    for (i in 0..num){
                        if(count == num+1){
                            //Toast.makeText(applicationContext, "No se pudo cargar todos los dispositivos", Toast.LENGTH_SHORT).show()
                            //break
                        }
                        if(lugarLv?.get(i).equals("")){

                            //asignar valores descargador de firebase

                            /*
                            ID[i] = it.child("ID").value.toString()
                            estado[i] = it.child("Estado_Corte_Energia").value.toString()
                            textLugar[i] = it.child("Lugar").value.toString()
                            textNumero[i] = it.child("Telefono").value.toString()
                            */

                            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
                            //val key = it.child("key").value.toString()
                            val key = it.key.toString()
                            val estado = it.child("Estado_Corte_Energia").value.toString()
                            val textLugar = it.child("Lugar").value.toString()
                            val textNumero = it.child("Telefono").value.toString()

                            //timestamp system.currentmillis
                            Toast.makeText(applicationContext, "descargando datos", Toast.LENGTH_SHORT).show()

                            keyLv?.set(i, key)
                            lugarLv?.set(i, textLugar)
                            NumeroLv?.set(i, textNumero)
                            estadoLv?.set(i, estado)

                            lugares = Array(count,{""})
                            estados = Array(count,{""})
                            keys = Array(count,{""})
                            System.out.println("---------------------------------------------------");
                            System.out.println("---------------------------------------------------");
                            System.out.println("---------------------------------------------------");
                                for (j in 0..i){
                                    keys[j] = keyLv?.get(j) as String
                                    lugares[j] = lugarLv?.get(j) as String
                                    estados[j] = estadoLv?.get(j) as String
                                    System.out.println(keys[j])
                                    System.out.println(lugares[j])
                                    System.out.println(estados[j])
                                    System.out.println("---------------")
                                    //que agregue el ultimo, actualmente agrega el primero
                            }
                            System.out.println("---------------------------------------------------");
                            System.out.println("---------------------------------------------------");
                            System.out.println("---------------------------------------------------");
                            //val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1,lugares)
                            val adapter = ColorAdapter(applicationContext, android.R.layout.simple_list_item_1,estados)


                            listaLugares!!.adapter = adapter
                            count++
                            break

                        }
                    }

                    /*
                    val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1,textLugar)
                    listaLugares!!.adapter = adapter
                    */

                }
            }

        })

        //addTextChangedListener(this) <- muestra mini notificacion
    }

    class ColorAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val entities: Array<String>):
            ArrayAdapter<String>(context, layoutResource, entities) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = super.getView(position, convertView, parent)
            val textView = view.findViewById<View>(android.R.id.text1) as TextView
            textView.setTextColor(Color.BLUE)
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
                var lugares: Array<String>
                for (i in 0..num){
                    //evitar crasheo al ingresar 20 lugares
                    /*
                    if(count == num+1){
                        Toast.makeText(this, "Cantidad máxima alcanzada", Toast.LENGTH_SHORT).show()
                        break
                    }*/
                    if(lugarLv?.get(i).equals("")){

                        //subir a firebase

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

                        /*
                        //TODO: no poner los que he puesto, sino que los que subi, pues el que puse se subió
                        lugarLv?.set(i, lugar)
                        NumeroLv?.set(i, Numero)
                        lugares = Array(count,{""})
                        for (j in 0..i){
                            lugares[j] = lugarLv?.get(j) as String
                        }
                        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lugares)
                        listaLugares!!.adapter = adapter
                        count++
                        */
                        break
                    }

                }
            }
        }
    }
}
