package com.example.bluetoothconnect

import android.bluetooth.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class OnScreenKeyboardActivity : AppCompatActivity() {
    lateinit var bthid : BluetoothHidDevice
    val keyCode2KeyCode = KeyCode2KeyCode()
    val keyboardReport = KeyboardReport()
    val keyboardReport2 = KeyboardReport2()
    lateinit var btAdapter: BluetoothAdapter
    lateinit var TARGET_DEVICE_NAME: String
    var isCapsLockPressed = false
    var isAltPressed = false
    var isCtrlPressed = false
    var isShiftPressed = false
    var isShiftPressedWithOthers = false
    var isWindowPressed = false
    var isAltRepeat = false
    var latestSentTime = System.currentTimeMillis()
    lateinit var imm: InputMethodManager
    var paused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onscreenkeyboard)

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
                        if (profile != BluetoothProfile.HID_DEVICE) {
                            return
                        }
                        // get bthid
                        bthid = proxy as BluetoothHidDevice
                        println("--- got hid proxy object ")
                        val btcallback = BluetoothCallback(this@OnScreenKeyboardActivity,bthid, btAdapter,TARGET_DEVICE_NAME)
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


        findViewById<Button>(R.id.keyboard).setOnClickListener {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        }

        findViewById<Button>(R.id.alt).setOnClickListener {
//            isAltPressed = isAltPressed.not()
//            if (isAltPressed){
//                sendModifierKeyDown(KeyEvent.KEYCODE_ALT_LEFT)
//            }
//            else {
////                sendKeyUp(KeyEvent.KEYCODE_ALT_LEFT)
//                sendAllKeyUp()
//            }

            isAltPressed = isAltPressed.not()
            if (!isAltPressed){
//                sendKeyUp(KeyEvent.KEYCODE_ALT_LEFT)
                sendAllKeyUp()
            }
        }

        findViewById<Button>(R.id.ctrl).setOnClickListener {
//            isCtrlPressed = isCtrlPressed.not()
//            if (isCtrlPressed){
//                sendModifierKeyDown(KeyEvent.KEYCODE_CTRL_LEFT)
//            }
//            else {
//                sendAllKeyUp()
//            }
            isCtrlPressed = true
        }

        findViewById<Button>(R.id.shift).setOnClickListener {
//            isShiftPressed = isShiftPressed.not()
//            if (isShiftPressed){
//                sendModifierKeyDown(KeyEvent.KEYCODE_SHIFT_LEFT)
//            }
//            else {
//                sendAllKeyUp()
//            }
            isShiftPressed = true
        }

        findViewById<Button>(R.id.window).setOnClickListener {
//            isWindowPressed = isWindowPressed.not()
//            if (isWindowPressed){
//                sendModifierKeyDown(KeyEvent.KEYCODE_WINDOW)
//            }
//            else {
//                sendAllKeyUp()
//            }
            isWindowPressed = true
        }

        findViewById<Button>(R.id.backspace)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_DEL)
            sendKeyUp(KeyEvent.KEYCODE_DEL)
        }
        findViewById<Button>(R.id.esc)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_ESCAPE)
            sendKeyUp(KeyEvent.KEYCODE_ESCAPE)
        }
        findViewById<Button>(R.id.tab)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_TAB)
            sendKeyUp(KeyEvent.KEYCODE_TAB)
        }
        findViewById<Button>(R.id.up)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_DPAD_UP)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }
        findViewById<Button>(R.id.down)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_DPAD_DOWN)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }
        findViewById<Button>(R.id.left)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_DPAD_LEFT)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }
        findViewById<Button>(R.id.right)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }
        findViewById<Button>(R.id.slash)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_SLASH)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }
        findViewById<Button>(R.id.delete)?.setOnClickListener {
            sendKeyDown(KeyEvent.KEYCODE_FORWARD_DEL)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }
        findViewById<Button>(R.id.question)?.setOnClickListener {
            isShiftPressed = true
            sendKeyDown(KeyEvent.KEYCODE_SLASH)
            sendAllKeyUp()
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
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
    }

    fun sendModifierKeyDown(keyCode:Int){
        if (keyCode == KeyEvent.KEYCODE_ALT_LEFT || keyCode == KeyEvent.KEYCODE_ALT_RIGHT) keyboardReport2.leftAlt = true
        if (keyCode == KeyEvent.KEYCODE_CTRL_LEFT || keyCode == KeyEvent.KEYCODE_CTRL_RIGHT) keyboardReport2.leftControl = true
        if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) keyboardReport2.leftShift = true
        if (keyCode == KeyEvent.KEYCODE_WINDOW)  keyboardReport2.leftGui = true
        if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) keyboardReport2.leftControl = true

