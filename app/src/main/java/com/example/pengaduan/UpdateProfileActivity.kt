package com.example.pengaduan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.FileDownloader
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.helper.ProfileResultParcelable
import com.example.pengaduan.model.response.UpdateProfileResponse
import com.example.pengaduan.services.UpdateProfileApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdateProfileActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var inputNim: TextInputEditText
    private lateinit var inputNama: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var profile: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnUpload: Button
    private lateinit var cardProgressBar: CardView
    private var dataProfile: ProfileResultParcelable? = null
    private lateinit var sampul: RequestBody
    private lateinit var file: File
    private lateinit var fileDownloader: FileDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        sharedPref = PrefrencesHelper(this)

//        menerima data dari frgment layout
        val intent = intent.extras
        dataProfile = intent?.getParcelable<ProfileResultParcelable>("profileResult")

//        menggambil uri foto dari url
        fileDownloader = FileDownloader(this)
        fileDownloader.downloadAndSaveFile(
            "https://pengamas.my.id/assets/img/${dataProfile?.foto}",
            "${dataProfile?.foto}"
        ) {
            if(it != null){
                file = File(it.path ?: "")
                sampul = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            }
        }

        setUpView()
        setUpListener()
    }

    private fun setUpView(){
        inputNim        = findViewById(R.id.text_input_nim_update)
        inputNama       = findViewById(R.id.text_input_nama_update)
        inputEmail      = findViewById(R.id.text_input_email_update)
        profile         = findViewById(R.id.profile_image_preview_update)
        btnSave         = findViewById(R.id.btn_update_profile)
        btnUpload       = findViewById(R.id.profile_button_upload_update)
        cardProgressBar    = findViewById(R.id.update_profile_card_pregress_bar)

        inputNim.setText(dataProfile?.nim)
        inputNama.setText(dataProfile?.nama)
        inputEmail.setText(dataProfile?.email)
        Picasso.get().load("https://pengamas.my.id/assets/img/${dataProfile?.foto}").into(profile)
    }

    private fun setUpListener(){
        btnSave.setOnClickListener(){
            updateProfile()
        }

        btnUpload.setOnClickListener(){
            getImage()
        }
    }

    private fun updateProfile(){
        val token = sharedPref.getString(Constant.PREF_IS_TOKEN).toString()

//        jika ada foto baru
        if(selectedImageUri != null){
            cardProgressBar.visibility = View.VISIBLE

            val id = dataProfile?.id!!.toInt()
            val requestBodyNim = inputNim.text.toString().trim()
            val nim = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBodyNim)
            val requestBodyNama = inputNama.text.toString().trim()
            val nama = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBodyNama)
            val requestBodyEmail = inputEmail.text.toString().trim()
            val email = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBodyEmail)

            file = File(getRealPathFromUri(selectedImageUri!!))
            sampul = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

            val retro = Retro().getClientInstance().create(UpdateProfileApi::class.java)
            retro.updateProfile(
                token,
                id,
                nim,
                nama,
                email,
                MultipartBody.Part.createFormData("sampul", file.name, sampul)
            ).enqueue(object: Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    if(response.isSuccessful){
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@UpdateProfileActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UpdateProfileActivity, ProfileMahasiswaActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@UpdateProfileActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            cardProgressBar.visibility = View.VISIBLE

            val id = dataProfile?.id!!.toInt()
            val requestBodyNim = inputNim.text.toString().trim()
            val nim = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBodyNim)
            val requestBodyNama = inputNama.text.toString().trim()
            val nama = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBodyNama)
            val requestBodyEmail = inputEmail.text.toString().trim()
            val email = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBodyEmail)


            val retro = Retro().getClientInstance().create(UpdateProfileApi::class.java)
            retro.updateProfile(
                token,
                id,
                nim,
                nama,
                email,
                MultipartBody.Part.createFormData("sampul", file.name, sampul)
            ).enqueue(object: Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    if(response.isSuccessful){
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@UpdateProfileActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UpdateProfileActivity, ProfileMahasiswaActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@UpdateProfileActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getRealPathFromUri(uri: Uri) : String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return filePath ?: ""
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            selectedImageUri = data?.data
            profile.setImageURI(selectedImageUri)
        }
    }
}