package com.example.pengaduan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.pengaduan.helper.PengaduanResultParcelable
import com.example.pengaduan.helper.PrefrencesHelper
import com.squareup.picasso.Picasso

class DetailTaskActivity : AppCompatActivity() {

    private var recivedData: PengaduanResultParcelable? = null
    private lateinit var detailImagePengaduan: ImageView
    private lateinit var detailIsiPengaduan: TextView
    private lateinit var detailStatusPengaduan: TextView
    private lateinit var detailDiubahPengaduan: TextView
    private lateinit var detailDiBuatPengaduan: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        val recivedBundle = intent.extras
        recivedData = recivedBundle?.getParcelable<PengaduanResultParcelable>("pengaduanResult")

        setUpView()
    }

    private fun setUpView(){
        detailImagePengaduan = findViewById(R.id.detail_image_preview)
        Picasso.get().load("https://pengamas.my.id/uploads/${recivedData?.foto}").into(detailImagePengaduan)
        detailIsiPengaduan = findViewById(R.id.detail_isi_pengaduan)
        detailIsiPengaduan.text = recivedData?.isiPengaduan
        detailStatusPengaduan = findViewById(R.id.detail_status_pengaduan)
        when(recivedData?.isActive){
            0 -> detailStatusPengaduan.text = "Menunggu"
            1 -> detailStatusPengaduan.text = "Diterima"
            2 -> detailStatusPengaduan.text = "Ditolak"
        }
        detailDiubahPengaduan = findViewById(R.id.detail_diubah_pengaduan)
        detailDiubahPengaduan.text = recivedData?.updatedAt
        detailDiBuatPengaduan = findViewById(R.id.detail_dibuat_pengaduan)
        detailDiBuatPengaduan.text = recivedData?.createdAt
    }
}