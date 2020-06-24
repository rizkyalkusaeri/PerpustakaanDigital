package com.example.perpustakaandigital.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.model.DataBuku
import kotlinx.android.synthetic.main.item_buku.view.*

class BukuAdapter(private val context: Context) : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    private val dataBuku = ArrayList<DataBuku>()

    fun setDataBuku(items : ArrayList<DataBuku>){
        dataBuku.clear()
        dataBuku.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        return BukuViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_buku,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return dataBuku.size
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        holder.bind(dataBuku[holder.adapterPosition])
    }

    inner class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataBuku: DataBuku){
            with(itemView){
                val penerbit = "Penerbit : "
                val jumlahBuku = "Jumlah Buku : "
                val edisi = "Edisi Ke "

                txt_judul_buku.text = dataBuku.judul

                txt_penulis.text = dataBuku.penulis

                txt_penerbit.text = penerbit + dataBuku.penerbit

                txt_edisi.text = edisi + dataBuku.edisi

                txt_jumlah_buku.text = jumlahBuku + dataBuku.jumlah_buku
            }
        }
    }
}