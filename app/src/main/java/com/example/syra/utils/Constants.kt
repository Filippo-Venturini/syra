package com.example.syra.utils

import com.example.syra.model.MqttCommand
import com.example.syra.model.MqttCommandParams
import io.github.cdimascio.dotenv.dotenv

object Constants{
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }

    val BROKER_URL = dotenv["BROKER_URL"]
    val MQTT_USERNAME = dotenv["MQTT_USERNAME"]
    val MQTT_PASSWORD = dotenv["MQTT_PASSWORD"]

    const val TOPIC: String = "shellyplus2pm-2cbcbb38e150/rpc"

    val SWITCH_0_ON: MqttCommand = MqttCommand("1", "app", "Switch.Set", MqttCommandParams("0", true))
    val SWITCH_0_OFF: MqttCommand = MqttCommand("1", "app", "Switch.Set", MqttCommandParams("0", false))
    val SWITCH_1_ON: MqttCommand = MqttCommand("1", "app", "Switch.Set", MqttCommandParams("1", true))
    val SWITCH_1_OFF: MqttCommand = MqttCommand("1", "app", "Switch.Set", MqttCommandParams("1", false))
}
