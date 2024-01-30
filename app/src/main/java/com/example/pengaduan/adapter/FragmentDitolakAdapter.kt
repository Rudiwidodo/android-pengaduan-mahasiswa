package com.example.pengaduan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengaduan.R
import com.example.pengaduan.helper.PengaduanResult
import com.example.pengaduan.model.response.PengaduanResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class FragmentDitolakAdapter(
    private val pengaduan: List<PengaduanResponse.Pengaduan>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<FragmentDitolakAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FragmentDitolakAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_dashboard_mahasiswa_adapter_ditolak, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FragmentDitolakAdapter.ViewHolder, position: Int) {
        val result = PengaduanResult(
            id = pengaduan[position].id,
            nim = pengaduan[position].nim,
            isiPengaduan = pengaduan[position].isiPengaduan,
            foto = pengaduan[position].foto,
            isActive = pengaduan[position].isActive,
            isArchive = pengaduan[position].isArchive,
            createdAt = pengaduan[position].createdAt,
            updatedAt = pengaduan[position].updatedAt
        )

        Picasso.get().load("https://pengamas.my.id/uploads/${pengaduan[position].foto}").into(holder.imagePengaduan)
        holder.isiPengaduan.text = pengaduan[position].isiPengaduan

        holder.fabDetail.setOnClickListener {
            listener.onDetailPengaduan(result)
        }
    }

    override fun getItemCount(): Int {
        return pengaduan.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagePengaduan = view.findViewById<ImageView>(R.id.image_preview_adapter_ditolak)
        val isiPengaduan = view.findViewById<TextView>(R.id.text_adapter_ditolak)
        val fabDetail = view.findViewById<FloatingActionButton>(R.id.fab_adapter_detail_ditolak)
    }

    interface OnAdapterListener {
        fun onDetailPengaduan(pengaduan: PengaduanResult)
    }
}