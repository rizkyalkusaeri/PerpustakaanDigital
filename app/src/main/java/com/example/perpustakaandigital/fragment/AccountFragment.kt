package com.example.perpustakaandigital.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.LoginActivity
import com.example.perpustakaandigital.activity.UbahActivity
import com.example.perpustakaandigital.model.Login
import com.example.perpustakaandigital.storage.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_account.*


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

        tv_email_profil.text = login.email
        tv_nama_profil.text = login.nama

        tv_ubah_password.setOnClickListener {
            val intent = Intent(activity, UbahActivity::class.java)
            startActivity(intent)
        }

        btn_logout.setOnClickListener {
            logout()
        }
    }

    private fun logout(){

        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Peringatan!")
                .setMessage("Silahkan Untuk Melakukan Login Kembali")
                .setPositiveButton("Oke") { _, _ ->
                    SharedPrefManager.getInstance(activity!!).clear()
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Batalkan") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }
    }

}
