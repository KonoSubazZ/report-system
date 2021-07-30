package com.novo.report.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import sun.misc.BASE64Encoder;

public class WordTest {
	@Test
	public void testFreeMarker() throws Exception {
		//1、创建一个模板文件
		//2、创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3、设置模板文件保存的目录
		String path=Class.class.getClass().getResource("/").getPath();
		System.out.println(path);
		configuration.setDirectoryForTemplateLoading(new File(path));
		//4、模板文件的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//5、加载一个模板文件，创建一个模板对象。
		Template template = configuration.getTemplate("test.ftl","UTF-8");
		//6、创建一个数据集。可以是pojo也可以是map。推荐使用map
		Map<String,String> data = new HashMap<String,String>();
		//data.put("image", getImageStr());
		data.put("patientnumber", "sfs");
		data.put("hospital", "sfs");
		data.put("collectedate", "sfs");
		data.put("client", "111");
		data.put("contact", "小明");
		data.put("customer", "小明");
		data.put("enterdate", "小明");
		data.put("barcode", "小明");
		data.put("receiveddate", "小明");
		data.put("reportdate", "小明");
		data.put("reportreceiver", "小明");
		data.put("patientname", "小明");
		data.put("diseasetype", "小明");
		data.put("bloodmelting", "小明");
		data.put("sex", "小明");
		data.put("specimentype", "小明");
		data.put("birthday", "小明");
		data.put("specimenquantity", "小明");
		data.put("v600efrequency", "小明");
		data.put("l858rfrequency", "小明");
		data.put("l858rtestresult2", "小明");
		data.put("t790mfrequency", "小明");
		data.put("t790mtestresult", "小明");
		data.put("t790mtestresult2", "小明");
		data.put("c797sfrequency", "小明");
		data.put("c797stestresult", "小明");
		data.put("c797stestresult2", "小明");
		data.put("egfr19delfrequency", "小明");
		data.put("egfr19deltestresult", "小明");
		data.put("egfr19deltestresult2", "小明");
		data.put("testedby", "小明");
		data.put("checkedby", "小明");
		data.put("testeddate", "2017-04-26");
		data.put("checkeddate", "2017-04-26");
		data.put("tumorcellexpression", "有表达");
		data.put("immunocellexpression", "有表达");
		data.put("tumorcellexpressionpct", "60%");
		data.put("immunocellexpressionpct", "20%");
		data.put("tumorcelldyingstrenghth", "1+2-");
		data.put("immunocelldyingstrenghth", "2-3+");
		data.put("pdl1picdescription", "IHC 检测结果为有表达，60%的肿瘤细胞有表达，染色强度1-2+；肿瘤浸润免疫细胞无表达");
		data.put("tumorpuritydesc", "肿瘤细胞占比20%-30%。");
		/*data.put("tumorpuritypicturetwo", getImageStr("C:/Users/Administrator/Desktop/邵文富-1.JPG"));*/
		
		String tabsta = "<w:tbl><w:tblPr><w:tblStyle w:val=\"a14\"/><w:tblW w:w=\"9400\" w:type=\"dxa\"/><w:jc w:val=\"center\"/><w:tblInd w:w=\"0\" w:type=\"dxa\"/><w:tblBorders><w:top w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:left w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:bottom w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:right w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:insideH w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:insideV w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/></w:tblBorders><w:tblLayout w:type=\"Fixed\"/><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"108\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"108\" w:type=\"dxa\"/></w:tblCellMar></w:tblPr><w:tblGrid><w:gridCol w:w=\"1567\"/><w:gridCol w:w=\"1565\"/><w:gridCol w:w=\"1567\"/><w:gridCol w:w=\"1567\"/><w:gridCol w:w=\"1566\"/><w:gridCol w:w=\"1568\"/></w:tblGrid>";
        String a = "<w:tr><w:tblPrEx><w:tblBorders><w:top w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:left w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:bottom w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:right w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:insideH w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:insideV w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/></w:tblBorders><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"108\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"108\" w:type=\"dxa\"/></w:tblCellMar></w:tblPrEx><w:trPr><w:trHeight w:val=\"265\" w:h-rule=\"atLeast\"/><w:jc w:val=\"center\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"1567\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:fareast=\"宋体\" w:hint=\"fareast\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>基因列表</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1565\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:h-ansi=\"Times New Roman\" w:fareast=\"宋体\" w:cs=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:h-ansi=\"Times New Roman\" w:cs=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>";
		String b = "</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1567\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:h-ansi=\"Times New Roman\" w:fareast=\"宋体\" w:cs=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:h-ansi=\"Times New Roman\" w:cs=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>";
		String c = "</w:t></w:r></w:p></w:tc></w:tr>";
		String a1 = "<w:tr><w:tblPrEx><w:tblBorders><w:top w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:left w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:bottom w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:right w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:insideH w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/><w:insideV w:val=\"single\" w:sz=\"4\" wx:bdrwidth=\"10\" w:space=\"0\" w:color=\"auto\"/></w:tblBorders><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"108\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"108\" w:type=\"dxa\"/></w:tblCellMar></w:tblPrEx><w:trPr><w:trHeight w:val=\"265\" w:h-rule=\"atLeast\"/><w:jc w:val=\"center\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"1567\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:fareast=\"宋体\" w:hint=\"fareast\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/><w:b/><w:b-cs/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>检测结果</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1565\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:fareast=\"宋体\" w:hint=\"fareast\"/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>";
		String b1 = "</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1567\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:fareast=\"宋体\" w:hint=\"fareast\"/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>";
		String b2 = "</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1567\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/></w:tcPr><w:p><w:pPr><w:listPr><w:ilvl w:val=\"0\"/><w:ilfo w:val=\"0\"/></w:listPr><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:fareast=\"宋体\" w:hint=\"fareast\"/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/><w:color w:val=\"FF0000\"/><w:kern w:val=\"2\"/><w:sz w:val=\"18\"/><w:sz-cs w:val=\"18\"/><w:vertAlign w:val=\"baseline\"/><w:lang w:val=\"EN-US\" w:fareast=\"ZH-CN\"/></w:rPr><w:t>";
		String c1 = "</w:t></w:r></w:p></w:tc></w:tr>";
		String tabend = "</w:tbl>";
		
		String check_gene = "";
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list1 = new ArrayList<String>();
		list.add("ALK");
		list.add("BRAF");
		list.add("EGFR");
		list.add("HER2");
		list.add("KRAS");
		list1.add("未见变异");
		list1.add("未见变异");
		list1.add("有变异"); 
		list1.add("未见变异");
		list1.add("有变异");
		check_gene += tabsta;
		check_gene += a+list.get(0)+b+list.get(1)+b+list.get(2)+b+list.get(3)+b+list.get(4)+c;
		
		check_gene += a1+"a"+b1+"b"+b2+"c"+b1+"d"+b2+"e"+c1;
		check_gene += tabend;
		
		data.put("detectiongene", check_gene);
		//String detectiongene="<w:tbl><w:tblPr><w:tblStyle w:val=\"a6\"/><w:tblpPr w:leftFromText=\"180\" w:rightFromText=\"180\" w:vertAnchor=\"page\" w:horzAnchor=\"page\" w:tblpX=\"1631\" w:tblpY=\"1458\"/><w:tblOverlap w:val=\"Never\"/><w:tblW w:w=\"9476\" w:type=\"dxa\"/><w:tblInd w:w=\"2\" w:type=\"dxa\"/><w:tblBorders><w:top w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:left w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:bottom w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:right w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideH w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideV w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/></w:tblBorders><w:tblLayout w:type=\"Fixed\"/><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"0\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"0\" w:type=\"dxa\"/></w:tblCellMar></w:tblPr><w:tblGrid><w:gridCol w:w=\"1721\"/><w:gridCol w:w=\"1551\"/><w:gridCol w:w=\"1551\"/><w:gridCol w:w=\"1551\"/><w:gridCol w:w=\"1551\"/><w:gridCol w:w=\"1551\"/></w:tblGrid><w:tr><w:tblPrEx><w:tblBorders><w:top w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:left w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:bottom w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:right w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideH w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideV w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/></w:tblBorders><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"0\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"0\" w:type=\"dxa\"/></w:tblCellMar></w:tblPrEx><w:trPr><w:trHeight w:val=\"274\" w:h-rule=\"exact\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"1721\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:line=\"214\" w:line-rule=\"exact\"/><w:ind w:left=\"496\"/><w:jc w:val=\"center\"/><w:rPr><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>基因列表</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"390\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(0).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(1).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"390\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(2).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(3).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(4).getGene_Symbol()+"</w:t></w:r></w:p></w:tc></w:tr><w:tr><w:tblPrEx><w:tblBorders><w:top w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:left w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:bottom w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:right w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideH w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideV w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/></w:tblBorders><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"0\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"0\" w:type=\"dxa\"/></w:tblCellMar></w:tblPrEx><w:trPr><w:trHeight w:val=\"274\" w:h-rule=\"exact\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"1721\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:line=\"214\" w:line-rule=\"exact\"/><w:ind w:left=\"496\"/><w:jc w:val=\"center\"/><w:rPr><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:b/><w:sz w:val=\"18\"/></w:rPr><w:t>检测结果</w:t></w:r></w:p></w:tc>"++"</w:tr><w:tr><w:tblPrEx><w:tblBorders><w:top w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:left w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:bottom w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:right w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideH w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideV w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/></w:tblBorders><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"0\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"0\" w:type=\"dxa\"/></w:tblCellMar></w:tblPrEx><w:trPr><w:trHeight w:val=\"274\" w:h-rule=\"exact\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"1721\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:line=\"214\" w:line-rule=\"exact\"/><w:ind w:left=\"496\"/><w:jc w:val=\"center\"/><w:rPr><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>基因列表</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(5).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(6).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(7).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(8).getGene_Symbol()+"</w:t></w:r></w:p></w:tc><w:tc><w:tcPr><w:tcW w:w=\"1551\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"004271\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:before=\"20\"/><w:ind w:left=\"391\" w:right=\"391\"/><w:jc w:val=\"center\"/><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hint=\"default\"/><w:b/><w:color w:val=\"FFFFFF\"/><w:sz w:val=\"18\"/></w:rPr><w:t>"+gsl.get(9).getGene_Symbol()+"</w:t></w:r></w:p></w:tc></w:tr><w:tr><w:tblPrEx><w:tblBorders><w:top w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:left w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:bottom w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:right w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideH w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/><w:insideV w:val=\"single\" w:sz=\"2\" wx:bdrwidth=\"5\" w:space=\"0\" w:color=\"000000\"/></w:tblBorders><w:tblCellMar><w:top w:w=\"0\" w:type=\"dxa\"/><w:left w:w=\"0\" w:type=\"dxa\"/><w:bottom w:w=\"0\" w:type=\"dxa\"/><w:right w:w=\"0\" w:type=\"dxa\"/></w:tblCellMar></w:tblPrEx><w:trPr><w:trHeight w:val=\"274\" w:h-rule=\"exact\"/></w:trPr><w:tc><w:tcPr><w:tcW w:w=\"1721\" w:type=\"dxa\"/><w:shd w:val=\"clear\" w:color=\"auto\" w:fill=\"auto\"/><w:vAlign w:val=\"center\"/></w:tcPr><w:p><w:pPr><w:pStyle w:val=\"a5\"/><w:spacing w:line=\"214\" w:line-rule=\"exact\"/><w:ind w:left=\"496\"/><w:jc w:val=\"center\"/><w:rPr><w:b/><w:sz w:val=\"18\"/></w:rPr></w:pPr><w:r><w:rPr><w:b/><w:sz w:val=\"18\"/></w:rPr><w:t>检测结果</w:t></w:r></w:p></w:tc>"+gsl.get(5).getDetection_gene()+gsl.get(6).getDetection_gene()+gsl.get(7).getDetection_gene()+gsl.get(8).getDetection_gene()+gsl.get(9).getDetection_gene()+"</w:tr></w:tbl>";
		
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File file = fsv.getHomeDirectory();
		String filePath = (file+"/小明+"+new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())+".doc").replace("\\","/");
		Writer out = new FileWriter(new File(filePath));
		//8、生成word文档 
		template.process(data, out);
		//9、关闭流
		out.close();
	}
	@Test
	private String getImageStr(String imgFile) {
		 InputStream in = null;
		 byte[] data = null;
		 try {
			 in = new FileInputStream(imgFile);
			 data = new byte[in.available()];
			 in.read(data);
			 in.close();
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
		 BASE64Encoder encoder = new BASE64Encoder();
		 return encoder.encode(data);
	 }
	
	
}
