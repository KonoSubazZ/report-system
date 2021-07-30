package com.novo.report.utils;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.novo.report.webservices.GenericServicesSoap;
import com.novo.report.webservices.GenericServicesSoapProxy;

public class LimsWebserviceProxyUtils {
	private static Log log = LogFactory.getLog(LimsWebserviceProxyUtils.class);
	public static Object getLimsWebserviceProxy(String webservice_url){
		try {
			//获取代理对象
			GenericServicesSoapProxy proxy = new GenericServicesSoapProxy (webservice_url);
			GenericServicesSoap soap = proxy.getGenericServicesSoap();
			return soap;
		} catch (Exception e) {
			log.error(">>>>>>>获取LimsWebserviceProxy失败>>>>>>>>>>>>>>>>>>>>接口调用失败！！！！");
			e.printStackTrace();
			return false;
			
		}
	}
	public static void main(String[] args) {
		try {
			GenericServicesSoap proxy = (GenericServicesSoap) LimsWebserviceProxyUtils.getLimsWebserviceProxy("http://172.17.8.223/starlims11.novogene/services/generic.asmx?wsdl");
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			System.out.println(time);
			String[] parameters1 = {"NIZM180000009-1A"};
			String[] parameters2 = {"NKHS180060164-1A",time};
			String[] parameters3 = {"TKHS180011081-1A"};
			//获取姓名
			String name = (String) proxy.runActionDirect("WebServices.GetPersonInfo", parameters1, "SYSADM", "Lims1234");
			System.out.println("webService 根据样本编号获取姓名："+name);
			//传递邮件发送状态
			Object result = proxy.runActionDirect("WebServices.ReceiveReportInfo", parameters2, "SYSADM", "Lims1234");
			System.out.println("webService 回传样本编号及报告发送时间 获取返回值："+result);
			Object[] result2 = (Object[]) proxy.runActionDirect("WebServices.LimsSendFtpToNovo", parameters3, "SYSADM", "Lims1234");
			System.out.println(result2.length);
			if(result2.length>1){
				for (Object string : result2) {
					System.out.println((String)string);
				}
			}
			//System.out.println(result2);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

