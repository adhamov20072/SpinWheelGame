package com.aimardon.spinname

import android.graphics.Bitmap

class WheelItem {

    var color: Int
    var bitmap: Bitmap? = null
    var text: String? = null

    constructor(color: Int, bitmap: Bitmap?) {
        this.color = color
        this.bitmap = bitmap
    }

    constructor(color: Int, bitmap: Bitmap?, text: String?) {
        this.color = color
        this.bitmap = bitmap
        this.text = text
    }

    constructor(color: Int, text: String?) {
        this.color = color
        this.text = text
    }
}