Use Android as A Bluetooth Keyboard, support OTG Keyboard too

# How to Use
1. pair Android and other devices, like Windows or iPhone to get the device name
2. forget this Android on Windows, no need for iPhone
3. open this app, choose Windows or iPhone's name to connect
4. if it's Windows, it will pop pair-dialog up, pair it, no need for iPhone
5. OTG connect this Android with wired keyboard, choose aosp English keyboard as input method on Android
6. no need to repeat above steps for reopen this app, but if the PC's changed, it needs to repeat it again, no need for iPhone


# How to Use for Archlinux
1. connect to wifi, bluetooth and wifi on same chip, so need wifi is on
2. pair android and archlinux 
3. open app, choose archlinux device name, press home button to put app on backgroup
4. forget device on archlinux (or android, just one is ok)
4. on andriod, app switcher, back to app
6. now it pop pair up dialog, pair it!

# Switch Window 
 'cause android capture Alt Tab, so, use Alt Space to switch window

# Remap CapsLock to Left Ctrl
  by default, capslock is switched to left ctrl

# about Window Key

since android 10, lineageos 17.1 won't release su <br/>
but it can get root with adb <br/>

enable 'rooted debugging' in developer options <br/>
run `adb root` then `adb shell` u will get root privilege <br/>

before 17.1, you can get su file via https://download.lineageos.org/extras <br/>
and just flash it via recovery, then in adb shell run `su` <br/>

connect android to PC via usb cable, swipe down notification bar,<br/>
click android system, change 'Charging the device via USB' to 'File Transfer'<br/>
`adb root` <br/>
`adb remount` remount /system as read-write, not readonly <br/>
`adb shell` <br/>
`nano /system/usr/keylayout/Generic.kl` <br/>
change 125 to WINDOW <br/>

125 META_LEFT is window/super key in the physical keyboard <br/>

https://blog.csdn.net/u012587637/article/details/115517908 <br/>


# Reference
https://github.com/ginkage/wearmouse<br/>
https://github.com/domi1294/BluetoothHidDemo <br/>
https://github.com/AchimStuy/Kontroller <br/>
https://www.usb.org/sites/default/files/documents/hut1_12v2.pdf<br/>
https://elementalx.org/button-mapper/android-key-codes/ <br/>

# Key code and Scan code

keycode is OS use it to map key
scan code is physical keyboard, there're press and release two things, send to OS, 
make is press, break is release, + 0x80, make code first position make it to 1 will be break code
scan code A is 0x04,
press code is 0x04,
release code is 0x04 + 0x80

send 0x0 will send all key up 


# notes

-- pair and connect not same thing in archlinux, if you pair it, but it may not be connected, you have to connect by right click on it 'connect',<br/>
-- on windows 10, in the settings, after pair, it will auto be connected,<br/>
-- the important thing is run this app and then connect it, you can't connect it before this app run, <br/>
-- neton, systemctl start lightdm, systemctl start bluetooth; blueman-applet; blueman-manager <br/>
-- 1. pair android and archlinux <br/>
-- 2. open this app choose archlinux device name<br/>
-- 3. forget archlinux device name in android <br/>
-- 4. archlinux blueman-manager right click on android device name, choose 'connect'<br/>
-- 5. pair each other<br/>
-- 6. it works<br/>

# Latency on iOS/iPadOS

it may have some latency on iOS/iPadOS, search "predict" in Settings,<br/>
disable "Predictive" in General -> Keyboard and all the smart things,<br/>
disable "Extended Predictions" in Accessibility -> Switch Control<br/>
be connected from other devices, not connect to other devices<br/>

# Weird Connection
the device connect to other devices, it can connect directly<br/>
but other devices connect to this device with hid profile,<br/>
only the last successed one can connect it directly,<br/>
otherwise you need to forget the last successed device on this device,<br/>
then use other device connect this device


