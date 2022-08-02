package com.infonuascape.osrshelper.controllers

import android.content.Context
import com.infonuascape.osrshelper.models.Account
import java.util.*

class AccountController(private val context: Context) {
    companion object {
        private const val TAG = "AccountController"
    }

    val currentProfile: Account? = null
    private val listeners = Collections.synchronizedList(mutableListOf<Listener>())

    fun addListener(listener: Listener) {
        listeners.remove(listener)
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    interface Listener {
        fun onCurrentProfileChanged(currentProfile: Account?)
    }
}