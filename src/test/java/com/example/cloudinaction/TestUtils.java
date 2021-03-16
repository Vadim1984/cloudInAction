package com.example.cloudinaction;


import java.net.InetAddress;

public class TestUtils {

    public static String createUrl(int port, String uri){
        return "http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + port + uri;
    }
}
