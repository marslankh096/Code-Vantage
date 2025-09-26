package com.demo.csvfilereader.utility;

import android.os.Build;


public class DeviceInfo {
    private DeviceInfo() {
    }

    public static boolean isLegacyDevice() {
        return Build.VERSION.SDK_INT < 22;
    }
}
