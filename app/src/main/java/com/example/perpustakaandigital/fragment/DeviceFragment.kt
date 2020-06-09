package com.example.perpustakaandigital.fragment

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.adapter.DeviceAdapter
import com.example.perpustakaandigital.database.AppDatabase
import com.example.perpustakaandigital.database.SkripsiEntity
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.HomeResponse
import kotlinx.android.synthetic.main.fragment_device.*
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass.
 */
class DeviceFragment : Fragment() {

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoData: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var skripsiDatabase: AppDatabase

    private var adapter by Delegates.notNull<DeviceAdapter>()
    private var mListSkripsi = arrayListOf<SkripsiEntity>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_device)
        tvNoData = view.findViewById(R.id.tv_no_data)
        swipeRefresh = view.findViewById(R.id.swipe_refresh_device)
        swipeRefresh.isEnabled = false

        skripsiDatabase = AppDatabase.getInstance(view.context)

        adapter = DeviceAdapter(this.requireActivity())

        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = mLayoutManager

        recyclerView.adapter = adapter
    }

    private fun showDetailSkripsi(){
        mListSkripsi.clear()
        mListSkripsi.addAll(skripsiDatabase.skripsiDao().getAllSkripsi())
        adapter.notifyDataSetChanged()

        if (adapter.itemCount > 0){
            recyclerView.visibility = View.VISIBLE
            tvNoData.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            tvNoData.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        showDetailSkripsi()
    }


}