//            keyboardReport2.key1=0.toByte()
        bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
        )
    }

    fun sendKeyDown(keyCode: Int){
        if (isShiftPressed) {
            keyboardReport2.leftShift = true
            isShiftPressedWithOthers = true
        }
        if (isCtrlPressed) keyboardReport2.leftControl = true
        if (isAltPressed) keyboardReport2.leftAlt = true
        if (isWindowPressed) keyboardReport2.leftGui = true
        if (isCapsLockPressed) keyboardReport2.leftControl = true

        var key = 0

        // replace ALt Space to Alt Tab
        if ((keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_TAB) && isAltPressed) {
            key = KeyboardReport2.KeyEventMap.get(KeyEvent.KEYCODE_TAB) ?: 0
        }
        else{
            // if keyCode is 0, then key will be 0
            key = KeyboardReport2.KeyEventMap.get(keyCode) ?: 0
        }

        keyboardReport2.key1=key.toByte()
        bthid.sendReport(
                ConnectedDevice.device,KeyboardReport2.ID,keyboardReport2.bytes
        )

        keyboardReport2.leftShift = false
        keyboardReport2.leftControl = false
//            keyboardReport2.leftAlt = false
        keyboardReport2.leftGui = false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) { isCapsLockPressed = false }
        if (keyCode == KeyEvent.KEYCODE_WINDOW) { isWindowPressed = false }
        if (keyCode == KeyEvent.KEYCODE_ALT_LEFT || keyCode == KeyEvent.KEYCODE_ALT_RIGHT) { isAltPressed = false }
        if (keyCode == KeyEvent.KEYCODE_CTRL_LEFT || keyCode == KeyEvent.KEYCODE_CTRL_RIGHT) { isCtrlPressed = false }
        if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT) {
            if (!isShiftPressedWithOthers) {
                sendModifierKeyDown(KeyEvent.KEYCODE_SHIFT_LEFT)
                sendKeyUp(KeyEvent.KEYCODE_SHIFT_LEFT)
            }
            isShiftPressed = false
        }

        // alt tab switch window is alt key down, tab key down, tab up, alt up
        if ((keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_TAB) && isAltPressed) {
            sendKeyUp(KeyEvent.KEYCODE_TAB)
//            println(" send alt space $keyCode")
        }
        else {
//            println(" send key up $keyCode")
//            sendKeyUp(keyCode)


            sendAllKeyUp()
//            if (isAltPressed) sendModifierKeyDown(KeyEvent.KEYCODE_ALT_LEFT)
//            if (isCapsLockPressed) sendModifierKeyDown(KeyEvent.KEYCODE_CTRL_LEFT)
//            if (isCtrlPressed) sendModifierKeyDown(KeyEvent.KEYCODE_CTRL_LEFT)
//            if (isShiftPressed) sendModifierKeyDown(KeyEvent.KEYCODE_SHIFT_LEFT)
//            if (isWindowPressed) sendModifierKeyDown(KeyEvent.KEYCODE_WINDOW)
            // after win r, release win
            isWindowPressed = false
            isCtrlPressed = false
            isShiftPressed = false
            isCapsLockPressed = false
            isAltPressed = false
        }

//        return super.onKeyUp(keyCode, event)
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (bthid.connectedDevices != null){
//                   println("--- keycode is $keyCode")
            if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) {
                isCapsLockPressed = true
                sendModifierKeyDown(keyCode)
//                if (!isCapsLockPressed) sendModifierKeyDown(keyCode)
            }
            if (keyCode == KeyEvent.KEYCODE_WINDOW) {
                isWindowPressed = true
                sendModifierKeyDown(keyCode)
//                if (!isWindowPressed) sendModifierKeyDown(keyCode)
            }
            if (keyCode == KeyEvent.KEYCODE_ALT_LEFT || keyCode == KeyEvent.KEYCODE_ALT_RIGHT) {
                isAltPressed = true
                // alt tab swith window, alt down, tab down, tab up, alt up, keey alt down
                // alt key down
                sendModifierKeyDown(keyCode)
                // hold alt, only send one alt key down
//                if (!isAltPressed) sendModifierKeyDown(keyCode)
            }
            if (keyCode == KeyEvent.KEYCODE_CTRL_LEFT || keyCode == KeyEvent.KEYCODE_CTRL_RIGHT){
                isCtrlPressed = true
                sendModifierKeyDown(keyCode)
//                if (!isCtrlPressed) sendModifierKeyDown(keyCode)
            }
            if (keyCode == KeyEvent.KEYCODE_SHIFT_LEFT || keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT){
                // shift pressed and hold only send one shift key event when released for shift switch input method
                isShiftPressed = true ;
                isShiftPressedWithOthers = false
            }

            if (!listOf<Int>(KeyEvent.KEYCODE_CAPS_LOCK, KeyEvent.KEYCODE_ALT_LEFT, KeyEvent.KEYCODE_ALT_RIGHT,KeyEvent.KEYCODE_WINDOW,
                            KeyEvent.KEYCODE_CTRL_LEFT,KeyEvent.KEYCODE_CTRL_RIGHT,KeyEvent.KEYCODE_SHIFT_LEFT,KeyEvent.KEYCODE_SHIFT_RIGHT).contains(keyCode))
                sendKeyDown(keyCode)
        }

//        return super.onKeyDown(keyCode, event)
        return true
    }

    override fun onPause() {
        super.onPause()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0)
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0)

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        imm.hideSoftInputFromWindow(currentFocus.windowToken,0)
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
                                        val btcallback = BluetoothCallback(this@OnScreenKeyboardActivity, bthid, btAdapter, TARGET_DEVICE_NAME)
                                        bthid.registerApp(sdpRecord, null, qosOut, { it.run() }, btcallback)
//                            bthid.registerApp(
//                                    Constants.SDP_RECORD, null, Constants.QOS_OUT, Executor { obj: Runnable -> obj.run() }, btcallback
//                            )
                                    }
                                }
                            }
                            , BluetoothProfile.HID_DEVICE)

//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
//                    println("--- on resume keyboard")
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
