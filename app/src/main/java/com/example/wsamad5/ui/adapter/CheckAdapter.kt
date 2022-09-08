package com.example.wsamad5.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.wsamad5.data.models.Symptom
import com.example.wsamad5.databinding.ActivityHomeBinding
import com.example.wsamad5.databinding.ItemSymptomBinding

class CheckAdapter(private val list :List<Symptom>):RecyclerView.Adapter<CheckAdapter.CheckViewHolder>() {
    private val checkBoxList = mutableListOf<CheckBox>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckViewHolder {
        val binding = ItemSymptomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CheckViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class CheckViewHolder(private val binding: ItemSymptomBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item : Symptom){
            checkBoxList.add(binding.checkbox)
            binding.txtTitle.text = item.title
        }
    }
}
