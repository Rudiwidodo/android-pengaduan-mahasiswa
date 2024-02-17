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
import com.example.pengaduan.adapter.FragmentDiterimaAdapter
import com.example.pengaduan.adapter.FragmentMenungguAdapter
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PengaduanResult
import com.example.pengaduan.helper.PengaduanResultParcelable
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.request.PengaduanRequest
import com.example.pengaduan.model.response.PengaduanResponse
import com.example.pengaduan.services.PengaduanDiterimaApi
import com.example.pengaduan.services.PengaduanDiterimaFilter
import com.example.pengaduan.services.PengaduanMenugguFilter
import com.example.pengaduan.utils.Retro
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TabDiterimaFragment : Fragment() {
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var rcView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var cardPregressBar: CardView
    private lateinit var inputFilterLayout: TextInputLayout
    private lateinit var inputFilter: TextInputEditText
    private lateinit var buttonFilter: Button
    private lateinit var calendar: Calendar
    private lateinit var datePicker: DatePickerDialog.OnDateSetListener
    private lateinit var tanggal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_diterima, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcView          = view.findViewById(R.id.recyecle_view_diterima)
        textView        = view.findViewById(R.id.text_recycle_view_diterima)
        cardPregressBar = view.findViewById(R.id.fragment_diterima_card_pregress_bar)
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
            cardPregressBar.visibility = View.VISIBLE

            sharedPref = PrefrencesHelper(requireContext())
            val token = sharedPref.getString(Constant.PREF_IS_TOKEN)

            val setFormat       = "yyyy-MM-dd"
            val sdf             = SimpleDateFormat(setFormat, Locale.UK)
            val inputTanggal    = sdf.format(calendar.time)

            val request         = PengaduanRequest()
            request.tanggal     = inputTanggal

            val retro = Retro().getClientInstance().create(PengaduanDiterimaFilter::class.java)
            retro.menugguFilter(token, request).enqueue(object: Callback<PengaduanResponse>{
                override fun onResponse(
                    call: Call<PengaduanResponse>,
                    response: Response<PengaduanResponse>
                ) {
                    cardPregressBar.visibility = View.INVISIBLE

                    if(response.isSuccessful){
                        if(response.body()?.result?.size != 0){
                            textView.visibility = View.INVISIBLE
                            rcView.visibility = View.VISIBLE
//                          kirim data ke adapter
                            val adapter = FragmentDiterimaAdapter(response.body()?.result!!, object: FragmentDiterimaAdapter.OnAdapterListener{
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
                    cardPregressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        cardPregressBar.visibility = View.VISIBLE

        sharedPref = PrefrencesHelper(requireContext())
        val token = sharedPref.getString(Constant.PREF_IS_TOKEN)

        val retro = Retro().getClientInstance().create(PengaduanDiterimaApi::class.java)
        retro.getPengaduanDiterima(
            token
        ).enqueue(object: Callback<PengaduanResponse> {
            override fun onResponse(
                call: Call<PengaduanResponse>,
                response: Response<PengaduanResponse>
            ) {
                if(response.isSuccessful){
                    cardPregressBar.visibility = View.INVISIBLE
                    if(response.body()?.result?.size != 0){
                        val adapter = FragmentDiterimaAdapter(response.body()?.result!!, object: FragmentDiterimaAdapter.OnAdapterListener{
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
                cardPregressBar.visibility = View.INVISIBLE
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