package com.novo.report.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.NumberOfMutations;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.SampleFilePageBean;
import com.novo.report.beans.SpecimenHead;
import com.novo.report.dao.one.SpecimenHeadDao;
import com.novo.report.dao.three.NewLimsSampleDao;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.service.SampleFileService;

@Service
public class SampleFileServiceImpl implements SampleFileService {

	@Autowired
	private SampleFileDao sampleFileDao;
	@Autowired
	private SpecimenHeadDao specimenHeadDao;
	@Autowired
	private NewLimsSampleDao newLimsSampleDao;
	
	@Override
	public void addSampleFile(SampleFile sampleFile) {
		String specimen_type = sampleFile.getSpecimen_type();
		if(specimen_type!=null && specimen_type.contains("血")){
			sampleFile.setSample_type("blood");
		}else if(specimen_type!=null && specimen_type.contains("组织")){
			sampleFile.setSample_type("tissue");
		}
		sampleFileDao.insertSampleFile(sampleFile);
	}

	@Override
	public SampleFile querySampleFileBySubbarcode(String subbarcode) {
		return sampleFileDao.selectSampleFileBySubbarcode(subbarcode);
	}

	@Override
	public Long getSampleIdBySubbarcode(String subbarcode) {
		return sampleFileDao.getSampleIdBySubbarcode(subbarcode);
	}

	@Override
	public PaginationVO<SampleFile> getsampleFileByPage(SampleFilePageBean sampleFilePageBean) {
		PaginationVO<SampleFile> paginationVO = new PaginationVO<SampleFile>();
		paginationVO.setTotal(sampleFileDao.getTotal(sampleFilePageBean));
		paginationVO.setDataList(sampleFileDao.getsampleFileByPage(sampleFilePageBean));
		return paginationVO;
	}

	@Override
	public void createSampleFile(SampleFile sampleFile) {
		sampleFileDao.insertSampleFile(sampleFile);
	}

	@Override
	public Integer getSampleIdByBarcode(String barcode) {
		return sampleFileDao.getSampleIdByBarcode(barcode);
	}

	@Override
	public Object RefulshLims() {
		try {
			List<SpecimenHead> shList = newLimsSampleDao.getNewLimsSampleList();
			for (SpecimenHead sh : shList) {
				SampleFile sf = new SampleFile();
				sh.setSubBarcode(sh.getBarcode());
				String age = sh.getAge();
				if(age==null || "".equals(age)){
					String birth = sh.getBirthday(); 
					if(birth!=null && !("".equals(birth))) { 
						long birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birth).getTime();
						long currDate = new Date().getTime(); //
						if(birthday<=currDate) { //
							age=(((currDate-birthday)/(24*60*60*1000))/365)+"";
						} 
					}else {
						age="";
					}
				}
				if(age.length()<2){
					age="";
				}
				sf.setEmailaddress(sh.getEmailaddress());
				sf.setSaleremail(sh.getSaleremail());
				sf.setSupportemail(sh.getSupportemail());
				sf.setManageremail(sh.getManageremail());
				sf.setPmemail(sh.getPmemail());
				sf.setPatient_id(sh.getIdnum());
				sf.setProduct_name(sh.getErptestname()==null?sh.getErptestname():sh.getErptestname().trim());
				sf.setCancertype(sh.getCancertype());
				sf.setPathologicaltype(sh.getPathologicaltype());
				sf.setAge(age);
				sf.setClient(sh.getPatientname());
				sf.setSales_contact(sh.getErpsalername());
				sf.setHospital(sh.getCustomername()==null?"":sh.getCustomername());
				sf.setCommission_date(sh.getEnterdate()==null?"":sh.getEnterdate().substring(0, 10));
				sf.setReceived_date(sh.getGetspecdate()==null?"":sh.getGetspecdate().substring(0, 10));
				sf.setPerson_name(sh.getPatientname());
				sf.setGender(sh.getSex());
				sf.setBarcode(sh.getBarcode());
				sf.setSubbarcode(sh.getSubBarcode());
				sf.setBirthday(sh.getBirthday()==null?"":sh.getBirthday().substring(0, 10));
				if(sh.getClinicalremark()==null || "".equals(sh.getClinicalremark())){
					sf.setDisease_type(sh.getCancertype());
					sf.setClinicalremark(sh.getCancertype());
				}else {
					sf.setDisease_type(sh.getClinicalremark());
				}
				sf.setReport_receiver(sh.getReportreceiver());
				sf.setSpecimen_type(sh.getSampletype()==null?(sh.getShsampletype()==null?(sh.getSrsampletype()==null?sh.getSrsampletype():sh.getSrsampletype().trim()):sh.getShsampletype().trim()):sh.getSampletype());
				String specimen_type = sf.getSpecimen_type();
				if(specimen_type!=null && specimen_type.contains("血")){
					sf.setSample_type("blood");
				}else if(specimen_type!=null && specimen_type.contains("组织")){
					sf.setSample_type("tissue");
				}
				String specimennum = sh.getSpecimennum();
				String samplenum = sh.getSamplenum();
				String unit = sh.getUnit();
				String sampleunit = sh.getSampleunit();
				sf.setSpecimen_quantity(((specimennum==null?samplenum:specimennum)==null?"":(specimennum==null?samplenum:specimennum))+((unit==null?sampleunit:unit)==null?"":(unit==null?sampleunit:unit)));
				sf.setCollect_date(sh.getSenddate()==null?"":sh.getSenddate().substring(0, 10));
				sf.setClinicalstages(sh.getClinicalstages());
				sf.setDoctorname(sh.getDoctorname());
				sf.setFastcode(sh.getFastcode());
				sf.setLibraryname(sh.getLibraryname());
				sf.setLocationname(sh.getLocationname());
				sf.setReport_upload_date(sh.getOperatedate());
				sf.setSample_source(sh.getSamplesource());
				sf.setFrom_organ(sh.getFromorgan());
				sf.setGene_type(sh.getGenetype());
				sf.setGene_result(sh.getGeneresult());
				sf.setBirthplace(sh.getBirthplace());
				sf.setFamily_history(sh.getFamilyhistory());
				sampleFileDao.insertSampleFile(sf);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void updateSmapleType(SampleFile sampleFile) {
		sampleFileDao.updateSmapleType(sampleFile);
	}
	

	@Override
	public Integer isExistPerson_id(Integer person_id) {
		
		return sampleFileDao.isExistPerson_id(person_id);
	}

	@Override
	public SampleFile getSampleFileBySubbarcode(String subbarcode) {
		return sampleFileDao.selectSampleFileBySubbarcode(subbarcode);
	}

	@Override
	public void saveMutationsNum(NumberOfMutations numberOfMutations) {
		sampleFileDao.saveMutationsNum(numberOfMutations);
	}

	@Override
	public void saveMutationsNum2(Integer mut_num, String subbarcode, String analysis_date, String file_type) {
		sampleFileDao.saveMutationsNum2(mut_num, subbarcode, analysis_date, file_type);
	}

	@Override
	public List<Map> findMutationsNum(String subbarcode, String analysis_date) {
		return sampleFileDao.findMutationsNum(subbarcode, analysis_date);
	}
	
	
}
