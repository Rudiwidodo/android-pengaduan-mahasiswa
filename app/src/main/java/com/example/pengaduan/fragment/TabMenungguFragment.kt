package com.example.pengaduan.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengaduan.DetailTaskActivity
import com.example.pengaduan.R
import com.example.pengaduan.adapter.FragmentMenungguAdapter
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PengaduanResult
import com.example.pengaduan.helper.PengaduanResultParcelable
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.request.PengaduanRequest
import com.example.pengaduan.model.response.PengaduanResponse
import com.example.pengaduan.services.PengaduanMenugguFilter
import com.example.pengaduan.services.PengaduanMenungguApi
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TabMenungguFragment : Fragment() {
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var rcView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var sessionToken: String
    private lateinit var cardProgressBar: CardView
    private lateinit var inputFilterLayout: TextInputLayout
    private lateinit var inputFilter: TextInputEditText
    private lateinit var buttonFilter: Button
    private lateinit var calendar: Calendar
    private lateinit var datePicker: DatePickerDialog.OnDateSetListener
    private lateinit var tanggal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = PrefrencesHelper(requireContext())
        sessionToken = sharedPref.getString(Constant.PREF_IS_TOKEN).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_menunggu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcView          = view.findViewById(R.id.recyecle_view_menunggu)
        textView        = view.findViewById(R.id.text_recycle_view_menunggu)
        cardProgressBar = view.findViewById(R.id.fragment_menunggu_card_pregress_bar)
        inputFilterLayout   = view.findViewById(R.id.input_filter_layout)
        inputFilter         = view.findViewById(R.id.input_filter)
        buttonFilter        = view.findViewById(R.id.button_filter)

        calendar    = Calendar.getInstance()
        datePicker  = DatePickerDialog.OnDateSetListener{ _, year, month, dayofmonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayofmonth)
            updateCalender()
        }

        inputFilter.setOnClickListener {
            showCalender()

            val setFormat   = "dd/MM/yyyy"
            val sdf         = SimpleDateFormat(setFormat, Locale.UK)
            tanggal         = sdf.format(calendar.time)
        }

        buttonFilter.setOnClickListener {
            cardProgressBar.visibility = View.VISIBLE

            val setFormat       = "yyyy-MM-dd"
            val sdf             = SimpleDateFormat(setFormat, Locale.UK)
            val inputTanggal    = sdf.format(calendar.time)

            val request         = PengaduanRequest()
            request.tanggal     = inputTanggal

            val retro = Retro().getClientInstance().create(PengaduanMenugguFilter::class.java)
            retro.menugguFilter(token = sessionToken, request).enqueue(object: Callback<PengaduanResponse>{
                override fun onResponse(
                    call: Call<PengaduanResponse>,
                    response: Response<PengaduanResponse>
                ) {
                    cardProgressBar.visibility = View.INVISIBLE

                    if(response.isSuccessful){
                        if(response.body()?.result?.size != 0){
                            textView.visibility = View.INVISIBLE
                            rcView.visibility = View.VISIBLE
//                      kirim data ke adapter
                            val adapter = FragmentMenungguAdapter(response.body()?.result!!, object : FragmentMenungguAdapter.OnAdapterListener{
//                                override fun onUpdatePengaduan(pengaduan: PengaduanResult) {
//                                    updatePengaduan(pengaduan)
//                                }
//
//                                override fun onDeletePengaduan(id: Int) {
//                                    deletePengaduan(sessionToken, id)
//                                }

                                override fun onDetailPengaduan(pengaduan: PengaduanResult) {
                                    detailPengaduan(pengaduan)
                                }
                            })
                            rcView.layoutManager = LinearLayoutManager(context)
                            rcView.adapter = adapter
                        } else {
                            rcView.visibility = View.INVISIBLE
                            textView.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<PengaduanResponse>, t: Throwable) {
                    cardProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()

        cardProgressBar.visibility = View.VISIBLE
        val retro = Retro().getClientInstance().create(PengaduanMenungguApi::class.java)
        retro.getPengaduanMenunggu(
            token = sessionToken
        ).enqueue(object: Callback<PengaduanResponse>{
            override fun onResponse(
                call: Call<PengaduanResponse>,
                response: Response<PengaduanResponse>
            ) {
                cardProgressBar.visibility = View.INVISIBLE

                if(response.isSuccessful){
                    if(response.body()?.result?.size != 0){
//                      kirim data ke adapter
                        val adapter = FragmentMenungguAdapter(response.body()?.result!!, object : FragmentMenungguAdapter.OnAdapterListener{
//                            override fun onUpdatePengaduan(pengaduan: PengaduanResult) {
//                                updatePengaduan(pengaduan)
//                            }
//
//                            override fun onDeletePengaduan(id: Int) {
//                                deletePengaduan(sessionToken, id)
//                            }

                            override fun onDetailPengaduan(pengaduan: PengaduanResult) {
                                detailPengaduan(pengaduan)
                            }
                        })
                        rcView.layoutManager = LinearLayoutManager(context)
                        rcView.adapter = adapter
                    } else {
                        rcView.visibility = View.INVISIBLE
                        textView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<PengaduanResponse>, t: Throwable) {
                cardProgressBar.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showCalender(){
        DatePickerDialog(
            requireContext(),
            datePicker,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateCalender(){
        val setFormat   = "dd/MM/yyyy"
        val sdf         = SimpleDateFormat(setFormat, Locale.UK)
        tanggal         = sdf.format(calendar.time)
        inputFilter.setText(tanggal)
    }

//    private fun updatePengaduan(pengaduan: PengaduanResult){
//        val dataSend = PengaduanResultParcelable(
//            id = pengaduan.id,
//            nim = pengaduan.nim,
//            isiPengaduan = pengaduan.isiPengaduan,
//            foto = pengaduan.foto,
//            isActive = pengaduan.isActive,
//            isArchive = pengaduan.isArchive,
//            createdAt = pengaduan.createdAt,
//            updatedAt = pengaduan.updatedAt
//        )
//        val bundle = Bundle()
//        bundle.putParcelable("pengaduanResult", dataSend)
//        val setActivity = Intent(requireContext(), UpdateTaskActivity::class.java)
//        setActivity.putExtras(bundle)
//        startActivity(setActivity)
//    }
//    private fun deletePengaduan(token: String, id: Int){
//        cardProgressBar.visibility = View.VISIBLE
//        val retro = Retro().getClientInstance().create(DeletePengaduanApi::class.java)
//        retro.deletePengaduan(
//            token,
//            id
//        ).enqueue(object: Callback<DeletePengaduanResponse>{
//            override fun onResponse(
//                call: Call<DeletePengaduanResponse>,
//                response: Response<DeletePengaduanResponse>
//            ) {
//                if(response.isSuccessful){
//                    cardProgressBar.visibility = View.INVISIBLE
//                    Toast.makeText(requireContext(), response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(requireContext(), DashboardMahasiswaActivity::class.java))
//                }
//            }
//
//            override fun onFailure(call: Call<DeletePengaduanResponse>, t: Throwable) {
//                cardProgressBar.visibility = View.INVISIBLE
//                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun detailPengaduan(pengaduan: PengaduanResult){
        val dataSend = PengaduanResultParcelable(
            id = pengaduan.id,
            nim = pengaduan.nim,
            isiPengaduan = pengaduan.isiPengaduan,
            foto = pengaduan.foto,
            isActive = pengaduan.isActive,
            isArchive = pengaduan.isArchive,
            createdAt = pengaduan.createdAt,
            updatedAt = pengaduan.updatedAt
        )
        val bundle = Bundle()
        bundle.putParcelable("pengaduanResult", dataSend)
        val setActivity = Intent(requireContext(), DetailTaskActivity::class.java)
        setActivity.putExtras(bundle)
        startActivity(setActivity)
    }

}