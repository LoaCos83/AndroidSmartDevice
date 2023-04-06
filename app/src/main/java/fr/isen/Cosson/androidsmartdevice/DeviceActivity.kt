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
import androidx.core.view.isVisible
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityDeviceBinding
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityScanBinding
import java.util.*

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    private val serviceUUID = UUID.fromString("0000feed-cc7a-482a-984a-7f2ed5b3e58f")
    private val characteristicLedUUID = UUID.fromString("0000abcd-8e22-4541-9d4c-21edae82ed19")
    private val characteristicButtonUUID = UUID.fromString("00001234-8e22-4541-9d4c-21edae82ed19")
    private var cptClick = 0
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")

        bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
       // bluetoothGatt?.connect()

        clickOnLed()
        Recup()
    }
    @SuppressLint("MissingPermission")
    override fun onStop(){
        super.onStop()
        bluetoothGatt?.close()
    }

    private fun Recup(){
        val characteristicButton = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicButtonUUID)
        cptClick = characteristicButton?.value?.get(0)?.toInt() ?: 0
        binding.cptClick.text = "Nombre de click : $cptClick"
    }

    @SuppressLint("MissingPermission")
    private fun clickOnLed(){
        binding.image1.setOnClickListener{

            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.image1.imageTintList == getColorStateList(R.color.teal_200)){
                binding.image1.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else{
                binding.image1.imageTintList = getColorStateList(R.color.teal_200)
                characteristic?.value = byteArrayOf(0x01)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
        binding.image2.setOnClickListener{
            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.image2.imageTintList == getColorStateList(R.color.teal_200)){
                binding.image2.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else{
                binding.image2.imageTintList = getColorStateList(R.color.teal_200)
                characteristic?.value = byteArrayOf(0x02)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
        binding.image3.setOnClickListener{
            val characteristic = bluetoothGatt?.getService(serviceUUID)?.getCharacteristic(characteristicLedUUID)

            if(binding.image3.imageTintList == getColorStateList(R.color.teal_200)){
                binding.image3.imageTintList = getColorStateList(R.color.black)
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else{
                binding.image3.imageTintList = getColorStateList(R.color.teal_200)
                characteristic?.value = byteArrayOf(0x03)
                bluetoothGatt?.writeCharacteristic(characteristic)
            }
        }
    }

    private val bluetoothGattCallback = object: BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if(newState == BluetoothProfile.STATE_CONNECTED) {
                runOnUiThread{
                    displayContentConnected()
                }
                bluetoothGatt?.discoverServices()
            }
        }
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            if (characteristic?.uuid == characteristicButtonUUID) {
                val value = characteristic?.value?.get(0)?.toInt() ?: 0
                // Mettre à jour le compteur de clics
                cptClick += value
                runOnUiThread {
                    binding.cptClick.text = "Nombre de clics: $cptClick"
                }
            }
        }

    }

    private fun displayContentConnected(){
        binding.tpble.text = "TPBLE"
        binding.textLed.text = "Affichage des LEDs"
        binding.abonneText.isVisible = true
        binding.checkBox.isVisible = true
        binding.progressBar.isVisible = false
        binding.image1.isVisible = true
        binding.image2.isVisible = true
        binding.image3.isVisible = true
        binding.cptClick.isVisible = true
    }
}