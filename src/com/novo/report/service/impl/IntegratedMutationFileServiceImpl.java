package com.novo.report.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.CondationIntegratedMutationFile;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.NameAndDataBean;
import com.novo.report.beans.NgsCountList;
import com.novo.report.dao.two.IntegratedMutationFileDao;
import com.novo.report.service.IntegratedMutationFileService;
@Service
public class IntegratedMutationFileServiceImpl implements IntegratedMutationFileService {
	
	@Autowired
	private IntegratedMutationFileDao integratedMutationFileDao;
	
	@Override
	public List<List<NgsCountList>> getNgsCountList(CondationIntegratedMutationFile condation){
		
		List<NameAndDataBean> list= new ArrayList<NameAndDataBean>();
		List<NameAndDataBean> list2= new ArrayList<NameAndDataBean>();
		List<NameAndDataBean> drugsForIndicationList= new ArrayList<NameAndDataBean>();
		List<NameAndDataBean> drugsForOtherIndicationList= new ArrayList<NameAndDataBean>();
		List<NameAndDataBean> drugsInClinicalTrialsList = new ArrayList<NameAndDataBean>();
		List<NgsCountList> list4=new ArrayList<NgsCountList>();
		List<NgsCountList> list5=new ArrayList<NgsCountList>();
		List<NgsCountList> list6=new ArrayList<NgsCountList>();
		List<NgsCountList> list7=new ArrayList<NgsCountList>();
		List<List<NgsCountList>> returnList=new ArrayList<List<NgsCountList>>();
		if("platform".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getPlatformAndCount(condation);
			list2 = integratedMutationFileDao.getPlatformAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getPlatformByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getPlatformByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getPlatformByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("hospital".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getHospitalAndCount(condation);
			list2 = integratedMutationFileDao.getHospitalAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getHospitalByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getHospitalByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getHospitalByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("specimen_type".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getSpecimenTypeAndCount(condation);
			list2 = integratedMutationFileDao.getSpecimenTypeAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getSpecimenTypeByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getSpecimenTypeByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getSpecimenTypeByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("product_name".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getProductNameAndCount(condation);
			list2 = integratedMutationFileDao.getProductNameAndCountAll(condation);
			NumberFormat   nf=new  DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getProductNameByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getProductNameByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getProductNameByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("cancertype".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getCancertypeAndCount(condation);
			list2 = integratedMutationFileDao.getCancertypeAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getCancertypeByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getCancertypeByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getCancertypeByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("clinicalremark".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getClinicalremarkAndCount(condation);
			list2 = integratedMutationFileDao.getClinicalremarkAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getClinicalremarkByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getClinicalremarkByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getClinicalremarkByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("pathologicaltype".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getPathologicaltypeAndCount(condation);
			list2 = integratedMutationFileDao.getPathologicaltypeAndCountAll(condation);
			NumberFormat   nf=new  DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getPathologicaltypeByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_indication");
								list5.add(ngsCountList);
							}
						}
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getPathologicaltypeByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsForOtherIndicationList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
								ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_for_other_indications");
								list6.add(ngsCountList);
							}
						}
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getPathologicaltypeByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						for (int j = 0; j < list2.size(); j++) {
							if(drugsInClinicalTrialsList.get(k).getName().equals(list2.get(j).getName())){
								NgsCountList ngsCountList = new NgsCountList();
								ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
								ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
								ngsCountList.setDatatwo(list2.get(j).getData());
								double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(j).getData())*100;
								rate = Double.parseDouble(nf.format(rate));
								ngsCountList.setRate("bt"+rate+"et");
								ngsCountList.setDrugs("drugs_in_clinical_trials");
								list7.add(ngsCountList);
							}
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					if(list.get(i).getName().equals(list2.get(j).getName())){
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(list.get(i).getName());
						ngsCountList.setData("bt"+list.get(i).getData()+"et");
						ngsCountList.setDatatwo(list2.get(j).getData());
						double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(j).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						list4.add(ngsCountList);
					}
				}
			}
		}
		if("mutation_frequency".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getMutationFrequencyAndCount(condation);
			list2 = integratedMutationFileDao.getMutationFrequencyAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getMutationFrequencyByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_indication");
						list5.add(ngsCountList);
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getMutationFrequencyByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_other_indications");
						list6.add(ngsCountList);
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getMutationFrequencyByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
						ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_in_clinical_trials");
						list7.add(ngsCountList);
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				NgsCountList ngsCountList = new NgsCountList();
				ngsCountList.setName(list.get(i).getName());
				ngsCountList.setData("bt"+list.get(i).getData()+"et");
				ngsCountList.setDatatwo(list2.get(0).getData());
				double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(0).getData())*100;
				rate = Double.parseDouble(nf.format(rate));
				ngsCountList.setRate("bt"+rate+"et");
				list4.add(ngsCountList);
			}
		}
		if("gene".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getGeneAndCount(condation);
			list2 = integratedMutationFileDao.getGeneAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getGeneByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_indication");
						list5.add(ngsCountList);
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getGeneByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_other_indications");
						list6.add(ngsCountList);
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getGeneByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
						ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_in_clinical_trials");
						list7.add(ngsCountList);
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				NgsCountList ngsCountList = new NgsCountList();
				ngsCountList.setName(list.get(i).getName());
				ngsCountList.setData("bt"+list.get(i).getData()+"et");
				ngsCountList.setDatatwo(list2.get(0).getData());
				double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(0).getData())*100;
				rate = Double.parseDouble(nf.format(rate));
				ngsCountList.setRate("bt"+rate+"et");
				list4.add(ngsCountList);
			}
		}
		if("gene_mutation".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getGeneMutationAndCount(condation);
			list2 = integratedMutationFileDao.getGeneMutationAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getGeneMutationByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_indication");
						list5.add(ngsCountList);
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getGeneMutationByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_other_indications");
						list6.add(ngsCountList);
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getGeneMutationByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
						ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_in_clinical_trials");
						list7.add(ngsCountList);
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				NgsCountList ngsCountList = new NgsCountList();
				ngsCountList.setName(list.get(i).getName());
				ngsCountList.setData("bt"+list.get(i).getData()+"et");
				ngsCountList.setDatatwo(list2.get(0).getData());
				double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(0).getData())*100;
				rate = Double.parseDouble(nf.format(rate));
				ngsCountList.setRate("bt"+rate+"et");
				list4.add(ngsCountList);
			}
		}
		if("fastcode".equals(condation.getCategoryType())){
			list = integratedMutationFileDao.getFastcodeAndCount(condation);
			list2 = integratedMutationFileDao.getFastcodeAndCountAll(condation);
			NumberFormat nf = new DecimalFormat( "0.0 ");
			for (int i = 0; i < 3; i++) {
				if(i==0){
					condation.setDrugs("drugs_for_indication");
					drugsForIndicationList = integratedMutationFileDao.getFastcodeByDrugs(condation);
					for (int k = 0; k < drugsForIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_indication");
						list5.add(ngsCountList);
					}
				} else if(i==1) {
					condation.setDrugs("drugs_for_other_indications");
					drugsForOtherIndicationList = integratedMutationFileDao.getFastcodeByDrugs(condation);
					for (int k = 0; k < drugsForOtherIndicationList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsForOtherIndicationList.get(k).getName());
						ngsCountList.setData("bt"+drugsForOtherIndicationList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsForOtherIndicationList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_for_other_indications");
						list6.add(ngsCountList);
					}
				} else {
					condation.setDrugs("drugs_in_clinical_trials");
					drugsInClinicalTrialsList = integratedMutationFileDao.getFastcodeByDrugs(condation);
					for (int k = 0; k < drugsInClinicalTrialsList.size(); k++) {
						NgsCountList ngsCountList = new NgsCountList();
						ngsCountList.setName(drugsInClinicalTrialsList.get(k).getName());
						ngsCountList.setData("bt"+drugsInClinicalTrialsList.get(k).getData()+"et");
						ngsCountList.setDatatwo(list2.get(0).getData());
						double rate=Double.parseDouble(drugsInClinicalTrialsList.get(k).getData())/Double.parseDouble(list2.get(0).getData())*100;
						rate = Double.parseDouble(nf.format(rate));
						ngsCountList.setRate("bt"+rate+"et");
						ngsCountList.setDrugs("drugs_in_clinical_trials");
						list7.add(ngsCountList);
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				NgsCountList ngsCountList = new NgsCountList();
				ngsCountList.setName(list.get(i).getName());
				ngsCountList.setData("bt"+list.get(i).getData()+"et");
				ngsCountList.setDatatwo(list2.get(0).getData());
				double rate=Double.parseDouble(list.get(i).getData())/Double.parseDouble(list2.get(0).getData())*100;
				rate = Double.parseDouble(nf.format(rate));
				ngsCountList.setRate("bt"+rate+"et");
				list4.add(ngsCountList);
			}
		}
		returnList.add(list4);
		returnList.add(list5);
		returnList.add(list6);
		returnList.add(list7);
		return returnList;
	}
	
	@Override
	public IntegratedMutationFileList getIntegratedMutationList() {
		IntegratedMutationFileList imfList = new IntegratedMutationFileList();
		imfList.setPlatformList(integratedMutationFileDao.getPlatformList());
		imfList.setHospitalList(integratedMutationFileDao.getHospitalList());
		imfList.setSpecimenTypeList(integratedMutationFileDao.getSpecimenTypeList());
		imfList.setProductNameList(integratedMutationFileDao.getProductNameList());
		imfList.setCancertypeList(integratedMutationFileDao.getCancertypeList());
		imfList.setClinicalremarkList(integratedMutationFileDao.getClinicalremarkList());
		imfList.setGeneList(integratedMutationFileDao.getGeneList());
		imfList.setPathologicaltypeList(integratedMutationFileDao.getPathologicaltypeList());
		imfList.setFastcodeList(integratedMutationFileDao.getFastcodeList());
		return imfList;
		
	}

}
