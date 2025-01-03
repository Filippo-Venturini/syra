package com.example.syra

import android.widget.Switch
import com.example.syra.model.Device

interface DeviceActionListener {
    fun onUpPressed(device: Device)
    fun onDownPressed(device: Device)
    fun onStopPressed(device: Device)
}