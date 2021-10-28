/*
 * Copyright 2018 Google LLC All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bluetoothconnect;

import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;

/**
 * Handy constants for the HID Report Descriptor and SDP configuration.
 */
class Constants {

    static final byte ID_KEYBOARD = 1;

    private static final byte[] HIDD_REPORT_DESC = {
            // Keyboard
            (byte) 0x05, (byte) 0x01, // Usage page (Generic Desktop)
            (byte) 0x09, (byte) 0x06, // Usage (Keyboard)
            (byte) 0xA1, (byte) 0x01, // Collection (Application)
            (byte) 0x85, ID_KEYBOARD, //    Report ID
            (byte) 0x05, (byte) 0x07, //       Usage page (Key Codes)
            (byte) 0x19, (byte) 0xE0, //       Usage minimum (224)
            (byte) 0x29, (byte) 0xE7, //       Usage maximum (231)
            (byte) 0x15, (byte) 0x00, //       Logical minimum (0)
            (byte) 0x25, (byte) 0x01, //       Logical maximum (1)
            (byte) 0x75, (byte) 0x01, //       Report size (1)
            (byte) 0x95, (byte) 0x08, //       Report count (8)
            (byte) 0x81, (byte) 0x02, //       Input (Data, Variable, Absolute) ; Modifier byte
            (byte) 0x75, (byte) 0x08, //       Report size (8)
            (byte) 0x95, (byte) 0x01, //       Report count (1)
            (byte) 0x81, (byte) 0x01, //       Input (Constant)                 ; Reserved byte
            (byte) 0x75, (byte) 0x08, //       Report size (8)
            (byte) 0x95, (byte) 0x06, //       Report count (6)
            (byte) 0x15, (byte) 0x00, //       Logical Minimum (0)
            (byte) 0x25, (byte) 0x65, //       Logical Maximum (101)
            (byte) 0x05, (byte) 0x07, //       Usage page (Key Codes)
            (byte) 0x19, (byte) 0x00, //       Usage Minimum (0)
            (byte) 0x29, (byte) 0x65, //       Usage Maximum (101)
            (byte) 0x81, (byte) 0x00, //       Input (Data, Array)              ; Key array (6 keys)
            (byte) 0xC0,              // End Collection
    };

    private static final String SDP_NAME = "Wear Input";
    private static final String SDP_DESCRIPTION = "Wear OS HID Device";
    private static final String SDP_PROVIDER = "Google Inc.";
    private static final int QOS_TOKEN_RATE = 800; // 9 bytes * 1000000 us / 11250 us
    private static final int QOS_TOKEN_BUCKET_SIZE = 9;
    private static final int QOS_PEAK_BANDWIDTH = 0;
    private static final int QOS_LATENCY = 11250;

    static final BluetoothHidDeviceAppSdpSettings SDP_RECORD =
            new BluetoothHidDeviceAppSdpSettings(
                    com.example.bluetoothconnect.Constants.SDP_NAME,
                    com.example.bluetoothconnect.Constants.SDP_DESCRIPTION,
                    com.example.bluetoothconnect.Constants.SDP_PROVIDER,
                    BluetoothHidDevice.SUBCLASS1_COMBO,
                    com.example.bluetoothconnect.Constants.HIDD_REPORT_DESC);

    static final BluetoothHidDeviceAppQosSettings QOS_OUT =
            new BluetoothHidDeviceAppQosSettings(
                    BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
                    com.example.bluetoothconnect.Constants.QOS_TOKEN_RATE,
                    com.example.bluetoothconnect.Constants.QOS_TOKEN_BUCKET_SIZE,
                    com.example.bluetoothconnect.Constants.QOS_PEAK_BANDWIDTH,
                    com.example.bluetoothconnect.Constants.QOS_LATENCY,
                    BluetoothHidDeviceAppQosSettings.MAX);
}
