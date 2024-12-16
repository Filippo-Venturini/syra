package com.example.syra

import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.syra.adapter.CardAdapter
import com.example.syra.service.MqttManager
import com.example.syra.utils.Constants.SWITCH_0_ON
import com.example.syra.utils.Constants.TOPIC
import io.github.cdimascio.dotenv.dotenv
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : ComponentActivity() {
    private lateinit var mqttManager: MqttManager
    private lateinit var cardAdapter: CardAdapter
    private lateinit var smartDevicesGridView: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.smartDevicesGridView = findViewById(R.id.smartDevicesGridView)

        this.cardAdapter = CardAdapter(this, listOf("Bedroom Device 1", "Bedroom Device 2"))
        this.smartDevicesGridView.adapter = this.cardAdapter

        val btnBedroom = findViewById<Button>(R.id.btnBedroom)
        val btnLivingRoom = findViewById<Button>(R.id.btnLivingRoom)
        val btnBathroom = findViewById<Button>(R.id.btnBathroom)

        btnBedroom.setOnClickListener {
            this.cardAdapter.updateDevices(listOf("Bedroom Device 1", "Bedroom Device 2"))
        }
        btnLivingRoom.setOnClickListener {
            this.cardAdapter.updateDevices(listOf("Living Room Device 1", "Living Room Device 2"))
        }
        btnBathroom.setOnClickListener {
            this.cardAdapter.updateDevices(listOf("Bathroom Device 1", "Bathroom Device 2"))
        }

        this.connectToMqttBroker()

        val btnMqtt = findViewById<Button>(R.id.btnMQTT)

        //{"id":123, "src":"user_1", "method":"Switch.Set", "params":{"id":1,"on":false}}
        btnMqtt.setOnClickListener{
            mqttManager.publishMessage(
                topic = TOPIC,
                command = SWITCH_0_ON,
                onSuccess = {
                    Toast.makeText(this, "Message published!", Toast.LENGTH_SHORT).show()
                },
                onError = { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun connectToMqttBroker(){
        val dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }

        mqttManager = MqttManager(
            context = this,
            brokerUrl = dotenv["BROKER_URL"],
            username = dotenv["MQTT_USERNAME"],
            password = dotenv["MQTT_PASSWORD"]
        )

        mqttManager.connect (
            callback = object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Connection lost!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    //Handle arriving messages
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Message sent!", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onSuccess = {
                Toast.makeText(this, "Connected to the broker", Toast.LENGTH_SHORT).show()
            },
            onError = { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        )
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
