package com.example.wsamad5.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wsamad5.R
import com.example.wsamad5.core.Constants
import com.example.wsamad5.data.get
import com.example.wsamad5.data.models.Symptom
import com.example.wsamad5.databinding.FragmentCheckListBinding
import com.example.wsamad5.ui.adapter.CheckAdapter
import com.google.android.material.snackbar.Snackbar
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.URI


class CheckListFragment : Fragment(R.layout.fragment_check_list) {
    private lateinit var binding: FragmentCheckListBinding
    private val symptomList = mutableListOf<Symptom>()
    private var uriResult: Uri? = null
    private val registerGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val data = it.data?.data
            data?.let { uri ->
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor =
                    requireContext().contentResolver.query(uri, projection, null, null, null)
                val column = cursor!!.getColumnIndex(MediaStore.Images.Media.DATA)
                if (cursor.moveToNext()) uriResult = Uri.parse(cursor.getString(column))

                binding.imgAdd.setImageURI(uriResult)
            }


        }
    private val registerPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (false in it.values) {
                Snackbar.make(
                    binding.root,
                    "You must enable the permissions",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                checkPermission()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCheckListBinding.bind(view)

        obtainList()
        clicks()

    }

    private fun clicks() {
        binding.imgAdd.setOnClickListener { checkPermission() }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickFromGallery()
            }
            else -> {
                registerPermission.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
        }
    }

    private fun pickFromGallery() {
        val i = Intent(Intent.ACTION_PICK)
        i.type = "image/*"
        registerGallery.launch(i)

    }

    private fun obtainList() {
        Constants.OKHTTP.newCall(get("symptom_list")).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("onFailure: ", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val json = JSONTokener(response.body!!.string()).nextValue() as JSONObject
                val data = json.getJSONArray("data")
                for (i in 0 until data.length()) {
                    val item = data.getJSONObject(i)
                    symptomList.add(
                        Symptom(
                            item.getInt("id"),
                            item.getString("title"),
                            item.getInt("priority")
                        )
                    )
                }
                requireActivity().runOnUiThread {
                    binding.rvList.adapter = CheckAdapter(symptomList)
                    binding.rvList.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        })
    }

}