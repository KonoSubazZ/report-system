package com.novo.report.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;
import java.util.Map;

public class Jdbc {

    /*private static JdbcTemplate jdbcTemplate;
    static {
        String driver = "com.mysql.jdbc.Driver";//mysql驱动
        String url ="jdbc:mysql://47.94.106.94:3306/ngs";//连接地址
        String user ="root";//用户
        String password ="root263";//密码

        DriverManagerDataSource dataSource=new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        String sql ="select * from ngs_qrcode where subbarcode = '110';";//student 数据库表明
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
    }*/

    private static JdbcTemplate jdbcTemplate;
    static {
        String driver = "com.mysql.jdbc.Driver";//mysql驱动
        String url ="jdbc:mysql://172.20.1.34:8806/omics";//连接地址
        String user ="novo";//用户
        String password ="GodIsLove";//密码

        DriverManagerDataSource dataSource=new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {
        String sql ="select sarcoma_subtype from mm_sarcoma_typing where report_id = '10710' and mutation = 'MDM2 Amplification';";//student 数据库表明
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            String[] sarcoma_subtypes = map.get("sarcoma_subtype").toString().split("\n");
            for (String sarcoma_subtype : sarcoma_subtypes) {
                System.out.println(sarcoma_subtype);
            }
        }
    }
}
