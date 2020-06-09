package com.example.perpustakaandigital.activity.pdf

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.PDF_URL
import com.example.perpustakaandigital.utils.FileUtils
import kotlinx.android.synthetic.main.activity_pdf.*
import java.io.File

class PdfActivity : AppCompatActivity() {

    private var fileNameIntent : String? = null
    private var passwordIntent : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        fileNameIntent = intent.getStringExtra("filename")
        passwordIntent = intent.getStringExtra("password")
        PRDownloader.initialize(applicationContext)
        pdfAction()
    }

    private fun showPdf(file : File){
        pdfView.fromFile(file)
            .password(passwordIntent)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@PdfActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    private fun downloadPdf(url: String, dirPath: String, fileName: String){
        PRDownloader.download(url,dirPath,fileName)
            .build()
            .start(object : OnDownloadListener{
                override fun onDownloadComplete() {
                    Toast.makeText(this@PdfActivity, "Download Selesai", Toast.LENGTH_LONG).show()
                    val downloadFile = File(dirPath,fileName)
                    progressBar.visibility = View.GONE
                    showPdf(downloadFile)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(this@PdfActivity, "Error in downloading file : $error",
                        Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    private fun pdfAction(){
        progressBar.visibility = View.VISIBLE
        val fileName = fileNameIntent
        if (fileName != null) {
            downloadPdf(
                PDF_URL + fileNameIntent,
                FileUtils.getRootDirPath(this),
                fileName
            )
        }
    }

}
