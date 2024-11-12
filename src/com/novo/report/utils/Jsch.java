package com.novo.report.utils;

import com.jcraft.jsch.*;
import com.novo.report.service.impl.PyReportServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Jsch {
    /**
     * @param host    主机ip
     * @param user    用户名
     * @param pass    用户密码
     * @param port    端口
     * @param command 要执行的指令
     */
    public static String sshCommand(String host, String user, String pass, int port, String command) {
        JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;
        InputStream in = null;
        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(pass);
            session.setTimeout(50000);
            Properties config = new Properties();
            //严格的主机key检查
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("exec");
            ChannelExec execChannel = (ChannelExec) channel;
            execChannel.setCommand(command);
            in = channel.getInputStream();
            channel.connect();

            StringBuffer sb = new StringBuffer();
            int c = -1;
            while ((c = in.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        InputStream inStream = PyReportServiceImpl.class.getClassLoader().getResourceAsStream("jsch.properties");
        try {
            prop.load(inStream);
//            String sshCommand = sshCommand(prop.getProperty("host"), prop.getProperty("user"), prop.getProperty("pass"), Integer.valueOf(prop.getProperty("port")), "python /TJPROJ2/OBD/module/tmb/TMBtoBase64.py  4.4 肺癌 TKHS220029334-1AT novopm3_tis_550  /TJPROJ13/CR/other/TMB_plot/");
//            String sshCommand = sshCommand(prop.getProperty("host"), prop.getProperty("user"), prop.getProperty("pass"), Integer.valueOf(prop.getProperty("port")), "echo 1");
            String sshCommand = sshCommand(prop.getProperty("host"), prop.getProperty("user"), prop.getProperty("pass"), Integer.valueOf(prop.getProperty("port")), "python /TJPROJ2/OBD/module/tmb_report/TMBtoBase64.py 1.333 结直肠癌 YKHS230000912-1A novopm3_blo_550  /TJPROJ13/CR/other/TMB_plot/");
            System.out.println(sshCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
