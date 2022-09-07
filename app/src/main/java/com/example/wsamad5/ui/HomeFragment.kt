package com.example.wsamad5.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.navigation.fragment.findNavController
import com.example.wsamad5.R
import com.example.wsamad5.core.Constants
import com.example.wsamad5.data.get
import com.example.wsamad5.data.models.History
import com.example.wsamad5.databinding.FragmentHomeBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val historyList = mutableListOf<History>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        obtainActualDate()
        obtainCases()
        obtainHistory()
        clicks()
    }

    private fun clicks() {
        binding.imgQr.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_qrFragment) }
        binding.imgMap.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_mapFragment) }
        binding.btnCheckIn.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_checkListFragment) }
    }

    private fun obtainHistory() {
        val sharedPreferences =
            requireContext().getSharedPreferences(Constants.USER, Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", "")
        val name = sharedPreferences.getString("name", "")
        setName(name)
        Constants.OKHTTP.newCall(get("symptoms_history?user_id=$id")).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONTokener(response.body!!.string()).nextValue() as JSONObject
                if (json.getBoolean("success")) {
                    val data = json.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val item = data.getJSONObject(i)
                        historyList.add(
                            History(
                                SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(
                                    item.getString(
                                        "date"
                                    )
                                ), item.getInt("probability_infection")
                            )
                        )
                    }
                    val finalData = historyList[data.length() - 1]
                    requireActivity().runOnUiThread {
                        if (finalData.probability_infection > 50) {
                            binding.llBgWithData.backgroundTintList = getColorStateList(requireContext(),R.color.dark_blue)
                            binding.txtTitleWithData.text = "CALL TO DOCTOR"
                            binding.txtMessageWithData.text = "You may be infected with a virus"
                        } else {
                            binding.llBgWithData.backgroundTintList = getColorStateList(requireContext(),R.color.blue_200)
                            binding.txtTitleWithData.text = "CLEAR"
                            binding.txtMessageWithData.text = "* Wear mask. Keep 2m distance. Wash hands."
                        }
                        binding.txtYearHour.text = SimpleDateFormat("/yyyy KK:mmaa").format(finalData.date)
                        binding.txtMonthDay.text = SimpleDateFormat("MM/dd").format(finalData.date)
                        binding.llWithData.visibility = View.VISIBLE
                        binding.llWithData1.visibility = View.VISIBLE
                    }
                } else {
                    requireActivity().runOnUiThread {
                        binding.llNoData.visibility = View.VISIBLE
                        binding.llNoData1.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setName(name: String?) {
        name?.let {
            binding.txtNameNoData.text = it
            binding.txtNameWithData.text = it
        }
    }

    private fun obtainCases() {
        Constants.OKHTTP.newCall(get("cases")).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val data = (0..50).random()
                Log.e("data", data.toString())
                requireActivity().runOnUiThread {
                    if (data > 0) {
                        binding.txtNumCases.text = "$data Cases"
                        binding.txtNumCases1.text = "$data Cases"
                        binding.llBgCases.backgroundTintList =
                            getColorStateList(requireContext(), R.color.dark_blue)
                        binding.llBgCases1.backgroundTintList =
                            getColorStateList(requireContext(), R.color.dark_blue)
                    }
                }
            }
        })
    }

    private fun obtainActualDate() {
        binding.txtActualDate.text = SimpleDateFormat("MMM dd, yyyy").format(Date())
    }
}