package fr.isen.Cosson.androidsmartdevice

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityMainBinding
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val bluetoothAdapter: BluetoothAdapter? by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }


    private val REQUEST_PERMISSIONS_CODE = 1234


    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            if (permissions.all { it.value }) {
                scanBLEDevices()
            }
        }

    private var mScanning = false

   // private val bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: ScanAdapter



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

    }

    @SuppressLint("MissingPermission")
    override fun onStop(){
        super.onStop()
        if(bluetoothAdapter?.isEnabled == true && allPermissionGranted()){
            mScanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }
    private fun initToggleActions() {
        binding.instructionToStart.setOnClickListener {
            scanBLEDevices()
        }

        binding.start2.setOnClickListener {
            scanBLEDevices()
        }

        binding.scanList.layoutManager = LinearLayoutManager(this)
        adapter = ScanAdapter(arrayListOf()){
            val intent = Intent(this, DeviceActivity::class.java)
            intent.putExtra("device", it)
            startActivity(intent)
        }
        binding.scanList.adapter = adapter
    }

    private fun handleBLENotAvailable() {
        binding.instructionToStart.text = getString(R.string.ble_scan_title_pause)
    }

    private fun scanDeviceWithPermission() {
        if(allPermissionGranted()){
            initToggleActions()
            scanBLEDevices()
        }else {
            requestPermissionLauncher.launch(getAllPermission())
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanBLEDevices() {
        if (!mScanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mScanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
                togglePlayPauseAction()
            }, SCAN_PERIOD)
            mScanning = true
            bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            mScanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
        togglePlayPauseAction()
    }


    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d("Scan","result: $result")
            adapter.addDevice(result.device)
            adapter.notifyDataSetChanged()
        }
    }


    private fun allPermissionGranted(): Boolean {
        val allPermissions = getAllPermission()
        return allPermissions.all { permission ->
            ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED
        } || requestPermissions(allPermissions)
        //true
    }
    private fun requestPermissions(permissions: Array<String>): Boolean {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE)
        return false
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
    companion object{
        private val SCAN_PERIOD: Long = 10000
    }

}
