package com.example.pengaduan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.request.ChangePasswordRequest
import com.example.pengaduan.model.response.ChangePasswordResponse
import com.example.pengaduan.services.ChangePasswordApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var inputNewPassword: TextInputEditText
    private lateinit var inputConfirmPassword: TextInputEditText
    private lateinit var btnChangePassword: Button
    private lateinit var cardProgressBar: CardView
    private var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        sharedPref = PrefrencesHelper(this)

        val intent = intent.extras
        id = intent?.getInt("id")!!

        setUpView()
        setUpListener()
    }

    private fun setUpView(){
        inputNewPassword        = findViewById(R.id.text_input_new_password)
        inputConfirmPassword    = findViewById(R.id.text_input_confirm_password)
        btnChangePassword       = findViewById(R.id.button_change_password)
        cardProgressBar         = findViewById(R.id.change_password_card_pregress_bar)
    }

    private fun setUpListener(){
        btnChangePassword.setOnClickListener(){
            cardProgressBar.visibility = View.VISIBLE
            if (inputNewPassword.text.toString().trim() == inputConfirmPassword.text.toString().trim()){
                val token = sharedPref.getString(Constant.PREF_IS_TOKEN)
                val request = ChangePasswordRequest()
                request.password = inputNewPassword.text.toString().trim()
                val retro = Retro().getClientInstance().create(ChangePasswordApi::class.java)
                retro.changePassword(
                    token,
                    id,
                    request
                ).enqueue(object: Callback<ChangePasswordResponse>{
                    override fun onResponse(
                        call: Call<ChangePasswordResponse>,
                        response: Response<ChangePasswordResponse>
                    ) {
                        cardProgressBar.visibility = View.INVISIBLE
                        if (response.isSuccessful){
                            Toast.makeText(this@ChangePasswordActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@ChangePasswordActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                cardProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "validasi gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
}