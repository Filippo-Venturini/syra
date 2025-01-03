package com.example.syra.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.syra.DeviceActionListener
import com.example.syra.R
import com.example.syra.model.Device

class CardAdapter (
    private val context: Context,
    private var devices : List<Device>,
    private val deviceActionListener: DeviceActionListener)
    : BaseAdapter() {
    override fun getCount(): Int = devices.size

    override fun getItem(position: Int): Any = devices[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ClickableViewAccessibility")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.smart_device_card, parent, false)

        val device = devices[position]
        val name = view.findViewById<TextView>(R.id.deviceName)
        val btnUp = view.findViewById<ImageButton>(R.id.btnUp)
        val btnDown = view.findViewById<ImageButton>(R.id.btnDown)
        val btnStop = view.findViewById<ImageButton>(R.id.btnStop)
        val card = view.findViewById<CardView>(R.id.cardView)

        name.text = device.name

        btnUp.setOnClickListener { _ ->
            deviceActionListener.onUpPressed(device)
        }

        btnDown.setOnClickListener { _ ->
            deviceActionListener.onDownPressed(device)
        }

        btnStop.setOnClickListener{ _ ->
            deviceActionListener.onStopPressed(device)
        }

        return view
    }

    fun updateDevices(newDevices: List<Device>){
        this.devices = newDevices
        notifyDataSetChanged()
    }
}