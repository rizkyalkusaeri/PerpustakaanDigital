package com.example.perpustakaandigital.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.model.Login
import com.example.perpustakaandigital.model.Response
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.DataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.presenter.UbahPasswordPresenter
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.storage.SharedPrefManager
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.utils.snackbar
import com.example.perpustakaandigital.view.UbahPasswordView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_ubah.*
import retrofit2.HttpException
import java.net.SocketTimeoutException

class UbahActivity : AppCompatActivity(),UbahPasswordView.View {

    private lateinit var presenter: UbahPasswordView.Presenter
    private var dataSource : DataSource? = null

    private lateinit var login : Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubah)

        setSupportActionBar(toolbar_ubah)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        login = SharedPrefManager.getInstance(this).login

        dataSource = NetworkProvider.getClient(this)?.create(DataSource::class.java)

        val repository = dataSource?.let { PerpusImp(it) }

        presenter = UbahPasswordPresenter(this,repository)

        ubahButton.setOnClickListener {
            loadDataUbahPassword()
        }
    }

    private fun loadDataUbahPassword(){
        val idAnggota = login.id_anggota.toString().trim()
        val compare = login.password.toString().trim()
        val passwordLama = editTextPasswordLama.text.toString().trim()
        val passwordBaru = editTextPasswordBaru.text.toString().trim()



        when {
            passwordLama.isEmpty() -> {
                editTextPasswordLama.requestFocus()
                editTextPasswordLama.error = "Field Password Lama Tidak Boleh Kosong"
            }
            passwordBaru.isEmpty() -> {
                editTextPasswordBaru.requestFocus()
                editTextPasswordBaru.error = "Field Password Baru Tidak Boleh Kosong"
            }
            passwordLama == passwordBaru -> {
                editTextPasswordBaru.requestFocus()
                editTextPasswordBaru.error = "Password Tidak Boleh Sama"
            }
            passwordLama != compare -> {
                editTextPasswordLama.requestFocus()
                editTextPasswordLama.error = "Password lama anda salah"
            }
            else -> {
                presenter.onUbahButtonClick(API_KEY,idAnggota,passwordBaru)

            }
        }
    }

    override fun showProgressBar() {
        pb_ubah.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pb_ubah.visibility = View.GONE
    }

    override fun onSuccess(ubah: Response) {
        AlertDialog.Builder(this)
            .setTitle(ubah.message)
            .setMessage("Silahkan Untuk Melakukan Login Kembali")
            .setPositiveButton("Oke") { _, _ ->
                SharedPrefManager.getInstance(this).clear()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }.show()
    }

    override fun onFailure(message: String) {
        pb_ubah.visibility = View.GONE
        root_layout.snackbar(message)
    }


    override fun noInternetConnection(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun handleError(t: Throwable?) {
        if(ConnectivityStatus.isConnected(this)){
            when (t) {
                is HttpException -> // non 200 error codes
                    NetworkError.handleError(t, this)
                is SocketTimeoutException -> // connection errors
                    Toast.makeText(this, resources.getString(R.string.timeout), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
