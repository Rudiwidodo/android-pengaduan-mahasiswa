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
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.response.CreatePengaduanResponse
import com.example.pengaduan.services.CreatePengaduanApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateTaskActivity : AppCompatActivity(){
    private var selectedImageUri: Uri? = null
    private lateinit var inputPengaduan: TextInputEditText
    private lateinit var btnUpload: Button
    private lateinit var imagePreview: ImageView
    private lateinit var btnSave: Button
    private lateinit var cardProgressBar: CardView
    private lateinit var sharedPref: PrefrencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        sharedPref = PrefrencesHelper(this)

        setUpView()
        setUpListener()
    }

//    set up tampilan berdasarkan id layout
    private fun setUpView(){
        inputPengaduan  = findViewById(R.id.text_input_isi_pengaduan)
        imagePreview    = findViewById(R.id.image_preview)
        btnUpload       = findViewById(R.id.button_upload)
        btnSave         = findViewById(R.id.save_task)
        cardProgressBar = findViewById(R.id.create_task_card_pregress_bar)
    }

//    set up button aksi
    private fun setUpListener(){
        btnUpload.setOnClickListener(){
            getImage()
        }

        btnSave.setOnClickListener(){
            saveCreateTask()
        }
    }

//    aksi ketika button simpan di tekan
    private fun saveCreateTask(){
        if(selectedImageUri != null){
            cardProgressBar.visibility = View.VISIBLE

            val file = File(getRealPathFromUri(selectedImageUri!!))
            val foto = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

            val requestBody = inputPengaduan.text.toString().trim()
            val isiPengaduan = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBody)
            val sessionToken = sharedPref.getString(Constant.PREF_IS_TOKEN)

            val retro = Retro().getClientInstance().create(CreatePengaduanApi::class.java)
            retro.createPengaduan(
                token = sessionToken,
                isiPengaduan,
                MultipartBody.Part.createFormData("foto", file.name, foto)
            ).enqueue(object: Callback<CreatePengaduanResponse>{
                override fun onResponse(
                    call: Call<CreatePengaduanResponse>,
                    response: Response<CreatePengaduanResponse>
                ) {
                    val res = response.body()
                    if (response.isSuccessful){
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@CreateTaskActivity, res?.message.toString(), Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@CreateTaskActivity, "invalid save pengaduan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CreatePengaduanResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@CreateTaskActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "foto tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
    }

//    ambil real path foto
    private fun getRealPathFromUri(uri: Uri) : String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return filePath ?: ""
    }

//    ambil foto dari galery
    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 10)
    }

//    menampilkan foto dan mmenggambil path uri foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 10 && resultCode == Activity.RESULT_OK){
            selectedImageUri = data?.data
            imagePreview.setImageURI(selectedImageUri)
        }
    }

}


