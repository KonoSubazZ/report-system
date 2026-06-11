package com.novo.report.utils;

import com.novo.report.controller.DriverController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {
    private static Properties prop = new Properties();
    private static String configFilePath = "config.properties";

    static {
        InputStream inStream = ServerConfig.class.getClassLoader().getResourceAsStream(configFilePath);
        try {
            prop.load(inStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getServerFormalIP() {
        return prop.getProperty("server_formal.ip");
    }

    public static String getServerTestIP() {
        return prop.getProperty("server_test.ip");
    }
    public static String getSubreportCallApi() {
        return prop.getProperty("subreport_call_api");
    }

    public static String getCyfzExcelCustomers() {
        return prop.getProperty("cyfz_excel_customers", "");
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getServerFormalIP());
        System.out.println(getServerTestIP());
    }
}
