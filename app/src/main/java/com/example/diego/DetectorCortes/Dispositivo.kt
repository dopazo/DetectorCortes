package com.example.diego.DetectorCortes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Dispositivo(val key: String, val estado: String, val lugar: String, val numero: String):Parcelable{
    constructor() : this("","", "","")
}