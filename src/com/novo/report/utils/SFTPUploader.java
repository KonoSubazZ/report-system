package com.novo.report.utils;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SFTPUploader {
    public static void hnzlUploaded(String localFilePath) {
        String host = "47.94.106.94"; //远程服务器地址
        String username = "zhongliu"; //服务器账号
        String password = "zhongliu123A"; //服务器密码
        int port = 22;
//        String localFilePath = "D:\\maven-ujiuye\\test\\二维码.png"; //需要上传的文件
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(date);
        String remoteDirectoryPath = "/data/HNZL/oss/"+currentDate;//上传到服务器的路径

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            File localFile = new File(localFilePath);
            InputStream inputStream = new FileInputStream(localFile);
            try {
                channelSftp.stat(remoteDirectoryPath);
            } catch (Exception e) {
                channelSftp.mkdir(remoteDirectoryPath);
            }

            channelSftp.cd(remoteDirectoryPath);
            channelSftp.put(inputStream, localFile.getName());

            System.out.println("File uploaded successfully");
        } catch (JSchException | SftpException | java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    public static void main(String[] args) {
//        SFTPUploader.hnzlUploaded("D:\\Novogene\\report_en7\\classes\\artifacts\\TESTREPORT\\UPLOAD\\FS032312290071赵平安泛实体瘤1238+1166基因报告11917.docx");
        try {
            Properties prop = new Properties();
            InputStream inStream = SFTPUploader.class.getClassLoader().getResourceAsStream("jdbc.properties");
            prop.load(inStream);
            Connection conn  = DriverManager.getConnection(prop.getProperty("dbTwo.jdbc.url"),prop.getProperty("dbTwo.jdbc.username"),prop.getProperty("dbTwo.jdbc.password"));
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from analysis_report where report_id = " + "13485");
            while (rs.next()) {
                String insertStatement = "INSERT INTO analysis_report VALUES (";
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    if (rs.getString(i) == null) {
                        insertStatement += rs.getString(i) + ",";
                    } else {
                        insertStatement += "'" + rs.getString(i).replace("\\", "\\\\").replace("\"", "\\\"") + "',";
                    }
                }
                insertStatement = insertStatement.substring(0, insertStatement.length() - 1) + ");";
                System.out.println(insertStatement);

                String sqlFilePath = "D:\\Novogene\\report_en7\\classes\\artifacts\\TESTREPORT\\SQL\\" + "13485" + ".sql"; // SQL文件保存路径
                FileWriter writer = new FileWriter(sqlFilePath);
                writer.write(insertStatement);
                writer.flush();
                writer.close();

                System.out.println("13486" + ".sql file generated successfully.");
//                SFTPUploader.hnzlUploaded(sqlFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
