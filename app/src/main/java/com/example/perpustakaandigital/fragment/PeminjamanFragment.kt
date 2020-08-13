@file:Suppress("DEPRECATION")

package com.example.perpustakaandigital.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.perpustakaandigital.adapter.PeminjamanAdapter
import com.example.perpustakaandigital.model.Login
import com.example.perpustakaandigital.model.Peminjaman
import com.example.perpustakaandigital.model.PeminjamanResponse
import com.example.perpustakaandigital.model.Response
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.DataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.presenter.UserReturnPresenter
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.storage.SharedPrefManager
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.STATE_SAVED
import com.example.perpustakaandigital.utils.FileUtils
import com.example.perpustakaandigital.view.OnReturnClickListener
import com.example.perpustakaandigital.view.PeminjamanView
import com.example.perpustakaandigital.view.UserReturnView
import com.example.perpustakaandigital.viewmodel.PeminjamanViewModel
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.LoginActivity
import com.example.perpustakaandigital.activity.PdfActivity
import kotlinx.android.synthetic.main.fragment_peminjaman.*
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PeminjamanFragment : Fragment(), PeminjamanView.View, UserReturnView.View, OnReturnClickListener {

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var peminjamanViewModel: PeminjamanViewModel
    private lateinit var presenterReturn: UserReturnView.Presenter
    private lateinit var login: Login

    private var dataSource: DataSource? = null
    private var adapter by Delegates.notNull<PeminjamanAdapter>()

    private var page : Int = 1



    private var totalPage: Int? = null
    private var isLoading = false
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peminjaman, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepare(view)
        scrollListener()

        if (savedInstanceState == null){
            showListPeminjaman()
        }

        swipeRefresh.setOnRefreshListener {
            showListPeminjaman()
        }

    }

    override fun onResume() {
        super.onResume()
        showListPeminjaman()
    }


    private fun prepare(view: View){
        recyclerView = view.findViewById(R.id.rv_pinjam)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_peminjaman)

        peminjamanViewModel = ViewModelProviders.of(this).get(PeminjamanViewModel::class.java)
        peminjamanViewModel.getDataPeminjaman().observe(viewLifecycleOwner, getDataPeminjaman)

        dataSource = NetworkProvider.getClient(view.context)
            ?.create(DataSource::class.java)

        val repository = dataSource?.let { PerpusImp(it) }

        presenterReturn = UserReturnPresenter(this,repository)

        login = context?.let { SharedPrefManager.getInstance(it).login }!!

        adapter = PeminjamanAdapter(this.requireActivity(),this)
        adapter.notifyDataSetChanged()

        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager

        recyclerView.adapter = adapter
    }

    private val getDataPeminjaman = Observer<ArrayList<Peminjaman>>{ peminjaman ->
        adapter.setDataPeminjaman(peminjaman)
        if(adapter.itemCount > 0){
            rv_pinjam.visibility = View.VISIBLE
            tv_no_data.visibility = View.GONE
        } else{
            rv_pinjam.visibility = View.GONE
            tv_no_data.visibility = View.VISIBLE
        }

    }

    private fun showListPeminjaman(){
        val idAnggota = login.id_anggota.toString().trim()
        val repository = dataSource?.let { PerpusImp(it) }
        val stat = "1"
        if (repository != null){
            peminjamanViewModel.setDataPeminjaman(
                API_KEY, view = this ,stat = stat,page = page ,perpus = repository,idAnggota = idAnggota)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_SAVED,"saved")
    }

    override fun getPeminjamanData(peminjaman: PeminjamanResponse) {
        totalPage = peminjaman.totalPages
    }

    override fun noInternetConnection(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        isLoading = true
        swipeRefresh.isRefreshing = true

    }

    override fun hideProgressBar() {
        isLoading = false
        swipeRefresh.isRefreshing = false
    }

    override fun onFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun handleError(t: Throwable?) {
        if(ConnectivityStatus.isConnected(context)){
            when (t) {
                is HttpException -> // non 200 error codes
                    NetworkError.handleError(t, context)
                is SocketTimeoutException -> // connection errors
                    Toast.makeText(context, resources.getString(R.string.timeout), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    //Return View

    override fun showProgressBarReturn() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgressBarReturn() {
        swipeRefresh.isRefreshing = false
    }

    override fun onSuccessReturn(returnResponse: Response) {
       Toast.makeText(context, returnResponse.message, Toast.LENGTH_LONG).show()
    }

    override fun onFailureReturn(message: String) {
        swipeRefresh.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun noInternetConnectionReturn(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun handleErrorReturn(t: Throwable?) {
        if(ConnectivityStatus.isConnected(context)){
            when (t) {
                is HttpException -> // non 200 error codes
                    NetworkError.handleError(t, context)
                is SocketTimeoutException -> // connection errors
                    Toast.makeText(context, resources.getString(R.string.timeout), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }


    private fun scrollListener(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page != totalPage) {
                    page = page.plus(1)
                    showListPeminjaman()
                }
            }
        })
    }

    private fun prepareAfterReturn(){
        peminjamanViewModel.getDataPeminjaman().observe(viewLifecycleOwner, getDataPeminjaman)
        adapter = PeminjamanAdapter(this.requireActivity(),this)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
    }

    override fun onReturnClickListener(idPeminjaman: Int, fileName: String, directoryName:String) {

        val dirPath = activity?.let { FileUtils.getRootDirPath(it) }
        val returnFile = File(dirPath, fileName)
        val dirThumbnail = File(context?.let { FileUtils.getRootDirPath(it) } + File.separator + directoryName)

        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Return")
                .setMessage("Yakin untuk mengembalikkan Skripsi ?")
                .setPositiveButton("Oke") { _, _ ->
                    presenterReturn.onReturnButtonClick(API_KEY,idPeminjaman)

                    if (returnFile.exists()){
                        returnFile.delete()
                        if (dirThumbnail.isDirectory) {
                            val children: Array<String> = dirThumbnail.list()
                            for (i in children.indices) {
                                File(dirThumbnail, children[i]).delete()
                            }
                        }

                        Toast.makeText(context, "Skripsi telah dikembalikan", Toast.LENGTH_LONG).show()
                        prepareAfterReturn()
                        showListPeminjaman()
                    } else{
                        Toast.makeText(context, "Maaf File Skripsi Tidak Ditemukkan", Toast.LENGTH_LONG).show()
                    }


                }.setNegativeButton("tidak"){dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }

    }

    override fun onOpenClickListener(fileName: String,password: String, halaman: String, directoryName: String) {
        val intent = Intent(activity, PdfActivity::class.java)
            intent.putExtra("filename",fileName)
            intent.putExtra("password",password)
            intent.putExtra("halaman",halaman)
            intent.putExtra("dirName",directoryName)
        startActivity(intent)
    }



}
