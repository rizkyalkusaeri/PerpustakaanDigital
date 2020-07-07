package com.example.perpustakaandigital.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.database.AppDatabase
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.Login
import com.example.perpustakaandigital.model.Response
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.DataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.presenter.UserPinjamPresenter
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.storage.SharedPrefManager
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.MAHASISWA_EXTRA
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.PDF_URL
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_FILE
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_HALAMAN
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_JUDUL
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_KEILMUAN
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_PASS
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.SAVE_PENULIS
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
    private lateinit var presenterPinjam: UserPinjamView.Presenter
    private var dataSource : DataSource? = null

    private lateinit var login : Login

    var position: Int = 1


    var id_skripsi: String? = null
    var id_penulis: String? = null
    var judul_skripsi: String? = null
    var halaman: String?= null
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
            intent.putExtra("halaman",halaman)
            intent.putExtra("dirName",id_penulis)
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

            presenterPinjam.onPinjamButtonClick(API_KEY,idSkripsi,idAnggota,tanggalPinjam,tanggalPengembalian)
        }

    }

    private fun downloadPdf(url: String, dirPath: String, fileName: String){
        PRDownloader.download(url,dirPath,fileName)
            .build()
            .setOnProgressListener {
                val progressPercent : Long = it.currentBytes * 100 / it.totalBytes
                progressbar_download.visibility = View.VISIBLE
                progressbar_download.progress = progressPercent.toInt()
                txtprogressdownload.visibility = View.VISIBLE
                btn_borrow.isEnabled = false
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    btn_borrow.isEnabled = false
                    txtprogressdownload.visibility = View.GONE
                }


                override fun onError(error: Error?) {
                    Toast.makeText(this@DetailSkripsiActivity, "Error in downloading file : $error",
                        Toast.LENGTH_LONG)
                        .show()

                    progressbar_download.progress = 0
                }

            })
    }

    private fun downloadThumbnail(url: String, dirPath: String, fileName: String){
        PRDownloader.download(url,dirPath,fileName)
            .build()
            .setOnProgressListener {
                btn_borrow.isEnabled = false
                val progressPercent : Long = it.currentBytes * 100 / it.totalBytes
                progressbar_download.visibility = View.VISIBLE
                progressbar_download.progress = progressPercent.toInt()
                txtprogressdownload.visibility = View.VISIBLE
                txtprogressdownload.text = getProgressDisplayLine(it.currentBytes, it.totalBytes)
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    txtprogressdownload.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(this@DetailSkripsiActivity, "Error in downloading file : $error",
                        Toast.LENGTH_LONG)
                        .show()
                    progressbar_download.progress = 0
                }

            })
    }

    private fun downloadComplete(){
        root_layout_detail.snackbar("Download telah selesai")
        progressbar_download.visibility = View.GONE
        txtprogressdownload.visibility = View.GONE
        btn_borrow.visibility = View.GONE
        btn_open.visibility = View.VISIBLE
    }

    private fun thumbnailDownload(){
        val thumbnailExt = ".jpg"
        val thumbnail = "/thumbnail/"
        val totalHalaman = data[position].halaman!!.toInt()
        val directoryName = data[position].id_penulis.toString()

        for(i in 1..totalHalaman){
            downloadThumbnail(
                PDF_URL+ directoryName + thumbnail + i + thumbnailExt,
                FileUtils.getRootDirPath(this) + File.separator + directoryName,
                i.toString() + thumbnailExt
            )
        }

        downloadComplete()

    }

    private fun pdfAction(){
        val pdf = "/pdf/"
        val fileName = file_pdf
        val directoryName = data[position].id_penulis.toString()
        val dirThumbnail = File(FileUtils.getRootDirPath(this) + File.separator + directoryName)

        if (fileName != null) {
            downloadPdf(
                PDF_URL + directoryName + pdf + file_pdf,
                FileUtils.getRootDirPath(this),
                fileName
            )
        }

        if(!dirThumbnail.exists()){
            dirThumbnail.mkdirs()
            thumbnailDownload()
        } else {
            thumbnailDownload()
        }

    }


    fun showDetailHome() {

        id_skripsi = data[position].id.toString()
        judul_skripsi = data[position].judul_skripsi.toString()
        halaman = data[position].halaman.toString()
        penulis = data[position].penulis.toString()
        id_penulis = data[position].id_penulis.toString()
        kelompok_keilmuan = data[position].kelompok_keilmuan.toString()
        file_pdf = data[position].file_pdf.toString()
        pass_pdf = data[position].pass_pdf.toString()

        getDetailMahasiswa()
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        judul_skripsi = savedInstanceState.getString(SAVE_JUDUL)
        halaman = savedInstanceState.getString(SAVE_HALAMAN)
        penulis = savedInstanceState.getString(SAVE_PENULIS)
        kelompok_keilmuan = savedInstanceState.getString(SAVE_KEILMUAN)
        file_pdf = savedInstanceState.getString(SAVE_FILE)
        pass_pdf = savedInstanceState.getString(SAVE_PASS)

        getDetailMahasiswa()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SAVE_JUDUL,judul_skripsi)
        outState.putString(SAVE_HALAMAN,halaman)
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

        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)

        login = SharedPrefManager.getInstance(this).login
        dataSource = NetworkProvider.getClient(this)?.create(DataSource::class.java)

        val repository = dataSource?.let { PerpusImp(it) }

        presenterPinjam = UserPinjamPresenter(this,repository)

        data = intent.getParcelableArrayListExtra(MAHASISWA_EXTRA)
        position = intent.getIntExtra("position",-1)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getDetailMahasiswa(){
        tvDetailJudul.text = judul_skripsi
        tvDetailHal.text = halaman
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

    override fun onSuccessPinjam(pinjam: Response) {
        Toast.makeText(this, pinjam.message, Toast.LENGTH_LONG).show()
        pdfAction()
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

    fun getProgressDisplayLine(
        currentBytes: Long,
        totalBytes: Long
    ): String? {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
    }

    private fun getBytesToMBString(bytes: Long): String {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }

}
