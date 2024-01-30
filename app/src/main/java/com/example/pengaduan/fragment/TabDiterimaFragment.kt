package com.example.pengaduan.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengaduan.DetailTaskActivity
import com.example.pengaduan.R
import com.example.pengaduan.adapter.FragmentDiterimaAdapter
import com.example.pengaduan.helper.Constant
import com.example.pengaduan.helper.PengaduanResult
import com.example.pengaduan.helper.PengaduanResultParcelable
import com.example.pengaduan.helper.PrefrencesHelper
import com.example.pengaduan.model.response.PengaduanResponse
import com.example.pengaduan.services.PengaduanDiterimaApi
import com.example.pengaduan.utils.Retro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabDiterimaFragment : Fragment() {
    private lateinit var sharedPref: PrefrencesHelper
    private lateinit var rcView: RecyclerView
    private lateinit var textView: TextView
    private lateinit var cardPregressBar: CardView
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