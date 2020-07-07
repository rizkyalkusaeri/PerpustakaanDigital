package com.example.perpustakaandigital.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.utils.ConstantUtils.Companion.MAHASISWA_EXTRA
import com.example.perpustakaandigital.activity.DetailSkripsiActivity
import com.example.perpustakaandigital.model.Data
import kotlinx.android.synthetic.main.item_home.view.*

class SkripsiAdapter(private val context: Context) : RecyclerView.Adapter<SkripsiAdapter.SkripsiViewHolder>() {

    private val data = ArrayList<Data>()

    fun setData(items: ArrayList<Data>){
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun refreshAdapter(data : List<Data>){
        this.data.addAll(data)
        notifyItemRangeChanged(0,this.data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):SkripsiViewHolder{
        return SkripsiViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_home,parent,false)
        )
    }

    override fun getItemCount(): Int {

        return data.size

    }

    override fun onBindViewHolder(holder: SkripsiViewHolder, position: Int) {

        holder.bind(data[holder.adapterPosition])
    }

    inner class SkripsiViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(data: Data){
            with(itemView){

                txt_title.text = data.judul_skripsi

                txt_nim.text = data.nim_penulis

                txt_penulis.text = data.penulis

                txt_kk.text = data.kelompok_keilmuan

            }
        }

        override fun onClick(v: View?) {
            val intent = Intent(context, DetailSkripsiActivity::class.java)
            intent.putParcelableArrayListExtra(MAHASISWA_EXTRA,data)
            intent.putExtra("position",adapterPosition)
            context.startActivity(intent)
        }
        init {
            itemView.setOnClickListener(this)
        }
    }
}


