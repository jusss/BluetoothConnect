package com.example.bluetoothconnect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BluetoothCallback(val context: Context, val btHid: BluetoothHidDevice, val btAdapter: BluetoothAdapter, val TARGET_DEVICE_NAME: String): BluetoothHidDevice.Callback(){
    override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
        super.onAppStatusChanged(pluggedDevice, registered)
        if (pluggedDevice != null) { println(" --- plugged device is   ${pluggedDevice.name}") }
        if (registered) {
            var pairedDevices = btHid.getDevicesMatchingConnectionStates(intArrayOf(BluetoothProfile.STATE_CONNECTING, BluetoothProfile.STATE_CONNECTED, BluetoothProfile.STATE_DISCONNECTED, BluetoothProfile.STATE_DISCONNECTING))
            println( "--- paired devices are : ${pairedDevices.map { it.name }}")

            if (pairedDevices.isNotEmpty()){
                val state  = btHid.getConnectionState(pairedDevices?.get(0))
                println("--- first connection is ${pairedDevices.get(0).name}, state is ${when(state) {
                    BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
                    BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
                    BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
                    BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
                    else -> state.toString()
                }}")

                if (state == BluetoothProfile.STATE_CONNECTED){
                    btHid.disconnect(pairedDevices.get(0))
                }


            }


            btAdapter.bondedDevices.map {
                println(it.name)
                if (it.name == TARGET_DEVICE_NAME) {
                    if (!btHid.connectedDevices.contains(it)) btHid.connect(it)
                }
            }



//            if (btHid.getConnectionState(pluggedDevice) == BluetoothProfile.STATE_DISCONNECTED && pluggedDevice != null) {
//                println("--- connect plugged device ${pluggedDevice.name}")
//
//
//
//                btHid.connect(pluggedDevice)
//
//
//            } else if (pairedDevices.isNotEmpty()) {
//                if (btHid.getConnectionState(pairedDevices?.get(0)) == BluetoothProfile.STATE_DISCONNECTED) {
//                    println("--- the latest paired device ${pairedDevices.get(0).name} is disconnected! reconnecting...")
//
//
//                    btHid.connect(pairedDevices?.get(0))
//
//
//
//                }
//            }
        }
    }

    val keyboardReport = KeyboardReport2()
    override fun onConnectionStateChanged(device: BluetoothDevice?, state: Int) {
        super.onConnectionStateChanged(device, state)
        println("--- device connection is ${device?.name}, state is ${when(state) {
            BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
            BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
            BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
            BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
            else -> state.toString()
        }}")


        CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context,"${device?.name} is ${when(state) {
            BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
            BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
            BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
            BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
            else -> state.toString()
        }}", Toast.LENGTH_SHORT).show()
        }

        if (state == BluetoothProfile.STATE_CONNECTED){

            ConnectedDevice.device = device

//            if (btHid != null && device != null) {
//
//                keyboardReport.key1=6.toByte()
//                btHid.sendReport(device,KeyboardReport2.ID,keyboardReport.bytes)
//                keyboardReport.bytes.fill(0)
//                if (!btHid.sendReport(device, KeyboardReport2.ID, keyboardReport.bytes)) {
//                    Log.e(" ", "Report wasn't sent")
//                }
//
//
//                keyboardReport.key1=4.toByte()
//                btHid.sendReport(device,KeyboardReport2.ID,keyboardReport.bytes)
//                keyboardReport.bytes.fill(0)
//                if (!btHid.sendReport(device, KeyboardReport2.ID, keyboardReport.bytes)) {
//                    Log.e(" ", "Report wasn't sent")
//                }
//
//            }
        }

//        if (state == BluetoothProfile.STATE_DISCONNECTED){
//            btHid.unregisterApp()
//            ConnectedDevice.device = null
//        }
    }

}

