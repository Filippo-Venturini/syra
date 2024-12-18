package com.example.syra

import com.example.syra.model.Device

interface DeviceActionListener {
    fun onUpPressed(device: Device)
    fun onUpReleased(device: Device)
    fun onDownPressed(device: Device)
    fun onDownReleased(device: Device)
}