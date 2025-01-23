package com.novo.report.service.impl;


import com.novo.report.dao.two.ModuleDao;
import com.novo.report.mod.ModCancerNoteSummary;
import com.novo.report.mod.ModImportantTargetedGeneSummaryNote;
import com.novo.report.mod.ModProductDesc;
import com.novo.report.mod.ModTestResultSummaryNote;
import com.novo.report.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
}
