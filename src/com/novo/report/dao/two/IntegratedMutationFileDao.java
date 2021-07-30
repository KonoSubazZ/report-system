package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.CondationIntegratedMutationFile;
import com.novo.report.beans.NameAndDataBean;

public interface IntegratedMutationFileDao {

	List<String> getIntegratedMutationList();
	
	List<NameAndDataBean> getPlatformAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getPlatformAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getHospitalAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getHospitalAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getSpecimenTypeAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getSpecimenTypeAndCountAll(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getProductNameAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getProductNameAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getCancertypeAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getCancertypeAndCountAll(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getClinicalremarkAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getClinicalremarkAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getPathologicaltypeAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getPathologicaltypeAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getMutationFrequencyAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getMutationFrequencyAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getGeneAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getGeneAndCountAll(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getGeneMutationAndCount(CondationIntegratedMutationFile condation);
	
	List<NameAndDataBean> getGeneMutationAndCountAll(CondationIntegratedMutationFile condation);

	
	List<String> getPlatformList();

	List<String> getHospitalList();

	List<String> getSpecimenTypeList();

	List<String> getProductNameList();

	List<String> getCancertypeList();

	List<String> getClinicalremarkList();

	List<String> getGeneList();
	
	List<String> getPathologicaltypeList();
	
	List<String> getFastcodeList();

	List<NameAndDataBean> getPlatformByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getCancertypeByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getClinicalremarkByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getPathologicaltypeByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getMutationFrequencyByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getGeneByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getGeneMutationByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getHospitalByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getSpecimenTypeByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getProductNameByDrugs(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getFastcodeAndCount(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getFastcodeAndCountAll(CondationIntegratedMutationFile condation);

	List<NameAndDataBean> getFastcodeByDrugs(CondationIntegratedMutationFile condation);



}
