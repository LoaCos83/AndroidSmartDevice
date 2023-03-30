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

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    private var cptClick=0
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
       // val bluetoothDevice: BluetoothDevice? =
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        bluetoothGatt?.connect()

        clickOnLed()
    }
    @SuppressLint("MissingPermission")
    override fun onStop(){
        super.onStop()
        bluetoothGatt?.close()
    }

    private fun clickOnLed(){
        binding.image1.setOnClickListener{
            if(binding.image1.imageTintList == getColorStateList(R.color.teal_200)){
                binding.image1.imageTintList = getColorStateList(R.color.black)
            } else{
                binding.image1.imageTintList = getColorStateList(R.color.teal_200)
                cptClick ++
                binding.cptClick.text="Nombre de click : $cptClick"
            }
        }
        binding.image2.setOnClickListener{
            if(binding.image2.imageTintList == getColorStateList(R.color.teal_200)){
                binding.image2.imageTintList = getColorStateList(R.color.black)
            } else{
                binding.image2.imageTintList = getColorStateList(R.color.teal_200)
                cptClick ++
                binding.cptClick.text="Nombre de click : $cptClick"
            }
        }
        binding.image3.setOnClickListener{
            if(binding.image3.imageTintList == getColorStateList(R.color.teal_200)){
                binding.image3.imageTintList = getColorStateList(R.color.black)
            } else{
                binding.image3.imageTintList = getColorStateList(R.color.teal_200)
                cptClick ++
                binding.cptClick.text="Nombre de click : $cptClick"
            }
        }
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