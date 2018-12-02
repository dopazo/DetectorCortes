package com.example.diego.DetectorCortes

import android.app.ProgressDialog.show
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dispositivo_row_home.view.*

//Creditos a https://www.youtube.com/watch?v=ihJGxFu2u9Q&list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-
//y a Brian Voong :)

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        verifyUserIsLoggedIn()

        supportActionBar?.title = "Devices"

//        recyclerview_home_ID.adapter = adapter
        fetchDispositivos()
        val lv = findViewById<ListView>(R.id.recyclerview_home_ID)

        lv.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, i, l ->
            Toast.makeText(this,
                    "posicion seleccionada: "+ i,
                    //TODO: como selecciona el valor lugar para poder modificarlo
                    Toast.LENGTH_LONG).show()
        }
    }


    private fun fetchDispositivos(){
        val userID = FirebaseAuth.getInstance().uid
        if (userID == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/Devices/$userID")

        ref.addValueEventListener(object: ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                //val adapter = GroupAdapter<ViewHolder>()
                val array = ArrayList<Dispositivo>() //de la clase que creare
                val adapter = ColorAdapter(applicationContext, android.R.layout.simple_list_item_1, array)
                adapter!!.clear()
                p0.children.forEach {
                    Log.d("HomeActivity", it.toString())
                    //val dispositivo = it.getValue(Dispositivo::class.java)
                    val key = it.key.toString()
                    val estado = it.child("Estado_Corte_Energia").value.toString()
                    val lugar = it.child("Lugar").value.toString()
                    val numero = it.child("Telefono").value.toString()

                    val dispositivo = Dispositivo(key, estado, lugar, numero) // y timestamp
                    adapter!!.add(dispositivo)
                }


                //recyclerview_home_ID.adapter = adapter
                recyclerview_home_ID!!.adapter = adapter
            }
        })
    }

     fun ListView.onClick(v: AdapterView<ColorAdapter>,view: View,position: Int,id: Long) {
         Toast.makeText(context, "position: "+ position.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun verifyUserIsLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_add_device -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}


class DispositivoItem(val dispositivo: Dispositivo): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //will be called in our list for each dispositivo item later on..
        viewHolder.itemView.nombredispositivoTxtV_row_home_ID.text = dispositivo.lugar
        viewHolder.itemView.estado_ONOFF_row_home_ID.text = dispositivo.estado
        viewHolder.itemView.GSMNumeroTxtV_row_home_ID.text = dispositivo.numero

    }

    override fun getLayout(): Int {

        return R.layout.dispositivo_row_home
    }
}

