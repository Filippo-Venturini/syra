package com.example.syra

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.syra.adapter.CardAdapter
import com.example.syra.model.DeviceViewModel
import com.example.syra.service.MqttManager
import com.example.syra.utils.Constants.SWITCH_0_OFF
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
    private lateinit var viewModel: DeviceViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]

        this.smartDevicesGridView = findViewById(R.id.smartDevicesGridView)

        this.cardAdapter = CardAdapter(this, listOf())
        this.smartDevicesGridView.adapter = this.cardAdapter

        val btnBedroom = findViewById<Button>(R.id.btnBedroom)
        val btnLivingRoom = findViewById<Button>(R.id.btnLivingRoom)
        val btnBathroom = findViewById<Button>(R.id.btnBathroom)

        viewModel.currentDeviceList.observe(this, { devices ->
            cardAdapter.updateDevices(devices)
        })

        btnBedroom.setOnClickListener {
            this.viewModel.loadBedroomDevices()
        }
        btnLivingRoom.setOnClickListener {
            this.viewModel.loadLivingRoomDevices()
        }
        btnBathroom.setOnClickListener {
            //this.cardAdapter.updateDevices(listOf("Bathroom Device 1", "Bathroom Device 2"))
        }

        this.connectToMqttBroker()

        val btnMqtt = findViewById<Button>(R.id.btnMQTT)

        //{"id":123, "src":"user_1", "method":"Switch.Set", "params":{"id":1,"on":false}}
        btnMqtt.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mqttManager.publishMessage(
                        topic = TOPIC,
                        command = SWITCH_0_ON,
                        onSuccess = {
                            Toast.makeText(this, "Message sent on press!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { e ->
                            Toast.makeText(this, "Error on press: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                    true
                }

                MotionEvent.ACTION_UP -> {
                    mqttManager.publishMessage(
                        topic = TOPIC,
                        command = SWITCH_0_OFF,
                        onSuccess = {
                            Toast.makeText(this, "Message sent on release!", Toast.LENGTH_SHORT).show()
                        },
                        onError = { e ->
                            Toast.makeText(this, "Error on release: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                    true
                }

                else -> false
            }
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

                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
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
