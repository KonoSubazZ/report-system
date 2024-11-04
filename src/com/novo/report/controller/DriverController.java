package com.novo.report.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import com.novo.report.utils.IpUtil;
import com.novo.report.utils.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.DataFileStatusPageBean;
import com.novo.report.beans.NkbChemicalDrugAnnotation;
import com.novo.report.beans.NkbVariantTreatmentAnnotation;
import com.novo.report.interceptor.LoadOcacleListener;
import com.novo.report.service.DriverService;
import com.novo.report.service.NkbVariantTreatmentAnnotationVwService;
import com.novo.report.service.SystemPropertyService;
import com.novo.report.utils.RemoteShellExecutor;
import com.novo.report.utils.SecurityKey;

@Controller
@RequestMapping("driver")
public class DriverController {
    private static Properties prop = new Properties();

    static {
        InputStream inStream = DriverController.class.getClassLoader().getResourceAsStream("user.properties");
        try {
            prop.load(inStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Autowired
    private SystemPropertyService systemPropertyService;

    @Autowired
    private NkbVariantTreatmentAnnotationVwService nkbVariantTreatmentAnnotationVwService;

    @Autowired
    private DriverService driverService;

    @RequestMapping("driverUploadData")
    public String DriverUploadDataController(HttpSession session) {
        return "ngs/driverUploadData";
    }

    @RequestMapping("scanUpload")
    @ResponseBody
    public Object scanUpload(String analysis_date, HttpSession session) {
        List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
        if (ips.contains(ServerConfig.getServerFormalIP()) || ips.contains(ServerConfig.getServerTestIP())) {
            //扫描集群
            final RemoteShellExecutor executor = new RemoteShellExecutor(prop.getProperty("cluster.host"), prop.getProperty("cluster.username"), prop.getProperty("cluster.password"));
            String path = "java -jar " + systemPropertyService.getPropertyValueByPropertyName("LOADER_PATH") + "/NovoLoader.jar daemon=no uploading=yes property_file=" + systemPropertyService.getPropertyValueByPropertyName("LOADER_PATH") + "/novoloader.properties dateFileter=" + analysis_date + " &";
            System.out.println(path);
            try {
                executor.exec(path);
                return true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        } else {
            //扫描本地
            List<String> cmd = new ArrayList<String>();
            cmd.add("java");
            cmd.add("-jar");
            cmd.add(systemPropertyService.getPropertyValueByPropertyName("LOADER_PATH")+"/NovoLoader.jar");
            cmd.add("daemon=no");
            cmd.add("uploading=yes");
            cmd.add("property_file="+systemPropertyService.getPropertyValueByPropertyName("LOADER_PATH")+"/novoloader.properties");
            cmd.add("dateFileter="+analysis_date);
            cmd.add("&");
            String[] cmds = new String[cmd.size()];
            cmd.toArray(cmds);
            System.err.println(cmd.toString());

            try {
//			executor.exec(path);
//                Runtime.getRuntime().exec(cmds);
                Process p = Runtime.getRuntime().exec(cmds);
                final InputStream is1 = p.getInputStream();
                new Thread(() -> {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                    try{
                        while(br.readLine() != null) ;
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                InputStream is2 = p.getErrorStream();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
                while(br2.readLine() != null){}
                int i = p.waitFor();
                System.out.println(i);
                return true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
    }

    @RequestMapping("getDataFileStatusByAnalysisDate")
    @ResponseBody
    private Object getDataFileStatusByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean) {
        dataFileStatusPageBean.setPageNo((dataFileStatusPageBean.getPageNo() - 1) * dataFileStatusPageBean.getPageSize());
        return driverService.getDataFileStatusByAnalysisDate(dataFileStatusPageBean);
    }

    @RequestMapping("deleteParseFile")
    @ResponseBody
    private Object deleteParseFile(Integer file_id) {
        boolean flag = true;
        try {
            driverService.deleteParseFile(file_id);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    //更新omics.nkb_variant_treatment_annotation 和snkb.nkb_variant_treatment_annotation 表
    @RequestMapping("updateNovomicsData")
    @ResponseBody
    public Object updateNovomicsData() {
        try {
            String table = "omics.nkb_variant_treatment_annotation_vw";
            String table1 = "omics.nkb_variant_treatment_annotation";
            String table2 = "snkb.nkb_variant_treatment_annotation";
            String table3 = "snkb.disease";
            String table4 = "snkb.drug";
            String table5 = "snkb.gene_variant";
            String table6 = "snkb.clinical_trial";
            String table7 = "snkb.ncbi_gene";
            String table8 = "nkb.disease";
            String table9 = "nkb.drug";
            String table10 = "nkb.gene_variant";
            String table11 = "nkb.clinical_trial";
            String table12 = "nkb.ncbi_gene";
            String table13 = "snkb.gene_variant_evw";
            String table14 = "nkb.gene_variant_evw";
            String table15 = "nkb.onco_gene_variant_desc_vw";
            String table16 = "snkb.onco_gene_variant_desc_vw";
            String table17 = "nkb.allele";
            String table18 = "snkb.allele";
            String table19 = "omics.nkb_chemical_drug_annotation_vw";
            String table20 = "omics.nkb_chemical_drug_annotation";
            String table21 = "snkb.nkb_chemical_drug_annotation";
            String table22 = "nkb.gene_variant_parent_vw";
            String table23 = "snkb.gene_variant_parent_vw";
            String table24 = "nkb.transcript";
            String table25 = "snkb.transcript";
            String table26 = "nkb.false_positive";
            String table27 = "snkb.false_positive";
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
            System.out.println("已清空表数据");
            nkbVariantTreatmentAnnotationVwService.InsertTable(table1, table);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table3, table8);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table4, table9);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table5, table10);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table6, table11);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table7, table12);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table13, table14);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table16, table15);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table18, table17);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table20, table19);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table23, table22);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table25, table24);
            nkbVariantTreatmentAnnotationVwService.InsertTable(table27, table26);
            System.out.println("已插入普通表数据");
            List<NkbVariantTreatmentAnnotation> nkbVariantTreatmentAnnotationList = nkbVariantTreatmentAnnotationVwService.getNkbVariantTreatmentAnnotationList();
            List<NkbChemicalDrugAnnotation> nkbChemicalDrugAnnotationList = nkbVariantTreatmentAnnotationVwService.getNkbChemicalDrugAnnotationList();
            SecurityKey sk = new SecurityKey();// 使用默认密钥
            for (NkbVariantTreatmentAnnotation nal : nkbVariantTreatmentAnnotationList) {
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
            System.out.println("已插入加密表数据:" + table2);
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
            System.out.println("已插入加密表数据:" + table21);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    @RequestMapping("deletePendingAndError")
    @ResponseBody
    private boolean deletePendingAndError(String analysis_date) {
        boolean flag = true;
        try {
            driverService.deletePendingAndError(analysis_date);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
