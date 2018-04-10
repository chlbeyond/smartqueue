package com.go2smartphone.paidui.utils.rx;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by ss on 2016/3/14.
 */
public class NetUtil {

    public static String getLocalIpAddress() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {

                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO: handle exception
        }

        return null;
    }

    private static long firstClickTime = 0;
    private static long lastClickTime = 0;

    public synchronized static boolean netIsBreak() {
        if (firstClickTime == 0) {
            firstClickTime = System.currentTimeMillis();

        }
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - firstClickTime > 10000) {
            firstClickTime = 0;
            return false;
        }
        if (lastClickTime - firstClickTime > 5000) {




            firstClickTime = 0;
            return true;
        }

        return false;

    }

}
