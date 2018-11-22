package com.example.diego.DetectorCortes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

        supportActionBar?.title = "Dispositivos"
//
//        val adapter = GroupAdapter<ViewHolder>()
//
//        adapter.add(DispositivoItem())
//        adapter.add(DispositivoItem())
//        adapter.add(DispositivoItem())
//
//        recyclerview_home_ID.adapter = adapter
        fetchDispositivos()
    }

    private fun fetchDispositivos(){
        val userID = FirebaseAuth.getInstance().uid
        if (userID == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/Devices/$userID")
        //val ref = FirebaseDatabase.getInstance().getReference("/Devices")
//        ref.addChildEventListener(object: ChildEventListener {
//
//            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                val chatMessage = p0.getValue(ChatMessage::class.java)
//
//                if (chatMessage != null) {
//                    Log.d(TAG, chatMessage.text)
//
//                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
//                        val currentUser = LatestMessagesActivity.currentUser ?: return
//                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
//                    } else {
//                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
//                    }
//                }
//
//                recyclerview_home_ID.adapter = adapter
//
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot) {
//
//            }
//
//        })
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("HomeActivity", it.toString())
                    val dispositivo = it.getValue(Dispositivo::class.java)
                    if(dispositivo!=null) {
                        adapter.add(DispositivoItem(dispositivo))

                    }
                }


                recyclerview_home_ID.adapter = adapter
            }
        })
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
                val intent = Intent(this, RegisterActivity::class.java)
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

//class CustomAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}

class DispositivoItem(val dispositivo: Dispositivo): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //will be called in our list for each dispositivo item later on..
        viewHolder.itemView.nombredispositivoTxtV_row_home_ID.text = dispositivo.Lugar
        viewHolder.itemView.estado_ONOFF_row_home_ID.text = dispositivo.Estado_Corte_Energia
        viewHolder.itemView.GSMNumeroTxtV_row_home_ID.text = dispositivo.Telefono
    }

    override fun getLayout(): Int {
        return R.layout.dispositivo_row_home
    }
}

