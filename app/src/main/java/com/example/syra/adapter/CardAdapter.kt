package com.example.syra.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.syra.DeviceActionListener
import com.example.syra.R
import com.example.syra.model.Device
import com.example.syra.utils.Constants.SWITCH_0_OFF
import com.example.syra.utils.Constants.SWITCH_0_ON
import com.example.syra.utils.Constants.TOPIC

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
        val switchDevice = view.findViewById<Switch>(R.id.switchDevice)
        val card = view.findViewById<CardView>(R.id.cardView)

        name.text = device.name

        btnUp.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    deviceActionListener.onUpPressed(device)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    deviceActionListener.onUpReleased(device)
                    true
                }

                else -> false
            }
        }

        btnDown.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    deviceActionListener.onDownPressed(device)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    deviceActionListener.onDownReleased(device)
                    true
                }

                else -> false
            }
        }

        switchDevice.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                deviceActionListener.onAllDownPressed(device, switchDevice)
            } else {
                deviceActionListener.onAllUpPressed(device, switchDevice)
            }
        }

        return view
    }

    fun updateDevices(newDevices: List<Device>){
        this.devices = newDevices
        notifyDataSetChanged()
    }
}