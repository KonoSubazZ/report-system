package com.novo.report.utils;

import net.sf.json.JSONObject;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class YFWebserviceProxyUtils {
    public static String httpURLGETCase() {
        String methodUrl = "http://bus.yinfenggene.com:9201/api/token";
        String appId = "109765B1FC5F5A643419A47CDC0C30B2";
        String appSecret = "157F9E173360C03D8E87C5A100C3B5B0";
        String lastAppSecret = AppSecret(appSecret);
        String parameter = "?appId=" + appId + "&appSecret=" + lastAppSecret;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line = null;
        String token = "";
        try {
            URL url = new URL(methodUrl + parameter); // 参数
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
                String s = result.toString();
                JSONObject jsonObject = JSONObject.fromObject(s);
                JSONObject data = (JSONObject)jsonObject.get("data");
                token = data.get("token").toString();
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
        return token;
    }

    private static String AppSecret(String appSecret) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = appSecret + dateFormat.format(date);
        String lastAppSecret = MD5Util.MD5(format).toUpperCase();
        return lastAppSecret;
    }

    public static String httpGet(String token) throws Exception {
        String resp= null;
        JSONObject obj = new JSONObject();
/*        obj.put("tmbh", "20TDM025548");
        obj.put("testno", "BRCA1/2遗传风险筛查（天津）");*/
        obj.put("tmbh", "20TDM023364");
        obj.put("testno", "1280基因");
        String query = obj.toString();
//        System.out.println("发送到URL的报文为：");
//        System.out.println(query);
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("lims-api-token", token);
        try {
//            String lastUrl = "http://bus.yinfenggene.com:9201/api/GetSampleInfo/yflims/GetSampleInfoAll?testno="+URLEncoder.encode("BRCA1/2遗传风险筛查（天津）", "utf-8")+"&tmbh=20TDM025548";
//            System.out.println(lastUrl);
            URL url = new URL("http://bus.yinfenggene.com:9201/api/GetSampleInfo/yflims/GetSampleInfoAll"); //url地址
//            URL url = new URL("http://bus.yinfenggene.com:9201/api/GetSampleInfo/yflims/GetSampleInfoAll?tmbh=20TDM025548&testno="+URLEncoder.encode("BRCA1/2遗传风险筛查（天津）", "utf-8")); //url地址
            System.out.println(url);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
//            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-type","application/json; charset=utf-8");
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            connection.connect();

            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
            List<Field> fields = new ArrayList<>() ;
            Class tempClass = connection.getClass();
            while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
                fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
            }
            for (Field field : fields) {
                if ("method".equals(field.getName())){
                    field.setAccessible(true);
                    field.set(connection,"GET");
                }
            }
            out.print(query);
            out.flush();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String lines;
                StringBuffer sbf = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbf.append(lines);
                }
                System.out.println("银丰编号："+obj.get("tmbh")+",银丰项目名称："+obj.get("testno")+",返回来的报文："+sbf.toString());
                resp = sbf.toString();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
//            JSONObject json = (JSONObject) JSON.parse(resp);
        }
        return resp;
    }

    public static void main(String[] args) {
        try {
            String sample = httpGet(httpURLGETCase());
            /*JSONObject resultData = JSONObject.fromObject(JSONObject.fromObject(sample).get("resultData").toString());
            String tmbh = resultData.get("tmbh").toString();
            String name = resultData.get("name").toString();
            String sex = resultData.get("sex").toString();
            String age = resultData.get("age").toString();
            String idcard = resultData.get("idcard").toString();
            String testno = resultData.get("testno").toString();
            String diseasetype = resultData.get("diseasetype").toString();
            String pathologicaltype = resultData.get("pathologicaltype").toString();
            String surgeryhistory = resultData.get("surgeryhistory").toString();
            String testhistory = resultData.get("testhistory").toString();
            String medicationhistory = resultData.get("medicationhistory").toString();
            JSONArray jsonArray = JSONArray.fromObject(resultData.get("children").toString());
            Object o = jsonArray.get(0);
            System.out.println(o.toString());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
