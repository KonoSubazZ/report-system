package com.novo.report.beans;

public class SampleFile {
	private Integer sample_id;// 主鍵
	private String person_id;
	private String person_name;// PATIENTNAME 姓名
	private String gender;// SEX 性别
	private String birthday;// BIRTHDAY 出生日期
	private String patient_id;
	private String subbarcode;// subbarcode 样本编号 | 患者筛选号
	private String barcode;
	private String hospital;// CUSTOMERNAME 送检医院 | 送检单位
	private String received_date;// GETSPECDATE 接收日期
	private String specimen_type;// SAMPLETYPE 样品种类
	private String specimen_quantity;// SAMPLENUM,SAMPLEUNIT 样本量
	private String testing_program;
	private String disease_type;// CLINICALREMARK 疾病种类
	private String comparison;
	private String client;// PATIENTNAME 委托人
	private String commission_date;// ENTERDATE 委托日期
	private String sales_contact;
	private String report_receiver;
	private String collect_date;//送检日期
	private String data_path;
	private String info_officer;
	private String sample_nature;
	private String selected_genes;
	private String testing_before_medicine;
	private String other_description;
	private String loaded_date;
	private Integer count;
	private String age;
	private String clinicalremark;
	private String cancertype;
	private String pathologicaltype;
	private String product_name;
	private String remark;
	
	private String clinicalstages;
	private String libraryname;
	private String doctorname;
	private String fastcode;
	private String locationname; //送检科室
	private String report_upload_date;  //报告上传时间
	
	private String sample_source; //标本来源
	private String from_organ;  //检测部位
	private String turmor_cell_ratio;  //肿瘤细胞比例
	private String gene_type;  //检测基因
	private String gene_result;  //检测结果
	private String medication_history;  //临床用药史
	private String birthplace;  //籍贯
	private String family_history;  //家族史
	private String sample_type;  
	private String room;  
	private String bed;  
	private String emailaddress;  //客户邮箱
	private String saleremail;   //销售员邮箱
	private String supportemail;   //技术支持邮箱
	private String manageremail;   //大区经理邮箱
	private String pmemail;   //运营群邮箱
	private String patient_phone;
	private String customer;
	private String tumorcellcontent;
	private String DNA_total;
	private String DNA_degradation;
	private String outbound_quantity;
	private String plane_data;
	private String sequencing_depth;
	private String coverage;
	private String coverage_uniformity;
	private String genome_alignment;
	private String base_quality;
	private String run_name;
	private String dna_index;
	private String run_code;
	private String rna_index;
	private String DNAQubit;
	private String RNAQubit;
	private String template_subbarcode;
	private String qubit;
	private String test_program;
	private String pool_quantity;
	private String report_type;
	private String dna_panel;
	private String rna_panel;
	private String run_id;
	private String I5;
	private String I7;
	private String test_product;
	private String ward;
	private String consultation;
	private String DNANucleic;
	private String RNANucleic;
	private String DNALibrary;
	private String RNALibrary;
	private String DNAPlaneData;
	private String meanSequencingDepth;
	private String targetAreaCoverage;
	private String RNAPlaneData;
	private String ReadsNumber;
	private String review_doctor;
	private String test_number;
	private String carcinoma;
	private String clinicaldiagnosis;
	private String sampleremark;
	private String firsttreatment;	//治疗史1
	private String secondtreatment;	//治疗史2
	private String thirdtreatment;	//治疗史3
	private String specimenno;	//病理编号 或者 银丰样本编号（银丰基因科技有限公司）
	private String serial_number;	//样本编号(流水号)
	private String registration_number;	//登记号
	private String recordercode;	//创建人编码
	private String mailingaddress;	//报告邮寄地址 / 病理诊断
	private String sample_barcode; // 样本条码
	private String tnm_periodization; // TNM分期
	private String inspection_number;	//送检次数
	private String receiv_ertele_phone;	//送检电话
	private String customertype;	//客户类别
	private String FAMILYFIRST;

	public String getPCODE() {
		return PCODE;
	}

	public void setPCODE(String PCODE) {
		this.PCODE = PCODE;
	}

	private String PCODE;

	public String getFAMILYFIRST() {
		return FAMILYFIRST;
	}

