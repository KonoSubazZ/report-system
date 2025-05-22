package com.novo.report.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.novo.report.beans.ModCancer;
import com.novo.report.dao.two.ModuleDao;
import com.novo.report.mod.ModCancerNoteSummary;
import com.novo.report.mod.ModCommonNote;
import com.novo.report.mod.ModProductDesc;
import com.novo.report.mod.ModReferences;
import com.novo.report.service.ModuleService;
import com.novo.report.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public ModProductDesc getProductDesc(String templateName) {

        return moduleDao.getProductDesc(templateName);
    }

    @Override
    public ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary) {

        return moduleDao.getCancerNote(modCancerNoteSummary);
    }

    @Override
    public ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary) {
        return moduleDao.getCancerTitle(modCancerNoteSummary);
    }

    @Override
    public List<String> getImportantTargetedGeneSummaryNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        if (commonNote != null) {
            String[] notes = commonNote.getNote().split("\r\n");
            noteList.addAll(Arrays.asList(notes));
        }
        return noteList;
    }

    @Override
    public List<String> getTestResultSummaryNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        if (commonNote != null) {
            String[] notes = commonNote.getNote().split("\r\n");
            noteList.addAll(Arrays.asList(notes));
        }
        return noteList;
    }

    @Override
    public List<String> getReferences(String templateName, String module) {
        ModReferences references = moduleDao.getReferences(templateName, module);
        if (references != null) {
            String[] referencesList = references.getReferences().split("\r\n");
            return Arrays.asList(referencesList);
        }
        return Collections.emptyList();
    }

    @Override
    public String getTMB1(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);

        return commonNote.getNote();
    }

    @Override
    public String getTMB2(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);

        return commonNote.getNote();
    }

    @Override
    public List<String> getTMB3(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public String getMSI1(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);

        return commonNote.getNote();
    }

    @Override
    public List<String> getMSI2(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public List<String> getMSI3(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }


    @Override
    public String getMMR1(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);

        return commonNote.getNote();
    }

    @Override
    public String getMMR2(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);

        return commonNote.getNote();
    }

    @Override
    public List<String> getMMR3(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public List<String> getChemo1List(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public List<String> getChemo2List(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public List<String> getSomaticMutationTipNote(ModCommonNote modCommonNote, Boolean reads, Boolean complex, String panel) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        // 所有的提示，根据 reads complex 筛选删除最后一条数据
        List<String> notesList = new ArrayList<>(Arrays.asList(notes));
        if (!reads) {
//            notesList.remove(9);
            notesList.remove(notesList.size() - 2);
            // notesList.set(notesList.size() - 1, notesList.get(notesList.size() - 1).replace("13.", "12."));
        }
        if (!complex) {
            notesList.remove(notesList.size() - 1);
        }
        // 关于拷贝数的提示，根据模板名称删除
        Map<String, Object> moduleConf = moduleDao.getModuleConf("SOMA_TIP_WITHOUT_CNV");

        // List<String> templateList = MapUtils.getCommaSeparatedList(moduleConf, "templates");
        List<String> panelList = MapUtils.getCommaSeparatedList(moduleConf, "panels");
        if (panelList.contains(panel)) {
            notesList.remove(9);
        }
        noteList.addAll(notesList);

        return noteList;
    }

    public List<String> getSomaticDrugTipNote(ModCommonNote modCommonNote, Boolean reads, Boolean complex, String panel) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        // 所有的提示，根据 reads complex 筛选删除最后一条数据
        List<String> notesList = new ArrayList<>(Arrays.asList(notes));
        if (!reads) {
//            notesList.remove(9);
            notesList.remove(notesList.size() - 2);
            // notesList.set(notesList.size() - 1, notesList.get(notesList.size() - 1).replace("13.", "12."));
        }
        if (!complex) {
            notesList.remove(notesList.size() - 1);
        }
        // 关于拷贝数的提示，根据模板名称删除
        Map<String, Object> moduleConf = moduleDao.getModuleConf("SOMA_TIP_WITHOUT_CNV");

        // List<String> templateList = MapUtils.getCommaSeparatedList(moduleConf, "templates");
        List<String> panelList = MapUtils.getCommaSeparatedList(moduleConf, "panels");
        if (panelList.contains(panel)) {
            notesList.remove(9);
        }
        noteList.addAll(notesList);

        return noteList;
    }

    @Override
    public List<String> getcrMutationTipNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));
        return noteList;
    }

    @Override
    public List<String> getcrDrugTipNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));
        return noteList;
    }

    @Override
    public List<String> getImmunityNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public List<String> getQcNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }

    @Override
    public ModCommonNote getImportantTargetedGeneSummaryNoteAndTitle(ModCommonNote modCommonNote) {
        return moduleDao.getCommonNote(modCommonNote);
    }

    @Override
    public List<String> getSarcomaTypingNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();

        String[] notes = commonNote.getNote().split("\r\n");
        List<String> notesList = new ArrayList<>(Arrays.asList(notes));

        // wesplus 不输出reads, RNA panel 才会输出 reads.
        String panel = modCommonNote.getPanel();
        Map<String, Object> moduleConf = moduleDao.getModuleConf("SARCOMA_WITHOUT_READS");
        List<String> panelList = MapUtils.getCommaSeparatedList(moduleConf, "panels");
        if (panelList.contains(panel)) {
            notesList.remove(2);
        }

        noteList.addAll(notesList);

        return noteList;
    }

    @Override
    public List<ModCancer> getSarcomaTypingNote1(ModCancer cancer) {
        return moduleDao.getSarcomaNote(cancer);
    }

    @Override
    public List<String> getconfPanelList(String conf) {
        Map<String, Object> moduleConf = moduleDao.getModuleConf(conf);

        return MapUtils.getCommaSeparatedList(moduleConf, "panels");
    }

    @Override
    public List<String> getconfTemplateList(String conf) {
        Map<String, Object> moduleConf = moduleDao.getModuleConf(conf);

        return MapUtils.getCommaSeparatedList(moduleConf, "templates");
    }

    @Override
    public String getConfGenes(String panel, List<String> geneSymbols) {
        String genesJson = moduleDao.getConfGenes(panel);
        // 特殊配置 gene_panel 是否存在
        if (genesJson != null && !genesJson.isEmpty()) {
            return genesJson;
        }

        // 不存在则根据 panel_gene 构建默认格式
        return buildDefaultGeneJson(geneSymbols);
    }

    @Override
    public String getCRTumors(String gene, String gender, String clinicalSignificance) {
         String DEFAULT_TUMORS = "-";
         String MALE = "男";
         String FEMALE = "女";

        // 未知临床意义
        if (clinicalSignificance.equals("3")) {
            return DEFAULT_TUMORS;
        }
        if (!MALE.equals(gender) && !FEMALE.equals(gender)) {
            gender = "/";
        }

        String CRTumors = moduleDao.getCRTumors(gene, gender);
        return CRTumors == null ? DEFAULT_TUMORS : CRTumors;
    }

    private String buildDefaultGeneJson(List<String> geneSymbols) {

        Gson gson = new Gson();
        // 创建基因表对象
        JsonObject geneTable = new JsonObject();

        // 使用Java 8+的String.join方法简化基因列表构建
        String genesStr = String.join(",", geneSymbols);

        // 设置基因和标题属性
        geneTable.addProperty("genes", genesStr);
        geneTable.addProperty("title", "");
        geneTable.addProperty("type", "ALL");

        // 创建基因表数组并添加当前表
        JsonArray geneTables = new JsonArray();
        geneTables.add(geneTable);

        // 创建最终JSON对象
        JsonObject finalJson = new JsonObject();
        finalJson.add("gene_tables", geneTables);

        return gson.toJson(finalJson);
    }
}
