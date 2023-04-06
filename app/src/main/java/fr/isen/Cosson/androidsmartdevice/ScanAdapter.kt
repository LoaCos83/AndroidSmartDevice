package fr.isen.Cosson.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.Cosson.androidsmartdevice.databinding.ScanCellBinding

class ScanAdapter(var devices: ArrayList<BluetoothDevice>, var onDeviceClickListener: (BluetoothDevice) -> Unit) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {
    private var rssiValues = arrayListOf<Int>() // <-- Créer la liste rssiValues ici
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)
    }

    override fun getItemCount(): Int = devices.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.deviceAddress.text = devices[position].address
        holder.RSSI.text = rssiValues[position].toString() // <-- Accéder à la valeur RSSI correspondante dans la liste rssiValues
        holder.deviceName.text = devices[position].name ?: "Inconnu"
        holder.itemView.setOnClickListener { onDeviceClickListener(devices[position]) }
    }

    class ScanViewHolder(binding: ScanCellBinding): RecyclerView.ViewHolder(binding.root){
        val deviceName : TextView = binding.ble
        val deviceAddress : TextView = binding.add
        val RSSI : TextView = binding.RSSI
    }

    fun addDevice(device: BluetoothDevice, rssi: Int) {
        val deviceIndex = devices.indexOfFirst { it.address == device.address }
        if (deviceIndex != -1) {
            devices[deviceIndex] = device
            rssiValues[deviceIndex] = rssi // <-- Ajouter la valeur RSSI correspondante à la liste rssiValues
        } else {
            devices.add(device)
            rssiValues.add(rssi) // <-- Ajouter la valeur RSSI correspondante à la liste rssiValues
        }
    }

}



