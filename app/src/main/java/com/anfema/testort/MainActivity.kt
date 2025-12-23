package com.anfema.testort

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, "Test", LENGTH_LONG).show()
    }
}
