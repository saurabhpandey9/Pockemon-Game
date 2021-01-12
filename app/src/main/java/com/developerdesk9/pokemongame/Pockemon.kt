package com.developerdesk9.pokemongame

import android.graphics.drawable.Drawable
import android.location.Location

class Pockemon {

    var title: String? = null
    var des: String? = null
    var power : Double? =null
    var location : Location? = null
    var isCatched:Boolean = false
    var image: Int? = null

    constructor(
        title: String?,
        des: String?,
        power: Double?,
        lat: Double?,
        long: Double?,
        isCatched: Boolean,
        image: Int?
    ) {
        this.title = title
        this.des = des
        this.power = power
        this.location = Location(title)
        this.location!!.latitude= lat!!
        this.location!!.longitude=long!!
        this.isCatched = isCatched
        this.image = image
    }
}