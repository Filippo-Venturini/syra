package com.example.syra

import com.example.syra.model.Device

interface DeviceActionListener {
    fun onUpClicked(device: Device)
    fun onDownClicked(device: Device)
}