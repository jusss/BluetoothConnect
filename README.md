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


-- pair and connect not same thing in archlinux, if you pair it, but it may not be connected, you have to connect by right click on it 'connect',
-- on windows 10, in the settings, after pair, it will auto be connected,
-- the important thing is run this app and then connect it, you can't connect it before this app run,
-- systemctl start lightdm, or systemctl start bluetooth; blueman-applet; blueman-manager
-- 1. pair android and archlinux
-- 2. open this app choose archlinux device name
-- 3. forget archlinux device name in android 
-- 4. archlinux blueman-manager right click on android device name, choose 'connect'
-- 5. pair each other
-- 6. it works

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
