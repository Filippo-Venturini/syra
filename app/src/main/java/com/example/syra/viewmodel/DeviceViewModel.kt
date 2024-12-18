package com.example.syra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.syra.model.Device

class DeviceViewModel : ViewModel() {

    private val _currentDeviceList = MutableLiveData<List<Device>>()
    val currentDeviceList: LiveData<List<Device>> get() = _currentDeviceList

    private val bedRoomDeviceList = listOf(Device("Bedroom Device 1"), Device("Bedroom Device 2"))
    private val livingRoomDeviceList = listOf(Device("Living Room Device 1"), Device("Living Room Device 2"))

    init {
        loadBedroomDevices()
    }

    fun loadBedroomDevices(){
        _currentDeviceList.value = bedRoomDeviceList
    }

    fun loadLivingRoomDevices(){
        _currentDeviceList.value = livingRoomDeviceList
    }
}