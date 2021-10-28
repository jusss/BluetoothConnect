package com.example.bluetoothconnect

import android.bluetooth.*
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class KeyboardActivity : AppCompatActivity() {
    lateinit var bthid : BluetoothHidDevice
    val keyCode2KeyCode = KeyCode2KeyCode()
    val keyboardReport = KeyboardReport()
    val keyboardReport2 = KeyboardReport2()
    lateinit var btAdapter: BluetoothAdapter
    lateinit var TARGET_DEVICE_NAME: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)

        val intent = intent
        val name = intent.getSerializableExtra("name") as ArrayList<String>?
        TARGET_DEVICE_NAME = name!![0]
        val screenSize = name[1]

        // use BluetoothProfile.ServiceListener.onServiceConneted to get BluetoothHidDevice proxy object, no matter if bluetooth is connected or not
        //BluetoothHidDevice.Callback can listen bluetooth connection state change, pass it to BluetoothHidDevice.registerApp
        // after registerApp, once bluetooth connection is changed, callback will be called,
        // get BluetoothHidDevice from BluetoothProfile.ServiceListener, get BluetoothDevice from registerApp
        // send message need BluetoothHidDevice and BluetoothDevice with sendReport
        // registerApp contain sdp_record, there's keyboard report id in sdp_record's last parameter descriptor

        btAdapter = BluetoothAdapter.getDefaultAdapter()

        btAdapter.getProfileProxy(this,
                object : BluetoothProfile.ServiceListener{
                    override fun onServiceDisconnected(profile: Int) {
                        println("--- Disconnect, profile is $profile")
                    }
                    override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                        println("--- connected, profile is $profile")
                        if (profile == BluetoothProfile.HID_DEVICE) {
                            // get bthid
                            bthid = proxy as BluetoothHidDevice
                            println("--- got hid proxy object ")
                            val btcallback = BluetoothCallback(this@KeyboardActivity,bthid, btAdapter,TARGET_DEVICE_NAME)
                            bthid.registerApp(sdpRecord, null, qosOut, {it.run()}, btcallback)
//                            bthid.registerApp(
//                                    Constants.SDP_RECORD, null, Constants.QOS_OUT, Executor { obj: Runnable -> obj.run() }, btcallback
//                            )
                        }
                    }
                }
                , BluetoothProfile.HID_DEVICE)

        fun sendKeyUp(){
            keyboardReport2.bytes.fill(0)
            if (!bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes)) {
                Log.e(" ", "Report wasn't sent")
            }
        }

        fun sendKey(keyCode: Int){
            var key = 0
            if (keyCode2KeyCode.regularPhysicsKey.containsKey(keyCode)){
                key = keyCode2KeyCode.keyMap.getOrDefault(keyCode2KeyCode.regularPhysicsKey.getOrDefault(keyCode,','),0)
            }
            if (keyCode2KeyCode.specialPhysicsKey.containsKey(keyCode)){
                key = keyCode2KeyCode.scancode.getOrDefault(keyCode2KeyCode.specialPhysicsKey.getOrDefault(keyCode,"Space"),0)
            }
            keyboardReport2.key1=key.toByte()
            bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
            )

            sendKeyUp()
        }

        fun sendModifierKey(keyCode:Int, modifierKey: String){
            if (modifierKey == "Shift") keyboardReport2.leftShift = true
            if (modifierKey == "Ctrl") keyboardReport2.leftControl = true
            if (modifierKey == "Alt") keyboardReport2.leftAlt = true
            if (modifierKey == "Window") keyboardReport2.leftGui = true
            var key = 0
            if (keyCode2KeyCode.regularPhysicsKey.containsKey(keyCode)){
                key = keyCode2KeyCode.keyMap.getOrDefault(keyCode2KeyCode.regularPhysicsKey.getOrDefault(keyCode,','),0)
            }
            if (keyCode2KeyCode.specialPhysicsKey.containsKey(keyCode)){
                key = keyCode2KeyCode.scancode.getOrDefault(keyCode2KeyCode.specialPhysicsKey.getOrDefault(keyCode,"Space"),0)
            }
            keyboardReport2.key1=key.toByte()
            bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
            )

            sendKeyUp()
        }

        fun keyboardReportSendKey(keyCode:Int, modifierKey: String){
            var key = 0
            if (keyCode2KeyCode.regularPhysicsKey.containsKey(keyCode)){
                key = keyCode2KeyCode.keyMap.getOrDefault(keyCode2KeyCode.regularPhysicsKey.getOrDefault(keyCode,','),0)
            }
            if (keyCode2KeyCode.specialPhysicsKey.containsKey(keyCode)){
                key = keyCode2KeyCode.scancode.getOrDefault(keyCode2KeyCode.specialPhysicsKey.getOrDefault(keyCode,"Space"),0)
            }

            var modifier = 0
            if (modifierKey == "Ctrl") modifier = 1
            if (modifierKey == "Shift") modifier = 2
            if (modifierKey == "Alt") modifier = 4
            if (modifierKey == "Window") modifier = 8
            val data = keyboardReport.setValue(modifier,key,0,0,0,0,0)
            bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,data
            )
            sendKeyUp()
        }

        var latestSentTime = System.currentTimeMillis()
        val editTextView = findViewById<EditText>(R.id.edit_text)
        editTextView?.requestFocus()

        editTextView?.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent? ->

            if (event!!.isShiftPressed()){
                val pressedTime = System.currentTimeMillis()
                if ((pressedTime - latestSentTime) > 160L && (keyCode != 59)) {
                    // shift is 59, press shift ? will send shift then shift ?, so block single shift
                    // shift space, it will send twice key event, reduce one
                    sendModifierKey(keyCode,"Shift")
//                    keyboardReportSendKey(keyCode,"Shift")
//                    println("keycode is $keyCode, with Shift ")
                    latestSentTime = pressedTime
                }
                return@setOnKeyListener true
            }

            if (event!!.isCtrlPressed()){
                val pressedTime = System.currentTimeMillis()
                if ((pressedTime - latestSentTime) > 160L   &&   (keyCode != 113)) {
                    // shift space, it will send twice key event, reduce one

                    keyboardReportSendKey(keyCode,"Ctrl")
//                    println("keycode is $keyCode, with Ctrl ")
                    latestSentTime = pressedTime
                }
                return@setOnKeyListener true
            }

            if (event!!.isAltPressed()){
                val pressedTime = System.currentTimeMillis()
                if ((pressedTime - latestSentTime) > 160L   &&   (keyCode != 57)) {
                    // shift space, it will send twice key event, reduce one
                    keyboardReportSendKey(keyCode,"Alt")
//                    println("keycode is $keyCode, with Alt ")
                    latestSentTime = pressedTime
                }
                return@setOnKeyListener true
            }

            // windows key is search key in android
            if (event!!.isMetaPressed()){
                val pressedTime = System.currentTimeMillis()
                if ((pressedTime - latestSentTime) > 160L) {
                    // shift space, it will send twice key event, reduce one
                    keyboardReportSendKey(keyCode,"Window")
                    println("keycode is $keyCode, with Window ")
                    latestSentTime = pressedTime
                }
                return@setOnKeyListener true
            }

            if (event?.action == KeyEvent.ACTION_DOWN) {
               if (bthid.connectedDevices != null){
                   sendKey(keyCode)
//                   println("--- keycode is $keyCode")
               }
                else {
                   btAdapter.getProfileProxy(this,
                       object : BluetoothProfile.ServiceListener{
                           override fun onServiceDisconnected(profile: Int) {
                               println("--- Disconnect, profile is $profile")
                           }
                           override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                               println("--- connected, profile is $profile")
                               if (profile == BluetoothProfile.HID_DEVICE) {
                                   // get bthid
                                   bthid = proxy as BluetoothHidDevice
                                   println("--- got hid proxy object ")
                                   val btcallback = BluetoothCallback(this@KeyboardActivity,bthid, btAdapter,TARGET_DEVICE_NAME)
                                   bthid.registerApp(sdpRecord, null, qosOut, {it.run()}, btcallback)
//                            bthid.registerApp(
//                                    Constants.SDP_RECORD, null, Constants.QOS_OUT, Executor { obj: Runnable -> obj.run() }, btcallback
//                            )
                               }
                           }
                       }
                       , BluetoothProfile.HID_DEVICE)

               }
            }
            return@setOnKeyListener false
        }





