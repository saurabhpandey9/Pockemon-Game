package com.developerdesk9.pokemongame

import android.graphics.drawable.Drawable

class Pockemon {

    var title: String? = null
    var des: String? = null
    var power : Double? =null
    var lat : Double? =null
    var long: Double? =null
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
        this.lat = lat
        this.long = long
        this.isCatched = isCatched
        this.image = image
    }
}