package com.example.bluetoothconnect

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHidDevice
import java.io.Serializable

//class IntentData(val bthid:BluetoothHidDevice, val btAdapter: BluetoothAdapter): Serializable {
//}

class IntentData {
    companion object {
        lateinit var bthid: BluetoothHidDevice
        lateinit var btAdapter: BluetoothAdapter
    }
}
