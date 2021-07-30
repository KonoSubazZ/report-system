package com.novo.report.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.novo.report.beans.NkbChemicalDrugAnnotation;
import com.novo.report.beans.NkbVariantTreatmentAnnotation;
import com.novo.report.service.NkbVariantTreatmentAnnotationVwService;
import com.novo.report.service.SampleFileService;
import com.novo.report.service.SpecimenHeadService;
import com.novo.report.service.SystemPropertyService;
import com.novo.report.utils.RemoteShellExecutor;
import com.novo.report.utils.SecurityKey;

public class LoadOcacleListener implements ServletContextListener {
	private static Properties prop = new Properties();  
	static {
		InputStream inStream = LoadOcacleListener.class.getClassLoader().getResourceAsStream("user.properties");
		try {
			prop.load(inStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private SpecimenHeadService specimenHeadService;
	private SampleFileService sampleFileService;
	private NkbVariantTreatmentAnnotationVwService nkbVariantTreatmentAnnotationVwService;
	private SystemPropertyService systemPropertyService;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		 WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());  
		 systemPropertyService = (SystemPropertyService) springContext.getBean("systemPropertyServiceImpl");  
		 specimenHeadService = (SpecimenHeadService) springContext.getBean("specimenHeadServiceImpl");  
		 sampleFileService = (SampleFileService) springContext.getBean("sampleFileServiceImpl");
		 nkbVariantTreatmentAnnotationVwService = (NkbVariantTreatmentAnnotationVwService) springContext.getBean("nkbVariantTreatmentAnnotationVwServiceImpl");  
		/* Integer count = specimenHeadService.count();
		 System.out.println(count);*/
		 final String progPath = this.getClass().getClassLoader().getResource("/").getPath().replace("/report/WEB-INF/classes/", "");
		 final RemoteShellExecutor executor = new RemoteShellExecutor(prop.getProperty("local.host"), prop.getProperty("local.username"), prop.getProperty("local.password"));
		 final RemoteShellExecutor targetExecutor = new RemoteShellExecutor(prop.getProperty("cluster.host"), prop.getProperty("cluster.username"), prop.getProperty("cluster.password"));
		 TimerTask task = new TimerTask() {  
	            @Override  
	            public void run() {  
	            	try {
	            		//=================================================扫描集群数据到生产服务器data_file_status=======================================
	            		String analysis_date =new SimpleDateFormat("yyyyMMdd").format(new Date());
//	            		System.out.println("自动扫描集群数据	"+new Date());
//	            		String path="java -jar "+systemPropertyService.getPropertyValueByPropertyName("LOADER_PATH")+"/NovoLoader.jar daemon=no uploading=yes property_file="+systemPropertyService.getPropertyValueByPropertyName("LOADER_PATH")+"/novoloader.properties dateFileter="+analysis_date+" &";
//	        			System.out.println(targetExecutor.exec(path));
	            		//=================================================更新lims sample_file==========================================================
	            		/*
	            		sampleFileService.RefulshLims(); 
	            		System.out.println("我更新LIMS了");
	            		*/
	            		//==========================================导出omics、novomics、nkb的sql脚本并备份到192.168.50.3==============================================
	            		System.out.println("即将备份数据库");
	            		String dumpOmicsCommand = String.format("mysqldump -u%s -p%s omics -h%s -P%s > ~/.sql_backup/omics_%s.sql", prop.getProperty("databases.username"), prop.getProperty("databases.password"), prop.getProperty("databases.host"), prop.getProperty("databases.port"), analysis_date);
	            		//String dumpNovomicsCommand = String.format("/usr/local/mysql/bin/mysqldump -u%s -p%s novomics >  ~/.sql_backup/novomics_%s.sql",prop.getProperty("databases.username"), prop.getProperty("databases.password"),analysis_date);
		        		//String dumpNkbCommand = String.format("/usr/local/mysql/bin/mysqldump -u%s -p%s nkb > ~/.sql_backup/nkb_%s.sql",prop.getProperty("databases.username"), prop.getProperty("databases.password"),analysis_date);
	            		System.out.println(systemPropertyService.getPropertyValueByPropertyName("BACKUP_PATH"));
		        		String scpSqlFileCommand = String.format("scp ~/.sql_backup/*.sql %s@%s:%s/sql",prop.getProperty("cluster.username"), prop.getProperty("cluster.host"),systemPropertyService.getPropertyValueByPropertyName("BACKUP_PATH"));
		        		String delLocalSqlFile = "rm -rf ~/.sql_backup/*.sql";
		        		executor.exec(dumpOmicsCommand);
		        		executor.exec(scpSqlFileCommand);
		        		executor.exec(delLocalSqlFile);
		        		System.out.println("数据库已备份");
		        		/*String dumpNovomicsCommand = "/usr/local/mysql/bin/mysqldump -uroot -proot263 novomics > /home/sqlfile/novomics_"+analysis_date+".sql";
		        		String dumpNkbCommand = "/usr/local/mysql/bin/mysqldump -uroot -proot263 nkb > /home/sqlfile/nkb_"+analysis_date+".sql";
		        		String scpSqlFileCommand = "scp /home/sqlfile/*.sql qianxiaofen@192.168.50.3:/nas01/CR/medical_database/";
		        		String delLocalSqlFile = "rm -rf /home/sqlfile/*.sql";
		        		executor.exec(dumpOmicsCommand);
		        		executor.exec(dumpNovomicsCommand);
		        		executor.exec(dumpNkbCommand);
		        		executor.exec(scpSqlFileCommand);
		        		executor.exec(delLocalSqlFile);*/
	            		//=====================================================删除10天前产生的sql文件============================================================
		        		Date date = getdate(-10);
		        		String tenDaysAgo = new SimpleDateFormat("yyyyMMdd").format(date);
		        		String delSqlFileCommand="rm -rf " + systemPropertyService.getPropertyValueByPropertyName("BACKUP_PATH") + "/sql/omics_"+tenDaysAgo+".sql";
		        		System.out.println(delSqlFileCommand);
		        		targetExecutor.exec(delSqlFileCommand);
	            		//=====================================================产生的报告文件备份============================================================
		        		String scpTemplateCommand = String.format("scp -r %s/TESTREPORT %s@%s:%s/report", progPath, prop.getProperty("cluster.username"), prop.getProperty("cluster.host"), systemPropertyService.getPropertyValueByPropertyName("BACKUP_PATH"));
		        		executor.exec(scpTemplateCommand);
		        		/*String scpTemplateCommand = "scp -r /usr/local/tomcat/webapps/TESTREPORT qianxiaofen@192.168.50.3:/nas01/CR/medical_database/";
		        		executor.exec(scpTemplateCommand);*/
	            		//==================================================向snkb同步数据===========================================================
		        		/*System.out.println(new Date() + "向snkb同步数据");
		        		List<NkbVariantTreatmentAnnotation> nkbVariantTreatmentAnnotationList=nkbVariantTreatmentAnnotationVwService.getNkbVariantTreatmentAnnotationList();
		        		List<NkbChemicalDrugAnnotation> nkbChemicalDrugAnnotationList=nkbVariantTreatmentAnnotationVwService.getNkbChemicalDrugAnnotationList();
		        		String table="omics.nkb_variant_treatment_annotation_vw";
	            		String table1="omics.nkb_variant_treatment_annotation";
	            		String table2="snkb.nkb_variant_treatment_annotation";
	            		String table3="snkb.disease";
	            		String table4="snkb.drug";
	            		String table5="snkb.gene_variant";
	            		String table6="snkb.clinical_trial";
	            		String table7="snkb.ncbi_gene";
	            		String table8="nkb.disease";
	            		String table9="nkb.drug";
	            		String table10="nkb.gene_variant";
	            		String table11="nkb.clinical_trial";
	            		String table12="nkb.ncbi_gene";
	            		String table13="snkb.gene_variant_evw";
	            		String table14="nkb.gene_variant_evw";
	            		String table15="nkb.onco_gene_variant_desc_vw";
	            		String table16="snkb.onco_gene_variant_desc_vw";
	            		String table17="nkb.allele";
	            		String table18="snkb.allele";
	            		String table19="omics.nkb_chemical_drug_annotation_vw";
	            		String table20="omics.nkb_chemical_drug_annotation";
	            		String table21="snkb.nkb_chemical_drug_annotation";
	            		String table22="nkb.gene_variant_parent_vw";
	            		String table23="snkb.gene_variant_parent_vw";
	            		String table24="nkb.transcript";
	            		String table25="snkb.transcript";
	            		String table26="nkb.false_positive";
	            		String table27="snkb.false_positive";
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table1);//清空表数据
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table2);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table3);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table4);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table5);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table6);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table7);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table13);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table16);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table18);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table20);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table21);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table23);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table25);
	            		nkbVariantTreatmentAnnotationVwService.TruncateTable(table27);
	            		System.out.println(new Date() + "已清空表数据");
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table1,table);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table3,table8);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table4,table9);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table5,table10);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table6,table11);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table7,table12);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table13,table14);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table16,table15);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table18,table17);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table20,table19);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table23,table22);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table25,table24);
	            		nkbVariantTreatmentAnnotationVwService.InsertTable(table27,table26);
	            		System.out.println(new Date() + "已插入普通表数据");
		            	SecurityKey sk = new SecurityKey();// 使用默认密钥  
		            	System.out.println(new Date() + "再次清空加密表数据");
		            	nkbVariantTreatmentAnnotationVwService.TruncateTable(table2);
		            	nkbVariantTreatmentAnnotationVwService.TruncateTable(table21);
		            	
		            	for (NkbVariantTreatmentAnnotation nal: nkbVariantTreatmentAnnotationList) {
							nal.setGene_symbol(sk.encrypt(nal.getGene_symbol()));
							nal.setGene_variant(sk.encrypt(nal.getGene_variant()));
							nal.setVariant_desc(sk.encrypt(nal.getVariant_desc()));
							nal.setAnnotation_type(sk.encrypt(nal.getAnnotation_type()));
							nal.setDrug_name_chinese(sk.encrypt(nal.getDrug_name_chinese()));
							nal.setApproved_indication(sk.encrypt(nal.getApproved_indication()));
							nal.setDisease_name_chinese(sk.encrypt(nal.getDisease_name_chinese()));
							nal.setRelationship_category_chinese(sk.encrypt(nal.getRelationship_category_chinese()));
							nal.setRelationship_chinese(sk.encrypt(nal.getRelationship_chinese()));
							nal.setAnnotation_chinese(sk.encrypt(nal.getAnnotation_chinese()));
							nal.setEvidence_type(sk.encrypt(nal.getEvidence_type()));
							nal.setEvidence_phase(sk.encrypt(nal.getEvidence_phase()));
							nal.setEvidence_phase_chinese(sk.encrypt(nal.getEvidence_phase_chinese()));
							nal.setEvidence_ranking(sk.encrypt(nal.getEvidence_ranking()));
							nal.setEvidence_ranking_chinese(sk.encrypt(nal.getEvidence_ranking_chinese()));
							nal.setChecking_status(sk.encrypt(nal.getChecking_status()));
							nkbVariantTreatmentAnnotationVwService.InsertNkbVariantTreatmentAnnotation(nal);
						}
		            	System.out.println(new Date() + "已插入加密表数据:"+table2);
		            	for (NkbChemicalDrugAnnotation ncda : nkbChemicalDrugAnnotationList) {
		            		ncda.setDrug_name(sk.encrypt(ncda.getDrug_name()));
		            		ncda.setGene(sk.encrypt(ncda.getGene()));
		            		ncda.setRs_id(sk.encrypt(ncda.getRs_id()));
		            		ncda.setGenotype(sk.encrypt(ncda.getGenotype()));
		            		ncda.setAllele_info(sk.encrypt(ncda.getAllele_info()));
		            		ncda.setAnnotation_chinese(sk.encrypt(ncda.getAnnotation_chinese()));
		            		ncda.setToxicity(sk.encrypt(ncda.getToxicity()));
		            		ncda.setEfficacy(sk.encrypt(ncda.getEfficacy()));
		            		ncda.setCancer_type_chinese(sk.encrypt(ncda.getCancer_type_chinese()));
		            		nkbVariantTreatmentAnnotationVwService.InsertNkbChemicalDrugAnnotation(ncda);
		            	}
		            	System.out.println(new Date() + "已插入加密表数据:"+table21);*/
	            	} catch (Exception e) {
	            		e.printStackTrace();
	            	}
	            }  
	        };  
         Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.HOUR_OF_DAY, 02);   // 控制时  
         calendar.set(Calendar.MINUTE, 00);        // 控制分  
         calendar.set(Calendar.SECOND, 00);        // 控制秒  
         Date time = calendar.getTime();           // 执行任务的时间
         Timer timer = new Timer();  
         long intevalPeriod = 1000 * 60 * 60 * 24; // 间隔时间
         // 第一个参数 任务	第二个参数 执行任务初始时间	第三个参数间隔时间
         timer.scheduleAtFixedRate(task, time, intevalPeriod);
	}
	
	/**
	 * 获取当前日期前几天（后几天）的日期
	 * @param i  获取前后日期i为正数 向后推迟i天，负数时向前提前i天
	 * @return
	 */
	public static Date getdate(int i) {
		Date dat = null;
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, i);
		dat = cd.getTime();
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp date = Timestamp.valueOf(dformat.format(dat));
		return date;
	}
	
}
