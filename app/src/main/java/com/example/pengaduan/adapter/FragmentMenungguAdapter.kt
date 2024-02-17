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

class FragmentMenungguAdapter(
    private val pengaduan: List<PengaduanResponse.Pengaduan>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<FragmentMenungguAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FragmentMenungguAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_dashboard_mahasiswa_adapter_menunggu, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FragmentMenungguAdapter.ViewHolder, position: Int) {
        Picasso.get().load("https://pengamas.my.id/uploads/${pengaduan[position].foto}").into(holder.imagePengaduan)
        holder.isiPengaduan.text = pengaduan[position].isiPengaduan

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

//        holder.fabEdit.setOnClickListener {
//            listener.onUpdatePengaduan(result)
//        }
//
//        holder.fabDelete.setOnClickListener {
//            listener.onDeletePengaduan(pengaduan[position].id!!.toInt())
//        }

        holder.fabDetail.setOnClickListener {
            listener.onDetailPengaduan(result)
        }
    }

    override fun getItemCount(): Int {
        return pengaduan.size
    }

//    menghubungkan antara fragment layout dengan adapter
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imagePengaduan = view.findViewById<ImageView>(R.id.image_preview_adapter_menunggu)
        val isiPengaduan = view.findViewById<TextView>(R.id.text_adapter_menunggu)
//        val fabEdit = view.findViewById<FloatingActionButton>(R.id.fab_adapter_edit)
//        val fabDelete = view.findViewById<FloatingActionButton>(R.id.fab_adapter_delete)
        val fabDetail = view.findViewById<FloatingActionButton>(R.id.fab_adapter_detail)
    }

    interface OnAdapterListener {
//        fun onUpdatePengaduan(pengaduan: PengaduanResult)
//        fun onDeletePengaduan(id: Int)

        fun onDetailPengaduan(pengaduan: PengaduanResult)
    }

}