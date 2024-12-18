package com.example.syra.repository

import android.content.Context
import com.example.syra.model.MqttCommand
import com.example.syra.utils.Constants.BROKER_URL
import com.example.syra.utils.Constants.MQTT_PASSWORD
import com.example.syra.utils.Constants.MQTT_USERNAME
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttRepository {

    private lateinit var mqttClient: MqttClient

    private val brokerUrl: String = BROKER_URL
    private val username: String = MQTT_USERNAME
    private val password: String = MQTT_PASSWORD

    private val gson: Gson = Gson()

    fun connect(callback: MqttCallback? = null, onSuccess: (() -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        try {
            mqttClient = MqttClient(brokerUrl, MqttClient.generateClientId(), null)

            val options = MqttConnectOptions().apply {
                isCleanSession = true
                userName = username
                password = this@MqttRepository.password.toCharArray()
            }

            if (callback != null) {
                mqttClient.setCallback(callback)
            }

            mqttClient.connect(options)
            onSuccess?.invoke()
        } catch (e: MqttException) {
            e.printStackTrace()
            onError?.invoke(e)
        }
    }

    fun publishMessage(topic: String, command: MqttCommand, qos: Int = 1, onSuccess: (() -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        if (::mqttClient.isInitialized && mqttClient.isConnected) {
            try {
                val mqttMessage = MqttMessage().apply {
                    val jsonCommand : String = gson.toJson(command)
                    payload = jsonCommand.toByteArray()
                    this.qos = qos
                }
                mqttClient.publish(topic, mqttMessage)
                onSuccess?.invoke()
            } catch (e: MqttException) {
                e.printStackTrace()
                onError?.invoke(e)
            }
        } else {
            onError?.invoke(IllegalStateException("MQTT Client not connected!"))
        }
    }

    fun disconnect(onSuccess: (() -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        try {
            if (::mqttClient.isInitialized && mqttClient.isConnected) {
                mqttClient.disconnect()
                onSuccess?.invoke()
            }
        } catch (e: MqttException) {
            e.printStackTrace()
            onError?.invoke(e)
        }
    }
}
