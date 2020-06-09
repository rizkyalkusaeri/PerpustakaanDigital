package com.example.perpustakaandigital.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.perpustakaandigital.activity.LoginActivity
import com.example.perpustakaandigital.model.Login
import com.example.perpustakaandigital.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_account.*
import com.example.perpustakaandigital.R


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {

    private lateinit var login: Login

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login = SharedPrefManager.getInstance(view.context).login

        nama_profile.text = login.nama
        tv_email.text = login.email
        tv_hakakses.text = login.hak_akses

        btn_logout.setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        SharedPrefManager.getInstance(activity!!).clear()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
