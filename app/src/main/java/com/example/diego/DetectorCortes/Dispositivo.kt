package com.example.diego.DetectorCortes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Dispositivo(val key: String, val Lugar: String, val Telefono: String, val Estado_Corte_Energia: String):Parcelable{
    constructor() : this("","", "","")
}