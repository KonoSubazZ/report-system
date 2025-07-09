package com.novo.report.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * 获取本机IP 地址
 */
public class IpUtil {
    public static Set<String> getLocalIp4Address() {
        Set<String> result = new HashSet<>();
        List<Inet4Address> list = null;
        try {
            list = getLocalIp4AddressFromNetworkInterface();
            list.forEach(d -> {
                result.add(d.toString().replaceAll("/", ""));
            });
        } catch (Exception e) {
            //LoggerManager.systemLogger.error("getLocalIp4Address Exception", e);
        }
        return result;
    }

    /*
     * 获取本机所有网卡信息   得到所有IPv4信息
     * @return Inet4Address>
     */
    public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<>(8);
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces == null) {
            return addresses;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    addresses.add((Inet4Address) inetAddress);
                }
            }
        }
        return addresses;
    }

    /**
     * 获取局域网内第一个可用的IPv4地址（非回环、非公网）
     * 适用于单网卡或优先使用的主网卡场景
     *
     * @return 局域网IPv4地址，无可用时返回null
     */
    public static String getFirstLocalIPv4() {
        List<String> localIps = getAllLocalIPv4s();
        return localIps.isEmpty() ? null : localIps.get(0);
    }

    /**
     * 获取所有局域网内的IPv4地址（非回环、非公网）
     * 适用于多网卡场景
     *
     * @return 局域网IPv4地址列表，无可用时返回空列表
     */
    public static List<String> getAllLocalIPv4s() {
        List<String> localIps = new ArrayList<>();
        try {
            // 遍历所有网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                // 跳过未启用的接口
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }

                // 遍历接口下的所有IP地址
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // 筛选IPv4、非回环、局域网地址
                    if (addr instanceof Inet4Address
                            && !addr.isLoopbackAddress()
                            && isLocalNetwork(addr.getHostAddress())) {

                        localIps.add(addr.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localIps;
    }

    /**
     * 判断IP是否为局域网地址
     * 局域网地址范围：
     * - 10.0.0.0 ~ 10.255.255.255
     * - 172.16.0.0 ~ 172.31.255.255
     * - 192.168.0.0 ~ 192.168.255.255
     */
    private static boolean isLocalNetwork(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        int first = Integer.parseInt(parts[0]);
        int second = Integer.parseInt(parts[1]);

        // 10.x.x.x 网段
        if (first == 10) {
            return true;
        }
        // 172.16.x.x ~ 172.31.x.x 网段
        if (first == 172 && second >= 16 && second <= 31) {
            return true;
        }
        // 192.168.x.x 网段
        if (first == 192 && second == 168) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(getLocalIp4Address());
    }
}
