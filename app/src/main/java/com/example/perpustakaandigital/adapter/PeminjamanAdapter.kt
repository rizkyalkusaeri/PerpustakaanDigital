package com.example.perpustakaandigital.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.DetailSkripsiActivity
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.Peminjaman
import com.example.perpustakaandigital.utils.ConstantUtils
import com.example.perpustakaandigital.view.OnReturnClickListener
import com.example.perpustakaandigital.view.ThumbnailView
import kotlinx.android.synthetic.main.item_pinjam.view.*
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class PeminjamanAdapter(private val context: Context,private val listener : OnReturnClickListener): RecyclerView.Adapter<PeminjamanAdapter.PeminjamanViewHolder>() {

    private val peminjaman = ArrayList<Peminjaman>()
    private val data = ArrayList<Data>()

    fun setDataPeminjaman(items: ArrayList<Peminjaman>){
        peminjaman.clear()
        peminjaman.addAll(items)
        notifyDataSetChanged()
    }

    fun refreshAdapter(peminjaman : List<Peminjaman>){
        this.peminjaman.addAll(peminjaman)
        notifyItemRangeChanged(0,this.peminjaman.size)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeminjamanViewHolder {
        return PeminjamanViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pinjam,parent,false)
        )
    }

    override fun getItemCount(): Int {
       return peminjaman.size
    }

    override fun onBindViewHolder(holder: PeminjamanViewHolder, position: Int) {
        holder.bind(peminjaman[holder.adapterPosition])
    }

    inner class PeminjamanViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(peminjaman: Peminjaman){
            with(itemView){

                txt_title_pinjam.text = peminjaman.judul_skripsi
                txt_title_pinjam.isSelected = true
                txt_tgl_pinjam.text = peminjaman.tanggal_pinjam
                txt_tgl_kembali.text = peminjaman.tanggal_pengembalian

                val idPeminjaman = peminjaman.id_peminjaman!!.toInt()
                val fileName = peminjaman.file_pdf.toString()
                val password = peminjaman.pass_pdf.toString()
                val halaman = peminjaman.halaman.toString()
                val directoryName = MD5Hash(peminjaman.id_penulis.toString()+"digitallibraryifunikom")

                btn_return.setOnClickListener {
                    if (directoryName != null) {
                        listener.onReturnClickListener(idPeminjaman,fileName,directoryName)
                    }
                }

                btn_open.setOnClickListener {
                    if (directoryName != null) {
                        listener.onOpenClickListener(fileName,password, halaman, directoryName)
                    }
                }

            }
        }

    }

    fun MD5Hash(s: String): String? {
        var m: MessageDigest? = null
        try {
            m = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        m?.update(s.toByteArray(), 0, s.length)
        return BigInteger(1, m?.digest()).toString(16)
    }
}