	public void setFAMILYFIRST(String FAMILYFIRST) {
		this.FAMILYFIRST = FAMILYFIRST;
	}


	public String getLaboratoryname() {
		return laboratoryname;
	}

	public void setLaboratoryname(String laboratoryname) {
		this.laboratoryname = laboratoryname;
	}

	private String laboratoryname;
	public String getTumorcellcontent() {
		return tumorcellcontent;
	}

	public void setTumorcellcontent(String tumorcellcontent) {
		this.tumorcellcontent = tumorcellcontent;
	}

	public String getDNA_total() {
		return DNA_total;
	}

	public void setDNA_total(String DNA_total) {
		this.DNA_total = DNA_total;
	}

	public String getDNA_degradation() {
		return DNA_degradation;
	}

	public void setDNA_degradation(String DNA_degradation) {
		this.DNA_degradation = DNA_degradation;
	}

	public String getOutbound_quantity() {
		return outbound_quantity;
	}

	public void setOutbound_quantity(String outbound_quantity) {
		this.outbound_quantity = outbound_quantity;
	}

	public String getPlane_data() {
		return plane_data;
	}

	public void setPlane_data(String plane_data) {
		this.plane_data = plane_data;
	}

	public String getSequencing_depth() {
		return sequencing_depth;
	}

