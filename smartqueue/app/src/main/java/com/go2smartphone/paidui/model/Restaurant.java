package com.go2smartphone.paidui.model;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import com.go2smartphone.pritln.SanyiUSBDriver;
import com.sanyipos.sdk.api.SanyiSDK;
import com.sanyipos.sdk.model.rest.StaffRest;
import com.sanyipos.sdk.utils.GenericHelper;

import java.io.IOException;
import java.net.InetAddress;

public class Restaurant {


    public static final String ACTION_USB_PERMISSION = "com.go2smartphone.smartpos.USB_PERMISSION";


    /*************************************
     * headers
     */
    public static String DEVICE_ID = "-1";

    public static String SHOP_ID = "-1";

    public static String DEVICE_NAME = "";

    public static String VERSION_NAME = "";

    public static String VERSION_CODE = "";
    /***
     * smartqueue = 2
     * <p>
     * smartdisplay = 5;
     */
    public static String PRODUCT_CODE = "2";

    public static long STAFF_ID = 0;
    /************************************/


    public static SanyiUSBDriver usbDriver;

    public static RegisterResult registerInfo;

    public static StaffRest getStaffByAccessCode(String code) {
        String sha1Code = GenericHelper.SHA1(code + SanyiSDK.rest.operationData.shop.salt);
        StaffRest tempStaff = null;
        if (SanyiSDK.rest.staffs.size() == 0)
            return null;
        for (StaffRest staff : SanyiSDK.rest.staffs) {
            if (staff.getPassword().equals(sha1Code) || (staff.getRfid() != null && staff.getRfid().equals(code))) {
                return staff;
            }
        }
        return tempStaff;
    }

    public static InetAddress getBroadcastAddress(Context context) throws IOException {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

}
