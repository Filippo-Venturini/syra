package com.example.syra

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.syra.service.MqttManager
import io.github.cdimascio.dotenv.dotenv
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : ComponentActivity() {
    private lateinit var mqttManager: MqttManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dotenv = dotenv()

        val username = dotenv["MQTT_USERNAME"]
        val password = dotenv["MQTT_PASSWORD"]

        Toast.makeText(this@MainActivity, username, Toast.LENGTH_SHORT).show()

//        mqttManager = MqttManager(
//            context = this,
//            brokerUrl = "",
//            username = "",
//            password = ""
//        )
//
//        mqttManager.connect (
//            callback = object : MqttCallback {
//                override fun connectionLost(cause: Throwable?) {
//                    runOnUiThread {
//                        Toast.makeText(this@MainActivity, "Connection lost!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun messageArrived(topic: String?, message: MqttMessage?) {
//                    //Handle arriving messages
//                }
//
//                override fun deliveryComplete(token: IMqttDeliveryToken?) {
//                    runOnUiThread {
//                        Toast.makeText(this@MainActivity, "Message sent!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            },
//            onSuccess = {
//                Toast.makeText(this, "Connected to the broker", Toast.LENGTH_SHORT).show()
//            },
//            onError = { e ->
//                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
//            }
//        )
//
//        val btnMqtt = findViewById<Button>(R.id.btnMQTT)
//
//        btnMqtt.setOnClickListener{
//            mqttManager.publishMessage(
//                topic = "my/test/topic",
//                message = "Hello from Kotlin!",
//                onSuccess = {
//                    Toast.makeText(this, "Message published!", Toast.LENGTH_SHORT).show()
//                },
//                onError = { e ->
//                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//            )
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttManager.disconnect(
            onSuccess = {
                Toast.makeText(this, "Disconnected from the broker!", Toast.LENGTH_SHORT).show()
            },
            onError = { e ->
                Toast.makeText(this, "Disconnection error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}
