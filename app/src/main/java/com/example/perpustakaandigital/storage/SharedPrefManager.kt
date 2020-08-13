package com.example.perpustakaandigital.storage

import android.content.Context
import com.example.perpustakaandigital.model.Login


class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("id", "") != ""
        }

    val login : Login
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return Login(
                sharedPreferences.getString("id", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("nama", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("hakakses", null)
            )
        }


    fun saveUser(login: Login) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("id", login.id_anggota)
        editor.putString("email", login.email)
        editor.putString("nama", login.nama)
        editor.putString("password", login.password)
        editor.putString("hakakses", login.hak_akses)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        editor.commit()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}