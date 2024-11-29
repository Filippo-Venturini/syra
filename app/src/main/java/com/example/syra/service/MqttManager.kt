package com.example.syra.service

import android.content.Context
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttManager(private val context: Context,
                  private val brokerUrl: String,
                  private val username: String,
                  private val password: String) {

    private lateinit var mqttClient: MqttClient

    fun connect(callback: MqttCallback? = null, onSuccess: (() -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        try {
            mqttClient = MqttClient(brokerUrl, MqttClient.generateClientId(), null)

            val options = MqttConnectOptions().apply {
                isCleanSession = true
                userName = username
                password = this@MqttManager.password.toCharArray()
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

    fun publishMessage(topic: String, message: String, qos: Int = 1, onSuccess: (() -> Unit)? = null, onError: ((Exception) -> Unit)? = null) {
        if (::mqttClient.isInitialized && mqttClient.isConnected) {
            try {
                val mqttMessage = MqttMessage().apply {
                    payload = message.toByteArray()
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
