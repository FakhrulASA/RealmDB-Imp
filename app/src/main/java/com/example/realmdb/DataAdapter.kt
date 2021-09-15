package com.example.realmdb

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(private val dataSet: MutableList<DataModel>) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
           val textName:TextView=view.findViewById(R.id.tvName)
           val textAge:TextView=view.findViewById(R.id.tvAge)
           val textGender:TextView=view.findViewById(R.id.tvGender)
           val textID:TextView=view.findViewById(R.id.tvID)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapter.DataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapter.DataViewHolder, position: Int) {
        holder.textAge.text=dataSet[position].age.toString()
        holder.textGender.text=dataSet[position].gender.toString()
        holder.textName.text=dataSet[position].name.toString()
        holder.textID.text=dataSet[position].id.toString()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
