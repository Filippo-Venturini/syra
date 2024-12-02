package com.example.syra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.syra.R

class CardAdapter (
    private val context: Context,
    private var devicesNames : List<String>)
    : BaseAdapter() {

    override fun getCount(): Int {
        return devicesNames.size
    }

    override fun getItem(position: Int): Any {
        return Any()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.smart_device_card, parent, false)

        val name: TextView = view.findViewById(R.id.smartDeviceName)

        name.text = devicesNames[position]

        return view
    }

    fun updateDevices(newDevices: List<String>){
        this.devicesNames = newDevices
        notifyDataSetChanged()
    }
}