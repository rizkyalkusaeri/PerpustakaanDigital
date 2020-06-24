package com.example.perpustakaandigital.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaandigital.GlideApp
import com.example.perpustakaandigital.R
import com.example.perpustakaandigital.view.ThumbnailView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_thumbnail.*
import kotlinx.android.synthetic.main.item_thumbnail.view.*
import java.io.File


class ThumbsAdapter(
    private val thumbs: ArrayList<File>,
    private val page: ArrayList<String>,
    private val listener: ThumbnailView
): RecyclerView.Adapter<ThumbsAdapter.ThumbsViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThumbsViewHolder {
        return ThumbsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_thumbnail,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return thumbs.size
    }

    override fun onBindViewHolder(holder: ThumbsViewHolder, position: Int) {
        val context = holder.itemView.context
        val uri: Uri = Uri.fromFile(thumbs[position])
        holder.apply {
            GlideApp.with(context)
                .load(uri)
                .into(image_thumbs)

            containerView.page_number.text = page[position]
            containerView.setOnClickListener {
                listener.onClickListener(position)
            }
        }
    }

    inner class ThumbsViewHolder(override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer

}