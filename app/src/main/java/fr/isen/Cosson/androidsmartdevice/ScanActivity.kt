package fr.isen.Cosson.androidsmartdevice

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityMainBinding
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val bluetoothAdapter: BluetoothAdapter? by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            if (permissions.all { it.value }) {
                scanBLEDevices()
            }
        }

    private var mScanning = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(bluetoothAdapter?.isEnabled == true){
            scanDeviceWithPermission()
            Toast.makeText(this,"bluetooth activer", Toast.LENGTH_LONG).show()
        }else{
            handleBLENotAvailable()
            Toast.makeText(this,"bluetooth pas activer", Toast.LENGTH_LONG).show()
        }

        binding.instructionToStart.setOnClickListener {
            togglePlayPauseAction()
        }


        binding.start2.setOnClickListener {
            togglePlayPauseAction()
            val progressBar = findViewById<ProgressBar>(R.id.loading)

            progressBar.setIndeterminate(true)
        }

        binding.scanList.layoutManager = LinearLayoutManager(this)
        binding.scanList.adapter = ScanAdapter(arrayListOf("Device 1", "Device 2", "Device 3", "Device 4", "Device 5"))


       // binding.instructionToStart.layoutManage = LinearLayoutManager(this)
       // binding.instructionToStart.layoutAdapter = LinearLayoutManager(this)

    }

    private fun handleBLENotAvailable() {
        binding.instructionToStart.text = getString(R.string.ble_scan_title_pause)
    }

    private fun scanDeviceWithPermission() {
        if(allPermissionGranted()){
            scanBLEDevices()
        }else {
            //request toutes les permissions
        }
    }

    private fun scanBLEDevices() {
        initToggleActions()
    }

    private fun initToggleActions() {
        //
    }

    private fun allPermissionGranted(): Boolean {
        val allPermissions = getAllPermission()
        return allPermissions.all {
            ContextCompat.checkSelfPermission( this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getAllPermission(): Array<String> {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        arrayOf(
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
         else{
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
         }
    }

    private fun togglePlayPauseAction(){
        mScanning = !mScanning
        if(mScanning){
            binding.instructionToStart.text = getString(R.string.ble_scan_title_play)
            binding.start2.setImageResource(R.drawable.pause)
            binding.loading.isVisible = true
        } else {
            binding.instructionToStart.text = getString(R.string.ble_scan_title_pause)
            binding.start2.setImageResource(R.drawable.start)
            binding.loading.isVisible = false
        }

    }

}