//        findViewById<Button>(R.id.button1).setOnClickListener{

            //KeyboardReport2 is faster than KeyboardReport

//            val keyboardReport = KeyboardReport()
//            val data = keyboardReport.setValue(0,4,0,0,0,0,0)
//            bthid.sendReport(
//                    ConnectedDevice.device,KeyboardReport2.ID,data
//            )
//            val data2 = keyboardReport.setValue(0,0,0,0,0,0,0)
//            if (!bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, data2)) {
//                Log.e(" ", "Report wasn't sent")
//            }
//        }

    }

    override fun onResume() {
        super.onResume()

        btAdapter.getProfileProxy(this,
            object : BluetoothProfile.ServiceListener{
                override fun onServiceDisconnected(profile: Int) {
                    println("--- Disconnect, profile is $profile")
                }
                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                    println("--- connected, profile is $profile")
                    if (profile == BluetoothProfile.HID_DEVICE) {
                        // get bthid
                        bthid = proxy as BluetoothHidDevice
                        println("--- got hid proxy object ")
                        val btcallback = BluetoothCallback(this@KeyboardActivity,bthid, btAdapter,TARGET_DEVICE_NAME)
                        bthid.registerApp(sdpRecord, null, qosOut, {it.run()}, btcallback)
//                            bthid.registerApp(
//                                    Constants.SDP_RECORD, null, Constants.QOS_OUT, Executor { obj: Runnable -> obj.run() }, btcallback
//                            )
                    }
                }
            }
            , BluetoothProfile.HID_DEVICE)
    }

    private val sdpRecord by lazy {
        BluetoothHidDeviceAppSdpSettings(
            "Pixel HID1",
            "Mobile BController",
            "bla",
            BluetoothHidDevice.SUBCLASS1_COMBO,
                DescriptorCollection.MOUSE_KEYBOARD_COMBO
        )
    }

    private val qosOut by lazy {
        BluetoothHidDeviceAppQosSettings(
                BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
                800,
                9,
                0,
                11250,
                BluetoothHidDeviceAppQosSettings.MAX
        )
    }
}
