package com.example.syra.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.syra.model.Device
import com.example.syra.repository.MqttRepository
import com.example.syra.utils.Constants.SWITCH_0_OFF
import com.example.syra.utils.Constants.SWITCH_0_ON
import com.example.syra.utils.Constants.SWITCH_1_OFF
import com.example.syra.utils.Constants.SWITCH_1_ON
import com.example.syra.utils.Constants.TOPIC
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class DeviceViewModel : ViewModel() {

    private val _currentDeviceList = MutableLiveData<List<Device>>()
    val currentDeviceList: LiveData<List<Device>> get() = _currentDeviceList

    private val mqttRepository: MqttRepository = MqttRepository()
    private val bedRoomDeviceList = listOf(Device("Bedroom Device 1", TOPIC), Device("Bedroom Device 2", TOPIC))
    private val livingRoomDeviceList = listOf(Device("Living Room Device 1", TOPIC), Device("Living Room Device 2", TOPIC))

    init {
        loadBedroomDevices()
    }

    fun loadBedroomDevices(){
        _currentDeviceList.value = bedRoomDeviceList
    }

    fun loadLivingRoomDevices(){
        _currentDeviceList.value = livingRoomDeviceList
    }

    fun connectToMqttBroker(){
        mqttRepository.connect (
            onSuccess = {
                //Toast.makeText(this, "Connected to the broker", Toast.LENGTH_SHORT).show()
            },
            onError = { e ->
                //Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    fun publishSwitch0On(device: Device){
        mqttRepository.publishMessage(device.topic, SWITCH_0_ON)
    }

    fun publishSwitch0Off(device: Device){
        mqttRepository.publishMessage(device.topic, SWITCH_0_OFF)
    }

    fun publishSwitch1On(device: Device){
        mqttRepository.publishMessage(device.topic, SWITCH_1_ON)
    }

    fun publishSwitch1Off(device: Device){
        mqttRepository.publishMessage(device.topic, SWITCH_1_OFF)
    }

}