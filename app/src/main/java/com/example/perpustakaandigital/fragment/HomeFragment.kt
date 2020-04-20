package com.example.perpustakaandigital.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.activity.ConstantUtils.Companion.STATE_SAVED
import com.example.perpustakaandigital.adapter.HomeAdapter
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.HomeDataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.repository.MahasiswaImplementation
import com.example.perpustakaandigital.view.HomeView
import com.example.perpustakaandigital.viewmodel.HomeViewModel
import retrofit2.HttpException
import java.net.SocketTimeoutException
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), HomeView.View {

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var homeViewModel: HomeViewModel
    private var dataSource: HomeDataSource? = null
    private var adapter by Delegates.notNull<HomeAdapter>()
    private var page: Int = 1
    private var totalPage: Int? = null
    private var isLoading = false
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_home)
        swipeRefresh = view.findViewById(R.id.swipe_refresh)

        prepare(view)
        scrollListener()

        if (savedInstanceState == null){
            showListMahasiswa()
        }

        swipeRefresh.setOnRefreshListener {
            showListMahasiswa()
        }
    }

    override fun onResume() {
        super.onResume()
        showListMahasiswa()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_SAVED, "saved")
    }

    override fun getMahasiswaData(data: HomeResponse) {
        totalPage = data.totalPages
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

    private fun prepare(view: View){
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.getDataMahasiswa().observe(this, getData)

        adapter = HomeAdapter(this.requireActivity())
        adapter.notifyDataSetChanged()

        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager

        dataSource = NetworkProvider.getClient(view.context)
                ?.create(HomeDataSource::class.java)

        recyclerView.adapter = adapter

    }

    private val getData =
            Observer<ArrayList<Data>> { datamahasiswa->
                adapter.setData(datamahasiswa)
            }

    private fun showListMahasiswa(){
        val repository = dataSource?.let { MahasiswaImplementation(it) }
        if (repository != null){
            homeViewModel.setDataMahasiswa(API_KEY, view = this,page = page, mahasiswa = repository)
        }
    }

    private fun scrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page != totalPage) {
                    page = page.plus(1)
                    showListMahasiswa()
                }
            }
        })
    }

    private val getMahasiswa =
        Observer<ArrayList<Data>>{mahasiswaItems ->
            if(mahasiswaItems != null){
                if (page == 1){
                    adapter.setData(mahasiswaItems)
                } else{
                    adapter.refreshAdapter(mahasiswaItems)
                }
            }

        }

}