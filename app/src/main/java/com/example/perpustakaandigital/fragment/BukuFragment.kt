@file:Suppress("DEPRECATION")

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
import com.example.perpustakaandigital.adapter.BukuAdapter
import com.example.perpustakaandigital.model.BukuResponse
import com.example.perpustakaandigital.model.DataBuku
import com.example.perpustakaandigital.network.ConnectivityStatus
import com.example.perpustakaandigital.network.DataSource
import com.example.perpustakaandigital.network.NetworkError
import com.example.perpustakaandigital.network.NetworkProvider
import com.example.perpustakaandigital.repository.MahasiswaImp
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.API_KEY
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.STATE_SAVED
import com.example.perpustakaandigital.view.BukuView
import com.example.perpustakaandigital.viewmodel.BukuViewModel
import retrofit2.HttpException
import java.net.SocketTimeoutException
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class BukuFragment : Fragment(), BukuView.View {

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var bukuViewModel: BukuViewModel

    private var dataSource: DataSource? = null
    private var adapter by Delegates.notNull<BukuAdapter>()

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
        return inflater.inflate(R.layout.fragment_buku, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepare(view)
        scrollListener()

        if (savedInstanceState == null){
            showListBuku()
        }

        swipeRefresh.setOnRefreshListener {
            showListBuku()
        }
    }

    override fun onResume() {
        super.onResume()
        showListBuku()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_SAVED,"saved")
    }
    override fun getBukuData(data: BukuResponse) {
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
        recyclerView = view.findViewById(R.id.rv_buku)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_buku)

        bukuViewModel = ViewModelProviders.of(this).get(BukuViewModel::class.java)
        bukuViewModel.getDataBuku().observe(viewLifecycleOwner, getDataBuku)

        adapter = BukuAdapter(this.requireActivity())
        adapter.notifyDataSetChanged()

        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager

        dataSource = NetworkProvider.getClient(view.context)
            ?.create(DataSource::class.java)

        recyclerView.adapter = adapter
    }

    private val getDataBuku = Observer<ArrayList<DataBuku>>{ dataBuku ->
        adapter.setDataBuku(dataBuku)
    }

    private fun showListBuku(){
        val repository = dataSource?.let { MahasiswaImp(it) }
        if (repository != null){
            bukuViewModel.setDataBuku(API_KEY, view = this ,page = page ,buku = repository)
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
                    showListBuku()
                }
            }
        })
    }

}
