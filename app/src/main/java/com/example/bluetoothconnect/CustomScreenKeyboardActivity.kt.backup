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


class CustomScreenKeyboardActivity : AppCompatActivity() {
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
//    lateinit var imm: InputMethodManager
    var paused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard_1280x720)

        val intent = intent
        val name = intent.getSerializableExtra("name") as ArrayList<String>?
        TARGET_DEVICE_NAME = name!![0]
        val screenSize = name[1]

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        if (screenSize == "1280x720") setContentView(R.layout.activity_keyboard_1280x720)
        if (screenSize == "2340x1080") setContentView(R.layout.activity_keyboard_2340x1080)
        if (screenSize == "1280x720_large") setContentView(R.layout.activity_keyboard_1280x720_large)
        if (screenSize == "custom_keyboard") setContentView(R.layout.activity_custom_keyboard)



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
                        val btcallback = BluetoothCallback(this@CustomScreenKeyboardActivity,bthid, btAdapter,TARGET_DEVICE_NAME)
                        bthid.registerApp(sdpRecord, null, qosOut, {it.run()}, btcallback)
//                            bthid.registerApp(
//                                    Constants.SDP_RECORD, null, Constants.QOS_OUT, Executor { obj: Runnable -> obj.run() }, btcallback
//                            )
                    }
                }
                , BluetoothProfile.HID_DEVICE)

//        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0)


//        findViewById<Button>(R.id.keyboard).setOnClickListener {
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
//        }

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

//            isWindowPressed = true

            isWindowPressed = isWindowPressed.not()
            if (!isWindowPressed){
//                sendKeyUp(KeyEvent.KEYCODE_ALT_LEFT)
                sendAllKeyUp()
            }
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




        val regularKey = mutableMapOf<Int, Int>(
            R.id.char_comma to KeyEvent.KEYCODE_COMMA, R.id.char_z to KeyEvent.KEYCODE_Z,
            R.id.char_x to KeyEvent.KEYCODE_X, R.id.char_c to KeyEvent.KEYCODE_C, R.id.char_v to KeyEvent.KEYCODE_V,
            R.id.char_b to KeyEvent.KEYCODE_B, R.id.char_n to KeyEvent.KEYCODE_N,R.id.char_m to KeyEvent.KEYCODE_M,
            R.id.char_period to KeyEvent.KEYCODE_PERIOD,R.id.char_a to KeyEvent.KEYCODE_A,
            R.id.char_s to KeyEvent.KEYCODE_S,R.id.char_d to KeyEvent.KEYCODE_D,R.id.char_f to KeyEvent.KEYCODE_F,
            R.id.char_g to KeyEvent.KEYCODE_G,R.id.char_h to KeyEvent.KEYCODE_H,R.id.char_j to KeyEvent.KEYCODE_J,
            R.id.char_k to KeyEvent.KEYCODE_K,R.id.char_l to KeyEvent.KEYCODE_L,R.id.char_q to KeyEvent.KEYCODE_Q,
            R.id.char_w to KeyEvent.KEYCODE_W,R.id.char_e to KeyEvent.KEYCODE_E,R.id.char_r to KeyEvent.KEYCODE_R,
            R.id.char_t to KeyEvent.KEYCODE_T,R.id.char_y to KeyEvent.KEYCODE_Y,R.id.char_u to KeyEvent.KEYCODE_U,
            R.id.char_i to KeyEvent.KEYCODE_I,R.id.char_o to KeyEvent.KEYCODE_O,R.id.char_p to KeyEvent.KEYCODE_P,
            R.id.char_1 to KeyEvent.KEYCODE_1,R.id.char_2 to KeyEvent.KEYCODE_2,R.id.char_3 to KeyEvent.KEYCODE_3,
            R.id.char_4 to KeyEvent.KEYCODE_4,R.id.char_5 to KeyEvent.KEYCODE_5,R.id.char_6 to KeyEvent.KEYCODE_6,
            R.id.char_7 to KeyEvent.KEYCODE_7,R.id.char_8 to KeyEvent.KEYCODE_8,R.id.char_9 to KeyEvent.KEYCODE_9,
            R.id.char_0 to KeyEvent.KEYCODE_0, R.id.char_slash to KeyEvent.KEYCODE_SLASH,
            R.id.backquote to KeyEvent.KEYCODE_GRAVE,
            R.id.char_slash to KeyEvent.KEYCODE_SLASH,
            R.id.semicolon to KeyEvent.KEYCODE_SEMICOLON,
            R.id.single_quote to KeyEvent.KEYCODE_APOSTROPHE,
            R.id.left_square to KeyEvent.KEYCODE_LEFT_BRACKET,
            R.id.right_square to KeyEvent.KEYCODE_RIGHT_BRACKET,
            R.id.back_slach to KeyEvent.KEYCODE_BACKSLASH,
            R.id.hyphen to KeyEvent.KEYCODE_MINUS,
            R.id.equal to KeyEvent.KEYCODE_EQUALS,
            R.id.left to KeyEvent.KEYCODE_DPAD_LEFT,
            R.id.right to KeyEvent.KEYCODE_DPAD_RIGHT,
            R.id.down to KeyEvent.KEYCODE_DPAD_DOWN,
            R.id.up to KeyEvent.KEYCODE_DPAD_UP,
            R.id.delete to KeyEvent.KEYCODE_FORWARD_DEL,
            R.id.space to KeyEvent.KEYCODE_SPACE,
            R.id.enter to KeyEvent.KEYCODE_ENTER,
            R.id.back to KeyEvent.KEYCODE_DEL,
            R.id.esc to KeyEvent.KEYCODE_ESCAPE
        )



        for ((k, v) in regularKey){
            findViewById<Button>(k)?.setOnClickListener {
                sendKeyDown(v)
                sendAllKeyUp()
                isWindowPressed = false
                isCtrlPressed = false
                isShiftPressed = false
                isCapsLockPressed = false
                isAltPressed = false
            }
        }

        val specialKey = mutableMapOf<Int, Int>(
            R.id.tab to KeyEvent.KEYCODE_TAB
        )

        for ((k, v) in specialKey){
            findViewById<Button>(k)?.setOnClickListener {
                sendKeyDown(v)
                sendKeyUp(v)
            }
        }



