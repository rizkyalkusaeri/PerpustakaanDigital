package com.example.perpustakaandigital.activity.pdf

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.adapter.ThumbsAdapter
import com.example.perpustakaandigital.utils.FileUtils
import com.example.perpustakaandigital.view.ThumbnailView
import com.github.barteksc.pdfviewer.util.FitPolicy
import kotlinx.android.synthetic.main.activity_pdf.*
import kotlinx.android.synthetic.main.activity_ubah.*
import java.io.File
import kotlin.properties.Delegates


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PdfActivity : AppCompatActivity(), ThumbnailView {

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private var adapter by Delegates.notNull<ThumbsAdapter>()

    private var fileNameIntent: String? = null
    private var passwordIntent: String? = null
    private var onClick : Boolean = true


    private var imageLists: ArrayList<File>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        prepare()
        setAdapter()

        pdfView.setOnClickListener {
            if (onClick) {
                recyclerView.visibility = View.VISIBLE
                onClick = false
            } else {
                recyclerView.visibility = View.GONE
                onClick = true
            }
        }



        val dirPath = FileUtils.getRootDirPath(this)
        val downloadFile = File(dirPath, fileNameIntent)

        showPdf(downloadFile)
    }

    private fun showPdf(file: File) {
        pdfView.fromFile(file)
            .password(passwordIntent)
            .defaultPage(0)
            .enableSwipe(true)
            .pageSnap(true)
            .pageFling(false)
            .autoSpacing(true)
            .enableAnnotationRendering(true)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .pageFitPolicy(FitPolicy.WIDTH)
            .onPageScroll { _, _ ->
                recyclerView.visibility = View.GONE
                onClick = true
            }
            .onPageChange { page, _ ->
                recyclerView.scrollToPosition(page)
            }
            .onPageError { page, _ ->
                Toast.makeText(
                    this@PdfActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    private fun setAdapter() {
        Log.d("MainActivity", imageLists.toString())
        adapter = ThumbsAdapter(getImageFromDevice(), getPageFromDevice(), this)
        adapter.notifyDataSetChanged()

        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = mLayoutManager

        recyclerView.adapter = adapter
    }

    private fun prepare() {
        setSupportActionBar(toolbar_pdf)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        fileNameIntent = intent.getStringExtra("filename")
        passwordIntent = intent.getStringExtra("password")

        recyclerView = findViewById(R.id.rv_thumbs)
        recyclerView.visibility = View.GONE
    }

    private fun getImageFromDevice(): ArrayList<File> {
        val tempImageList: ArrayList<File> = ArrayList()

        for (i in 1..6) {
            val file = File(FileUtils.getRootDirPath(this) + "/thumbs/$i.jpg")
            tempImageList.add(file)
        }

        return tempImageList
    }

    private fun getPageFromDevice(): ArrayList<String> {
        val tempPageList: ArrayList<String> = ArrayList()

        for (i in 1..6) {
            tempPageList.add(i.toString())
        }

        return tempPageList
    }

    override fun onClickListener(position: Int) {
        val dirPath = FileUtils.getRootDirPath(this)
        val downloadFile = File(dirPath, fileNameIntent)

        pdfView.fromFile(downloadFile)
            .password(passwordIntent)
            .defaultPage(position)
            .enableSwipe(true)
            .pageSnap(true)
            .pageFling(true)
            .autoSpacing(true)
            .enableAnnotationRendering(true)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .onPageScroll { _, _ ->
                recyclerView.visibility = View.GONE
                onClick = true
            }
            .onPageError { page, _ ->
                Toast.makeText(
                    this@PdfActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
