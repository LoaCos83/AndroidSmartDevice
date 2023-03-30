package fr.isen.Cosson.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.Cosson.androidsmartdevice.databinding.ScanCellBinding

class ScanAdapter (var devices: ArrayList<BluetoothDevice>, var onDeviceClickListener: (BluetoothDevice) -> Unit) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)
    }

    override fun getItemCount(): Int = devices.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        //holder.deviceAddress.text = devices[position].address
        holder.deviceName.text = devices[position].name ?: "Inconnu"
        holder.itemView.setOnClickListener { onDeviceClickListener(devices[position]) }
    }
    class ScanViewHolder(binding: ScanCellBinding): RecyclerView.ViewHolder(binding.root){
        val deviceName : TextView = binding.ble
    }

    fun addDevice(device: BluetoothDevice){
        var shouldAddDevice = true
        devices.forEachIndexed { index, bluetoothDevice ->
            if(bluetoothDevice.address == device.address){
                devices[index] = device
                shouldAddDevice = false
            }

        }
        if(shouldAddDevice){
            devices.add(device)
        }
    }

}



