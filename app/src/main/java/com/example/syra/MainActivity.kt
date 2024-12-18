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
import com.example.syra.model.Device
import com.example.syra.viewmodel.DeviceViewModel
import com.example.syra.repository.MqttRepository
import com.example.syra.utils.Constants.BROKER_URL
import com.example.syra.utils.Constants.MQTT_PASSWORD
import com.example.syra.utils.Constants.MQTT_USERNAME
import com.example.syra.utils.Constants.SWITCH_0_OFF
import com.example.syra.utils.Constants.SWITCH_0_ON
import com.example.syra.utils.Constants.TOPIC
import io.github.cdimascio.dotenv.dotenv
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : ComponentActivity(), DeviceActionListener{
    private val mqttRepository: MqttRepository = MqttRepository()
    private lateinit var cardAdapter: CardAdapter
    private lateinit var smartDevicesGridView: GridView
    private lateinit var viewModel: DeviceViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]

        this.smartDevicesGridView = findViewById(R.id.smartDevicesGridView)

        this.cardAdapter = CardAdapter(this, listOf(), this)
        this.smartDevicesGridView.adapter = this.cardAdapter

        val btnBedroom = findViewById<Button>(R.id.btnBedroom)
        val btnLivingRoom = findViewById<Button>(R.id.btnLivingRoom)
        val btnBathroom = findViewById<Button>(R.id.btnBathroom)

        viewModel.currentDeviceList.observe(this) { devices ->
            cardAdapter.updateDevices(devices)
        }

        btnBedroom.setOnClickListener {
            this.viewModel.loadBedroomDevices()
        }
        btnLivingRoom.setOnClickListener {
            this.viewModel.loadLivingRoomDevices()
        }
        btnBathroom.setOnClickListener {
            //this.cardAdapter.updateDevices(listOf("Bathroom Device 1", "Bathroom Device 2"))
        }

        viewModel.connectToMqttBroker()
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttRepository.disconnect(
            onSuccess = {
                Toast.makeText(this, "Disconnected from the broker!", Toast.LENGTH_SHORT).show()
            },
            onError = { e ->
                Toast.makeText(this, "Disconnection error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    override fun onUpPressed(device: Device) {
        viewModel.publishSwitch0On(device)
    }

    override fun onUpReleased(device: Device) {
        viewModel.publishSwitch0Off(device)
    }

    override fun onDownPressed(device: Device) {
        viewModel.publishSwitch1On(device)
    }

    override fun onDownReleased(device: Device) {
        viewModel.publishSwitch1Off(device)
    }
}
