package com.example.perpustakaandigital.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
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
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.utils.FileUtils
import kotlinx.android.synthetic.main.activity_detail_home.*
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailHomeActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private lateinit var data : ArrayList<Data>
    private lateinit var skripsiDatabase: AppDatabase

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
        downloadState()


        if (savedInstanceState == null){
            showDetailHome()
        }

        swipe_refresh.isRefreshing = false

        btn_open.setOnClickListener {
            val intent = Intent(this, PdfActivity::class.java)
            intent.putExtra("filename",file_pdf)
            intent.putExtra("password",pass_pdf)
            startActivity(intent)
        }

        btn_borrow.setOnClickListener {
            pdfAction()
        }

    }

    private fun downloadPdf(url: String, dirPath: String, fileName: String){
        PRDownloader.download(url,dirPath,fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(this@DetailHomeActivity, "Download Selesai", Toast.LENGTH_LONG).show()
                    progressBar_Detail.visibility = View.GONE
                    btn_borrow.visibility = View.GONE
                    btn_open.visibility = View.VISIBLE
                }

                override fun onError(error: Error?) {
                    Toast.makeText(this@DetailHomeActivity, "Error in downloading file : $error",
                        Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    private fun pdfAction(){
        progressBar_Detail.visibility = View.VISIBLE
        val fileName = file_pdf
        if (fileName != null) {
            downloadPdf(
                PDF_URL + file_pdf,
                FileUtils.getRootDirPath(this),
                fileName
            )
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

        PRDownloader.initialize(applicationContext)

        data = intent.getParcelableArrayListExtra(MAHASISWA_EXTRA)
        position = intent.getIntExtra("position",-1)


        skripsiDatabase = AppDatabase.getInstance(this)
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

    private fun downloadState(){
        val file_name_state = data.get(position).file_pdf.toString()
        val dirPath = FileUtils.getRootDirPath(this)
        val downloadFile = File(dirPath,file_name_state)
        if (downloadFile.exists()){
            btn_borrow.visibility = View.GONE
            btn_open.visibility = View.VISIBLE
        } else {
            btn_borrow.visibility = View.VISIBLE
            btn_open.visibility = View.GONE
        }
    }

    private fun pinjamState(){
        val pinjam = id_skripsi?.let { skripsiDatabase.skripsiDao().getSkripsiById(it) }
        if (pinjam != null) {
            if (pinjam.isNotEmpty()) isBorrow = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }

}
