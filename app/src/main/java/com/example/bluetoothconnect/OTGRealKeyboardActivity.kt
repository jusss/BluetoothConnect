package com.example.bluetoothconnect

import android.bluetooth.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OTGRealKeyboardActivity : AppCompatActivity() {
    lateinit var bthid : BluetoothHidDevice
    val keyCode2KeyCode = KeyCode2KeyCode()
    val keyboardReport = KeyboardReport()
    val keyboardReport2 = KeyboardReport2()
    lateinit var btAdapter: BluetoothAdapter
    lateinit var TARGET_DEVICE_NAME: String
    var isCapsLockPressed = false
    var isAltPressed = 0
    var isCtrlPressed = false
    var isShiftPressed = 0
    var isShiftPressedWithOthers = false
    var isWindowPressed = false
    var isWindowPressedWithOthers = false
    var isAltRepeat = false
    var latestSentTime = System.currentTimeMillis()
    lateinit var imm: InputMethodManager
    var paused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otgkeyboard)

        val intent = intent
        val name = intent.getSerializableExtra("name") as ArrayList<String>?
        TARGET_DEVICE_NAME = name!![0]
        val screenSize = name[1]
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        val ll = findViewById<LinearLayout>(R.id.choose_target)
        val buttons = ArrayList<Button>()


        btAdapter.bondedDevices.map {btd ->
            buttons.add(Button(this))
            buttons.get(buttons.size - 1).setText(btd.name)
            buttons.get(buttons.size - 1).setAllCaps(false)
            buttons.get(buttons.size - 1).setFocusable(false)

            buttons.get(buttons.size - 1).setOnClickListener {

                bthid.connectedDevices.map {
                    println("--- connected device ${it.name} will be disconnected")
                    bthid.disconnect(it)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    delay(600)
//                    if (bthid.connectedDevices.isEmpty()){
                        println("--- connect to target ${btd.name}")
                        bthid.connect(btd)
//                    }
                }

                TARGET_DEVICE_NAME = btd.name
            }
            ll.addView(buttons.get(buttons.size - 1))
        }

        // use BluetoothProfile.ServiceListener.onServiceConneted to get BluetoothHidDevice proxy object, no matter if bluetooth is connected or not
        //BluetoothHidDevice.Callback can listen bluetooth connection state change, pass it to BluetoothHidDevice.registerApp
        // after registerApp, once bluetooth connection is changed, callback will be called,
        // get BluetoothHidDevice from BluetoothProfile.ServiceListener, get BluetoothDevice from registerApp
        // send message need BluetoothHidDevice and BluetoothDevice with sendReport
        // registerApp contain sdp_record, there's keyboard report id in sdp_record's last parameter descriptor

        btAdapter.getProfileProxy(this,
                object : BluetoothProfile.ServiceListener{
                    override fun onServiceDisconnected(profile: Int) {
                        println("--- Disconnect, profile is $profile")
                    }
                    override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                        println("--- connected, profile is $profile")
                        if (profile != BluetoothProfile.HID_DEVICE) {
                            return
                        }
                        // get bthid
                        bthid = proxy as BluetoothHidDevice
                        println("--- got hid proxy object ")
                        val btcallback = BluetoothCallback(this@OTGRealKeyboardActivity,bthid, btAdapter,TARGET_DEVICE_NAME)
                        bthid.registerApp(sdpRecord, null, qosOut, {it.run()}, btcallback)
//                            bthid.registerApp(
//                                    Constants.SDP_RECORD, null, Constants.QOS_OUT, Executor { obj: Runnable -> obj.run() }, btcallback
//                            )
                    }
                }
                , BluetoothProfile.HID_DEVICE)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0)


        findViewById<Button>(R.id.window).setOnClickListener {
            if (isWindowPressed) sendKeyUp(KeyEvent.KEYCODE_WINDOW)
            else {
                sendKeyDown(KeyEvent.KEYCODE_WINDOW)
                isWindowPressed = !isWindowPressed
            }
        }

        findViewById<Button>(R.id.tab).setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_TAB)
            sendKeyUp(KeyEvent.KEYCODE_TAB)
        }

        findViewById<Button>(R.id.keyboard).setOnClickListener {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        }
    }


    fun sendAllKeyUp(){
        keyboardReport2.bytes.fill(0)
        if (!bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes)) {
            Log.e(" ", "Report wasn't sent")
        }
    }

    fun sendKeyUp(keyCode: Int){
        var key = KeyboardReport2.KeyEventMap.get(keyCode) ?: 0
        // scan code is press code, press code + 0x80 will be release code
        key = key + 0x80
        keyboardReport2.key1=key.toByte()
        bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
        )
        keyboardReport2.key1=0.toByte()
    }

    fun sendKeyDown(keyCode: Int){
        val key = KeyboardReport2.KeyEventMap.get(keyCode) ?: 0
        keyboardReport2.key1=key.toByte()
        bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
        )
    }

    fun sendModifierKeyDown(keyCode:Int) {
        when (keyCode) {
            KeyEvent.KEYCODE_ALT_LEFT -> keyboardReport2.leftAlt = true
            KeyEvent.KEYCODE_ALT_RIGHT -> keyboardReport2.rightAlt = true
            KeyEvent.KEYCODE_CTRL_LEFT -> keyboardReport2.leftControl = true
            KeyEvent.KEYCODE_CTRL_RIGHT -> keyboardReport2.rightControl = true
            KeyEvent.KEYCODE_SHIFT_LEFT -> keyboardReport2.leftShift = true
            KeyEvent.KEYCODE_SHIFT_RIGHT -> keyboardReport2.rightShift = true
            KeyEvent.KEYCODE_WINDOW -> keyboardReport2.leftGui = true
            KeyEvent.KEYCODE_CAPS_LOCK -> keyboardReport2.leftControl = true
            else -> println("invalid modifier")
        }
        keyboardReport2.key1 = 0.toByte()
        bthid.sendReport(
                        ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes
        )
    }

    fun sendModifierKeyUp(keyCode:Int){
        when (keyCode) {
            KeyEvent.KEYCODE_ALT_LEFT -> keyboardReport2.leftAlt = false
            KeyEvent.KEYCODE_ALT_RIGHT -> keyboardReport2.rightAlt = false
            KeyEvent.KEYCODE_CTRL_LEFT -> keyboardReport2.leftControl = false
            KeyEvent.KEYCODE_CTRL_RIGHT -> keyboardReport2.rightControl = false
            KeyEvent.KEYCODE_SHIFT_LEFT -> keyboardReport2.leftShift = false
            KeyEvent.KEYCODE_SHIFT_RIGHT -> keyboardReport2.rightShift = false
            KeyEvent.KEYCODE_WINDOW -> keyboardReport2.leftGui = false
            KeyEvent.KEYCODE_CAPS_LOCK -> keyboardReport2.leftControl = false
            else -> println("invalid modifier")
        }
        keyboardReport2.key1=0.toByte()
        bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
        )
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        println("--- onKeyUp keycode is $keyCode")
        if (listOf<Int>(KeyEvent.KEYCODE_CAPS_LOCK, KeyEvent.KEYCODE_ALT_LEFT, KeyEvent.KEYCODE_ALT_RIGHT,KeyEvent.KEYCODE_WINDOW,
                        KeyEvent.KEYCODE_CTRL_LEFT,KeyEvent.KEYCODE_CTRL_RIGHT,KeyEvent.KEYCODE_SHIFT_LEFT,
                        KeyEvent.KEYCODE_SHIFT_RIGHT).contains(keyCode)) {
            sendModifierKeyUp(keyCode)
            when (keyCode){
                KeyEvent.KEYCODE_SHIFT_LEFT -> isShiftPressed = 0
                KeyEvent.KEYCODE_SHIFT_RIGHT -> isShiftPressed = 0
                KeyEvent.KEYCODE_ALT_LEFT -> {println("alt left is up"); isAltPressed=0;sendAllKeyUp()}
                KeyEvent.KEYCODE_ALT_RIGHT -> {println("alt right is up"); isAltPressed=0;sendAllKeyUp()}
//                KeyEvent.KEYCODE_ALT_LEFT -> sendAllKeyUp()
//                KeyEvent.KEYCODE_ALT_RIGHT -> sendAllKeyUp()
//                else -> sendAllKeyUp()
            }
        }
        else{
            when (keyCode){
                KeyEvent.KEYCODE_TAB -> sendKeyUp(keyCode)
                KeyEvent.KEYCODE_SPACE -> {if (isAltPressed == 0) sendKeyUp(keyCode) else sendKeyUp(KeyEvent.KEYCODE_TAB)}
                else -> {
                        sendAllKeyUp()
                        when (isShiftPressed) {
                            1 -> sendModifierKeyDown(KeyEvent.KEYCODE_SHIFT_LEFT)
                            2 -> sendModifierKeyDown(KeyEvent.KEYCODE_SHIFT_RIGHT)
                        }
                }
            }
        }
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (bthid.connectedDevices != null){
            println("--- onKeyDown keycode is $keyCode")
            if (listOf<Int>(KeyEvent.KEYCODE_CAPS_LOCK, KeyEvent.KEYCODE_ALT_LEFT, KeyEvent.KEYCODE_ALT_RIGHT,KeyEvent.KEYCODE_WINDOW,
                            KeyEvent.KEYCODE_CTRL_LEFT,KeyEvent.KEYCODE_CTRL_RIGHT,KeyEvent.KEYCODE_SHIFT_LEFT,
                            KeyEvent.KEYCODE_SHIFT_RIGHT).contains(keyCode)) {
                sendModifierKeyDown(keyCode)
//                println(keyboardReport2.bytes[0])
//                shift is 2, ctrl is 1, window is 8, and alt is 4
                when (keyCode){
                    KeyEvent.KEYCODE_SHIFT_LEFT -> isShiftPressed = 1
                    KeyEvent.KEYCODE_SHIFT_RIGHT -> isShiftPressed = 2
//                    for alt space to send alt tab
                    KeyEvent.KEYCODE_ALT_LEFT -> {println("alt left is down"); isAltPressed =1}
                    KeyEvent.KEYCODE_ALT_RIGHT -> {println("alt right is down"); isAltPressed =2 }
                }
            }
            else{
                if ((keyCode == KeyEvent.KEYCODE_SPACE) && (isAltPressed != 0)) {
                    sendKeyDown(KeyEvent.KEYCODE_TAB)
                }
                else {
                    sendKeyDown(keyCode)
                    if (keyCode == KeyEvent.KEYCODE_DEL) sendKeyUp(keyCode)
//                sendKeyUp(keyCode) this will delete anything when press y in Notes on iPadOS
                }
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
        paused = true
        println("--- hide keyboard")
    }

    override fun onResume() {
                super.onResume()
                if (paused) {
                    btAdapter.getProfileProxy(this,
                            object : BluetoothProfile.ServiceListener {
                                override fun onServiceDisconnected(profile: Int) {
                                    println("--- Disconnect, profile is $profile")
                                }
                                override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                                    println("--- connected, profile is $profile")
                                    if (profile == BluetoothProfile.HID_DEVICE) {
                                        // get bthid
                                        bthid = proxy as BluetoothHidDevice
                                        println("--- got hid proxy object ")
                                        val btcallback = BluetoothCallback(this@OTGRealKeyboardActivity, bthid, btAdapter, TARGET_DEVICE_NAME)
                                        bthid.registerApp(sdpRecord, null, qosOut, { it.run() }, btcallback)
                                    }
                                }
                            }
                            , BluetoothProfile.HID_DEVICE)
                }
                paused = false
    }


    private val sdpRecord by lazy {
        BluetoothHidDeviceAppSdpSettings(
            "Bluetooth HID Keyboard",
            "Bluetooth HID Keyboard",
            "Fixed Point",
            BluetoothHidDevice.SUBCLASS1_COMBO,
//                DescriptorCollection.MOUSE_KEYBOARD_COMBO
                        DescriptorCollection.KeyboardDescriptor
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
