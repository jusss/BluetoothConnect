package com.example.bluetoothconnect

import android.bluetooth.*
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
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
    var capsLockToCtrl = false
    var ctrlToCapsLock = false
    var keyRepeat = false
    var isCapsLockPressed = false
    var isAltPressed = 0
    var isCtrlPressed = 0
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
        var lastButton : Button? = null

        btAdapter.bondedDevices.map {btd ->
            buttons.add(Button(this))
            buttons.get(buttons.size - 1).setText(btd.name)
            buttons.get(buttons.size - 1).setAllCaps(false)
            buttons.get(buttons.size - 1).setFocusable(false)

            buttons.get(buttons.size - 1).setOnClickListener {
//                buttons.map { btn -> btn.setBackgroundColor(Color.LTGRAY) }
//                buttons.map { btn -> btn.setTextColor(Color.BLACK) }
                val btn = it as Button
                btn.setTextColor(Color.parseColor("#006400"))

                lastButton?.setTextColor(Color.BLACK)
                lastButton = btn


                bthid.connectedDevices.map {
                    println("--- connected device ${it.name} will be disconnected")
                    bthid.disconnect(it)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    delay(600)
//                    if (bthid.connectedDevices.isEmpty()){
                        println("--- connect to target ${btd.name}")
                        val _s = bthid.connect(btd)
//                        if (_s) it.setBackgroundColor(Color.parseColor("#A4C639"))
//                        if (_s) btn.setTextColor(Color.GREEN)
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

//        val background = findViewById<Button>(R.id.capslock_to_ctrl).background as Drawable
//        val colorDrawable = background as ColorDrawable
//        val defaultButtonColor = colorDrawable.color


        findViewById<Button>(R.id.capslock_to_ctrl).focusable = View.NOT_FOCUSABLE
        findViewById<Button>(R.id.capslock_to_ctrl).setOnClickListener {
            val btn = it as Button
            capsLockToCtrl = !capsLockToCtrl
//            if (capsLockToCtrl) it.setBackgroundColor(Color.parseColor("#A4C639"))
//            else it.setBackgroundColor(Color.LTGRAY)
            if (capsLockToCtrl) btn.setTextColor(Color.parseColor("#006400"))
            else btn.setTextColor(Color.BLACK)
        }

        findViewById<Button>(R.id.ctrl_to_capslock).focusable = View.NOT_FOCUSABLE
        findViewById<Button>(R.id.ctrl_to_capslock).setOnClickListener {
            val btn = it as Button
            ctrlToCapsLock = !ctrlToCapsLock
//            if (ctrlToCapsLock) it.setBackgroundColor(Color.parseColor("#A4C639"))
//            else it.setBackgroundColor(Color.LTGRAY)
            if (ctrlToCapsLock) btn.setTextColor(Color.parseColor("#006400"))
            else btn.setTextColor(Color.BLACK)
        }

        findViewById<Button>(R.id.key_repeat).focusable = View.NOT_FOCUSABLE
        findViewById<Button>(R.id.key_repeat).setOnClickListener {
            val btn = it as Button
            keyRepeat = !keyRepeat
//            if (keyRepeat) it.setBackgroundColor(Color.parseColor("#A4C639"))
//            else it.setBackgroundColor(Color.LTGRAY)
            if (keyRepeat) btn.setTextColor(Color.parseColor("#006400"))
            else btn.setTextColor(Color.BLACK)
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
        var key1 : Int? = 0
        when (keyCode) {
            KeyEvent.KEYCODE_ALT_LEFT -> {keyboardReport2.leftAlt = false; isAltPressed = 0}
            KeyEvent.KEYCODE_ALT_RIGHT -> {keyboardReport2.rightAlt = false; isAltPressed = 0}
            KeyEvent.KEYCODE_CTRL_LEFT -> { if (ctrlToCapsLock) { key1 = KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_CAPS_LOCK); isCapsLockPressed = false } else { keyboardReport2.leftControl = false; isCtrlPressed = 0}}
            KeyEvent.KEYCODE_CTRL_RIGHT -> {keyboardReport2.rightControl = false; isCtrlPressed = 0}
            KeyEvent.KEYCODE_SHIFT_LEFT -> {keyboardReport2.leftShift = false; isShiftPressed = 0}
            KeyEvent.KEYCODE_SHIFT_RIGHT -> {keyboardReport2.rightShift = false; isShiftPressed = 0}
            KeyEvent.KEYCODE_WINDOW -> {keyboardReport2.leftGui = false; isWindowPressed = false}
            KeyEvent.KEYCODE_CAPS_LOCK -> {if (capsLockToCtrl) {keyboardReport2.leftControl = false; isCtrlPressed = 0} else key1 = KeyboardReport2.KeyEventMap.get(keyCode)}
            else -> key1 = KeyboardReport2.KeyEventMap.get(keyCode)
        }

        if (key1 != null){

//            zero
//            keyboardReport2.key1=(key1 + 0x80).toByte()
//            bthid.sendReport(ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes)

//          one
            if (keyCode == KeyEvent.KEYCODE_TAB
                    || keyCode == KeyEvent.KEYCODE_SPACE
                    || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT
                    || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT)
                {   if (isAltPressed != 0 && keyCode == KeyEvent.KEYCODE_SPACE){  key1 = KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_TAB) ?: 0 } // alt space send alt tab
                    keyboardReport2.key1 = (key1 + 0x80).toByte()
                }
            else {
                keyboardReport2.reset()
            }
            bthid.sendReport(ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes)


//            for holding modifier keys
            when (isShiftPressed)    {
                1 -> keyboardReport2.leftShift = true
                2 -> keyboardReport2.rightShift = true
            }

            when (isAltPressed)    {
                1 -> keyboardReport2.leftAlt = true
                2 -> keyboardReport2.rightAlt = true
            }

            when (isCtrlPressed)    {
                1 -> keyboardReport2.leftControl = true
                2 -> keyboardReport2.rightControl = true
            }
            if (isWindowPressed) keyboardReport2.leftGui = true

//            if (ctrlToCapsLock && (isCtrlPressed == 1)){
//                keyboardReport2.key1 = (KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_CAPS_LOCK) ?: 0).toByte()
//                bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes)
//            }
            if (isCapsLockPressed){
                keyboardReport2.key1 = (KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_CAPS_LOCK) ?: 0).toByte()
                bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes)
            }

            }
    }

    fun sendKeyDown(keyCode: Int){
        var key1 : Int? = 0
        when (keyCode) {
            KeyEvent.KEYCODE_ALT_LEFT -> {keyboardReport2.leftAlt = true; isAltPressed = 1}
            KeyEvent.KEYCODE_ALT_RIGHT -> {keyboardReport2.rightAlt = true; isAltPressed = 2}
            KeyEvent.KEYCODE_CTRL_LEFT -> { if (ctrlToCapsLock) { key1 = KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_CAPS_LOCK); isCapsLockPressed = true } else {keyboardReport2.leftControl = true; isCtrlPressed = 1}}
            KeyEvent.KEYCODE_CTRL_RIGHT -> {keyboardReport2.rightControl = true; isCtrlPressed = 2}
            KeyEvent.KEYCODE_SHIFT_LEFT -> {keyboardReport2.leftShift = true; isShiftPressed = 1}
            KeyEvent.KEYCODE_SHIFT_RIGHT -> {keyboardReport2.rightShift = true; isShiftPressed = 2}
            KeyEvent.KEYCODE_WINDOW -> {keyboardReport2.leftGui = true; isWindowPressed = true}
            KeyEvent.KEYCODE_CAPS_LOCK -> { if (capsLockToCtrl) {keyboardReport2.leftControl = true; isCtrlPressed = 1} else key1 = KeyboardReport2.KeyEventMap.get(keyCode)}
            else -> key1 = KeyboardReport2.KeyEventMap.get(keyCode)
        }
