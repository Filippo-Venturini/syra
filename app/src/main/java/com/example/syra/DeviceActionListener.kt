package com.example.syra

import android.widget.Switch
import com.example.syra.model.Device

interface DeviceActionListener {
    fun onUpPressed(device: Device)
    fun onUpReleased(device: Device)
    fun onDownPressed(device: Device)
    fun onDownReleased(device: Device)
    fun onAllUpPressed(device: Device, switchDevice: Switch)
    fun onAllDownPressed(device: Device, switchDevice: Switch)
}