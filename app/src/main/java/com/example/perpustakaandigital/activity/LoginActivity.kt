package com.example.perpustakaandigital.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.model.LoginResponse
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.HomeDataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.presenter.LoginPresenter
import com.example.perpustakaandigital.repository.MahasiswaImplementation
import com.example.perpustakaandigital.view.LoginView
import com.example.perpustakaandigital.view.snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_login.*
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoginActivity : AppCompatActivity(), LoginView.View {

    private lateinit var presenter: LoginView.Presenter
    private var dataSource: HomeDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dataSource = NetworkProvider.getClient(this)
            ?.create(HomeDataSource::class.java)

        val repository = dataSource?.let { MahasiswaImplementation(it) }
        presenter = LoginPresenter(this,repository)

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            presenter.onLoginButtonClick(API_KEY,email,password)

        }
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


    override fun showProgressBar() {
        pb_login.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pb_login.visibility = View.GONE
    }

    override fun onSuccess(data:LoginResponse) {

        val intent = Intent(this,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        pb_login.visibility = View.GONE
        root_layout.snackbar(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}