//        if (key1 != null) {
//            keyboardReport2.key1 = key1.toByte()
//            bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes)
//        }
        if (key1 != null) {
//            press alt space to send alt tab, because android consume alt tab first
            if ((isAltPressed != 0) && (keyCode == KeyEvent.KEYCODE_SPACE)){
                key1 = KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_TAB) ?: 0
            }
            keyboardReport2.key1 = key1.toByte()
            bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, keyboardReport2.bytes)

           if (keyRepeat) {
//               repeat letters in alphabet and numbers for iOS, since apple doesn't support key repeat
               if ((28 < keyCode && keyCode < 55) || (6 < keyCode && keyCode < 17) || (keyCode == KeyEvent.KEYCODE_DEL)) {
//                    y will delete everything in Notes app on iPad, y is 28 in scan code, plus 128 is 156, and 156 is Keyboard Clear in scan code
//                   there are Set 1, Set 2, Set 3, and USB HID scan code, in PS/2 Set 1, key 'a' make code is 1e, break code is 9e, release key will send break code
//                   break code = make code + 0x80, using 'sudo showkeys --scancodes' in linux tty and press a can get a's make code and break code
//                   the weird is using usb keyboard, press a and get 1e, which is Set 1, but a in USB HID scan code is 0x04
//                   val _bytes = keyboardReport2.bytes
//                   _bytes.fill(0)
////                   _bytes[2] =(key1 + 0x80).toByte()
//                   _bytes[2] =(key1 + 128).toByte()
//                   bthid.sendReport(ConnectedDevice.device,KeyboardReport2.ID,_bytes)
                   val _bytes = keyboardReport2.bytes
                   _bytes.fill(0)
                   bthid.sendReport(ConnectedDevice.device, KeyboardReport2.ID, _bytes)
               }
           }
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        println("--- onKeyUp keycode is $keyCode")
        sendKeyUp(keyCode)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (bthid.connectedDevices != null) {
            println("--- onKeyDown keycode is $keyCode")
            sendKeyDown(keyCode)
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
