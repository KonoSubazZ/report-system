package com.novo.report.utils;

import java.util.HashSet;
import java.util.List;

public class TemplateUtil {
	
	public List ParentDiseaseIDList;
	public HashSet allGeneSet;
	
	public List getParentDiseaseIDList() {
		return ParentDiseaseIDList;
	}

	public void setParentDiseaseIDList(List parentDiseaseIDList) {
		ParentDiseaseIDList = parentDiseaseIDList;
	}

	public HashSet getAllGeneSet() {
		return allGeneSet;
	}

	public void setAllGeneSet(HashSet allGeneSet) {
		this.allGeneSet = allGeneSet;
	}

	public boolean isWildType(String geneList,String DiseaseID) {
		String[] split = DiseaseID.split(",");
		for (String string : split) {
			if(!ParentDiseaseIDList.contains(Integer.parseInt(string))) {
				return false;
			}  
		}
		String[] split2 = geneList.split(",");
		for (String string : split2) {
			if (allGeneSet.contains(string)) {
				return false;
			}
		}
		return true;
	}
}
