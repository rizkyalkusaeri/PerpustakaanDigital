package com.example.perpustakaandigital.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.pdf.PdfActivity
import com.example.perpustakaandigital.database.AppDatabase
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.Login
import com.example.perpustakaandigital.model.PinjamResponse
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.DataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.presenter.UserPinjamPresenter
import com.example.perpustakaandigital.repository.MahasiswaImp
import com.example.perpustakaandigital.storage.SharedPrefManager
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.MAHASISWA_EXTRA
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.PDF_URL
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_FILE
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_JUDUL
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_KEILMUAN
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_NIM
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_PASS
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_PENULIS
import com.example.perpustakaandigital.utils.DateFormat
import com.example.perpustakaandigital.utils.FileUtils
import com.example.perpustakaandigital.utils.snackbar
import com.example.perpustakaandigital.view.UserPinjamView
import kotlinx.android.synthetic.main.activity_detail_home.*
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailSkripsiActivity : AppCompatActivity(), UserPinjamView.View {

    private lateinit var data : ArrayList<Data>
    private lateinit var presenter: UserPinjamView.Presenter
    private var dataSource : DataSource? = null

    private lateinit var login : Login

    var position: Int = 1


    var id_skripsi: String? = null
    var judul_skripsi: String? = null
    var nim_penulis: String?= null
    var penulis: String?= null
    var kelompok_keilmuan: String? = null
    var file_pdf: String? = null
    var pass_pdf: String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_home)

        init()
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
            val idAnggota = login.id_anggota.toString().trim()
            val idSkripsi = data[position].id.toString().trim()

            val calendar = Calendar.getInstance()
            val today = calendar.time

            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val tomorrow = calendar.time
            val date = SimpleDateFormat("yyyy-MM-dd")

            val tanggalPinjam = date.format(today)
            val tanggalPengembalian = date.format(tomorrow)

            presenter.onPinjamButtonClick(API_KEY,idSkripsi,idAnggota,tanggalPinjam,tanggalPengembalian)
        }

    }

    private fun downloadPdf(url: String, dirPath: String, fileName: String){
        PRDownloader.download(url,dirPath,fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    progressbar_download.visibility = View.GONE
                    Toast.makeText(this@DetailSkripsiActivity, "Download Selesai", Toast.LENGTH_LONG).show()
                    btn_borrow.visibility = View.GONE
                    btn_open.visibility = View.VISIBLE
                }

                override fun onError(error: Error?) {
                    Toast.makeText(this@DetailSkripsiActivity, "Error in downloading file : $error",
                        Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    private fun downloadThumbnail(url: String, dirPath: String, fileName: String){
        PRDownloader.download(url,dirPath,fileName)
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    progressbar_download.visibility = View.GONE
                    Toast.makeText(this@DetailSkripsiActivity, "Download Selesai", Toast.LENGTH_LONG).show()
                    btn_borrow.visibility = View.GONE
                    btn_open.visibility = View.VISIBLE
                }

                override fun onError(error: Error?) {
                    Toast.makeText(this@DetailSkripsiActivity, "Error in downloading file : $error",
                        Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    private fun pdfAction(){
        progressbar_download.visibility = View.VISIBLE
        val thumbnailName = "thumb.jpg"
        val fileName = file_pdf
        if (fileName != null) {
            downloadPdf(
                PDF_URL + file_pdf,
                FileUtils.getRootDirPath(this),
                fileName
            )

            downloadThumbnail(
                PDF_URL , // + id_anggota + thumbnail
                FileUtils.getRootDirPath(this),
                thumbnailName

            )

        }
    }


    fun showDetailHome() {

        id_skripsi = data[position].id.toString()
        judul_skripsi = data[position].judul_skripsi.toString()
        nim_penulis = data[position].nim_penulis.toString()
        penulis = data[position].penulis.toString()
        kelompok_keilmuan = data[position].kelompok_keilmuan.toString()
        file_pdf = data[position].file_pdf.toString()
        pass_pdf = data[position].pass_pdf.toString()

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

        login = SharedPrefManager.getInstance(this).login
        dataSource = NetworkProvider.getClient(this)?.create(DataSource::class.java)

        val repository = dataSource?.let { MahasiswaImp(it) }

        presenter = UserPinjamPresenter(this,repository)

        data = intent.getParcelableArrayListExtra(MAHASISWA_EXTRA)
        position = intent.getIntExtra("position",-1)


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


    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }

    //PEMINJAMAN STATE

    override fun showProgressBar() {
        progressbar_pinjam.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressbar_pinjam.visibility = View.GONE
    }

    override fun onSuccess(pinjam: PinjamResponse) {
        pdfAction()
    }

    override fun onPinjam(message: String) {
        progressbar_pinjam.visibility = View.GONE
        root_layout_detail.snackbar(message)
    }

    override fun onFailure(message: String) {
        progressbar_pinjam.visibility = View.GONE
        root_layout_detail.snackbar(message)
    }

    override fun noInternetConnection(message: String) {
        root_layout_detail.snackbar(message)
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

}
