package com.example.bluetoothconnect

import android.view.KeyEvent

class KeyCode2KeyCode {

    val regularPhysicsKey = mutableMapOf<Int, Char>(
        KeyEvent.KEYCODE_COMMA to ',', KeyEvent.KEYCODE_Z to 'z', KeyEvent.KEYCODE_X to 'x', KeyEvent.KEYCODE_C to 'c', KeyEvent.KEYCODE_V to 'v',
        KeyEvent.KEYCODE_B to 'b', KeyEvent.KEYCODE_N to 'n', KeyEvent.KEYCODE_M to 'm', KeyEvent.KEYCODE_PERIOD to '.', KeyEvent.KEYCODE_A to 'a',
        KeyEvent.KEYCODE_S to 's', KeyEvent.KEYCODE_D to 'd', KeyEvent.KEYCODE_F to 'f', KeyEvent.KEYCODE_G to 'g', KeyEvent.KEYCODE_H to 'h', KeyEvent.KEYCODE_J to 'j',
        KeyEvent.KEYCODE_K to 'k', KeyEvent.KEYCODE_L to 'l', KeyEvent.KEYCODE_Q to 'q', KeyEvent.KEYCODE_W to 'w', KeyEvent.KEYCODE_E to 'e', KeyEvent.KEYCODE_R to 'r',
        KeyEvent.KEYCODE_T to 't', KeyEvent.KEYCODE_Y to 'y', KeyEvent.KEYCODE_U to 'u', KeyEvent.KEYCODE_I to 'i', KeyEvent.KEYCODE_O to 'o', KeyEvent.KEYCODE_P to 'p',
        KeyEvent.KEYCODE_1 to '1', KeyEvent.KEYCODE_2 to '2', KeyEvent.KEYCODE_3 to '3', KeyEvent.KEYCODE_4 to '4', KeyEvent.KEYCODE_5 to '5', KeyEvent.KEYCODE_6 to '6',
        KeyEvent.KEYCODE_7 to '7', KeyEvent.KEYCODE_8 to '8', KeyEvent.KEYCODE_9 to '9', KeyEvent.KEYCODE_0 to '0', KeyEvent.KEYCODE_GRAVE to '`',
        KeyEvent.KEYCODE_SLASH to '/', KeyEvent.KEYCODE_SEMICOLON to ';', KeyEvent.KEYCODE_APOSTROPHE to '\'', KeyEvent.KEYCODE_LEFT_BRACKET to '[', KeyEvent.KEYCODE_RIGHT_BRACKET to ']',
        KeyEvent.KEYCODE_BACKSLASH to '\\', KeyEvent.KEYCODE_MINUS to '-', KeyEvent.KEYCODE_EQUALS to '='
    )

    val specialPhysicsKey = mutableMapOf<Int, String>(
        KeyEvent.KEYCODE_SPACE to "Space", KeyEvent.KEYCODE_ENTER to "Enter", KeyEvent.KEYCODE_DEL to "Back",
        KeyEvent.KEYCODE_TAB to "Tab", KeyEvent.KEYCODE_ESCAPE to "Esc",
        KeyEvent.KEYCODE_DPAD_LEFT to "Left", KeyEvent.KEYCODE_DPAD_DOWN to "Down",
        KeyEvent.KEYCODE_DPAD_UP to "Up", KeyEvent.KEYCODE_DPAD_RIGHT to "Right",
        KeyEvent.KEYCODE_FORWARD_DEL to "Del", KeyEvent.KEYCODE_INSERT to "Ins",
        KeyEvent.KEYCODE_PAGE_DOWN to "PgDn", KeyEvent.KEYCODE_PAGE_UP to "PgUp",
        KeyEvent.KEYCODE_SYSRQ to "PRINTSCREEN", KeyEvent.KEYCODE_MENU to "MENU", KeyEvent.KEYCODE_SCROLL_LOCK to "SCROLLLOCK",
        KeyEvent.KEYCODE_BREAK to "PAUSE", KeyEvent.KEYCODE_MOVE_HOME to "HOME", KeyEvent.KEYCODE_MOVE_END to "END",
        KeyEvent.KEYCODE_F1 to "F1", KeyEvent.KEYCODE_F2 to "F2", KeyEvent.KEYCODE_F3 to "F3",
        KeyEvent.KEYCODE_F4 to "F4", KeyEvent.KEYCODE_F5 to "F5", KeyEvent.KEYCODE_F6 to "F6",
        KeyEvent.KEYCODE_F7 to "F7", KeyEvent.KEYCODE_F8 to "F8", KeyEvent.KEYCODE_F9 to "F9",
        KeyEvent.KEYCODE_F10 to "F10", KeyEvent.KEYCODE_F11 to "F11", KeyEvent.KEYCODE_F12 to "F12"
    )
    // https://elementalx.org/button-mapper/android-key-codes/


    val scancode = mutableMapOf<String, Int>(
        "Enter" to 40, "Esc" to 41, "Back" to 42, "Tab" to 43, "Space" to 44, "Right" to 79,
        "Left" to 80, "Down" to 81, "Up" to 82, "Ins" to 73, "Del" to 76, "PgUp" to 75, "PgDn" to 78,
        "PRINTSCREEN" to 70, "MENU" to 188, "SCROLLLOCK" to 71, "PAUSE" to 72, "HOME" to 74, "END" to 77,
        "F1" to 58, "F2" to 59, "F3" to 60, "F4" to 61, "F5" to 62, "F6" to 63, "F7" to 64, "F8" to 65,
        "F9" to 66, "F10" to 67, "F11" to 68, "F12" to 69, "Ctrl" to 224, "Shift" to 225, "Alt" to 226,
        "Win" to 227
    )
    // https://www.usb.org/sites/default/files/documents/hut1_12v2.pdf

    val keyMap = mutableMapOf<Char,Int>(
        'a' to  0x04, 'b' to  0x05, 'c' to  0x06, 'd' to  0x07, 'e' to  0x08, 'f' to  0x09, 'g' to  0x0A,
        'h' to  0x0B, 'i' to  0x0C, 'j' to  0x0D, 'k' to  0x0E, 'l' to  0x0F, 'm' to  0x10, 'n' to  0x11,
        'o' to  0x12, 'p' to  0x13, 'q' to  0x14, 'r' to  0x15, 's' to  0x16, 't' to  0x17, 'u' to  0x18,
        'v' to  0x19, 'w' to  0x1A, 'x' to  0x1B, 'y' to  0x1C, 'z' to  0x1D, '1' to  0x1E, '2' to  0x1F,
        '3' to  0x20, '4' to  0x21, '5' to  0x22, '6' to  0x23, '7' to  0x24, '8' to  0x25, '9' to  0x26,
        '0' to  0x27, ' ' to  0x2C, '-' to  0x2D, '=' to  0x2E, '[' to  0x2F, ']' to  0x30, '\\' to  0x31,
        ';' to  0x33, '\'' to  0x34, '`' to  0x35, ',' to 0x36, '.' to 0x37, '/' to 0x38
    )
}