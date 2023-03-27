package fr.isen.Cosson.androidsmartdevice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewParent
import fr.isen.Cosson.androidsmartdevice.databinding.ActivityScanBinding
import java.text.FieldPosition
import java.util.Scanner

class ScanAdapter (var devices: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)
    }

    override fun getItemCount(): Int = devices.size

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        TODO("holder.deviceName.text= devices(position)")
    }
    class ScanViewHolder(binding: ActivityScanBinding): RecyclerView.ViewHolder(binding.root){
        val deviceName = binding.deviceName
    }
}


