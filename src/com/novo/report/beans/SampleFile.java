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
	@Override
	public String toString() {
		return "SampleFile [sample_id=" + sample_id + ", person_id=" + person_id + ", person_name=" + person_name
				+ ", gender=" + gender + ", birthday=" + birthday + ", patient_id=" + patient_id + ", subbarcode="
				+ subbarcode + ", barcode=" + barcode + ", hospital=" + hospital + ", received_date=" + received_date
				+ ", specimen_type=" + specimen_type + ", specimen_quantity=" + specimen_quantity + ", testing_program="
				+ testing_program + ", disease_type=" + disease_type + ", comparison=" + comparison + ", client="
				+ client + ", commission_date=" + commission_date + ", sales_contact=" + sales_contact
				+ ", report_receiver=" + report_receiver + ", collect_date=" + collect_date + ", data_path=" + data_path
				+ ", info_officer=" + info_officer + ", sample_nature=" + sample_nature + ", selected_genes="
				+ selected_genes + ", testing_before_medicine=" + testing_before_medicine + ", other_description="
				+ other_description + ", loaded_date=" + loaded_date + ", count=" + count + ", age=" + age
				+ ", clinicalremark=" + clinicalremark + ", cancertype=" + cancertype + ", pathologicaltype="
				+ pathologicaltype + ", product_name=" + product_name + ", remark=" + remark + ", clinicalstages="
				+ clinicalstages + ", libraryname=" + libraryname + ", doctorname=" + doctorname + ", fastcode="
				+ fastcode + ", locationname=" + locationname + ", report_upload_date=" + report_upload_date
				+ ", sample_source=" + sample_source + ", from_organ=" + from_organ + ", turmor_cell_ratio="
				+ turmor_cell_ratio + ", gene_type=" + gene_type + ", gene_result=" + gene_result
				+ ", medication_history=" + medication_history + ", birthplace=" + birthplace + ", family_history="
				+ family_history + ", sample_type=" + sample_type + ", room=" + room + ", bed=" + bed
				+ ", emailaddress=" + emailaddress + "]";
	}
		
}