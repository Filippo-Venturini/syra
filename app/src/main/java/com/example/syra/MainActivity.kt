package com.example.syra

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Switch
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.syra.adapter.CardAdapter
import com.example.syra.model.Device
import com.example.syra.viewmodel.DeviceViewModel
import com.example.syra.repository.MqttRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        val btnKitchen = findViewById<Button>(R.id.btnKitchen)
        val btnStudy = findViewById<Button>(R.id.btnStudy)

        viewModel.currentDeviceList.observe(this) { devices ->
            cardAdapter.updateDevices(devices)
        }

        val buttons: List<Button> = listOf(btnBedroom, btnKitchen, btnLivingRoom, btnStudy)
        btnBedroom.isSelected = true

        buttons.forEach { currentBtn -> currentBtn.setOnClickListener{
            this.viewModel.loadRoomDevices(resources.getResourceEntryName(currentBtn.id))
            currentBtn.isSelected = true
            buttons.filter { otherBtn -> otherBtn.id != currentBtn.id}.forEach{ otherBtn -> otherBtn.isSelected = false }
        } }

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
        viewModel.publishSwitch1Off(device)
        viewModel.publishSwitch0On(device)
    }

    override fun onDownPressed(device: Device) {
        viewModel.publishSwitch0Off(device)
        viewModel.publishSwitch1On(device)
    }

    override fun onStopPressed(device: Device) {
        viewModel.publishSwitch0Off(device)
        viewModel.publishSwitch1Off(device)
    }
}
