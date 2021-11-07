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

class BluetoothCallback(val context: Context, val bthid: BluetoothHidDevice, val btAdapter: BluetoothAdapter, val TARGET_DEVICE_NAME: String): BluetoothHidDevice.Callback(){
    override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
        super.onAppStatusChanged(pluggedDevice, registered)
        if (pluggedDevice != null) { println(" --- plugged device is   ${pluggedDevice.name}") }
        if (registered) {
            var pairedDevices = bthid.getDevicesMatchingConnectionStates(intArrayOf(BluetoothProfile.STATE_CONNECTING, BluetoothProfile.STATE_CONNECTED, BluetoothProfile.STATE_DISCONNECTED, BluetoothProfile.STATE_DISCONNECTING))
            // paired device may only contain one, it is not like bonded device
            println( "--- paired devices are : ${pairedDevices.map { it.name }}")

//            if (pairedDevices.isNotEmpty()){
//                val state  = bthid.getConnectionState(pairedDevices?.get(0))
//                println("--- first connection is ${pairedDevices.get(0).name}, state is ${when(state) {
//                    BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
//                    BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
//                    BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
//                    BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
//                    else -> state.toString()
//                }}")
//
//                if (state == BluetoothProfile.STATE_CONNECTED){
//                    bthid.disconnect(pairedDevices.get(0))
//                }
//            }

            pairedDevices.map {
                val state = bthid.getConnectionState(it)
                println("--- paired device ${it.name} is ${when(state) {
                    BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
                    BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
                    BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
                    BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
                    else -> state.toString()
                }}")
//                if (state == BluetoothProfile.STATE_CONNECTED && it.name != TARGET_DEVICE_NAME) bthid.disconnect(it)
            }

//            btAdapter.bondedDevices.map {
//                println("--- bonded device is ${it.name}")
//                if (it.name == TARGET_DEVICE_NAME) {
//                    if (!bthid.connectedDevices.contains(it)) btHid.connect(it)
//                }
//            }
            bthid.connectedDevices.map {
                println("--- connected device ${it.name} will be disconnected")
                bthid.disconnect(it)
            }

            btAdapter.bondedDevices.map {
                if (it.name == TARGET_DEVICE_NAME) {
                    println("--- connect to target $TARGET_DEVICE_NAME")
                    bthid.connect(it)
                }
            }



//            if (bthid.getConnectionState(pluggedDevice) == BluetoothProfile.STATE_DISCONNECTED && pluggedDevice != null) {
//                println("--- connect plugged device ${pluggedDevice.name}")
//
//
//
//                bthid.connect(pluggedDevice)
//
//
//            } else if (pairedDevices.isNotEmpty()) {
//                if (bthid.getConnectionState(pairedDevices?.get(0)) == BluetoothProfile.STATE_DISCONNECTED) {
//                    println("--- the latest paired device ${pairedDevices.get(0).name} is disconnected! reconnecting...")
//
//
//                    bthid.connect(pairedDevices?.get(0))
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

//            if (bthid != null && device != null) {
//
//                keyboardReport.key1=6.toByte()
//                bthid.sendReport(device,KeyboardReport2.ID,keyboardReport.bytes)
//                keyboardReport.bytes.fill(0)
//                if (!bthid.sendReport(device, KeyboardReport2.ID, keyboardReport.bytes)) {
//                    Log.e(" ", "Report wasn't sent")
//                }
//
//
//                keyboardReport.key1=4.toByte()
//                bthid.sendReport(device,KeyboardReport2.ID,keyboardReport.bytes)
//                keyboardReport.bytes.fill(0)
//                if (!bthid.sendReport(device, KeyboardReport2.ID, keyboardReport.bytes)) {
//                    Log.e(" ", "Report wasn't sent")
//                }
//
//            }
        }

//        if (state == BluetoothProfile.STATE_DISCONNECTED){
//            bthid.unregisterApp()
//            ConnectedDevice.device = null
//        }
    }

    val featureReport = FeatureReport()
    val keyboardReport2 = KeyboardReport2()

    override fun onSetReport(device: BluetoothDevice?, type: Byte, id: Byte, data: ByteArray?) {
        super.onSetReport(device, type, id, data)
        Log.i("setreport","this $device and $type and $id and $data")

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context,"onSetReport", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onGetReport(device: BluetoothDevice?, type: Byte, id: Byte, bufferSize: Int) {
        super.onGetReport(device, type, id, bufferSize)

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context,"onGetReport", Toast.LENGTH_SHORT).show()
        }

//        if (type == BluetoothHidDevice.REPORT_TYPE_FEATURE) {
//            featureReport.wheelResolutionMultiplier = true
//            featureReport.acPanResolutionMultiplier = true
//            Log.i("getbthid","$bthid")
//
//            var wasrs=bthid?.replyReport(device, type, FeatureReport.ID, featureReport.bytes)
//            Log.i("replysuccess flag ",wasrs.toString())
//        }

        if (type == BluetoothHidDevice.REPORT_TYPE_INPUT){
            bthid?.replyReport(device,type,id,keyboardReport2.bytes)
        }

    }






}

