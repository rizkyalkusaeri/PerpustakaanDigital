package com.example.perpustakaandigital.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.activity.DetailHomeActivity
import com.example.perpustakaandigital.database.SkripsiEntity
import com.example.perpustakaandigital.utils.ConstantUtils
import kotlinx.android.synthetic.main.item_home.view.*
import kotlinx.android.synthetic.main.item_home.view.txt_file
import kotlinx.android.synthetic.main.item_home.view.txt_kk
import kotlinx.android.synthetic.main.item_home.view.txt_nim
import kotlinx.android.synthetic.main.item_home.view.txt_pass
import kotlinx.android.synthetic.main.item_home.view.txt_penulis
import kotlinx.android.synthetic.main.item_home.view.txt_title

class DeviceAdapter(private val context: Context) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private val skripsi = ArrayList<SkripsiEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return skripsi.size
    }

    override fun onBindViewHolder(holder: DeviceAdapter.DeviceViewHolder, position: Int) {
        holder.bind(skripsi[holder.adapterPosition])
    }

    inner class DeviceViewHolder(itemView : View): RecyclerView.ViewHolder(itemView),View.OnClickListener {

        fun bind(skripsi: SkripsiEntity){
            with(itemView){

                txt_title.text = skripsi.judul_skripsi

                txt_nim.text = skripsi.nim_penulis

                txt_penulis.text = skripsi.penulis

                txt_kk.text = skripsi.kelompok_keilmuan

                txt_file.text = skripsi.file_pdf

                txt_pass.text = skripsi.pass_pdf
            }
        }

        override fun onClick(v: View?) {
            val intent = Intent(context, DetailHomeActivity::class.java)
            intent.putParcelableArrayListExtra(ConstantUtils.MAHASISWA_EXTRA,skripsi)
            intent.putExtra("position",adapterPosition)
            context.startActivity(intent)
        } init {
            itemView.setOnClickListener(this)
        }

    }
}