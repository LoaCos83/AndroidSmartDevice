package fr.isen.Cosson.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityDeviceBinding
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityScanBinding

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_device)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        bluetoothGatt?.connect()
    }
    @SuppressLint("MissingPermission")
    override fun onStop(){
        super.onStop()
        bluetoothGatt?.close()
    }

    private val bluetoothGattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if(newState == BluetoothProfile.STATE_CONNECTED) {
                runOnUiThread{
                    displayContentConnected()
                }
            }
        }
    }

    private fun displayContentConnected(){
        //avec tes variables
        binding.TPBLE.text = getString(R.string.device_led_text)
        binding.chargement.isVisible = false
        binding.led1.isVisible = true
    }
}