package com.example.pengaduan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.helper.ProfileResultParcelable
import com.example.pengaduan.model.response.GetProfileMahasiswaResponse
import com.example.pengaduan.services.GetProfileMahaisswaApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var profile: ImageView
    private lateinit var nim: TextView
    private lateinit var nama: TextView
    private lateinit var email: TextView
    private lateinit var btnUpdateProfile: FloatingActionButton
    private lateinit var btnChangePasword: FloatingActionButton
    private lateinit var cardProgressBar: CardView
    private lateinit var dataProfile: ProfileResultParcelable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_mahasiswa)

        sharedPref = PrefrencesHelper(this)
        setUpView()
        setUpListener()
    }

    override fun onStart() {
        super.onStart()
        getProfileMahasiswa()
    }

    private fun getProfileMahasiswa(){
        cardProgressBar.visibility = View.VISIBLE

        val token = sharedPref.getString(Constant.PREF_IS_TOKEN).toString()
        val retro = Retro().getClientInstance().create(GetProfileMahaisswaApi::class.java)
        retro.getProfileMahasiswa(
            token
        ).enqueue(object: Callback<GetProfileMahasiswaResponse> {
            override fun onResponse(
                call: Call<GetProfileMahasiswaResponse>,
                response: Response<GetProfileMahasiswaResponse>
            ) {
                val res = response.body()
                if (response.isSuccessful){

                    dataProfile = ProfileResultParcelable(
                        id = res?.result?.id?.toInt(),
                        nim = res?.result?.nim,
                        nama = res?.result?.nama,
                        email = res?.result?.email,
                        foto = res?.result?.sampul
                    )

                    cardProgressBar.visibility = View.INVISIBLE
                    nim.text = res?.result?.nim
                    nama.text = res?.result?.nama
                    email.text = res?.result?.email
                    Picasso.get().load("https://pengamas.my.id/assets/img/${res?.result?.sampul}").into(profile)
                }
            }

            override fun onFailure(call: Call<GetProfileMahasiswaResponse>, t: Throwable) {
                cardProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this@ProfileMahasiswaActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpView(){
        profile             = findViewById(R.id.detail_profile_image)
        nim                 = findViewById(R.id.detail_profile_nim)
        nama                = findViewById(R.id.detail_profile_nama)
        email               = findViewById(R.id.detail_profile_email)
        btnChangePasword    = findViewById(R.id.fab_ganti_password)
        btnUpdateProfile    = findViewById(R.id.fab_ganti_foto)
        cardProgressBar     = findViewById(R.id.profile_card_pregress_bar)
    }

    private fun setUpListener(){
        btnUpdateProfile.setOnClickListener(){
            val intent = Intent(this, UpdateProfileActivity::class.java)
            intent.putExtra("profileResult", dataProfile)
            startActivity(intent)
        }

        btnChangePasword.setOnClickListener(){
            val intent = Intent(this, ChangePasswordActivity::class.java)
            intent.putExtra("id", dataProfile.id)
            startActivity(intent)
        }
    }

}