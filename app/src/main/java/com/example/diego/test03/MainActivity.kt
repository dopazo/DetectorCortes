package com.example.diego.test03

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener {

    //inicializacion de variables, junto con su tipo de variable
    //terminar el tipo en "?" para permitir que este vacia o null

    private var editLugar: EditText? = null
    private var editTelefono: EditText? = null
    private var button: Button? = null
    private var lugar = ""
    private var telefono = ""
    internal var lista: ListView? = null
    private var num = 10
    private var count = 1
    internal var lugarLv: Array<String>? = null
    internal var telefonoLv: Array<String>? = null

    //metodo que se ejecuta al iniciar la aplicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //se asocian las variables a su objeto en la pantalla
        //"R." para buscar en la pantalla según tengo entendido
        //"!!" significa que debe tener un valor obligatoriamente (no puede estar null)
        editLugar = findViewById(R.id.editText_Lugar) as EditText
        editTelefono = findViewById(R.id.editText_Telefono) as EditText
        button = findViewById(R.id.button_Agregar) as Button
        button!!.setOnClickListener(this)
        editLugar?.addTextChangedListener(this)
        editTelefono?.addTextChangedListener(this)
        lista?.findViewById<ListView>(R.id.listaLugares)

        lugarLv = Array<String>(10, {""})
        telefonoLv = Array<String>(10, {""})

        //addTextChangedListener(this) <- muestra mini notificacion

        //operacion()
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

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.button_Agregar -> operacion()
        }
    }
    private fun operacion(){
        //si hay dato vacio, se hace "focus" para indicar que lo llenee
        lugar = editLugar?.text.toString()
        telefono = editTelefono?.text.toString()
        if(lugar?.equals("") ?: ("" === null)){
            editLugar!!.requestFocus()
        }else{
            if (telefono?.equals("") ?: ("" === null)){
                editTelefono!!.requestFocus()
            }else{
                var lugares: Array<String>
                for (i in 0..num){
                    if(lugarLv?.get(i).equals("")){
                        lugarLv?.set(i, lugar)
                        telefonoLv?.set(i, telefono)
                        lugares = Array(count,{""})
                        for (j in 0..i){
                            lugares[j] = lugarLv?.get(j) as String
                        }
                        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lugares)
                        listaLugares!!.adapter = adapter
                        count++
                        break
                    }

                }
            }
        }
    }
}
