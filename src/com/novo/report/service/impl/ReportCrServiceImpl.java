package com.novo.report.service.impl;

import com.novo.report.beans.*;
import com.novo.report.dao.two.*;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.LifeService;
import com.novo.report.service.ReportCrService;
import com.novo.report.utils.AES;
import com.novo.report.utils.TranslateUtil;
import javafx.util.Pair;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportCrServiceImpl implements ReportCrService {

    @Autowired
    private AnalysisReportDao analysisReportDao;

    @Autowired
    private ReportVarDrugDao reportVarDrugDao;

    @Autowired
    private ReportDrugInfoDao reportDrugInfoDao;

    @Autowired
    private ReportClinicalTrialDao reportClinicalTrialDao;

    @Autowired
    private ReportUnknownVarDao reportUnknownVarDao;

    @Autowired
    private ComplexMutationService complexMutationService;

    @Autowired
    private LifeService lifeService;

    private String user;

    private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    /**
     * 获取用药信息（暂时理解体细胞突变都会匹配用药 胚系只有has_drug=1才会匹配用药 待确认）
     * 根据匹配规则获取用药信息 突变list diseaseIdList 病种id列表
     *
     * @param user
     * @param diseaseId           本癌种 id
     * @param a                   位点信息（基因 突变）
     * @param diseaseIdList       病种id列表-子父
     * @param parentdiseaseIdList 父级癌种id列表 这个是什么作用??
     * @param Flag                0 、1去知识库获取用药
     * @param lang
     * @param report_id
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @Override
    @Transactional
    //用药信息获取，支持修改
    public void handleDrugList(String user, Integer diseaseId, Map a, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList, Integer Flag, Integer lang, Integer report_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        this.user = user;
        TranslateUtil translateUtil = new TranslateUtil();
        String gene = a.get("gene").toString();
        String variant = a.get("variant").toString();
        String ori_variant = a.get("ori_variant").toString();
        String cosmic = a.get("cosmic") == null ? "." : a.get("cosmic").toString();
        String mutFreq = "0";
        if (a.get("mutFreq") != null) {
            mutFreq = a.get("mutFreq").toString().equals(".") ? "0" : a.get("mutFreq").toString();
        }

        // 获取位点突变id
        Integer mutationId = getMutationID(gene, variant);
        // 获取突变list
        List<Integer> mutationIdList = getMutIdList(mutationId);

        // 根据本地库的数据补充父mutID ？？什么作用
        // 如果本地记录包含父级mutID，则添加到mutationIdList中 （去重添加）
        // TODO 待确定 这里不会出现问题吗？ 比如知识库删除了某个突变的关联父级突变，增加本地库到 突变list 不会有问题吗
        String parent_mutID = a.get("parent_mutID") == null ? "-1" : a.get("parent_mutID").toString();
        if (!parent_mutID.equals("-1")) {
            String[] split = parent_mutID.split(",");
            for (String string : split) {
                if (!mutationIdList.contains(Integer.parseInt(string))) {
                    mutationIdList.add(Integer.parseInt(string));
                }
            }
        }
		/*
		List<String> complex_ids = (List<String>) a.get("complexIDs");
		if (complex_ids != null) {
			for (String id :  complex_ids) {
				mutationIdList.add(Integer.parseInt(id));
			}
		}
		*/
        // 获取性别
        String gender = lifeService.getGender(report_id) == null ? "" : lifeService.getGender(report_id);
        // 查询本地库，看该位点是否有靶向药物信息、一般来说只有一条
        List<ReportVarDrug> varDrugs = reportVarDrugDao.selectRecord(gene, ori_variant, diseaseId, lang, gender);
        // 查询本地库，未知临床意义或不报告的位点 ？？
        // TODO 待确定 为什么既查询 靶药表 ，又查询未知临床意义表（vus）
        Map varUnknown = reportUnknownVarDao.selectRpUnknownVar(gene, ori_variant, diseaseId, lang);
        ReportVarDrug reportVarDrug = null;

        // 本地库更新时间
        Timestamp reportVarDrugUpdateTime = new Timestamp(0);
        if (varDrugs == null || CollectionUtils.isEmpty(varDrugs)) {
            // 本地没有数据
            reportVarDrug = new ReportVarDrug();
            reportVarDrug.setGene(gene);
            reportVarDrug.setVariant(variant);
            reportVarDrug.setOri_variant(ori_variant);
            reportVarDrug.setDisease_id(diseaseId);
            reportVarDrug.setLang(lang);
            reportVarDrug.setGender(gender);
        } else {
            // 本地有数据,获取第一条，这里不知道有没有其他数据 所以用list 取第一条;
            reportVarDrug = varDrugs.get(0);

            // 将本地库的父mutID 拆分出来，并添加到mutationIdList中 (去重)
            if (reportVarDrug.getParent_mutID() != null && !"".equals(reportVarDrug.getParent_mutID())) {
                String parent_mutID1 = reportVarDrug.getParent_mutID();
                String[] split = parent_mutID1.split(";");
                for (String string : split) {
                    if (!mutationIdList.contains(Integer.parseInt(string))) {
                        mutationIdList.add(Integer.parseInt(string));
                    }
                }
            }
            reportVarDrugUpdateTime = new Timestamp(reportVarDrug.getUpdate_date().getTime());
        }
        if (varUnknown != null) {
            Timestamp tempTime = new Timestamp(Long.valueOf((varUnknown.get("update_date").toString())) * 1000);
            if (reportVarDrugUpdateTime.before(tempTime)) {
                reportVarDrugUpdateTime = tempTime;
            }
        }
        //  InNKB 位点是否存在知识库
        if (mutationId != null) {
            a.put("InNKB", "true");
            a.put("mapped_variant_id", mutationId);
        } else {
            a.put("InNKB", "false");
        }

        // NKB更新时间 多个更新时间包括突变的描述、用药、基因描述
        Timestamp nkbUpdateTime = new Timestamp(0);
        nkbUpdateTime = getNkbUpdateTime(mutationIdList, gene, diseaseIdList);
        String has_drug = a.get("has_drug") == null ? "" : a.get("has_drug").toString();
        String unvariantDescription = "";
        if ("Amplification".equals(variant)) {
            unvariantDescription = "该变异为基因扩增，可能导致蛋白表达增加。";
        } else if ((variant.indexOf("fs") > -1 || variant.indexOf("*") > -1 || variant.indexOf("+") > -1 || variant.indexOf("-") > -1) && !(variant.indexOf("Fusion") > -1)) {
            unvariantDescription = "该变异为失活突变，可能会导致蛋白功能缺失。";
        } else {
            unvariantDescription = "该突变临床意义未明，若导致蛋白功能异常，可能影响下游信号通路，参与肿瘤发生发展。";
        }
        Map variantDesc = getFirst(CollectionUtils.isEmpty(mutationIdList) ? new ArrayList<>() : analysisReportDao.getVariantDescription(mutationIdList, lang));
        String variantDescription = "";
        //突变说明
        String mutDesc = translateUtil.translate2(gene, ori_variant, mutFreq);
        a.put("mutDesc2", mutDesc);
        //男性不输出女性生殖器官肿瘤及子级癌种、女性不输出男性生殖器官肿瘤及子级癌种；实体瘤不输出血液肿瘤及子级癌种、血液肿瘤不输出实体瘤及子级癌种
        List<Integer> sonIdList = complexMutationService.solidTumorFiltration(gender, diseaseIdList);

        if (reportVarDrugUpdateTime.before(nkbUpdateTime) || Flag != 0) { //知识库新
            fetchNkbDrugInfo(user, a, reportVarDrug, mutationIdList, diseaseIdList, parentdiseaseIdList, sonIdList, gene, variant, ori_variant, Flag, lang, diseaseId);
            //知识库位点说明
            if (reportVarDrug.getRecord_id() != null) {
                variantDescription = variantDesc == null ? "" : (variantDesc.get("description") == null ? "" : variantDesc.get("description").toString());
            } else {
                variantDescription = variantDesc == null ? unvariantDescription : (StringUtils.isEmpty(variantDesc.get("description")) ? unvariantDescription : variantDesc.get("description").toString());
            }
            a.put("mutDesc", mutDesc + variantDescription);
        } else { //本地库新
            if (reportVarDrug.getRecord_id() != null) {
                fetchLocalDrugInfo(user, a, reportVarDrug, lang, diseaseId, diseaseIdList, mutationIdList, parentdiseaseIdList);
                //本地库位点说明
                String varDrugNote = (String) a.get("varDrugNote");
                JSONArray array = JSONArray.fromObject(varDrugNote);
                Object o = array.get(2);
                if (o.toString().indexOf("突变说明:") != -1) {
                    array.remove(2);
                }
                JSONObject variantDescriptionJson = (JSONObject) array.get(2);
                String variantDescription1 = variantDescriptionJson.get("value") == null ? "" : variantDescriptionJson.get("value").toString();
                a.put("mutDesc", mutDesc + variantDescription1);
            } else {
                getUnknownVarInfo(user, diseaseId, diseaseIdList, a, gene, variant, ori_variant, lang);
                //位点说明
                variantDescription = variantDesc == null ? unvariantDescription : (StringUtils.isEmpty(variantDesc.get("description")) ? unvariantDescription : variantDesc.get("description").toString());
                a.put("mutDesc", mutDesc + variantDescription);
            }
        }
        a.put("variantDescription", variantDescription);
        setOrderNum(a);
    }

    //获取某突变是否出具报告，2为VUS， 3为不报告
    public Integer getvusLevel(String gene, String variant) {
        Integer mutationId = getMutationID(gene, variant);
        if (mutationId == null) {
            return 2;
        }
        String effect = analysisReportDao.getMutationEffect(mutationId);
        if ("activation".equals(effect) || "inactivation".equals(effect)) {
            return 2;
        } else {
            return 2;
        }
    }

    @Override
    public void matchNKBVarDrug(Map a, String gene, String variant, String ori_variant, Integer diseaseId, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList, List<Integer> sonIdList, Integer lang) {
        a.put("gene", gene);
        a.put("variant", variant);
        a.put("ori_variant", ori_variant);
        a.put("diseaseId", diseaseId);
        a.put("lang", lang);
        a.put("resultType", 0);
        Integer mutationId = getMutationID(gene, variant);
        List<Integer> mutationIdList = getMutIdList(mutationId);
        fetchNkbDrugInfo("empty", a, null, mutationIdList, diseaseIdList, parentdiseaseIdList, sonIdList, gene, variant, ori_variant, 0, lang, diseaseId);
    }

    public void setOrderNum(Map a) {
        Float orderNum = 0f;
        String mutFreq = "0";
        String gene = a.get("gene").toString();
        String variant = a.get("variant").toString();
        String resultTypeDesc = a.get("resultTypeDesc") != null ? a.get("resultTypeDesc").toString() : "";
        if (a.get("mutFreq") != null) {
            mutFreq = (a.get("mutFreq").toString().equals(".") || a.get("mutFreq").toString().equals("")) ? "0" : a.get("mutFreq").toString();
        }
        if (!(mutFreq.equals(".") || mutFreq.indexOf("合") != -1 || mutFreq.indexOf("H") != -1)) {
            if (mutFreq.endsWith("X")) {
                orderNum = 200 + Float.valueOf(mutFreq.substring(0, mutFreq.length() - 1));
            } else {
                orderNum = Float.valueOf(mutFreq);
            }
        }
        //cr的排在最前面
        if (a.get("has_drug") != null && a.get("has_drug").toString().equals("true")) {
            a.put("resultTypeVal", 1);
            a.put("resultTypeDesc", "靶向药物");
            a.put("rpUnknownVar", null);
            orderNum += 90000;
        } else if (resultTypeDesc.equals("靶向药物")) {
            if (gene.equals("Complex")) {
                orderNum += 50000;
            } else {
                orderNum += 70000;
                if (variant.indexOf("Amplification") != -1 || variant.indexOf("Deletion") != -1) {
                    orderNum += 300;
                } else if (variant.indexOf("Fusion") != -1) {
                    orderNum += 200;
                }
            }
        } else {
            orderNum += 10000;
        }
        //有cosmic的排在前面
        String cosmic = a.getOrDefault("cosmic", ".").toString();
        if (!".".equals(cosmic)) {
            orderNum += 100;
        }
        Set<Integer> orderNumSet = new HashSet<>();
        List<Map> drugList = (List<Map>) a.getOrDefault("drugList", new ArrayList<Map>());
        if (!CollectionUtils.isEmpty(drugList)) {
            for (Map map : drugList) {
                String approve_range = map.get("approve_range").toString();
                if ("1".equals(approve_range)) {
                    orderNumSet.add(9000);
                } else if ("2".equals(approve_range)) {
                    orderNumSet.add(8000);
                } else if ("3".equals(approve_range)) {
                    orderNumSet.add(7000);
                } else if ("4".equals(approve_range)) {
                    orderNumSet.add(6000);
                } else if ("5".equals(approve_range)) {
                    orderNumSet.add(5000);
                } else if ("6".equals(approve_range)) {
                    orderNumSet.add(4000);
                } else if ("7".equals(approve_range)) {
                    orderNumSet.add(3000);
                } else if ("8".equals(approve_range)) {
                    orderNumSet.add(2000);
                } else if ("9".equals(approve_range)) {
                    orderNumSet.add(1000);
                }
            }
            a.put("orderNum", orderNum + Collections.max(orderNumSet));
        } else {
            a.put("orderNum", orderNum);
        }
    }

    /**
     * 获取【vus】信息
     *
     * @param user
     * @param diseaseId
     * @param diseaseIdList
     * @param a
     * @param gene
     * @param variant
     * @param ori_variant
     * @param lang
     */
    public void getUnknownVarInfo(String user, Integer diseaseId, List<Integer> diseaseIdList, Map a, String gene, String variant, String ori_variant, Integer lang) {
        //查询知识库
        //获取基因说明和信号通路说明
        Map geneDesc = getFirst(analysisReportDao.getGeneDesc(gene, lang));
        String geneDescription = geneDesc.get("gene_description") == null ? "" : geneDesc.get("gene_description").toString();
        String pathwayDescription = geneDesc.get("pathway_description") == null ? "" : geneDesc.get("pathway_description").toString();
        Long geneDescription_updateTime = geneDesc.get("update_date") == null ? 0L : Long.valueOf(geneDesc.get("update_date").toString());
        //获取用药说明及预后和诊断说明
//		String clinicalInfo = "";
//		String drugAnnotation = "";
        Long geneAnnotation_updateTime = 0L;
        Long variantAnnotation_updateTime = 0L;
        if (!CollectionUtils.isEmpty(diseaseIdList)) {
            Map geneAnnotation = getFirst(analysisReportDao.getGeneAnnotationByIdList(gene, diseaseIdList, lang));
            Map variantAnnotation = getFirst(analysisReportDao.getVarAnnotationByIdList(gene, variant, diseaseIdList, lang));
//			clinicalInfo = geneAnnotation.get("clinical_annotation") == null ? "" : geneAnnotation.get("clinical_annotation").toString();
//			clinicalInfo += variantAnnotation.get("clinical_annotation") == null ? "" : variantAnnotation.get("clinical_annotation").toString();
//			drugAnnotation = geneAnnotation.get("drug_annotation") == null ? "" : geneAnnotation.get("drug_annotation").toString();
//			drugAnnotation += variantAnnotation.get("drug_annotation") == null ? "" : variantAnnotation.get("drug_annotation").toString();
            geneAnnotation_updateTime = geneAnnotation.get("update_date") == null ? 0L : Long.valueOf(geneAnnotation.get("update_date").toString());
            variantAnnotation_updateTime = variantAnnotation.get("update_date") == null ? 0L : Long.valueOf(variantAnnotation.get("update_date").toString());
        }
        //用药说明整合
		/*List<Pair<String, String>> listDrugNote = new ArrayList<Pair<String, String>>();
		listDrugNote.add(new Pair<>("预后和诊断说明:",clinicalInfo.trim()));
		listDrugNote.add(new Pair<>("用药说明:",drugAnnotation.trim()));
		String varDrugNote = JSONArray.fromObject(listDrugNote).toString();*/
        Integer level = getvusLevel(gene, variant);
        if ("empty".equals(user)) {
            a.put("resultTypeVal", level);
            if (level == 2) {
                a.put("resultTypeDesc", "未知临床意义");
            } else {
                a.put("resultTypeDesc", "不报告");
            }
            a.put("gene_description", geneDescription + pathwayDescription);
            //a.put("var_drug_desc", varDrugNote);
            return;
        }
        // 查询本地库--未知临床意义
        Map rpUnknownVar = reportUnknownVarDao.selectRpUnknownVar(gene, ori_variant, diseaseId, lang);
        Long rpUnknownVar_updateTime = 0L;
        if (rpUnknownVar == null || CollectionUtils.isEmpty(rpUnknownVar)) {
            rpUnknownVar = new HashMap<>();
            rpUnknownVar.put("gene", gene);
            rpUnknownVar.put("variant", variant);
            rpUnknownVar.put("ori_variant", ori_variant);
            rpUnknownVar.put("disease_id", diseaseId);
            rpUnknownVar.put("gene_description", geneDescription + pathwayDescription);
//			rpUnknownVar.put("var_drug_desc", varDrugNote);
            rpUnknownVar.put("update_by", user);
            if (level == 2) {
                rpUnknownVar.put("result_type", "未知临床意义");
            } else {
                rpUnknownVar.put("result_type", "不报告");
            }
            rpUnknownVar.put("lang", lang);
            rpUnknownVar.put("check_date", null);
            reportUnknownVarDao.insertRpUnknownVar(rpUnknownVar);
        } else {
            rpUnknownVar_updateTime = Long.valueOf(rpUnknownVar.get("update_date").toString());
            boolean changed = false;
            if (geneDescription_updateTime > rpUnknownVar_updateTime) {
                rpUnknownVar.put("gene_description", geneDescription + pathwayDescription);
                rpUnknownVar.put("check_date", null);
                changed = true;
            }
            if (geneAnnotation_updateTime > rpUnknownVar_updateTime || variantAnnotation_updateTime > rpUnknownVar_updateTime) {
//				rpUnknownVar.put("var_drug_desc", varDrugNote);
                rpUnknownVar.put("check_date", null);
                changed = true;
            }
            if (changed) {
                rpUnknownVar.put("update_by", user);
                rpUnknownVar.put("check_date", null);
                if (level == 2) {
                    rpUnknownVar.put("result_type", "未知临床意义");
                } else {
                    rpUnknownVar.put("result_type", "不报告");
                }
                reportUnknownVarDao.updateRpUnknownVar(rpUnknownVar);
            }
        }
        boolean changed = false;
        String old_result_Type = (rpUnknownVar.get("result_type") == null) ? "" : rpUnknownVar.get("result_type").toString();
        if ("3".equals(a.getOrDefault("resultTypeVal", "").toString())) {
            if (old_result_Type != "不报告") {
                rpUnknownVar.put("result_type", "不报告");
                rpUnknownVar.put("check_date", null);
                changed = true;
            }
        } else if ("2".equals(a.getOrDefault("resultTypeVal", "").toString()) || rpUnknownVar.get("result_type") == null) {
            if (old_result_Type != "未知临床意义") {
                rpUnknownVar.put("result_type", "未知临床意义");
                rpUnknownVar.put("check_date", null);
                changed = true;
            }
        }
        if (changed) reportUnknownVarDao.updateRpUnknownVar(rpUnknownVar);
        a.put("rpUnknownVar", rpUnknownVar);
        String resultType = rpUnknownVar.get("result_type").toString();
        a.put("check_date", rpUnknownVar.get("check_date"));
        if (rpUnknownVar.get("record_id") != null) {
            a.put("record_id", rpUnknownVar.get("record_id"));
        }
        if ("未知临床意义".equals(resultType)) {
            a.put("resultTypeVal", 2);
            a.put("resultTypeDesc", resultType);
        } else {
            a.put("resultTypeVal", 3);
            a.put("resultTypeDesc", resultType);
        }
    }

    //根据药物名称和癌种名称作为过滤条件,保留最高等级
    public String getDrugNameStr(List<Map> list, Map<String, Boolean> drugFlag, Set<String> drugSet) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<String> drugs = new ArrayList<String>();
        Set<String> redDrugSet = new HashSet<String>();
        for (Map map : list) {
            String drug_name = map.get("drug_name").toString();
            String other_test_required = map.get("other_test_required") == null ? "0" : map.get("other_test_required").toString();
            if (other_test_required.equals("1")) {
                redDrugSet.add(drug_name);
            }
        }
        for (Map map : list) {
            String drug_name = map.get("drug_name").toString();
            if (redDrugSet.contains(drug_name)) {
                map.put("other_test_required", "1");
            }
        }
        for (Map map : list) {
            String drug_name = map.get("drug_name").toString();
            String disease_name = map.get("anno_disease_name").toString();
            String approveRange = map.get("approve_range").toString();
            String evidence_phase = map.get("evidence_phase").toString();
            String other_test_required = map.get("other_test_required") == null ? "0" : map.get("other_test_required").toString();
            // 根据药物名称和癌种名称作为过滤条件
            String drugAndDisease = drug_name + disease_name;
            if (drugSet.contains(drugAndDisease)) continue;
            drugSet.add(drugAndDisease);
            if (drugFlag.getOrDefault(drug_name, false)) {
                drug_name = drug_name + "#";
            }
            if (other_test_required.equals("1")) {
                drug_name = drug_name + "%";
            }
            String durgStr = drug_name + "&" + disease_name + "&" + evidence_phase;

            // 20250313 如果approveRange为1或者5（敏感A、耐药A），则添加approving_agency（获批机构）
            if (approveRange.equals("1") || approveRange.equals("5")) {
                String approvingAgency = map.get("approving_agency") == null ? null : map.get("approving_agency").toString();
                durgStr = durgStr + "&" + approvingAgency;
            }
            drugs.add(durgStr);
        }
