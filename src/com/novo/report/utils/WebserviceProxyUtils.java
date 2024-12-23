package com.novo.report.utils;

import com.novo.report.beans.CurrentNgsAvailableData;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WebserviceProxyUtils {
    public static String httpURLGETCase(String methodUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line = null;
        String s = "";
        try {
            URL url = new URL(methodUrl); // 参数
            // 根据URL生成HttpURLConnection
            connection = (HttpURLConnection) url.openConnection();
            // 默认GET请求
            connection.setRequestMethod("GET");
            // 建立TCP连接
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 发送http请求
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder result = new StringBuilder();
                // 循环读取流
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.getProperty("line.separator"));
                }
                s = result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return s;
    }

    public static String sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
			/*if (isproxy) {// 使用代理模式
				@SuppressWarnings("static-access")
				Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				conn = (HttpURLConnection) realUrl.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) realUrl.openConnection();
			}*/
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); //POST方法

            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void status(String subbarcode, String status_type, String status) {
//        String result = httpURLGETCase("http://192.168.200.82/index.php/Api/Reportid/report/subbarcode/"+ subbarcode +"/"+status_type+"/"+ status);
//        String result = httpURLGETCase("http://10.168.4.236/index.php/Api/Reportid/report/subbarcode/"+ subbarcode +"/"+status_type+"/"+ status);
        String result = httpURLGETCase("http://172.20.1.34/index.php/Api/Reportid/report/subbarcode/"+ subbarcode +"/"+status_type+"/"+ status);
        System.out.println(subbarcode+"样本发送"+status+"状态结果："+result);
    }

    /**
     * 更新样本状态到新系统
     * @param currentNgsAvailable
     *
     */
    public static void updateStatus(CurrentNgsAvailableData currentNgsAvailable) {
        String user = currentNgsAvailable.getUser();
        String product = currentNgsAvailable.getProduct_name();
        String subbarcode = currentNgsAvailable.getSubbarcode();
        Integer reportId = currentNgsAvailable.getReport_id();
        String date = getCurrentDateFormatted();
        String res = httpURLGETCase("http://10.1.181.174:9098/report/update_sample_report_status/" + user + "/" + date + "/" + product + "/" + subbarcode + "/" + reportId);
        System.out.println("更新样本状态到新系统结果："+res);
    }

    /**
     * 生成当前日期并格式化为 yyyyMMdd
     * @return 格式化后的日期字符串
     */
    public static String getCurrentDateFormatted() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return currentDate.format(formatter);
    }

    public static void main(String[] args) {
        try {
//            String s = httpURLGETCase("http://10.1.181.174:9090/update_report_status/test/TKHS230045160-3A");
//            String s = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/qrcode/client/Wright1/subbarcode/TKHS230060742-1A/product_name/双样本-DNA损伤修复组织45基因分子分型研究/username/3/qrcode/888/report_date/2023-12-12 00:00:00");
//            String s = httpURLGETCase("http://10.1.181.174:9090/create_xiao_report/78260");
            /*String s = sendPost("http://10.1.181.174:9099/create_xiao_report/", "report_detail={\"k\":1,\"v\":\"1\"}&template_name=dasdada999");
            JSONObject object= JSONObject.fromObject(s);
            String file_path = object.get("file_path").toString();
            System.out.println(file_path);*/
//            String s = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/client/333/subbarcode/9999999/report_name/2/report_status/999/report_url/888/product_status/99/product_url/99/report_date/2022-11-15%2000:00:00");
//            String s = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/subbarcode/LKHS210000450-1A/product_status/测试3/");
//            String s = WebserviceProxyUtils.httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/subbarcode/LKHS210000450-1A/product_status/生信审核");
//            status("LKHS210000450-1A", "product_status", "1");
//            String s = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/subbarcode/55/report_status/2");
//            String s = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/subbarcode/55/product_url/3");
//            String s1 = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/subbarcode/55/report_name/4");
//            String s = httpURLGETCase("http://qrcode.novogene.com/index.php/Api/Reportid/report/subbarcode/LKHS210000450-1A/client/11115");
//            System.out.println(s);
//            status("LKHS210000450-1A", "product_status", "生信审核1");
//            status("LKHS210000450-1A", "client", "11115");
//            status("LKHS210000450-1A", "report_name", "11115");
//            status("FS022312170057", "product_status", "生信审核");
            status("FS022312170057", "report_status", "报告审核通过");
//            status("LKHS210000450-1A", "report_status", "报告审核未通过");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
