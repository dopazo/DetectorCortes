package com.example.diego.DetectorCortes

import android.content.Context
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

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
        textView.text = dispositivo.lugar + " - " + dispositivo.estado

        //TODO: modificarlo para que se vea mejor (como lo tenia antes la popi)

        textView.setTextColor(Color.BLUE)
        if (dispositivo.estado == "true" || dispositivo.estado == "ON") {
            textView.setBackgroundColor(Color.GREEN)
        }
        else{
            textView.setBackgroundColor(Color.RED)
        }
        return view
    }
}