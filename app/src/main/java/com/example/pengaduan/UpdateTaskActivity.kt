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
import com.example.pengaduan.helper.PengaduanResultParcelable
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.response.UpdatePengaduanResponse
import com.example.pengaduan.services.UpdatePengaduanApi
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

class UpdateTaskActivity : AppCompatActivity() {

    var selectedImageUri: Uri? = null
    private var recivedData: PengaduanResultParcelable? = null
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var inputPengaduan: TextInputEditText
    private lateinit var imagePreview: ImageView
    private lateinit var btnUpdate: Button
    private lateinit var btnUpload: Button
    private lateinit var fileDownloader: FileDownloader
    private lateinit var foto: RequestBody
    private lateinit var file: File
    private lateinit var cardProgressBar: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        sharedPref = PrefrencesHelper(this)

        val recivedBundle = intent.extras
        recivedData = recivedBundle?.getParcelable<PengaduanResultParcelable>("pengaduanResult")

        fileDownloader = FileDownloader(this)
        fileDownloader.downloadAndSaveFile(
            "https://pengamas.my.id/uploads/${recivedData?.foto}",
            "${recivedData?.foto}"
        ) {
            if(it != null){
                file = File(it.path ?: "")
                foto = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            }
        }

        setUpView()
        setUpListener()
    }

    private fun setUpView(){
        inputPengaduan  = findViewById(R.id.text_input_isi_pengaduan_update)
        imagePreview    = findViewById(R.id.image_preview_update)
        btnUpdate       = findViewById(R.id.update_task)
        btnUpload       = findViewById(R.id.button_upload_update)
        cardProgressBar = findViewById(R.id.update_task_card_pregress_bar)

        if(recivedData != null){
            inputPengaduan.setText(recivedData?.isiPengaduan)
            Picasso.get().load("https://pengamas.my.id/uploads/${recivedData?.foto}").into(imagePreview)
        }
    }

    private fun setUpListener(){
        btnUpdate.setOnClickListener(){
            updateTask()
        }

        btnUpload.setOnClickListener(){
            getImage()
        }
    }

    private fun updateTask(){
        val token = sharedPref.getString(Constant.PREF_IS_TOKEN).toString()
        if(selectedImageUri != null){
            cardProgressBar.visibility = View.VISIBLE

            val id = recivedData?.id
            val nim = recivedData?.nim
            val requestBody = inputPengaduan.text.toString().trim()
            val isiPengaduan = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBody)
            file = File(getRealPathFromUri(selectedImageUri!!))
            foto = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

            val retro = Retro().getClientInstance().create(UpdatePengaduanApi::class.java)
            retro.updatePengaduan(
                token,
                id,
                nim,
                isiPengaduan,
                MultipartBody.Part.createFormData("foto", file.name, foto)
            ).enqueue(object: Callback<UpdatePengaduanResponse>{
                override fun onResponse(
                    call: Call<UpdatePengaduanResponse>,
                    response: Response<UpdatePengaduanResponse>
                ) {
                    if(response.isSuccessful){
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@UpdateTaskActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UpdateTaskActivity, DashboardMahasiswaActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<UpdatePengaduanResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@UpdateTaskActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            cardProgressBar.visibility = View.VISIBLE

            val id = recivedData?.id
            val nim = recivedData?.nim
            val requestBody = inputPengaduan.text.toString().trim()
            val isiPengaduan = RequestBody.create("text/plain".toMediaTypeOrNull(), requestBody)

            val retro = Retro().getClientInstance().create(UpdatePengaduanApi::class.java)
            retro.updatePengaduan(
                token,
                id,
                nim,
                isiPengaduan,
                MultipartBody.Part.createFormData("foto", file.name, foto)
            ).enqueue(object: Callback<UpdatePengaduanResponse>{
                override fun onResponse(
                    call: Call<UpdatePengaduanResponse>,
                    response: Response<UpdatePengaduanResponse>
                ) {
                    if(response.isSuccessful){
                        cardProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this@UpdateTaskActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UpdateTaskActivity, DashboardMahasiswaActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<UpdatePengaduanResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@UpdateTaskActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
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
            imagePreview.setImageURI(selectedImageUri)
        }
    }
}