	public void setSequencing_depth(String sequencing_depth) {
		this.sequencing_depth = sequencing_depth;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getCoverage_uniformity() {
		return coverage_uniformity;
	}

	public void setCoverage_uniformity(String coverage_uniformity) {
		this.coverage_uniformity = coverage_uniformity;
	}

	public String getGenome_alignment() {
		return genome_alignment;
	}

	public void setGenome_alignment(String genome_alignment) {
		this.genome_alignment = genome_alignment;
	}

	public String getBase_quality() {
		return base_quality;
	}

	public void setBase_quality(String base_quality) {
		this.base_quality = base_quality;
	}

	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getSaleremail() {
		return saleremail;
	}
	public void setSaleremail(String saleremail) {
		this.saleremail = saleremail;
	}
	public String getSupportemail() {
		return supportemail;
	}
	public void setSupportemail(String supportemail) {
		this.supportemail = supportemail;
	}
	public String getManageremail() {
		return manageremail;
	}
	public void setManageremail(String manageremail) {
		this.manageremail = manageremail;
	}
	public String getPmemail() {
		return pmemail;
	}
	public void setPmemail(String pmemail) {
		this.pmemail = pmemail;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getBed() {
		return bed;
	}
	public void setBed(String bed) {
		this.bed = bed;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	public String getSample_source() {
		return sample_source;
	}
	public void setSample_source(String sample_source) {
		this.sample_source = sample_source;
	}
	public String getFrom_organ() {
		return from_organ;
	}
	public void setFrom_organ(String from_organ) {
		this.from_organ = from_organ;
	}
	public String getTurmor_cell_ratio() {
		return turmor_cell_ratio;
	}
	public void setTurmor_cell_ratio(String turmor_cell_ratio) {
		this.turmor_cell_ratio = turmor_cell_ratio;
	}
	public String getGene_type() {
		return gene_type;
	}
	public void setGene_type(String gene_type) {
		this.gene_type = gene_type;
	}
	public String getGene_result() {
		return gene_result;
	}
	public void setGene_result(String gene_result) {
		this.gene_result = gene_result;
	}
	public String getMedication_history() {
		return medication_history;
	}
	public void setMedication_history(String medication_history) {
		this.medication_history = medication_history;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getFamily_history() {
		return family_history;
	}
	public void setFamily_history(String family_history) {
		this.family_history = family_history;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public Integer getSample_id() {
		return sample_id;
	}
	public void setSample_id(Integer sample_id) {
		this.sample_id = sample_id;
	}
	public String getPerson_id() {
		return person_id;
	}
	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getReceived_date() {
		return received_date;
	}
	public void setReceived_date(String received_date) {
		this.received_date = received_date;
	}
	public String getSpecimen_type() {
		return specimen_type;
	}
	public void setSpecimen_type(String specimen_type) {
		this.specimen_type = specimen_type;
	}
	public String getSpecimen_quantity() {
		return specimen_quantity;
	}
	public void setSpecimen_quantity(String specimen_quantity) {
		this.specimen_quantity = specimen_quantity;
	}
	public String getTesting_program() {
		return testing_program;
	}
	public void setTesting_program(String testing_program) {
		this.testing_program = testing_program;
	}
	public String getDisease_type() {
		return disease_type;
	}
	public void setDisease_type(String disease_type) {
		this.disease_type = disease_type;
	}
	public String getComparison() {
		return comparison;
	}
	public void setComparison(String comparison) {
		this.comparison = comparison;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getCommission_date() {
		return commission_date;
	}
	public void setCommission_date(String commission_date) {
		this.commission_date = commission_date;
	}
	public String getSales_contact() {
		return sales_contact;
	}
	public void setSales_contact(String sales_contact) {
		this.sales_contact = sales_contact;
	}
	public String getReport_receiver() {
		return report_receiver;
	}
	public void setReport_receiver(String report_receiver) {
		this.report_receiver = report_receiver;
	}
	public String getCollect_date() {
		return collect_date;
	}
	public void setCollect_date(String collect_date) {
		this.collect_date = collect_date;
	}
	public String getData_path() {
		return data_path;
	}
	public void setData_path(String data_path) {
		this.data_path = data_path;
	}
	public String getInfo_officer() {
		return info_officer;
	}
	public void setInfo_officer(String info_officer) {
		this.info_officer = info_officer;
	}
	public String getSample_nature() {
		return sample_nature;
	}
	public void setSample_nature(String sample_nature) {
		this.sample_nature = sample_nature;
	}
	public String getSelected_genes() {
		return selected_genes;
	}
	public void setSelected_genes(String selected_genes) {
		this.selected_genes = selected_genes;
	}
	public String getTesting_before_medicine() {
		return testing_before_medicine;
	}
	public void setTesting_before_medicine(String testing_before_medicine) {
		this.testing_before_medicine = testing_before_medicine;
	}
	public String getOther_description() {
		return other_description;
	}
	public void setOther_description(String other_description) {
		this.other_description = other_description;
	}
	public String getLoaded_date() {
		return loaded_date;
	}
	public void setLoaded_date(String loaded_date) {
		this.loaded_date = loaded_date;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getClinicalremark() {
		return clinicalremark;
	}
	public void setClinicalremark(String clinicalremark) {
		this.clinicalremark = clinicalremark;
	}
	public String getCancertype() {
		return cancertype;
	}
	public void setCancertype(String cancertype) {
		this.cancertype = cancertype;
	}
	public String getPathologicaltype() {
		return pathologicaltype;
	}
	public void setPathologicaltype(String pathologicaltype) {
		this.pathologicaltype = pathologicaltype;
	}
	
	public String getClinicalstages() {
		return clinicalstages;
	}
	public void setClinicalstages(String clinicalstages) {
		this.clinicalstages = clinicalstages;
	}
	public String getLibraryname() {
		return libraryname;
	}
	public void setLibraryname(String libraryname) {
		this.libraryname = libraryname;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getFastcode() {
		return fastcode;
	}
	public void setFastcode(String fastcode) {
		this.fastcode = fastcode;
	}
	
	public String getLocationname() {
		return locationname;
	}
	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}
	
	
	public String getReport_upload_date() {
		return report_upload_date;
	}
	public void setReport_upload_date(String report_upload_date) {
		this.report_upload_date = report_upload_date;
	}
	public String getSample_type() {
		return sample_type;
	}
	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}
	
	
	public String getPatient_phone() {
		return patient_phone;
	}
	public void setPatient_phone(String patient_phone) {
		this.patient_phone = patient_phone;
	}

	public String getRun_name() {
		return run_name;
	}

	public void setRun_name(String run_name) {
		this.run_name = run_name;
	}

	public String getDna_index() {
		return dna_index;
	}

	public void setDna_index(String dna_index) {
		this.dna_index = dna_index;
	}

	public String getRun_code() {
		return run_code;
	}

	public void setRun_code(String run_code) {
		this.run_code = run_code;
	}

	public String getRna_index() {
		return rna_index;
	}

	public void setRna_index(String rna_index) {
		this.rna_index = rna_index;
	}

	public String getDNAQubit() {
		return DNAQubit;
	}

	public void setDNAQubit(String DNAQubit) {
		this.DNAQubit = DNAQubit;
	}

	public String getRNAQubit() {
		return RNAQubit;
	}

	public void setRNAQubit(String RNAQubit) {
		this.RNAQubit = RNAQubit;
	}

	public String getTemplate_subbarcode() {
		return template_subbarcode;
	}

	public void setTemplate_subbarcode(String template_subbarcode) {
		this.template_subbarcode = template_subbarcode;
	}

	public String getQubit() {
		return qubit;
	}

	public void setQubit(String qubit) {
		this.qubit = qubit;
	}

	public String getTest_program() {
		return test_program;
	}

	public void setTest_program(String test_program) {
		this.test_program = test_program;
	}

	public String getPool_quantity() {
		return pool_quantity;
	}

	public void setPool_quantity(String pool_quantity) {
		this.pool_quantity = pool_quantity;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	public String getDna_panel() {
		return dna_panel;
	}

	public void setDna_panel(String dna_panel) {
		this.dna_panel = dna_panel;
	}

	public String getRna_panel() {
		return rna_panel;
	}

	public void setRna_panel(String rna_panel) {
		this.rna_panel = rna_panel;
	}

	public String getRun_id() {
		return run_id;
	}

	public void setRun_id(String run_id) {
		this.run_id = run_id;
	}

	public String getI5() {
		return I5;
	}

	public void setI5(String i5) {
		I5 = i5;
	}

	public String getI7() {
		return I7;
	}

	public void setI7(String i7) {
		I7 = i7;
	}

	public String getTest_product() {
		return test_product;
	}

	public void setTest_product(String test_product) {
		this.test_product = test_product;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getConsultation() {
		return consultation;
	}

	public void setConsultation(String consultation) {
		this.consultation = consultation;
	}

	public String getDNANucleic() {
		return DNANucleic;
	}

	public void setDNANucleic(String DNANucleic) {
		this.DNANucleic = DNANucleic;
	}

	public String getRNANucleic() {
		return RNANucleic;
	}

	public void setRNANucleic(String RNANucleic) {
		this.RNANucleic = RNANucleic;
	}

	public String getDNALibrary() {
		return DNALibrary;
	}

	public void setDNALibrary(String DNALibrary) {
		this.DNALibrary = DNALibrary;
	}

	public String getRNALibrary() {
		return RNALibrary;
	}

	public void setRNALibrary(String RNALibrary) {
		this.RNALibrary = RNALibrary;
	}

	public String getDNAPlaneData() {
		return DNAPlaneData;
	}

	public void setDNAPlaneData(String DNAPlaneData) {
		this.DNAPlaneData = DNAPlaneData;
	}

	public String getMeanSequencingDepth() {
		return meanSequencingDepth;
	}

	public void setMeanSequencingDepth(String meanSequencingDepth) {
		this.meanSequencingDepth = meanSequencingDepth;
	}

	public String getTargetAreaCoverage() {
		return targetAreaCoverage;
	}

	public void setTargetAreaCoverage(String targetAreaCoverage) {
		this.targetAreaCoverage = targetAreaCoverage;
	}

	public String getRNAPlaneData() {
		return RNAPlaneData;
	}

	public void setRNAPlaneData(String RNAPlaneData) {
		this.RNAPlaneData = RNAPlaneData;
	}

	public String getReadsNumber() {
		return ReadsNumber;
	}

	public void setReadsNumber(String readsNumber) {
		ReadsNumber = readsNumber;
	}

	public String getReview_doctor() {
		return review_doctor;
	}

	public void setReview_doctor(String review_doctor) {
		this.review_doctor = review_doctor;
	}

	public String getTest_number() {
		return test_number;
	}

	public void setTest_number(String test_number) {
		this.test_number = test_number;
	}

	public String getCarcinoma() {
		return carcinoma;
	}

	public void setCarcinoma(String carcinoma) {
		this.carcinoma = carcinoma;
	}

	public String getClinicaldiagnosis() {
		return clinicaldiagnosis;
	}

	public String getSampleremark() {
		return sampleremark;
	}

	public void setSampleremark(String sampleremark) {
		this.sampleremark = sampleremark;
	}

	public void setClinicaldiagnosis(String clinicaldiagnosis) {
		this.clinicaldiagnosis = clinicaldiagnosis;
	}

	public String getFirsttreatment() {
		return firsttreatment;
	}

	public void setFirsttreatment(String firsttreatment) {
		this.firsttreatment = firsttreatment;
	}

	public String getSecondtreatment() {
		return secondtreatment;
	}

	public void setSecondtreatment(String secondtreatment) {
		this.secondtreatment = secondtreatment;
	}

	public String getThirdtreatment() {
		return thirdtreatment;
	}

	public void setThirdtreatment(String thirdtreatment) {
		this.thirdtreatment = thirdtreatment;
	}

	public String getSpecimenno() {
		return specimenno;
	}

	public void setSpecimenno(String specimenno) {
		this.specimenno = specimenno;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getRegistration_number() {
		return registration_number;
	}

	public void setRegistration_number(String registration_number) {
		this.registration_number = registration_number;
	}

	public String getRecordercode() {
		return recordercode;
	}

	public void setRecordercode(String recordercode) {
		this.recordercode = recordercode;
	}

	public String getMailingaddress() {
		return mailingaddress;
	}

	public void setMailingaddress(String mailingaddress) {
		this.mailingaddress = mailingaddress;
	}

	public String getSample_barcode() {
		return sample_barcode;
	}

	public void setSample_barcode(String sample_barcode) {
		this.sample_barcode = sample_barcode;
	}

	public String getTnm_periodization() {
		return tnm_periodization;
	}

	public void setTnm_periodization(String tnm_periodization) {
		this.tnm_periodization = tnm_periodization;
	}

	public String getInspection_number() {
		return inspection_number;
	}

	public void setInspection_number(String inspection_number) {
		this.inspection_number = inspection_number;
	}

	public String getReceiv_ertele_phone() {
		return receiv_ertele_phone;
	}

	public void setReceiv_ertele_phone(String receiv_ertele_phone) {
		this.receiv_ertele_phone = receiv_ertele_phone;
	}

	public String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	@Override
	public String toString() {
		return "SampleFile{" +
				"sample_id=" + sample_id +
				", person_id='" + person_id + '\'' +
				", person_name='" + person_name + '\'' +
				", gender='" + gender + '\'' +
				", birthday='" + birthday + '\'' +
				", patient_id='" + patient_id + '\'' +
				", subbarcode='" + subbarcode + '\'' +
				", barcode='" + barcode + '\'' +
				", hospital='" + hospital + '\'' +
				", received_date='" + received_date + '\'' +
				", specimen_type='" + specimen_type + '\'' +
				", specimen_quantity='" + specimen_quantity + '\'' +
				", testing_program='" + testing_program + '\'' +
				", disease_type='" + disease_type + '\'' +
				", comparison='" + comparison + '\'' +
				", client='" + client + '\'' +
				", commission_date='" + commission_date + '\'' +
				", sales_contact='" + sales_contact + '\'' +
				", report_receiver='" + report_receiver + '\'' +
				", collect_date='" + collect_date + '\'' +
				", data_path='" + data_path + '\'' +
				", info_officer='" + info_officer + '\'' +
				", sample_nature='" + sample_nature + '\'' +
				", selected_genes='" + selected_genes + '\'' +
				", testing_before_medicine='" + testing_before_medicine + '\'' +
				", other_description='" + other_description + '\'' +
				", loaded_date='" + loaded_date + '\'' +
				", count=" + count +
				", age='" + age + '\'' +
				", clinicalremark='" + clinicalremark + '\'' +
				", cancertype='" + cancertype + '\'' +
				", pathologicaltype='" + pathologicaltype + '\'' +
				", product_name='" + product_name + '\'' +
				", remark='" + remark + '\'' +
				", clinicalstages='" + clinicalstages + '\'' +
				", libraryname='" + libraryname + '\'' +
				", doctorname='" + doctorname + '\'' +
				", fastcode='" + fastcode + '\'' +
				", locationname='" + locationname + '\'' +
				", report_upload_date='" + report_upload_date + '\'' +
				", sample_source='" + sample_source + '\'' +
				", from_organ='" + from_organ + '\'' +
				", turmor_cell_ratio='" + turmor_cell_ratio + '\'' +
				", gene_type='" + gene_type + '\'' +
				", gene_result='" + gene_result + '\'' +
				", medication_history='" + medication_history + '\'' +
				", birthplace='" + birthplace + '\'' +
				", family_history='" + family_history + '\'' +
				", sample_type='" + sample_type + '\'' +
				", room='" + room + '\'' +
				", bed='" + bed + '\'' +
				", emailaddress='" + emailaddress + '\'' +
				", saleremail='" + saleremail + '\'' +
				", supportemail='" + supportemail + '\'' +
				", manageremail='" + manageremail + '\'' +
				", pmemail='" + pmemail + '\'' +
				", patient_phone='" + patient_phone + '\'' +
				", customer='" + customer + '\'' +
				", tumorcellcontent='" + tumorcellcontent + '\'' +
				", DNA_total='" + DNA_total + '\'' +
				", DNA_degradation='" + DNA_degradation + '\'' +
				", outbound_quantity='" + outbound_quantity + '\'' +
				", plane_data='" + plane_data + '\'' +
				", sequencing_depth='" + sequencing_depth + '\'' +
				", coverage='" + coverage + '\'' +
				", coverage_uniformity='" + coverage_uniformity + '\'' +
				", genome_alignment='" + genome_alignment + '\'' +
				", base_quality='" + base_quality + '\'' +
				", run_name='" + run_name + '\'' +
				", dna_index='" + dna_index + '\'' +
				", run_code='" + run_code + '\'' +
				", rna_index='" + rna_index + '\'' +
				", DNAQubit='" + DNAQubit + '\'' +
				", RNAQubit='" + RNAQubit + '\'' +
				", template_subbarcode='" + template_subbarcode + '\'' +
				", qubit='" + qubit + '\'' +
				", test_program='" + test_program + '\'' +
				", pool_quantity='" + pool_quantity + '\'' +
				", report_type='" + report_type + '\'' +
				", dna_panel='" + dna_panel + '\'' +
				", rna_panel='" + rna_panel + '\'' +
				", run_id='" + run_id + '\'' +
				", I5='" + I5 + '\'' +
				", I7='" + I7 + '\'' +
				", test_product='" + test_product + '\'' +
				", ward='" + ward + '\'' +
				", consultation='" + consultation + '\'' +
				", DNANucleic='" + DNANucleic + '\'' +
				", RNANucleic='" + RNANucleic + '\'' +
				", DNALibrary='" + DNALibrary + '\'' +
				", RNALibrary='" + RNALibrary + '\'' +
				", DNAPlaneData='" + DNAPlaneData + '\'' +
				", meanSequencingDepth='" + meanSequencingDepth + '\'' +
				", targetAreaCoverage='" + targetAreaCoverage + '\'' +
				", RNAPlaneData='" + RNAPlaneData + '\'' +
				", ReadsNumber='" + ReadsNumber + '\'' +
				", review_doctor='" + review_doctor + '\'' +
				", test_number='" + test_number + '\'' +
				", carcinoma='" + carcinoma + '\'' +
				", clinicaldiagnosis='" + clinicaldiagnosis + '\'' +
				", sampleremark='" + sampleremark + '\'' +
				", firsttreatment='" + firsttreatment + '\'' +
				", secondtreatment='" + secondtreatment + '\'' +
				", thirdtreatment='" + thirdtreatment + '\'' +
				", specimenno='" + specimenno + '\'' +
				", serial_number='" + serial_number + '\'' +
				", registration_number='" + registration_number + '\'' +
				", recordercode='" + recordercode + '\'' +
				", mailingaddress='" + mailingaddress + '\'' +
				", sample_barcode='" + sample_barcode + '\'' +
				", tnm_periodization='" + tnm_periodization + '\'' +
				", inspection_number='" + mailingaddress + '\'' +
				", receiv_ertele_phone='" + receiv_ertele_phone + '\'' +
				", customertype='" + customertype + '\'' +
				'}';
	}
}