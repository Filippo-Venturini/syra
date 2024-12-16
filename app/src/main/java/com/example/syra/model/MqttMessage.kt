package com.example.syra.model

data class MqttCommand(val id: String, val src: String, val method: String, val params: MqttCommandParams)

data class MqttCommandParams(val id: String, val on: Boolean)
