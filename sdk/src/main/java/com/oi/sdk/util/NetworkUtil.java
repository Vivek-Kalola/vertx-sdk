package com.oi.sdk.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class NetworkUtil {

    public static String getDefaultAddress() {

        try {

            return Collections.list(NetworkInterface.getNetworkInterfaces()).stream()
                    .flatMap(ni -> Collections.list(ni.getInetAddresses()).stream())
                    .filter(address -> !address.isAnyLocalAddress())
                    .filter(address -> !address.isMulticastAddress())
                    .filter(address -> !address.isLoopbackAddress())
                    .filter(address -> !(address instanceof Inet6Address))
                    .map(InetAddress::getHostAddress)
                    .findFirst().orElse("0.0.0.0");
        } catch (SocketException e) {
            return "0.0.0.0";
        }
    }
}