//        val specialPhysicsKey = mutableMapOf<Int, String>(
//            KeyEvent.KEYCODE_SPACE to "Space", KeyEvent.KEYCODE_ENTER to "Enter", KeyEvent.KEYCODE_DEL to "Back",
//            KeyEvent.KEYCODE_TAB to "Tab", KeyEvent.KEYCODE_ESCAPE to "Esc",
//            KeyEvent.KEYCODE_DPAD_LEFT to "Left", KeyEvent.KEYCODE_DPAD_DOWN to "Down",
//            KeyEvent.KEYCODE_DPAD_UP to "Up", KeyEvent.KEYCODE_DPAD_RIGHT to "Right",
//            KeyEvent.KEYCODE_FORWARD_DEL to "Del", KeyEvent.KEYCODE_INSERT to "Ins",
//            KeyEvent.KEYCODE_PAGE_DOWN to "PgDn", KeyEvent.KEYCODE_PAGE_UP to "PgUp",
//            KeyEvent.KEYCODE_SYSRQ to "PRINTSCREEN", KeyEvent.KEYCODE_MENU to "MENU", KeyEvent.KEYCODE_SCROLL_LOCK to "SCROLLLOCK",
//            KeyEvent.KEYCODE_BREAK to "PAUSE", KeyEvent.KEYCODE_MOVE_HOME to "HOME", KeyEvent.KEYCODE_MOVE_END to "END",
//            KeyEvent.KEYCODE_F1 to "F1", KeyEvent.KEYCODE_F2 to "F2", KeyEvent.KEYCODE_F3 to "F3",
//            KeyEvent.KEYCODE_F4 to "F4", KeyEvent.KEYCODE_F5 to "F5", KeyEvent.KEYCODE_F6 to "F6",
//            KeyEvent.KEYCODE_F7 to "F7", KeyEvent.KEYCODE_F8 to "F8", KeyEvent.KEYCODE_F9 to "F9",
//            KeyEvent.KEYCODE_F10 to "F10", KeyEvent.KEYCODE_F11 to "F11", KeyEvent.KEYCODE_F12 to "F12"
//        )







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

//        keyboardReport2.leftShift = false
//        keyboardReport2.leftControl = false
//            keyboardReport2.leftAlt = false
//        keyboardReport2.leftGui = false
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
        if (((keyCode == KeyEvent.KEYCODE_SPACE || keyCode == KeyEvent.KEYCODE_TAB) && isAltPressed) || ((keyCode == KeyEvent.KEYCODE_TAB) && isWindowPressed)) {
            sendKeyUp(KeyEvent.KEYCODE_TAB)
//            println(" send alt space $keyCode")
        }
//        if (isWindowPressed || isCtrlPressed || isShiftPressed || isCapsLockPressed || isAltPressed){
//            sendKeyUp(keyCode)
//        }
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
//                avoid continuous backspace
                if (keyCode == KeyEvent.KEYCODE_DEL) sendKeyUp(keyCode)

        }

//        return super.onKeyDown(keyCode, event)
        return true
    }

    override fun onPause() {
        super.onPause()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0)
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0)

//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        imm.hideSoftInputFromWindow(currentFocus.windowToken,0)
        paused = true
        println("--- hide keyboard")
    }

    override fun onResume() {
                super.onResume()
                val decorView = window.decorView
                val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                decorView.systemUiVisibility = uiOptions
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
                                        val btcallback = BluetoothCallback(this@CustomScreenKeyboardActivity, bthid, btAdapter, TARGET_DEVICE_NAME)
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