package com.novo.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.NkbChemicalDrugAnnotationVw;
import com.novo.report.beans.PreviewNkbChemicalDrugAnnotationVwLists;
import com.novo.report.dao.two.NkbChemicalDrugAnnotationVwDao;
import com.novo.report.service.NkbChemicalDrugAnnotationVwService;
@Service
public class NkbChemicalDrugAnnotationVwServiceImpl implements NkbChemicalDrugAnnotationVwService {
	
	@Autowired
	private NkbChemicalDrugAnnotationVwDao nkbChemicalDrugAnnotationVwDao;

	@Override
	public PreviewNkbChemicalDrugAnnotationVwLists getNkbChemicalDrugAnnotationVwLists(Integer report_id,
			Integer primary_cancer_id) {
		PreviewNkbChemicalDrugAnnotationVwLists previewChemicalLists =  new PreviewNkbChemicalDrugAnnotationVwLists();
		// 获取针对您所患肺癌 化疗药物有效性解析
		List<NkbChemicalDrugAnnotationVw> nkbChemicalDrugAnnotationVw1 = nkbChemicalDrugAnnotationVwDao.getnkbChemicalDrugAnnotationVw1(report_id,primary_cancer_id);
		// 获取针对其它癌种 化疗药物有效性解析
		List<NkbChemicalDrugAnnotationVw> nkbChemicalDrugAnnotationVw2 = nkbChemicalDrugAnnotationVwDao.getnkbChemicalDrugAnnotationVw2(report_id,primary_cancer_id);
		// 获取针对肺癌 化疗药物毒副作用风险解析
		List<NkbChemicalDrugAnnotationVw> nkbChemicalDrugAnnotationVw3 = nkbChemicalDrugAnnotationVwDao.getnkbChemicalDrugAnnotationVw3(report_id,primary_cancer_id);
		// 获取针对其它癌种 化疗药物毒副作用风险解析
		List<NkbChemicalDrugAnnotationVw> nkbChemicalDrugAnnotationVw4 = nkbChemicalDrugAnnotationVwDao.getnkbChemicalDrugAnnotationVw4(report_id,primary_cancer_id);
		//化疗药物相关基因UGT1A1检测结果
		List<NkbChemicalDrugAnnotationVw> nkbChemicalDrugAnnotationVw5 = nkbChemicalDrugAnnotationVwDao.getnkbChemicalDrugAnnotationVw5(report_id, primary_cancer_id);
		
		if(nkbChemicalDrugAnnotationVw1 != null && nkbChemicalDrugAnnotationVw1.size() != 0){
			//重复表示
			Boolean flag = true;
			List<NkbChemicalDrugAnnotationVw> ChemicalDrugAnnotationVw2List = new ArrayList<NkbChemicalDrugAnnotationVw>();
			for (NkbChemicalDrugAnnotationVw nkbChemicalDrugAnnotation2 : nkbChemicalDrugAnnotationVw2) {
				for (NkbChemicalDrugAnnotationVw nkbChemicalDrugAnnotation1 : nkbChemicalDrugAnnotationVw1) {
					if(nkbChemicalDrugAnnotation2.getDrug_name_chinese().equals(nkbChemicalDrugAnnotation1.getDrug_name_chinese()) &&
					   nkbChemicalDrugAnnotation2.getGene().equals(nkbChemicalDrugAnnotation1.getGene()) &&
					   nkbChemicalDrugAnnotation2.getRs_id().equals(nkbChemicalDrugAnnotation1.getRs_id()) &&
					   nkbChemicalDrugAnnotation2.getGenotype().equals(nkbChemicalDrugAnnotation1.getGenotype())){
						flag = false;
						break;
					}
				}
				if(flag){
					ChemicalDrugAnnotationVw2List.add(nkbChemicalDrugAnnotation2);
				}else{
					flag = true;
				}
			}
			previewChemicalLists.setNkbChemicalDrugAnnotationVwList1(nkbChemicalDrugAnnotationVw1);
			previewChemicalLists.setNkbChemicalDrugAnnotationVwList2(ChemicalDrugAnnotationVw2List);
		}else{
			previewChemicalLists.setNkbChemicalDrugAnnotationVwList2(nkbChemicalDrugAnnotationVw2);
		}
		//////////////////////////////////////////////////化疗药物毒副作用风险解析 /////////////
		if(nkbChemicalDrugAnnotationVw3 != null && nkbChemicalDrugAnnotationVw3.size() != 0){
			//重复表示
			Boolean flag = true;
			List<NkbChemicalDrugAnnotationVw> ChemicalDrugAnnotationVw4List = new ArrayList<NkbChemicalDrugAnnotationVw>();
			for (NkbChemicalDrugAnnotationVw nkbChemicalDrugAnnotation4 : nkbChemicalDrugAnnotationVw4) {
				for (NkbChemicalDrugAnnotationVw nkbChemicalDrugAnnotation3 : nkbChemicalDrugAnnotationVw3) {
					if(nkbChemicalDrugAnnotation4.getDrug_name_chinese().equals(nkbChemicalDrugAnnotation3.getDrug_name_chinese()) &&
					   nkbChemicalDrugAnnotation4.getGene().equals(nkbChemicalDrugAnnotation3.getGene()) &&
					   nkbChemicalDrugAnnotation4.getRs_id().equals(nkbChemicalDrugAnnotation3.getRs_id()) &&
					   nkbChemicalDrugAnnotation4.getGenotype().equals(nkbChemicalDrugAnnotation3.getGenotype())){
						flag = false;
						break;
					}
				}
				if(flag){
					ChemicalDrugAnnotationVw4List.add(nkbChemicalDrugAnnotation4);
				}else{
					flag = true;
				}
			}
			previewChemicalLists.setNkbChemicalDrugAnnotationVwList3(nkbChemicalDrugAnnotationVw3);
			previewChemicalLists.setNkbChemicalDrugAnnotationVwList4(ChemicalDrugAnnotationVw4List);
		}else{
			previewChemicalLists.setNkbChemicalDrugAnnotationVwList4(nkbChemicalDrugAnnotationVw4);
		}
		//化疗药物相关基因UGT1A1检测结果
		previewChemicalLists.setNkbChemicalDrugAnnotationVwList5(nkbChemicalDrugAnnotationVw5);
		
		return previewChemicalLists;
	}
}
