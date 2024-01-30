package com.example.pengaduan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pengaduan.model.request.RegisterRequest
import com.example.pengaduan.model.response.RegisterResponse
import com.example.pengaduan.services.RegisterApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

//  inisialisasi variable dari layout register
    private lateinit var inputNim: TextInputEditText
    private lateinit var inputNimLayout: TextInputLayout
    private lateinit var inputNama: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var btnLogin: TextView
    private lateinit var btnRegister: Button
    private lateinit var cardProgressBar: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setUpView()
        setUpListener()
    }

//  inisialisasi variable dengan id dari layout register
    private fun setUpView(){
        inputNim            = findViewById(R.id.text_input_nim_register)
        inputNimLayout      = findViewById(R.id.input_nim_register)
        inputNama           = findViewById(R.id.text_input_nama_register)
        inputEmail          = findViewById(R.id.text_input_email_register)
        inputPassword       = findViewById(R.id.text_input_password_register)
        btnRegister         = findViewById(R.id.button_register)
        btnLogin            = findViewById(R.id.button_text_masuk)
        cardProgressBar     = findViewById(R.id.register_card_pregress_bar)
    }

//    membuat set up aksi ketika di klik
    private fun setUpListener(){
//      aksi ketika button register di klik.
        btnRegister.setOnClickListener(){
//          set card progress bar (lodaing) menjadi visible
            cardProgressBar.visibility = View.VISIBLE

//          set variable request dari class register request.
            val request = RegisterRequest()
            request.inputNim        = inputNim.text.toString().trim()
            request.inputNama       = inputNama.text.toString().trim()
            request.inputEmail      = inputEmail.text.toString().trim()
            request.inputPassword   = inputPassword.text.toString().trim()

//          panggil retrofit dan masukan request api register
            val retro = Retro().getClientInstance().create(RegisterApi::class.java)
            retro.register(request).enqueue(object : Callback<RegisterResponse>{
//              response ketika api success
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
//                  tangkap response yang dikirim oleh server
                    val res = response.body()
//                  cek apakah response yang di terima itu success atau error
                    if(response.isSuccessful){
//                      set card progress (loading) menjadi invisible
//                      tampilkan pesan yang di kirim oleh server
//                      pindah ke activity login
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@RegisterActivity, res?.message, Toast.LENGTH_SHORT).show()
                        Intent(this@RegisterActivity, LoginActivity::class.java).also {
                            startActivity(it)
                        }
                    } else {
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@RegisterActivity, "validasi gagal", Toast.LENGTH_SHORT).show()
                    }
                }

//                jika response dari server itu gagal atau error
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

//      action button masuk
        btnLogin.setOnClickListener(){
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}