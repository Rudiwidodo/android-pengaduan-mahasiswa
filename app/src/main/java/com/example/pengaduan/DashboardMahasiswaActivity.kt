package com.example.pengaduan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.example.pengaduan.adapter.ViewPagerAdapter
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.response.GetProfileMahasiswaResponse
import com.example.pengaduan.model.response.LogoutResponse
import com.example.pengaduan.model.response.PengaduanResponse
import com.example.pengaduan.services.GetProfileMahaisswaApi
import com.example.pengaduan.services.LogoutApi
import com.example.pengaduan.services.PengaduanDiterimaApi
import com.example.pengaduan.services.PengaduanDitolakApi
import com.example.pengaduan.services.PengaduanMenungguApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardMahasiswaActivity : AppCompatActivity() {

    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var profile : ImageView
    private lateinit var nim: TextView
    private lateinit var nama: TextView
    private lateinit var menungguCount: TextView
    private lateinit var diterimaCount: TextView
    private lateinit var ditolakCount: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnCreateTask: Button
    private lateinit var sessionToken: String
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var cardProgressBar: CardView
    private lateinit var cardProfile: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_mahasiswa)

        sharedPref = PrefrencesHelper(this)

        setUpView()
        setUpListener()
    }

//    menentukan variable berdasarkan id di layoutnya
    private fun setUpView(){
        profile         = findViewById(R.id.image_profile_dashboard)
        nim             = findViewById(R.id.text_dashboard_nim)
        nama            = findViewById(R.id.text_dashboard_nama)
        menungguCount   = findViewById(R.id.text_menunggu_count)
        diterimaCount   = findViewById(R.id.text_diterima_count)
        ditolakCount    = findViewById(R.id.text_ditolak_count)
        btnLogout       = findViewById(R.id.button_logout)
        btnCreateTask   = findViewById(R.id.button_create_task)
        tabLayout       = findViewById(R.id.tab_layout)
        viewPager2      = findViewById(R.id.view_pager)
        cardProgressBar = findViewById(R.id.dashboard_card_pregress_bar)
        cardProfile     = findViewById(R.id.card_profile)
    }

//    set up untuk menampilakan fragment tab berdasarkan posisinya
    private fun setUpAdapter(){
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = viewPagerAdapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

//        mengatur tampilan tab yang akan ditampilkan berdasarkan posisinya
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }


//    lifecycle pada android
    override fun onStart() {
        super.onStart()
        sessionToken = sharedPref.getString(Constant.PREF_IS_TOKEN).toString()
        setUpAdapter()
        getProfileMahasiswa()
        getPengaduanMenunggu()
        getPengaduanDiterima()
        getPengaduanDitolak()
    }

//    menutup aplikasi ketika tombol back di klik 2x
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

//    get profile mahasiswa
    private fun getProfileMahasiswa(){
    cardProgressBar.visibility = View.VISIBLE
    val retro = Retro().getClientInstance().create(GetProfileMahaisswaApi::class.java)
    retro.getProfileMahasiswa(
        token = sessionToken
    ).enqueue(object: Callback<GetProfileMahasiswaResponse>{
        override fun onResponse(
            call: Call<GetProfileMahasiswaResponse>,
            response: Response<GetProfileMahasiswaResponse>
        ) {
            val res = response.body()
            if (response.isSuccessful){
                cardProgressBar.visibility = View.INVISIBLE

                nim.text = res?.result?.nim
                nama.text = res?.result?.nama
                Picasso.get().load("https://pengamas.my.id/assets/img/${res?.result?.sampul}").into(profile)
            } else {
                if (response.code() == 401){
                    sharedPref.put(Constant.PREF_IS_LOGIN, false)
                    sharedPref.put(Constant.PREF_IS_TOKEN, "")

                    cardProgressBar.visibility = View.INVISIBLE

                    Toast.makeText(this@DashboardMahasiswaActivity, "invalid token", Toast.LENGTH_SHORT).show()
                    Intent(this@DashboardMahasiswaActivity, LoginActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
        }

        override fun onFailure(call: Call<GetProfileMahasiswaResponse>, t: Throwable) {
            Toast.makeText(this@DashboardMahasiswaActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
        }
    })
}

    //  pengaduan menunggu
    private fun getPengaduanMenunggu(){
        val retro = Retro().getClientInstance().create(PengaduanMenungguApi::class.java)
        retro.getPengaduanMenunggu(
            token = sessionToken
        ).enqueue(object: Callback<PengaduanResponse> {
            override fun onResponse(
                call: Call<PengaduanResponse>,
                response: Response<PengaduanResponse>
            ) {
                val res = response.body()
                if(response.isSuccessful){
                    menungguCount.text = res?.result?.size.toString()
                }
            }

            override fun onFailure(call: Call<PengaduanResponse>, t: Throwable) {
                Toast.makeText(this@DashboardMahasiswaActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //  pengaduan Diterima
    private fun getPengaduanDiterima(){
        val retro = Retro().getClientInstance().create(PengaduanDiterimaApi::class.java)
        retro.getPengaduanDiterima(
            token = sessionToken
        ).enqueue(object: Callback<PengaduanResponse> {
            override fun onResponse(
                call: Call<PengaduanResponse>,
                response: Response<PengaduanResponse>
            ) {
                val res = response.body()
                if (response.isSuccessful){
                    diterimaCount.text = res?.result?.size.toString()
                }
            }

            override fun onFailure(call: Call<PengaduanResponse>, t: Throwable) {
                Toast.makeText(this@DashboardMahasiswaActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //  pengaduan Ditolak
    private fun getPengaduanDitolak(){
        val retro = Retro().getClientInstance().create(PengaduanDitolakApi::class.java)
        retro.getPengaduanDitolak(
            token = sessionToken
        ).enqueue(object: Callback<PengaduanResponse> {
            override fun onResponse(
                call: Call<PengaduanResponse>,
                response: Response<PengaduanResponse>
            ) {
                val res = response.body()
                if (response.isSuccessful){
                    ditolakCount.text = res?.result?.size.toString()
                }
            }

            override fun onFailure(call: Call<PengaduanResponse>, t: Throwable) {
                Toast.makeText(this@DashboardMahasiswaActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    //  initial set up action listener
    private fun setUpListener(){
//      button logout action
        btnLogout.setOnClickListener(){
            cardProgressBar.visibility = View.VISIBLE

            val retro = Retro().getClientInstance().create(LogoutApi::class.java)
            retro.logout(
                token = sessionToken
            ).enqueue(object: Callback<LogoutResponse> {
                override fun onResponse(
                    call: Call<LogoutResponse>,
                    response: Response<LogoutResponse>
                ) {
                    cardProgressBar.visibility = View.INVISIBLE

                    if(response.isSuccessful){
                        sharedPref.put(Constant.PREF_IS_LOGIN, false)
                        sharedPref.put(Constant.PREF_IS_TOKEN, "")

                        val res = response.body()
                        Toast.makeText(this@DashboardMahasiswaActivity, res?.message.toString(), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DashboardMahasiswaActivity, LoginActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@DashboardMahasiswaActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

//      button create task action
        btnCreateTask.setOnClickListener(){
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }

//      click image to profile
        cardProfile.setOnClickListener() {
            startActivity(Intent(this, ProfileMahasiswaActivity::class.java))
        }
    }
}