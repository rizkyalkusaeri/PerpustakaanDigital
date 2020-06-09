@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.perpustakaandigital.activity

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.MAHASISWA_EXTRA
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.PDF_URL
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_FILE
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_JUDUL
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_KEILMUAN
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_NIM
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_PASS
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_PENULIS
import com.example.perpustakaandigital.activity.pdf.PdfActivity
import com.example.perpustakaandigital.database.AppDatabase
import com.example.perpustakaandigital.database.SkripsiEntity
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.presenter.DevicePresenter
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_ID
import com.example.perpustakaandigital.view.DeviceView
import kotlinx.android.synthetic.main.activity_detail_home.*

@Suppress("DEPRECATION")
class DetailHomeActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private lateinit var data : ArrayList<Data>
    private lateinit var devicePresenter: DeviceView.DevicePresenter
    private lateinit var skripsiDatabase: AppDatabase

    private val skripsiEntity = SkripsiEntity()
    private var isBorrow = false

    var position: Int = 1

    private var menuItem: Menu? = null

    var id_skripsi: String? = null
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
        pinjamState()


        if (savedInstanceState == null){
            showDetailHome()
        }

        swipe_refresh.setOnRefreshListener {
            showDetailHome()
        }

        btn_open.setOnClickListener {
            val intent = Intent(this, PdfActivity::class.java)
            intent.putExtra("filename",file_pdf)
            intent.putExtra("password",pass_pdf)
            startActivity(intent)
        }

//        btn_borrow.setOnClickListener {
//
//            if (isBorrow)
//            {
//                removeFromDevice()
//            } else {
//                addToDevice()
//                isBorrow = !isBorrow
//                setPinjam()
//            }
//        }

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
        id_skripsi = data.get(position).id.toString()
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

        id_skripsi = savedInstanceState.getString(SAVE_ID)
        judul_skripsi = savedInstanceState.getString(SAVE_JUDUL)
        nim_penulis = savedInstanceState.getString(SAVE_NIM)
        penulis = savedInstanceState.getString(SAVE_PENULIS)
        kelompok_keilmuan = savedInstanceState.getString(SAVE_KEILMUAN)
        file_pdf = savedInstanceState.getString(SAVE_FILE)
        pass_pdf = savedInstanceState.getString(SAVE_PASS)

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

        data = intent.getParcelableArrayListExtra(MAHASISWA_EXTRA)
        position = intent.getIntExtra("position",-1)


        skripsiDatabase = AppDatabase.getInstance(this)
        devicePresenter = DevicePresenter(skripsiDatabase.skripsiDao())
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
    }

    private fun addToDevice(){
        skripsiEntity.skripsiId = data.get(position).id.toString()
        skripsiEntity.nim_penulis = data.get(position).nim_penulis.toString()
        skripsiEntity.penulis = data.get(position).penulis.toString()
        skripsiEntity.judul_skripsi = data.get(position).judul_skripsi.toString()
        skripsiEntity.kelompok_keilmuan = data.get(position).kelompok_keilmuan.toString()
        skripsiEntity.file_pdf = data.get(position).file_pdf.toString()
        skripsiEntity.pass_pdf = data.get(position).pass_pdf.toString()

        devicePresenter.insertSkripsi(skripsiEntity)
        Toast.makeText(this, "Berhasil Meminjam Skripsi", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromDevice(){
        id_skripsi?.let { devicePresenter.deleteSkripsi(it) }
        Toast.makeText(this, "Berhasil Mengembalikkan Skripsi", Toast.LENGTH_SHORT).show()
    }

    private fun setPinjam(){
        if (isBorrow)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_done_black_24dp)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_file_download_black_24dp)

    }

    private fun pinjamState(){
        val pinjam = id_skripsi?.let { skripsiDatabase.skripsiDao().getSkripsiById(it) }
        if (pinjam != null) {
            if (pinjam.isNotEmpty()) isBorrow = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_detail,menu)
        menuItem = menu
        setPinjam()
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.add_to_device ->{
                if (isBorrow)
                    removeFromDevice()
                else
                    addToDevice()

                    isBorrow = !isBorrow
                    setPinjam()
                    true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
        devicePresenter.onDestroy()
    }

}
