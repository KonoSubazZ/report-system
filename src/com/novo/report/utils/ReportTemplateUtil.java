package com.novo.report.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ReportTemplateUtil {
	public static Report getFreeMarker(ReportTemplate rt, HttpSession session,Report pr) throws Exception {
		//1、创建一个模板文件
		//2、创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3、设置模板文件保存的目录
		//获取当前应用的路径
		String path = session.getServletContext().getRealPath("/");
		//String path=Class.class.getClass().getResource("/").getPath();
		//path = (path+"ftl").replace("\\","/");
		configuration.setDirectoryForTemplateLoading(new File(path+"ftl"));
		//4、模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5、加载一个模板文件，创建一个模板对象。
		Template template = configuration.getTemplate(rt.getTemplate_name()+".ftl","UTF-8");
		//6、创建一个数据集。可以是pojo也可以是map。推荐使用map
		Map<String,String> data = new HashMap<String,String>();
		//data.put("image", getImageStr());
		data.put("age", rt.getAge());
		data.put("client", rt.getClient());
		data.put("contact", rt.getContact());
		data.put("customer", rt.getCustomer());
		data.put("enterdate", rt.getEnterdate());
		data.put("barcode", rt.getBarcode());
		data.put("receiveddate", rt.getReceiveddate());
		data.put("reportreceiver", rt.getReportreceiver());
		data.put("patientname", rt.getPatientname());
		data.put("sex", rt.getSex());
		data.put("birthday", rt.getBirthday());
		data.put("diseasetype", rt.getDiseasetype());
		data.put("specimentype", rt.getSpecimentype());
		data.put("specimenquantity", rt.getSpecimenquantity());
		data.put("patientnumber", rt.getPatientnumber());
		data.put("hospital", rt.getHospital());
		data.put("collectedate", rt.getCollectdate());
		data.put("testedby", rt.getTestedby());
		data.put("checkedby", rt.getCheckedby());
		data.put("testeddate", rt.getTesteddate());
		data.put("checkeddate", rt.getCheckeddate());
		data.put("reportdate", rt.getReportdate());
		data.put("v600efrequency", rt.getV600efrequency());
		data.put("v600etestresult", rt.getV600etestresult());
		data.put("v600etestresult2", rt.getV600etestresult2());
		data.put("t790mfrequency", rt.getT790mfrequency());
		data.put("t790mtestresult", rt.getT790mtestresult());
		data.put("t790mtestresult2", rt.getT790mtestresult2());
		data.put("l858rfrequency", rt.getL858rfrequency());
		data.put("l858rtestresult", rt.getL858rtestresult());
		data.put("l858rtestresult2", rt.getL858rtestresult2());
		data.put("c797sfrequency", rt.getC797sfrequency());
		data.put("c797stestresult", rt.getC797stestresult());
		data.put("c797stestresult2", rt.getC797stestresult2());
		data.put("egfr19delfrequency", rt.getEgfr19delfrequency());
		data.put("egfr19deltestresult", rt.getEgfr19deltestresult());
		data.put("egfr19deltestresult2", rt.getEgfr19deltestresult2());
		data.put("tumorcellexpression", rt.getTumorcellexpression());
		data.put("immunocellexpression", rt.getImmunocellexpression());
		data.put("tumorcellexpressionpct", rt.getTumorcellexpressionpct());
		data.put("immunocellexpressionpct", rt.getImmunocellexpressionpct());
		data.put("tumorcelldyingstrenghth", rt.getTumorcelldyingstrenghth());
		data.put("immunocelldyingstrenghth", rt.getImmunocelldyingstrenghth());
		data.put("specimentestingpicture", rt.getSpecimentestingpicture());
		data.put("controltestingpicture", rt.getControltestingpicture());
		data.put("pdl1picdescription", rt.getPdl1picdescription());
		data.put("tumorpuritydescription", rt.getTumorpuritydescription());
		data.put("tumorpuritypictureone", rt.getTumorpuritypictureone());
		data.put("tumorpuritypicturetwo", rt.getTumorpuritypicturetwo());
		data.put("tumorpuritypicturethree", rt.getTumorpuritypicturethree());
		data.put("typingresult", rt.getTypingresult());
		data.put("lesionsampleresult", rt.getLesionsampleresult());
		data.put("checksampleresult", rt.getChecksampleresult());
		data.put("specimentypetwo", rt.getSpecimentypetwo());
		data.put("specimenquantitytwo", rt.getSpecimenquantitytwo());
		data.put("sex2", rt.getSex2());
		data.put("detectiongene", rt.getDetectiongene());
		data.put("detectionresult", rt.getDetectionresult());
		data.put("resultannotation", rt.getResultannotation());
		data.put("subbarcode", rt.getSubbarcode());
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
//		FileSystemView fsv = FileSystemView.getFileSystemView();
//		File file = fsv.getHomeDirectory();// 获取系统桌面位置
		String webappsPath = new File(path).getParent();
		File file = new File(webappsPath+"/TESTREPORT/"+rt.getPlatforms());
		if(!file.exists()){//如果有此文件,则不再创建
			file.mkdirs();
		}
		String fileName = rt.getBarcode()+" "+new SimpleDateFormat("yyyy-MM-dd_HH∶mm∶ss").format(new Date())+".doc";
		String filePath = (webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/"+fileName);
		pr.setReport_filename(fileName);
		pr.setReport_file_path(webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/");
		Writer out = new FileWriter(new File(filePath));
		//8、生成word文档 
		template.process(data, out);
		//9、关闭流
		out.close();
		return pr;
	}
}
