package com.novo.report.service.impl;


import com.novo.report.dao.two.ModuleDao;
import com.novo.report.mod.*;
import com.novo.report.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
    public List<String> getImportantTargetedGeneSummaryNote(String templateName) {
        ModImportantTargetedGeneSummaryNote modImportantTargetedGeneSummaryNote = moduleDao.getImportantTargetedGeneSummaryNote(templateName);
        List<String> noteList = new ArrayList<>();
        if (modImportantTargetedGeneSummaryNote != null) {
            String[] notes = modImportantTargetedGeneSummaryNote.getNote().split("\r\n");
            noteList.addAll(Arrays.asList(notes));
        }

        return noteList;
    }

    @Override
    public List<String> getTestResultSummaryNote(String templateName) {
        ModTestResultSummaryNote modTestResultSummaryNote = moduleDao.getTestResultSummaryNote(templateName);
        List<String> noteList = new ArrayList<>();
        if (modTestResultSummaryNote != null) {
            String[] notes = modTestResultSummaryNote.getNote().split("\r\n");
            noteList.addAll(Arrays.asList(notes));
        }
        return noteList;
    }

    @Override
    public List<String> getReferences(String productName, String module) {
        ModReferences references = moduleDao.getReferences(productName, module);
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
    public List<String> getSomaticMutationTipNote(ModCommonNote modCommonNote, Boolean reads, Boolean complex) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        // 所有的提示，根据 reads complex 筛选删除最后一条数据
        List<String> notesList = new ArrayList<>(Arrays.asList(notes));
        if (!reads) {
            notesList.remove(notesList.size() - 2);
            notesList.get(notesList.size() - 1).replace("13.", "12.");
        }
        if (!complex) {
            notesList.remove(notesList.size() - 1);
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
    public List<String> getImmunityNote(ModCommonNote modCommonNote) {
        ModCommonNote commonNote = moduleDao.getCommonNote(modCommonNote);
        List<String> noteList = new ArrayList<>();
        String[] notes = commonNote.getNote().split("\r\n");
        noteList.addAll(Arrays.asList(notes));

        return noteList;
    }
}
