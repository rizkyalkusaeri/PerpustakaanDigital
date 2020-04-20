@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.perpustakaandigital.activity

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.BASE_URL
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.MAHASISWA_EXTRA
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.PDF_URL
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.SAVE_FILE
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.SAVE_JUDUL
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.SAVE_KEILMUAN
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.SAVE_NIM
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.SAVE_PASS
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.SAVE_PENULIS
import com.example.perpustakaandigital.model.Data
import kotlinx.android.synthetic.main.activity_detail_home.*
import java.io.FileOutputStream

class DetailHomeActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private lateinit var data : ArrayList<Data>
    var position: Int = 1

    var judul_skripsi: String? = null
    var nim_penulis: String?= null
    var penulis: String?= null
    var kelompok_keilmuan: String? = null
    var file_pdf: String? = null
    var pass_pdf: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_home)

        init()

        data = intent.getParcelableArrayListExtra(MAHASISWA_EXTRA)
        position = intent.getIntExtra("position",-1)

        if (savedInstanceState == null){
            showDetailHome()
        }

        swipe_refresh.setOnRefreshListener {
            showDetailHome()
        }

        btnDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    //permission denied

                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    startDownloading()
                }
            }
            else {
                //system os is less than
                startDownloading()
            }
        }

    }

    private fun startDownloading() {
        val request = DownloadManager.Request(Uri.parse(PDF_URL + file_pdf))

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(file_pdf)
        request.setDescription("The File is Downloading")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    startDownloading()
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun showDetailHome() {
        judul_skripsi = data.get(position).judul_skripsi.toString()
        nim_penulis = data.get(position).nim_penulis.toString()
        penulis = data.get(position).penulis.toString()
        kelompok_keilmuan = data.get(position).kelompok_keilmuan.toString()
        file_pdf = data.get(position).file_pdf.toString()
        pass_pdf = data.get(position).pass_pdf.toString()

        getDetailMahasiswa()
    }



    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        layout_data.visibility = View.VISIBLE

        judul_skripsi = savedInstanceState?.getString(SAVE_JUDUL)
        nim_penulis = savedInstanceState?.getString(SAVE_NIM)
        penulis = savedInstanceState?.getString(SAVE_PENULIS)
        kelompok_keilmuan = savedInstanceState?.getString(SAVE_KEILMUAN)
        file_pdf = savedInstanceState?.getString(SAVE_FILE)
        pass_pdf = savedInstanceState?.getString(SAVE_PASS)

        getDetailMahasiswa()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SAVE_JUDUL,judul_skripsi)
        outState.putString(SAVE_NIM,nim_penulis)
        outState.putString(SAVE_PENULIS,penulis)
        outState.putString(SAVE_KEILMUAN,kelompok_keilmuan)
        outState.putString(SAVE_FILE,file_pdf)
        outState.putString(SAVE_PASS,pass_pdf)
    }

    private fun init(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getDetailMahasiswa(){
        tvDetailJudul.text = judul_skripsi
        tvDetailNim.text = nim_penulis
        tvDetailPenulis.text = penulis
        tvDetailKk.text = kelompok_keilmuan
        tvDetailFile.text = file_pdf
        tvDetailPass.text = pass_pdf
    }

}
