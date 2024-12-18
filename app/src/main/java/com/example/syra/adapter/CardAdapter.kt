package com.example.syra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.syra.DeviceActionListener
import com.example.syra.R
import com.example.syra.model.Device
import com.example.syra.viewholder.DeviceViewHolder

class CardAdapter (
    private val context: Context,
    private var devices : List<Device>,
    private val deviceActionListener: DeviceActionListener)
    : BaseAdapter() {
    override fun getCount(): Int = devices.size

    override fun getItem(position: Int): Any = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.smart_device_card, parent, false)

        val device = devices[position]
        val name = view.findViewById<TextView>(R.id.deviceName)
        val btnUp = view.findViewById<Button>(R.id.btnUp)
        val btnDown = view.findViewById<Button>(R.id.btnDown)

        name.text = device.name

        btnUp.setOnClickListener {
            deviceActionListener.onUpClicked(device)
        }

        btnDown.setOnClickListener {
            deviceActionListener.onDownClicked(device)
        }

        return view
    }

    fun updateDevices(newDevices: List<Device>){
        this.devices = newDevices
        notifyDataSetChanged()
    }
}