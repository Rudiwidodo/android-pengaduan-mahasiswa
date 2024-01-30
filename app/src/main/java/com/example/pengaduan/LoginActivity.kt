package com.example.pengaduan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.request.LoginRequest
import com.example.pengaduan.model.response.LoginResponse
import com.example.pengaduan.services.LoginApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

//  inisialisai variable
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var inputNim: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView
    private lateinit var cardProgressBar: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUpView()
        setUpListener()
        sharedPref = PrefrencesHelper(this)
    }

//  cek session login
    override fun onStart() {
        super.onStart()
//      jika session login == true maka pindah ke dashboard mahasiswa
        if(sharedPref.getBoolean(Constant.PREF_IS_LOGIN)){
            startActivity(Intent(this,DashboardMahasiswaActivity::class.java))
        }
    }

//    menghentikan aplikasi
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

//  inisialisasi set up view
    private fun setUpView(){
        inputNim        = findViewById(R.id.text_input_nim_login)
        inputPassword   = findViewById(R.id.text_input_password_login)
        btnLogin        = findViewById(R.id.button_login)
        btnRegister     = findViewById(R.id.button_text_daftar)
        cardProgressBar = findViewById(R.id.login_card_pregress_bar)
    }

//  inisialisasi set up action listener
    private fun setUpListener(){
//      action button login
        btnLogin.setOnClickListener(){
//          view pregressbar
            cardProgressBar.visibility = View.VISIBLE

//          membuat request sesuai dengan server
            val request = LoginRequest()
            request.userdata = inputNim.text.toString().trim()
            request.password = inputPassword.text.toString().trim()

//          panggi api login dengan retrofit
            val retro = Retro().getClientInstance().create(LoginApi::class.java)
            retro.login(request).enqueue(object : Callback<LoginResponse>{
//              success response
                override fun onResponse(
    call: Call<LoginResponse>,
    response: Response<LoginResponse>
                ) {
                    val res = response.body()
                    if(response.isSuccessful){
                        cardProgressBar.visibility = View.INVISIBLE
//                      get profile mahasiswa
                        sharedPref.put(Constant.PREF_IS_TOKEN, "Bearer ${res?.token.toString()}")
                        sharedPref.put(Constant.PREF_IS_LOGIN, true)
                        Toast.makeText(this@LoginActivity, res?.message.toString(), Toast.LENGTH_SHORT).show()
//                      move to dashboard acttivity
                            Intent(this@LoginActivity, DashboardMahasiswaActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                    } else {
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

//      action button daftar
        btnRegister.setOnClickListener(){
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}