//        Collections.sort(drugs, CHINA_COMPARE);
        String sb = "";
        for (String string : drugs) {
            sb += string + ";";
        }
        return "".equals(sb) ? "" : sb.substring(0, sb.length() - 1);
    }

    public String getClinicalIdAndDrugNameStr(List<Map> list) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Collections.sort(list, (Map h1, Map h2) -> Integer.parseInt(h2.get("order_num").toString()) - Integer.parseInt(h1.get("order_num").toString()));
        Set<String> tempSet = new HashSet<String>();
        for (Map map : list) {
            String k = map.get("drug_name").toString() + "-" + map.get("clinical_trial_id").toString();
            if (tempSet.contains(k)) continue;
            tempSet.add(k);
            sb.append(k);
            sb.append(";");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    public Long getMaxUpdateTime(Long... updateTime) {
        return Collections.max(Arrays.asList(updateTime));
    }
	
	/*public Map getDrugInfo(String drug_name,Integer lang) {
		Map drugInfo1 = reportDrugInfoDao.selectOneNkbByDrugChineseName(drug_name,lang).get(0);
		Map drugInfo2 = reportDrugInfoDao.selectOneByDrugChineseName(drug_name,lang);
		Long drugInfoUpdateDate1 = 0L;
		Long drugInfoUpdateDate2 = 0L;
		if (CollectionUtils.isEmpty(drugInfo1) && CollectionUtils.isEmpty(drugInfo2)) {
			return null;
		}
		if (!CollectionUtils.isEmpty(drugInfo1)) {
			drugInfoUpdateDate1 = Long.valueOf(drugInfo1.get("update_date").toString());
		}
		if (!CollectionUtils.isEmpty(drugInfo2)) {
			drugInfoUpdateDate2 = Long.valueOf(drugInfo2.get("update_date").toString());
		}
		if (drugInfoUpdateDate1 >= drugInfoUpdateDate2) {
			return drugInfo1;
		} else {
			return drugInfo2;
		}
	}*/

    //截取到最后一位数字为止，比如：E1234V，最终得到E1234
    public String getVariant(String v) {
        char[] charArray = v.toCharArray();
        int index = charArray.length - 1;
        for (; index > 0; index--) {
            int num = charArray[index];
            if (num > 47 && num < 58) {
                break;
            }
        }
        return v.substring(0, index + 1);
    }

    /**
     * list可能有多条，获取第一个元素
     *
     * @param queryList
     * @return
     */
    public Map getFirst(List<Map> queryList) {
        Map result = new HashMap<>();
        if (queryList == null || CollectionUtils.isEmpty(queryList)) {
            return result;
        } else {
            return queryList.get(0);
        }
    }

    /**
     * 由gene 和 variant找出在知识库中的mutationID
     * variant 解释 p.R611Q 一般为 p点后缀，比如R611Q，但是有些时候没有，比如p.Arg611Gln，此时需要截取到R611，再从知识库中查找，如果还是没有，则返回null
     *
     * @param gene
     * @param variant
     * @return
     */
    public Integer getMutationID(String gene, String variant) {
        Integer mutationId = analysisReportDao.getMutationId(gene, variant);
        if (mutationId == null) {
            if (variant.indexOf("fs") > -1) {
                String[] split = variant.split("fs");
                String tmp_variant = getVariant(split[0]) + "fs";
                mutationId = analysisReportDao.getMutationId(gene, tmp_variant);
            }
            if (mutationId == null) {
                if ((variant.indexOf("fs") > -1 || variant.indexOf("*") > -1 || variant.indexOf("+") > -1 || variant.indexOf("-") > -1) && !(variant.indexOf("Fusion") > -1)) {
                    mutationId = analysisReportDao.getMutationId(gene, "Inactive Mutation");
                }
            }
        }
        return mutationId;
    }

    /**
     * 由mutationID找出所有的父级 mutationId
     * 获取包括自身在内的mutationId list ，一个突变可能有多个父级
     *
     * @param mutationId
     * @return
     */
    public List<Integer> getMutIdList(Integer mutationId) {
        List<Integer> mutationIdList = new ArrayList<>();
        if (mutationId != null) {
            mutationIdList.add(mutationId);
            List<Integer> parentMutationIdList = analysisReportDao.getParentMutationId(mutationId);
            mutationIdList.addAll(parentMutationIdList);
        }
        return mutationIdList;
    }

    /**
     * 获取知识库的更新时间
     *
     * @param mutationIdList
     * @param gene
     * @param diseaseIdList
     * @return
     */
    public Timestamp getNkbUpdateTime(List<Integer> mutationIdList, String gene, List<Integer> diseaseIdList) {
        Timestamp nkbUpdateTime = null;
        if (mutationIdList.isEmpty()) {
            nkbUpdateTime = new Timestamp(0);
        } else {
            // 获取知识库的突变、用药、临床信息获取 var_drug_anno 最晚更新时间更新时间
            nkbUpdateTime = analysisReportDao.getVarDrugAnnoUpdateTime(mutationIdList, diseaseIdList);
        }
        if (nkbUpdateTime == null) nkbUpdateTime = new Timestamp(0);
        // 获取基因 gene_anno 的更新时间
        Timestamp geneAnnoUpdateTime = analysisReportDao.getGeneAnnoUpdateTime(gene, diseaseIdList);
        Timestamp varAnnoUpdateTime = new Timestamp(0);
        Timestamp variantDescriptionUpdateTime = new Timestamp(0);
        if (!mutationIdList.isEmpty()) {
            variantDescriptionUpdateTime = analysisReportDao.getVariantDescriptionUpdateTime(mutationIdList.get(0));
            varAnnoUpdateTime = analysisReportDao.getVariantAnnoUpdateTime(mutationIdList.get(0), diseaseIdList);
        }
        Timestamp geneDescriptionUpdateTime = analysisReportDao.getGeneDescriptionUpdateTime(gene);
        if (geneAnnoUpdateTime != null && geneAnnoUpdateTime.after(nkbUpdateTime)) nkbUpdateTime = geneAnnoUpdateTime;
        if (varAnnoUpdateTime != null && varAnnoUpdateTime.after(nkbUpdateTime)) nkbUpdateTime = varAnnoUpdateTime;
        if (variantDescriptionUpdateTime != null && variantDescriptionUpdateTime.after(nkbUpdateTime))
            nkbUpdateTime = variantDescriptionUpdateTime;
        if (geneDescriptionUpdateTime != null && geneDescriptionUpdateTime.after(nkbUpdateTime))
            nkbUpdateTime = geneDescriptionUpdateTime;
        return nkbUpdateTime;
    }

    /**
     * 从知识库获取所有信息，并更新 rp_var_drug_en7 表
     * 如果user为空，就表示只读，不修改本地库
     *
     * @param user
     * @param a
     * @param reportVarDrug
     * @param mutationIdList
     * @param diseaseIdList
     * @param parentdiseaseIdList
     * @param sonIdList
     * @param gene
     * @param variant
     * @param ori_variant
     * @param Flag
     * @param lang
     * @param diseaseId
     */
    public void fetchNkbDrugInfo(String user, Map a, ReportVarDrug reportVarDrug, List<Integer> mutationIdList, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList, List<Integer> sonIdList, String gene, String variant, String ori_variant, Integer Flag, Integer lang, Integer diseaseId) {
        List<Map> drugList = new ArrayList<Map>();

        // 判断mutation_type: S or G ; S ==> 体系; G ==> 胚系;
        String mutation_type = "S";
        String has_drug = a.get("has_drug") == null ? "" : a.get("has_drug").toString();
        if (!has_drug.equals("") && (has_drug.equals("true") || has_drug.equals("1"))) {
            mutation_type = "G";
        }

        if (!mutationIdList.isEmpty()) {
            // 根据 mutationIdList diseaseIdList mutation_type获取所有的药物信息
            drugList = analysisReportDao.getDrugListByIdList(mutationIdList, diseaseIdList, lang, mutation_type);
            drugList.sort((o1, o2) -> o2.get("evidence_phase_id").toString().compareTo(o1.get("evidence_phase_id").toString()));
        }
        Iterator<Map> iterator = drugList.iterator();
        while (iterator.hasNext()) {
            Map b = iterator.next();
            int drugLevel = getDrugLevel(b, parentdiseaseIdList);
            b.put("approve_range", String.valueOf(drugLevel));
        }

        // TODO drugFlag 药物是否加#的标记 ==> 什么时候加 # ==> 有临床实验加 #
        Map<String, Boolean> drugFlag = new HashMap<String, Boolean>();
        List<Map> clinicalList = getClinicalList(drugList, drugFlag, parentdiseaseIdList, lang); //根据drugList获取临床试验列表
        // 添加其他癌种A级证据，并输出为C级药物
        if (!mutationIdList.isEmpty()) {
            List<Map> otherADrugList = analysisReportDao.getOtherADrugListByIdList(mutationIdList, diseaseIdList, lang, mutation_type, sonIdList);
            // 过滤不是A、B药物
            List<Map> mapList = drugList.stream().filter(s -> Arrays.asList("1", "2", "5", "6").contains(s.get("approve_range"))).collect(Collectors.toList());
            List<String> drugId = mapList.stream().map(map -> map.get("drug_id").toString()).collect(Collectors.toList());
            List<Map> otherADrugListFilter = otherADrugList.stream().filter(s -> !drugId.contains(s.get("drug_id").toString())).collect(Collectors.toList());
            Iterator<Map> otherADrugIterator = otherADrugListFilter.iterator();
            while (otherADrugIterator.hasNext()) {
                Map b = otherADrugIterator.next();
                if ("Resistant".equals(b.get("relationship").toString())) {
                    b.put("approve_range", "7");
                } else {
                    b.put("approve_range", "3");
                }
            }
            drugList.addAll(otherADrugListFilter);
        }
        // TODO 过滤药物信息，同一点可能会有多个用药信息（癌种不同），保留最高药物等级信息
        filterDrugList(drugList);

        String varDrugNote = getVarDrugNote(diseaseIdList, mutationIdList, drugList, gene, !clinicalList.isEmpty(), lang);
        if ((drugList.size() > 0 && Flag != 2) || Flag == 1) {
            if ("empty".equals(user)) {
                Set<String> drugSet = new HashSet<String>();
                Set<String> drugResistanceSet = new HashSet<String>();
                a.put("drugsA", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("1")).collect(Collectors.toList()), drugFlag, drugSet));
                a.put("drugsB", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("2")).collect(Collectors.toList()), drugFlag, drugSet));
                a.put("drugsC", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("3")).collect(Collectors.toList()), drugFlag, drugSet));
                a.put("drugsD", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("4")).collect(Collectors.toList()), drugFlag, drugSet));
                a.put("resistant_drugsA", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("5")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
                a.put("resistant_drugsB", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("6")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
                a.put("resistant_drugsC", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("7")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
                a.put("resistant_drugsD", getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("8")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
                a.put("clinical_trial", getClinicalIdAndDrugNameStr(clinicalList));
            } else {
                Map rpUnknownVar = reportUnknownVarDao.selectRpUnknownVar(gene, ori_variant, diseaseIdList.get(0), lang);
                if (rpUnknownVar != null) {
                    reportUnknownVarDao.deleteRpUnknownVarById(Integer.valueOf(rpUnknownVar.get("record_id").toString()));
                }
                updateReportVarDrug(user, reportVarDrug, drugList, drugFlag, clinicalList, varDrugNote, lang, diseaseId, diseaseIdList, mutationIdList);
                if (reportVarDrug.getRecord_id() != null) {
                    a.put("record_id", reportVarDrug.getRecord_id());
                }
            }
            if (lang == 1) {
                Map<String, List> maps = PotentialInformation(mutationIdList, diseaseIdList, lang, mutation_type, reportVarDrug);
                List potentialDrugList = maps.get("potentialDrugList");
                List drugResearchList = maps.get("drugResearchList");
                a.put("potentialDrugList", potentialDrugList);
                a.put("drugResearchList", drugResearchList);
            }
            a.put("drugList", drugList);
            a.put("clinicalList", clinicalList);
            a.put("varDrugNote", varDrugNote);
            a.put("resultTypeVal", 1);
            a.put("resultTypeDesc", "靶向药物");
            a.put("check_date", null);
        } else {
            if (!"empty".equals(user) && reportVarDrug.getRecord_id() != null) {
                reportVarDrugDao.deleteRpVarDrugById(reportVarDrug.getRecord_id());
            }
            getUnknownVarInfo(user, diseaseIdList.get(0), diseaseIdList, a, gene, variant, ori_variant, lang);
        }
    }


    //从本地库的drugNameStr还原druglist
    public List<Map> getDrugListFromStr(String drugNameStr, Integer level, Integer lang, Integer diseaseId, List<Integer> diseaseIdList) {
        List<Map> drugList = new ArrayList<Map>();
        if (drugNameStr == null || "".equals(drugNameStr)) return drugList;
        List<String> groupList = Arrays.asList(drugNameStr.split(";"));
        for (String group : groupList) {
            List<String> list = Arrays.asList(group.split("&"));
            String drugName = list.get(0);
            String diseaseName = list.get(1);
            // 20241205 知识库更新了胰脏腺癌 本地没有更新 修复
            if (diseaseName.equals("胰脏腺癌")) {
                diseaseName = "胰腺腺癌";
            }
            Map disease = analysisReportDao.getDiseaseId(diseaseName);
            Integer disease_id = Integer.valueOf(disease.get("do_id").toString());
            String evidencePhase = list.get(2);
            Map map = new HashMap<>();

            // 20250313 A级药物耐药敏感增加获批机构、指南推荐
            if ((level == 1 || level == 5) ) {
                String approvingAgency = "";
                if (list.size() == 4) {
                    approvingAgency = list.get(3);
                } else {
                    String oriName = drugName.replaceAll("[#*]", "");
                    if ("获批上市".equals(evidencePhase)){
                        approvingAgency = reportDrugInfoDao.getApprovingAgency(disease_id, oriName);
                    } else if ("指南推荐".equals(evidencePhase)) {
                        List<String> guides = reportDrugInfoDao.getApprovingAgency1(disease_id, oriName);
                         approvingAgency = String.join("/", guides);
                    }
                }
                map.put("approvingAgency", approvingAgency);
            }

            if (drugName.endsWith("%")) {
                map.put("other_test_required", "1");
                drugName = drugName.substring(0, drugName.length() - 1);
            } else {
                map.put("other_test_required", "0");
            }
            if (drugName.endsWith("#")) {
                map.put("recruiting", "1");
                drugName = drugName.substring(0, drugName.length() - 1);
            } else {
                map.put("recruiting", "0");
            }

            // 更新本地A级获批用药信息，使用较新的药物信息
            Map drugInfo1 = null;
            List<Map> drugs = reportDrugInfoDao.selectOneApprovedByDrugChineseName(drugName, lang, disease_id);
            if (!drugs.isEmpty()) {
                drugInfo1 = drugs.get(0);
            }
            if (drugInfo1 != null) drugInfo1.put("disease_id", disease_id);
            Map drugInfo2 = reportDrugInfoDao.selectOneByDrugChineseName(drugName, lang, disease_id);
            Long drugInfo1_updateTime = CollectionUtils.isEmpty(drugInfo1) ? 0L : Long.valueOf(drugInfo1.get("update_date").toString());
            Long drugInfo2_updateTime = CollectionUtils.isEmpty(drugInfo2) ? 0L : Long.valueOf(drugInfo2.get("update_date").toString());
            if (drugInfo1_updateTime > drugInfo2_updateTime) {
                ReportDrugInfo reportDrugInfo = new ReportDrugInfo();
                Integer drugId = Integer.valueOf(drugInfo1.get("drug_id").toString());
                String drug_name = drugInfo1.get("drug_name").toString();
                Integer cfda = Integer.valueOf(drugInfo1.get("cfda").toString());
                String approval_desc = drugInfo1.get("approval_desc") == null ? "" : drugInfo1.get("approval_desc").toString();
                reportDrugInfo.setDrug_id(drugId);
                reportDrugInfo.setDrug_name(drug_name);
                reportDrugInfo.setOld_drug_name(drug_name);
                reportDrugInfo.setCfda(cfda);
                reportDrugInfo.setApproval_desc(approval_desc);
                reportDrugInfo.setUpdate_by(user);
                reportDrugInfo.setLang(lang);
                reportDrugInfo.setDisease_id(disease_id);
                if (CollectionUtils.isEmpty(drugInfo2)) {
                    reportDrugInfoDao.insertRpDrugInfo(reportDrugInfo);
                } else {
                    reportDrugInfoDao.updateRpDrugInfo(reportDrugInfo);
                }
            } else if (drugInfo1_updateTime == 0 && !CollectionUtils.isEmpty(drugInfo2)) {
                reportDrugInfoDao.deleteRpDrugInfo2(drugName, lang, disease_id);
            }

            drugInfo2 = reportDrugInfoDao.selectOneByDrugChineseName(drugName, lang, disease_id);
            if (CollectionUtils.isEmpty(drugInfo2)) {
                map.put("drug_name", drugName);
                map.put("disease_id", disease_id);
                map.put("status", "add");
                map.put("cfda", "0");
                map.put("annotation", "");
            } else {
                map.putAll(drugInfo2);
            }
            map.put("approve_range", String.valueOf(level));
            map.put("anno_disease_name", diseaseName);
            map.put("evidence_phase", evidencePhase);
            // 耐药分级
            /*if (level == 5) {
                Integer evidencePhaseId = analysisReportDao.getEvidencePhaseId(evidencePhase);
//                getDrugLevel();
            }*/
            drugList.add(map);
        }
        return drugList;
    }

    //从本地库的clinicalInfo中获取临床试验列表
    public List<Map> getClinicalListFromStr(String clinical_trial_str, List<Map> drugList, Integer lang) {
        List<Map> clinicalList = new ArrayList<Map>();
        if (clinical_trial_str == null || "".equals(clinical_trial_str))
            return clinicalList;
        List<String> clinicalTrialList = Arrays.asList(clinical_trial_str.split(";"));
        for (String clinicalTrial : clinicalTrialList) {
            int sep_pos = clinicalTrial.lastIndexOf("-");
            if (sep_pos < 0)
                continue;
            String drug_name = clinicalTrial.substring(0, sep_pos);
            String cfda = getCFDA(drug_name, drugList);
            String other_test_required = getOtherTestRequired(drug_name, drugList);
            String clinical_trial_id = clinicalTrial.substring(sep_pos + 1);

            Map clinicalInfo1 = reportClinicalTrialDao.selectOneNkbClinicalTrialById(clinical_trial_id, lang);
            Long clinicalInfoUpdateDate1 = 0L;
            if (clinicalInfo1 != null) {
                clinicalInfoUpdateDate1 = clinicalInfo1.get("update_date") == null ? 0L : Long.valueOf(clinicalInfo1.get("update_date").toString());
            }
            Map clinicalInfo2 = reportClinicalTrialDao.selectOneClinicalTrialById(clinical_trial_id, lang);
            if (clinicalInfo2 == null) clinicalInfo2 = new HashMap<>();
            Long clinicalInfoUpdateDate2 = clinicalInfo2.get("update_date") == null ? 0L : Long.valueOf(clinicalInfo2.get("update_date").toString());
            if (clinicalInfoUpdateDate1 >= clinicalInfoUpdateDate2) {
                ReportClinicalTrial reportClinicalTrial = new ReportClinicalTrial();
//				String clinical_trial_id = clinicalInfo1.get("clinical_trial_id").toString();
                String title = clinicalInfo1.get("title") == null ? "" : clinicalInfo1.get("title").toString();
                String recruiting_condition = clinicalInfo1.get("recruiting_condition") == null ? "" : clinicalInfo1.get("recruiting_condition").toString();
                String phase = clinicalInfo1.get("phase") == null ? "" : clinicalInfo1.get("phase").toString();
                String location = clinicalInfo1.get("location") == null ? "" : clinicalInfo1.get("location").toString();
                String inclusion_criteria = clinicalInfo1.get("inclusion_criteria") == null ? "" : clinicalInfo1.get("inclusion_criteria").toString();
                String exclusion_criteria = clinicalInfo1.get("exclusion_criteria") == null ? "" : clinicalInfo1.get("exclusion_criteria").toString();
                reportClinicalTrial.setClinical_trial_id(clinical_trial_id);
                reportClinicalTrial.setOld_clinical_trial_id(clinical_trial_id);
                reportClinicalTrial.setTitle(title);
                reportClinicalTrial.setRecruiting_condition(recruiting_condition);
                reportClinicalTrial.setPhase(phase);
                reportClinicalTrial.setLocation(location);
                reportClinicalTrial.setUpdate_by(user);
                reportClinicalTrial.setInclusion_criteria(inclusion_criteria);
                reportClinicalTrial.setExclusion_criteria(exclusion_criteria);
                reportClinicalTrial.setLang(lang);
                if (CollectionUtils.isEmpty(clinicalInfo2)) {
                    reportClinicalTrialDao.insertRpClinicalTrial(reportClinicalTrial);
                } else {
                    reportClinicalTrialDao.updateRpClinicalTrial(reportClinicalTrial);
                }
            }

            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> tempMap = reportClinicalTrialDao.selectOneClinicalTrialById(clinical_trial_id, lang);
            if (!CollectionUtils.isEmpty(tempMap)) {
                map.putAll(tempMap);
                map.put("drug_name", drug_name);
                map.put("cfda", cfda);
                map.put("other_test_required", other_test_required);
                clinicalList.add(map);
            }
        }
        return clinicalList;
    }

    //从drugList中获取某药物是否为CFDA批准
    public String getCFDA(String drug_name, List<Map> drugList) {
        for (Map d : drugList) {
            if (drug_name.equals(d.get("drug_name").toString())) {
                return d.get("cfda").toString();
            }
        }
        return "0";
    }

    //从drugList中获取某药物TestRequired
    public String getOtherTestRequired(String drug_name, List<Map> drugList) {
        for (Map d : drugList) {
            if (drug_name.equals(d.get("drug_name").toString())) {
                return d.get("other_test_required").toString();
            }
        }
        return "0";
    }

    //从本地库获取信息
    public void fetchLocalDrugInfo(String user, Map a, ReportVarDrug reportVarDrug, Integer lang, Integer disease_id, List<Integer> diseaseIdList, List<Integer> mutationIdList, List<Integer> parentdiseaseIdList) {
        List<Map> drugList = new ArrayList<Map>();
        String has_drug = a.get("has_drug") == null ? "" : a.get("has_drug").toString();
        String mutation_type = "S";
        if (!has_drug.equals("") && (has_drug.equals("true") || has_drug.equals("1"))) {
            mutation_type = "G";
        }
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsA(), 1, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsB(), 2, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsC(), 3, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsD(), 4, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsA(), 5, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsB(), 6, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsC(), 7, lang, disease_id, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsD(), 8, lang, disease_id, diseaseIdList));

        String var_drug_desc = reportVarDrug.getVar_drug_desc();

        List<Map> clinicalList = getClinicalListFromStr(reportVarDrug.getClinical_trial(), drugList, lang);
        if (lang == 1) {
            Map<String, List> maps = PotentialInformation(mutationIdList, diseaseIdList, lang, mutation_type, reportVarDrug);
            List<PotentialDrug> potentialDrugList = maps.get("potentialDrugList");
            List<DrugResearch> drugResearchList = maps.get("drugResearchList");
            a.put("potentialDrugList", potentialDrugList);
            a.put("drugResearchList", drugResearchList);
        }
        a.put("drugList", drugList);
        a.put("record_id", reportVarDrug.getRecord_id());
        a.put("clinicalList", clinicalList);
        a.put("varDrugNote", reportVarDrug.getVar_drug_desc());
        a.put("resultTypeVal", 1);
        a.put("resultTypeDesc", "靶向药物");
        a.put("check_date", reportVarDrug.getCheck_date());
    }

    /**
     * 更新本地库 用药
     * 只有从知识库获取的用药信息才更新
     *
     * @param user
     * @param reportVarDrug
     * @param drugList
     * @param drugFlag
     * @param clinicalList
     * @param varDrugNote
     * @param lang
     * @param diseaseId
     * @param diseaseIdList
     * @param mutationIdList
     */
    public void updateReportVarDrug(String user, ReportVarDrug reportVarDrug, List<Map> drugList, Map<String, Boolean> drugFlag, List<Map> clinicalList, String varDrugNote, Integer lang, Integer diseaseId, List<Integer> diseaseIdList, List<Integer> mutationIdList) {
        Set<String> drugSet = new HashSet<String>();
        Set<String> drugResistanceSet = new HashSet<String>();
        reportVarDrug.setDrugsA(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("1")).collect(Collectors.toList()), drugFlag, drugSet));
        reportVarDrug.setDrugsB(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("2")).collect(Collectors.toList()), drugFlag, drugSet));
        reportVarDrug.setDrugsC(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("3")).collect(Collectors.toList()), drugFlag, drugSet));
        reportVarDrug.setDrugsD(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("4")).collect(Collectors.toList()), drugFlag, drugSet));
        reportVarDrug.setResistant_drugsA(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("5")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
        reportVarDrug.setResistant_drugsB(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("6")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
        reportVarDrug.setResistant_drugsC(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("7")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
        reportVarDrug.setResistant_drugsD(getDrugNameStr(drugList.stream().filter(item -> item.get("approve_range").toString().equals("8")).collect(Collectors.toList()), drugFlag, drugResistanceSet));
        reportVarDrug.setVar_drug_desc(varDrugNote);
        reportVarDrug.setClinical_trial(getClinicalIdAndDrugNameStr(clinicalList));
        reportVarDrug.setUpdate_by(user);
        reportVarDrug.setLang(lang);
        String mutationIdStr = "";
        if (!mutationIdList.isEmpty()) {
            for (Integer mutationId : mutationIdList) {
                mutationIdStr += mutationId + ";";
            }
            mutationIdStr = mutationIdStr.substring(0, mutationIdStr.length() - 1);
        }
        reportVarDrug.setParent_mutID(mutationIdStr);
        reportVarDrug.setCheck_date(null);

        // 更新本地库用药信息表 rp_var_drug_en7
        if (reportVarDrug.getRecord_id() == null) {
            reportVarDrugDao.insertRpVarDrug(reportVarDrug);
        } else {
//            reportVarDrugDao.updateRpVarDrug(reportVarDrug);
            reportVarDrugDao.deleteRpVarDrug(reportVarDrug.getGene(), reportVarDrug.getOri_variant(), reportVarDrug.getDisease_id(), lang, reportVarDrug.getGender());
            reportVarDrugDao.insertRpVarDrug(reportVarDrug);
        }
        drugList.clear();
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsA(), 1, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsB(), 2, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsC(), 3, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getDrugsD(), 4, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsA(), 5, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsB(), 6, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsC(), 7, lang, diseaseId, diseaseIdList));
        drugList.addAll(getDrugListFromStr(reportVarDrug.getResistant_drugsD(), 8, lang, diseaseId, diseaseIdList));
        clinicalList.clear();
        clinicalList.addAll(getClinicalListFromStr(reportVarDrug.getClinical_trial(), drugList, lang));
    }

    // 获取用药说明
    public String getVarDrugNote(List<Integer> diseaseIdList, List<Integer> mutationIdList, List<Map> drugList, String gene, boolean hasClinical, Integer lang) {
        //查询知识库
        //获取基因说明和信号通路说明
        Map geneDesc = getFirst(analysisReportDao.getGeneDesc(gene, lang));
        String geneDescription = geneDesc.get("gene_description") == null ? "" : geneDesc.get("gene_description").toString();
        String pathwayDescription = geneDesc.get("pathway_description") == null ? "" : geneDesc.get("pathway_description").toString();
        Map variantDesc = getFirst(CollectionUtils.isEmpty(mutationIdList) ? new ArrayList<>() : analysisReportDao.getVariantDescription(mutationIdList, lang));
        String variantDescription = variantDesc == null ? "" : (variantDesc.get("description") == null ? "" : variantDesc.get("description").toString());
        //获取预后和诊断说明
        String clinicalInfo = "";
        if (!CollectionUtils.isEmpty(diseaseIdList)) {
            // 获取本癌种的geneAnnotation和varAnnotation 加进去
            List<Map> AnnotationList = new ArrayList<Map>();
            Map geneAnnotation = getFirst(analysisReportDao.getGeneAnnotationById(gene, diseaseIdList.get(0), lang));
            if (!geneAnnotation.isEmpty()) AnnotationList.add(geneAnnotation);
            if (mutationIdList != null && !mutationIdList.isEmpty()) {
                Map varAnnotation = getFirst(analysisReportDao.getVarAnnotationById(mutationIdList.get(0), diseaseIdList.get(0), lang));
                if (!varAnnotation.isEmpty()) AnnotationList.add(varAnnotation);
            }
            // 获取一级子的geneAnnotation和varAnnotation 加进去
            List<Map> tmp = analysisReportDao.getSonDiseaseList(diseaseIdList.get(0));
            List<Integer> sonDiseaseIdList = getFieldFromList(tmp, "do_id", 1);
            List<Map> temp_geneAnnotations = new ArrayList<Map>();
            if (sonDiseaseIdList != null && !sonDiseaseIdList.isEmpty())
                temp_geneAnnotations = analysisReportDao.getGeneAnnotationByIdList(gene, sonDiseaseIdList, lang);
            if (temp_geneAnnotations != null && !temp_geneAnnotations.isEmpty())
                AnnotationList.addAll(temp_geneAnnotations);
            List<Map> temp_varAnnotations = new ArrayList<Map>();
            if (mutationIdList != null && !mutationIdList.isEmpty()) {
                if (sonDiseaseIdList != null && !sonDiseaseIdList.isEmpty())
                    temp_varAnnotations = analysisReportDao.getVarAnnotationByIdList2(mutationIdList.get(0), sonDiseaseIdList, lang);
                if (temp_varAnnotations != null && !temp_varAnnotations.isEmpty())
                    AnnotationList.addAll(temp_varAnnotations);
            }
            // 获取一级父的geneAnnotation和varAnnotation
            tmp = analysisReportDao.getParentDiseaseList(diseaseIdList.get(0));
            List<Integer> parentIdList1 = getFieldFromList(tmp, "parent_do_id", 1);
            if (!parentIdList1.isEmpty()) {
                temp_geneAnnotations = analysisReportDao.getGeneAnnotationByIdList(gene, parentIdList1, lang);
                if (temp_geneAnnotations != null && !temp_geneAnnotations.isEmpty())
                    AnnotationList.addAll(temp_geneAnnotations);
                if (mutationIdList != null && !mutationIdList.isEmpty()) {
                    temp_varAnnotations = analysisReportDao.getVarAnnotationByIdList2(mutationIdList.get(0), parentIdList1, lang);
                    if (temp_varAnnotations != null && !temp_varAnnotations.isEmpty())
                        AnnotationList.addAll(temp_varAnnotations);
                }
                // 获取二级父的geneAnnotation和varAnnotation
                tmp = analysisReportDao.getParentDiseaseList2(parentIdList1);
                List<Integer> parentIdList2 = getFieldFromList(tmp, "parent_do_id", 1);
                if (!parentIdList2.isEmpty()) {
                    if (temp_geneAnnotations.isEmpty()) {
                        temp_geneAnnotations = analysisReportDao.getGeneAnnotationByIdList(gene, parentIdList2, lang);
                        if (temp_geneAnnotations != null && !temp_geneAnnotations.isEmpty())
                            AnnotationList.addAll(temp_geneAnnotations);
                    }
                    if (temp_varAnnotations.isEmpty() && mutationIdList != null && !mutationIdList.isEmpty()) {
                        temp_varAnnotations = analysisReportDao.getVarAnnotationByIdList2(mutationIdList.get(0), parentIdList2, lang);
                        if (temp_varAnnotations != null && !temp_varAnnotations.isEmpty())
                            AnnotationList.addAll(temp_varAnnotations);
                    }
                }
            }
            String drug_annotation = "";
            for (Map annotation : AnnotationList) {
                clinicalInfo += annotation.get("clinical_annotation") == null ? "" : annotation.get("clinical_annotation").toString();
                drug_annotation += annotation.get("drug_annotation") == null ? "" : annotation.get("drug_annotation").toString();
            }
            clinicalInfo += drug_annotation;
        }
        //获取用药说明及耐药说明
        String drugAnnotation = "";
        String resistantDrugNote = "";
        String nccnInfo = "";
        List<Map> guidelineDrugs = new ArrayList<>();
        Set<String> drug_annno_set = new HashSet<String>();
        Set<String> drugName = new HashSet<String>();
        Set<String> resistantDrugName = new HashSet<String>();
        drugList.sort((o1, o2) -> o2.get("evidence_phase_id").toString().compareTo(o1.get("evidence_phase_id").toString()));
        Iterator<Map> iterator = drugList.iterator();
        while (iterator.hasNext()) {
            Map b = iterator.next();
            Integer drugLevel = Integer.parseInt(b.get("approve_range").toString());
            Integer drug_id = Integer.parseInt(b.get("drug_id").toString());
            String drug_name = b.get("drug_name").toString();
            String anno_disease_name = b.get("anno_disease_name").toString();
            String drugAndDiseaseName = drug_name + "(" + anno_disease_name + ")";
            if (drugLevel >= 5) {
                if (resistantDrugName.contains(drugAndDiseaseName)) {
                    continue;
                }
                resistantDrugName.add(drugAndDiseaseName);
            } else {
                if (drugName.contains(drugAndDiseaseName)) {
                    continue;
                }
                drugName.add(drugAndDiseaseName);
            }
            Integer anno_disease_id = Integer.parseInt(b.get("anno_disease_id").toString());
            int evidence_phase_id = Integer.parseInt(b.get("evidence_phase_id").toString());
            String annotation = b.getOrDefault("annotation", "").toString() + "\r\n";
            if (drugLevel >= 5) { //耐药药物
                if (!drug_annno_set.contains(annotation)) resistantDrugNote += drugAndDiseaseName + "：" + annotation;
            } else if (evidence_phase_id == 23) {//指南推荐
//                Map guidelineDrug = getFirst(analysisReportDao.getNccnDrugs(drugId, anno_disease_id,lang));
                List<Map> drugs = AES.getDecList(analysisReportDao.getNccnDrugs(drug_id, anno_disease_id, lang));
                String anno = "";
                String annoNCCNStr = "";
                String annoCSCOStr = "";
                for (Map drug : drugs) {
                    if (drug.get("guideline_description") != null && !drug.get("guideline_description").toString().equals("")) {
                        if (drug.get("guideline_type").equals("NCCN")) {
                            if (lang == 1) {
                                annoNCCNStr += "NCCN指南推荐" + drugAndDiseaseName + drug.get("guideline_description").toString();
                            } else {
                                annoNCCNStr += "NCCN Guildline recommended " + drugAndDiseaseName + drug.get("guideline_description").toString();
                            }
                        } else if (drug.get("guideline_type").equals("CSCO")) {
                            if (lang == 1) {
                                annoCSCOStr += "CSCO指南推荐" + drugAndDiseaseName + drug.get("guideline_description").toString();
                            } else {
                                annoCSCOStr += "CSCO Guildline recommended " + drugAndDiseaseName + drug.get("guideline_description").toString();
                            }
                        }
                    }
                }
                if (!"".equals(annoNCCNStr)) {
                    anno += annoNCCNStr + "\r\n";
                }
                if (!"".equals(annoCSCOStr)) {
                    anno += annoCSCOStr + "\r\n";
                }
                nccnInfo += anno;
//                if (guidelineDrug != null) guidelineDrugs.add(guidelineDrug);
            } else if (drugLevel >= 2 || drugLevel <= 4 && !"\r\n".equals(annotation)) {
                if (evidence_phase_id != 24 && (b.get("has_previous_clinical_result") == null || "Y".equals(b.get("has_previous_clinical_result").toString()))) {
                    if (!drug_annno_set.contains(annotation)) drugAnnotation += drugAndDiseaseName + "：" + annotation;
                }
            }
            drug_annno_set.add(drugAndDiseaseName + "：" + annotation);
        }
        /*if (!CollectionUtils.isEmpty(guidelineDrugs)) {
            String nccnStr = "";
            Map<String, List<String>> guideHash = new HashMap<String, List<String>>();
            for (int i = 0; i < guidelineDrugs.size(); i++) {
                if (guidelineDrugs.get(i).isEmpty()) continue;
                String drug_name = guidelineDrugs.get(i).get("drug_name") == null ? "" : guidelineDrugs.get(i).get("drug_name").toString();
                String guideline_description = guidelineDrugs.get(i).get("guideline_description") == null ? "" : guidelineDrugs.get(i).get("guideline_description").toString();
                List<String> value = guideHash.getOrDefault(guideline_description, new ArrayList<String>());
                if (value.contains(drug_name)) continue;
                value.add(drug_name);
                guideHash.put(guideline_description, value);
            }
            for (String guide : guideHash.keySet()) {
                if (lang == 2) {
                    nccnStr += "NCCN guideline recommends " + String.join(", ", guideHash.get(guide)) + " " + guide + " ";
                } else {
                    nccnStr += String.join("，", guideHash.get(guide)) + guide;
                }

            }
            if (!nccnStr.isEmpty()) {
                if (lang == 2) {
                    nccnInfo += nccnStr;
                } else {
                    nccnInfo += "NCCN指南推荐" + nccnStr;
                }
            }
        }*/
        Pair<String, String> pair0 = new Pair<>("基因说明:", geneDescription.trim());
        Pair<String, String> pair1 = new Pair<>("信号通路说明:", pathwayDescription.trim());
        Pair<String, String> pair2 = new Pair<>("位点说明:", variantDescription.trim());
        Pair<String, String> pair4 = new Pair<>("NCCN指南:", nccnInfo.trim());
        Pair<String, String> pair5 = new Pair<>("耐药说明:", resistantDrugNote.trim());
        Pair<String, String> pair6 = new Pair<>("用药说明:", drugAnnotation.trim());
        //Pair<String, String> pair7 = new Pair<>("预后和诊断说明:",clinicalInfo.trim());
        //Pair<String, String> pair8 = new Pair<>("recommend:","推荐下表所示的临床试验。");
        List<Pair<String, String>> listDrugNote = new ArrayList<Pair<String, String>>();
        listDrugNote.add(pair0);
        listDrugNote.add(pair1);
        listDrugNote.add(pair2);
        listDrugNote.add(pair4);
        //listDrugNote.add(pair7);
        listDrugNote.add(pair6);
        listDrugNote.add(pair5);
//        if (hasClinical) {
//        	listDrugNote.add(pair8);
//        }
        JSONSerializer js = new JSONSerializer();
        String varDrugNote = js.toJSON(listDrugNote).toString();
        return varDrugNote;
    }

    //根据drugList获取临床试验列表
    public List<Map> getClinicalList(List<Map> drugList, Map<String, Boolean> drugFlag, List<Integer> parentdiseaseIdList, Integer lang) {
        List<Map> clinicalList = new ArrayList<>();
        Set<String> cliSet = new HashSet<String>();
        Iterator<Map> iterator = drugList.iterator();
        while (iterator.hasNext()) {
            Map b = iterator.next();
            String drug_name = b.get("drug_name").toString();
            Integer annotation_id = Integer.valueOf(b.get("annotation_id").toString());
            List<Map> clinicals = analysisReportDao.getClinicalTrial(annotation_id, drug_name, parentdiseaseIdList, lang);
            Iterator<Map> iterator2 = clinicals.iterator();
            while (iterator2.hasNext()) {
                Map c = iterator2.next();
                String clinicalID = c.get("clinical_trial_id").toString();
                String other_test_required = getOtherTestRequired(drug_name, drugList);
                c.put("other_test_required", other_test_required);
                String kk = drug_name + " " + clinicalID;
                if (cliSet.contains(kk)) continue;
                clinicalList.add(c);
                cliSet.add(kk);
            }
            drugFlag.put(drug_name, drugFlag.getOrDefault(drug_name, false) || (!clinicals.isEmpty()));
        }
        Collections.sort(clinicalList, (Map h1, Map h2) -> Integer.parseInt(h2.get("order_num").toString()) - Integer.parseInt(h1.get("order_num").toString()));
        return clinicalList;
    }

    //一个药物仅保留最高级别的信息，去掉相同药物的信息,并且仅保留ABC级药及耐药药物
    public void filterDrugList(List<Map> drugList) {
        Map<String, Integer> drugMap = new HashMap<String, Integer>();
        Map<String, Integer> resistant_drugsMap = new HashMap<String, Integer>();
        Iterator<Map> iterator = drugList.iterator();
        while (iterator.hasNext()) {
            Map b = iterator.next();
            String drug_id = b.get("drug_id").toString();
            String anno_disease_id = b.get("anno_disease_id").toString();
            Integer drugLevel = Integer.parseInt(b.get("approve_range").toString());
            if ("Resistant".equals(b.get("relationship").toString())) {
                if (resistant_drugsMap.containsKey(drug_id + "&" + anno_disease_id)) {
                    Integer preDrugLevel = resistant_drugsMap.get(drug_id + "&" + anno_disease_id);
                    if (drugLevel < preDrugLevel) {
                        drugMap.put(drug_id + "&" + anno_disease_id, drugLevel);
                    }
                } else {
                    resistant_drugsMap.put(drug_id + "&" + anno_disease_id, drugLevel);
                }
            } else {
                if (drugMap.containsKey(drug_id + "&" + anno_disease_id)) {
                    Integer preDrugLevel = drugMap.get(drug_id + "&" + anno_disease_id);
                    if (drugLevel < preDrugLevel) {
                        drugMap.put(drug_id + "&" + anno_disease_id, drugLevel);
                    }
                } else {
                    drugMap.put(drug_id + "&" + anno_disease_id, drugLevel);
                }
            }
        }
        iterator = drugList.iterator();
        while (iterator.hasNext()) {
            Map b = iterator.next();
            String drug_id = b.get("drug_id").toString();
            String anno_disease_id = b.get("anno_disease_id").toString();
            Integer drugLevel = Integer.parseInt(b.get("approve_range").toString());
            if ("Resistant".equals(b.get("relationship").toString())) {
                Integer preDrugLevel = resistant_drugsMap.get(drug_id + "&" + anno_disease_id);
                if (preDrugLevel == 9 || drugLevel != preDrugLevel) {
                    iterator.remove();
                }
            } else {
                Integer preDrugLevel = drugMap.get(drug_id + "&" + anno_disease_id);
                if (preDrugLevel == 9 || drugLevel != preDrugLevel) {
                    iterator.remove();
                }
            }
        }
        Collections.sort(drugList, (Map h1, Map h2) -> Integer.parseInt(h1.get("approve_range").toString()) - Integer.parseInt(h2.get("approve_range").toString()));
    }


    /**
     * 获取药物级别 根据 evidence_phase_id
     * 获取药物级别：1-4为获益A，B，C，D级药物， 5-8为耐药A，B，C，D级药物， 9，其他
     *
     * @param b
     * @param parentdiseaseIdList 用来判断耐药C
     * @return
     */
    public int getDrugLevel(Map b, List<Integer> parentdiseaseIdList) {
        int evidence_phase_id = Integer.parseInt(b.get("evidence_phase_id").toString());

        // Resistant 耐药
        if ("Resistant".equals(b.get("relationship").toString()) && evidence_phase_id >= 12) {
            if (evidence_phase_id > 22) {
                return 5;
            } else if (evidence_phase_id > 17 && evidence_phase_id < 23) {
                return 6;
            } else if (evidence_phase_id > 15 && evidence_phase_id < 18) {
                return 7;
            } else if (evidence_phase_id == 14) {
                String give = getGive(b, parentdiseaseIdList);
                if ("2".equals(give)) {
                    return 7;
                } else if ("1".equals(give)) {
                    return 8;
                } else {
                    return 9;
                }
            } else if (evidence_phase_id > 11 && evidence_phase_id < 14) {
                return 8;
            } else {
                return 9;
            }
        } else if (evidence_phase_id > 22) {
            return 1;
        } else if (evidence_phase_id > 17 && evidence_phase_id < 23) {
            return 2;
        } else if (evidence_phase_id > 15 && evidence_phase_id < 18) {
            return 3;
        } else if (evidence_phase_id == 14) {
            String give = getGive(b, parentdiseaseIdList);
            if ("2".equals(give)) {
                return 3;
            } else if ("1".equals(give)) {
                return 4;
            } else {
                return 9;
            }
        } else if (evidence_phase_id > 11 && evidence_phase_id < 14) {
            return 4;
        } else {
            return 9;
        }
    }

    /**
     * 获取 Give
     * 针对于 evidence_phase_id == 14 的特殊判断
     *
     * @param b
     * @param parentdiseaseIdList
     * @return
     */
    public String getGive(Map b, List<Integer> parentdiseaseIdList) {
        Integer clinical_num = analysisReportDao.getClinicalNumber(Integer.parseInt(b.get("annotation_id").toString()), parentdiseaseIdList);
        if (clinical_num > 0) {
            return "2";
        } else {
            if (b.get("has_previous_clinical_result") == null || "Y".equals(b.get("has_previous_clinical_result").toString())) {
                return "1";
            }
        }
        return "0";
    }

    public List getFieldFromList(List<Map> lst, String field, Integer resultType) {
        List Result = new ArrayList();
        for (Map d : lst) {
            String r = d.get(field) == null ? "" : d.get(field).toString();
            if (resultType == 1) {
                Result.add(Integer.valueOf(r));
            } else {
                Result.add(r);
            }
        }
        return Result;
    }

    @Override
    public Map getNKBDrugInfo(String drug_name, Integer lang, Integer disease_id) {
        Map drugInfo1 = null;
        List<Map> drugs = reportDrugInfoDao.selectOneApprovedByDrugChineseName(drug_name, lang, disease_id);
        if (drugs.size() > 0 && !drugs.isEmpty()) {
            drugInfo1 = drugs.get(0);
        }
        return drugInfo1;
    }

    @Override
    public Map getClinicalInfo(String clinical_trial_id, String drug_name, Integer lang) {
        Map result = new HashMap<>();
        Map clinicalInfo1 = reportClinicalTrialDao.selectOneNkbClinicalTrialById(clinical_trial_id, lang);
        Map clinicalInfo2 = reportClinicalTrialDao.selectOneClinicalTrialById(clinical_trial_id, lang);
        Long clinicalInfoUpdateDate1 = 0L;
        Long clinicalInfoUpdateDate2 = 0L;
        if (CollectionUtils.isEmpty(clinicalInfo1) && CollectionUtils.isEmpty(clinicalInfo2)) {
            return null;
        }
        if (!CollectionUtils.isEmpty(clinicalInfo1)) {
            clinicalInfoUpdateDate1 = Long.valueOf(clinicalInfo1.get("update_date").toString());
        }
        if (!CollectionUtils.isEmpty(clinicalInfo2)) {
            clinicalInfoUpdateDate2 = Long.valueOf(clinicalInfo2.get("update_date").toString());
        }
        if (clinicalInfo1 != null) {
            result.putAll(clinicalInfo1);
        }
        List<Map> cfda = analysisReportDao.getCfda(drug_name);
        if (!CollectionUtils.isEmpty(cfda)) {
            result.put("cfda", "1");
        } else {
            result.put("cfda", "0");
        }
        if ((!CollectionUtils.isEmpty(clinicalInfo1) && CollectionUtils.isEmpty(clinicalInfo2))) {
            result.put("status", "add");
        } else {
            result.put("status", "update");
        }
        return result;
    }

    public Map<String, List> PotentialInformation(List<Integer> mutationIdList, List<Integer> diseaseIdList, Integer lang, String mutation_type, ReportVarDrug reportVarDrug) {
        List<Map> drugList = new ArrayList<Map>();
        if (reportVarDrug != null) {
            group(reportVarDrug.getDrugsA(), 1, drugList);
            group(reportVarDrug.getDrugsB(), 2, drugList);
            group(reportVarDrug.getDrugsC(), 3, drugList);
            group(reportVarDrug.getDrugsD(), 4, drugList);
            group(reportVarDrug.getResistant_drugsA(), 5, drugList);
            group(reportVarDrug.getResistant_drugsB(), 6, drugList);
            group(reportVarDrug.getResistant_drugsC(), 7, drugList);
            group(reportVarDrug.getResistant_drugsD(), 8, drugList);

            Iterator<Map> iterator = drugList.iterator();
            while (iterator.hasNext()) {
                Map b = iterator.next();
                int drugLevel = Integer.valueOf(b.get("approve_range").toString());
//                Integer drug_id = Integer.parseInt(b.get("drug_id").toString());
                String drug_name = b.get("drug_name").toString();
                String anno_disease_name = b.get("anno_disease_name").toString();
                String drugAndDiseaseName = drug_name + "(" + anno_disease_name + ")";
                Integer disease_id = Integer.parseInt(b.get("anno_disease_id").toString());
                int evidence_phase_id = Integer.parseInt(b.get("evidence_phase_id").toString());
                String annotation = b.getOrDefault("annotation", "").toString();
                b.put("annotation", annotation);
                String anno = "";
                String varDrugNote = reportVarDrug.getVar_drug_desc();
                JSONArray array = JSONArray.fromObject(varDrugNote);
                if (drugLevel >= 5) {
                    for (Object a : array) {
                        JSONObject j = (JSONObject) a;
                        if (j.get("key").toString().indexOf("耐药说明:") != -1) {
                            String resistantDrugNote = j.get("value") == null ? "" : j.get("value").toString();
                            String[] splits = resistantDrugNote.split("\\r\\n");
                            for (String split : splits) {
                                if (split.startsWith(drugAndDiseaseName)) {
                                    String s = drugAndDiseaseName + "：";
                                    String replaceFirst = split.replace(s, "");
                                    anno += replaceFirst + "\n";
                                }
                            }
                            if (!anno.equals("")) {
                                b.put("annotation", anno);
                            }
                        }
                    }
                } else if ("24".equals(b.get("evidence_phase_id").toString())) {
                    Map drugInfo = reportDrugInfoDao.selectOneByDrugChineseName(drug_name, lang, disease_id);
                    if (drugInfo != null) {
                        String approval_desc = drugInfo.get("approval_desc").toString();
                        if (approval_desc != null && !"".equals(approval_desc)) {
                            b.put("annotation", approval_desc);
                        }
                    }
                } else if ("23".equals(b.get("evidence_phase_id").toString())) {
                    for (Object a : array) {
                        JSONObject j = (JSONObject) a;
                        if (j.get("key").toString().indexOf("NCCN指南:") != -1) {
                            String nccnInfo1 = j.get("value") == null ? "" : j.get("value").toString();
                            String[] splits = nccnInfo1.split("\\r\\n");
                            for (String split : splits) {
                                if (split.startsWith(drugAndDiseaseName, 8)) {
                                    String s = "(" + anno_disease_name + ")";
                                    String replaceFirst = split.replace(s, "");
                                    anno += replaceFirst + "\n";
                                }
                            }
                            if (!anno.equals("")) {
                                b.put("annotation", anno);
                            }
                        }
                    }
                } else if (drugLevel >= 2 || drugLevel <= 4) {
                    if (evidence_phase_id != 24) {
                        for (Object a : array) {
                            JSONObject j = (JSONObject) a;
                            if (j.get("key").toString().indexOf("用药说明:") != -1) {
                                String drugAnnotation1 = j.get("value") == null ? "" : j.get("value").toString();
                                String[] splits = drugAnnotation1.split("\\r\\n");
                                for (String split : splits) {
                                    if (split.startsWith(drugAndDiseaseName)) {
                                        String s = drugAndDiseaseName + "：";
                                        String replaceFirst = split.replace(s, "");
                                        anno += replaceFirst + "\n";
                                    }
                                }
                                if (!anno.equals("")) {
                                    b.put("annotation", anno);
                                }
                            }
                        }
                    } else {
                        b.put("annotation", "");
                    }
                }
            }
        }
        Map<String, List> maps = new HashMap<>();
        List<PotentialDrug> potentialDrugList = new ArrayList<>();
        List<DrugResearch> drugResearchList = new ArrayList<>();
        Set<String> drugName = new HashSet<String>();
        Set<String> resistantDrugName = new HashSet<String>();
        for (Map map : drugList) {
            PotentialDrug potentialDrug = new PotentialDrug();
            DrugResearch drugResearch = new DrugResearch();
            int drugLevel = Integer.valueOf(map.get("approve_range").toString());
            String drug_name = map.get("drug_name").toString();
            String anno_disease_name = map.get("anno_disease_name").toString();
            String drugAndDiseaseName = drug_name + anno_disease_name;
            if (drugLevel >= 5) {
                if (resistantDrugName.contains(drugAndDiseaseName)) {
                    continue;
                }
                resistantDrugName.add(drugAndDiseaseName);
            } else {
                if (drugName.contains(drugAndDiseaseName)) {
                    continue;
                }
                drugName.add(drugAndDiseaseName);
            }
            // 耐药
            if (Integer.valueOf(map.get("approve_range").toString()) >= 5 && Integer.valueOf(map.get("approve_range").toString()) < 9) {
//                resistantCancerDrugsList += drug_name + ",";
                if (!map.get("annotation").toString().equals("")) {
                    potentialDrug.setAnnotation_chinese(map.get("annotation").toString());
                    potentialDrug.setDisease_name_chinese(map.get("anno_disease_name").toString());
                    potentialDrug.setDrug_name_chinese(map.get("drug_name").toString());
                    potentialDrug.setEvidence_phase_chinese(map.get("evidence_phase").toString());
                    potentialDrugList.add(potentialDrug);
                }
            }
            // 获益
            if (Integer.valueOf(map.get("approve_range").toString()) < 5) {
                if (!map.get("annotation").toString().equals("")) {
                    drugResearch.setAnnotation_chinese(map.get("annotation").toString());
                    drugResearch.setDrug_name_chinese(map.get("drug_name").toString());
                    drugResearch.setDisease_name_chinese(map.get("anno_disease_name").toString());
                    drugResearch.setEvidence_phase_chinese(map.get("evidence_phase").toString());
                    drugResearchList.add(drugResearch);
                }
            }
        }
        maps.put("potentialDrugList", potentialDrugList);
        maps.put("drugResearchList", drugResearchList);
        return maps;
    }

    public void group(String drugNameStr, Integer drugLevel, List<Map> drugList) {
        if (!"".equals(drugNameStr) && drugNameStr != null) {
            List<String> groupList = Arrays.asList(drugNameStr.split(";"));
            for (String group : groupList) {
                List<String> list = Arrays.asList(group.split("&"));
                String drugName = list.get(0);
                String diseaseName = list.get(1);
                String evidencePhase = list.get(2);
                Map map = new HashMap<>();
                if (drugName.endsWith("%")) {
                    map.put("other_test_required", "1");
                    drugName = drugName.substring(0, drugName.length() - 1);
                } else {
                    map.put("other_test_required", "0");
                }
                if (drugName.endsWith("#")) {
                    map.put("recruiting", "1");
                    drugName = drugName.substring(0, drugName.length() - 1);
                } else {
                    map.put("recruiting", "0");
                }
                Integer drug_id = analysisReportDao.getDrugId(drugName);

                // TODO 暂时解决一下 待排查
                if (diseaseName.equals("胰脏腺癌")) {
                    diseaseName = "胰腺腺癌";
                }
                Map disease = analysisReportDao.getDiseaseId(diseaseName);
                Integer disease_id = Integer.valueOf(disease.get("do_id").toString());
                Integer evidence_phase_id = analysisReportDao.getEvidencePhaseId(evidencePhase);
                map.put("drug_name", drugName);
                map.put("drug_id", drug_id == null ? -1 : drug_id);
                map.put("anno_disease_name", diseaseName);
                map.put("anno_disease_id", disease_id);
                map.put("evidence_phase", evidencePhase);
                map.put("evidence_phase_id", evidence_phase_id);
                map.put("approve_range", drugLevel);
                drugList.add(map);
            }
        }
    }
}
