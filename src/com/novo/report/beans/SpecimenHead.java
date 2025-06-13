package com.novo.report.beans;

public class SpecimenHead {
	
	private String shsampletype;
	private String srsampletype;
	private String samplenum;
	private String sampleunit;//样品种类
	private String operatedate;  //报告上传时间
	private String familyhistory;  //家族史
	private String idnum;  //住院号
	private String cancertype;//疾病种类
	private String pathologicaltype;//病理分型
	private String barcode;//样本编号  
	private String subBarcode;//样本编号  | 子条码
	private String patientname;//姓名
	private String erpsalername;//联系人/销售员姓名
	private String sex;//性别 
	private String age;//年龄
	private String enterdate;//订单创建时间
	private String getspecdate;//收样日期
	private String reportreceiver;//报告接收人
	private String birthday;//出生日期
	private String clinicalremark;//临床备注  临床诊断
	private String room;//房号
	private String bed;//床号
	private String customername;//送检单位  | 送检医院
	private String emailaddress;//客户邮箱
	private String collectdate;//送检日期 ？ 样本采集日期
	private String specimennum;//样本数量
	private String sampletime;//寄养时间
	private String unit;//样本单位
	private String sampletype;//样本种类
	private String clinicalstages; //临床分期
	private String doctorname;  //送检医生
	private String locationname; //送检科室
	private String erptestname;//病理分型 产品名称
	private String samplesource; //标本来源
	private String fromorgan;  //检测部位 来源器官
	private String genetype;  //检测基因
	private String generesult;  //检测结果
	private String birthplace;  //籍贯 出生地

	public String getFAMILYFIRST() {
		return FAMILYFIRST;
	}

	public void setFAMILYFIRST(String FAMILYFIRST) {
		this.FAMILYFIRST = FAMILYFIRST;
	}

	private String FAMILYFIRST;  //家族史1
	private String familyfirst_cancertype;  //家族史1 肿瘤类型
	private String familyfirst_confirmtime;  //家族史1 确认时间
	private String familyfirst_age;  //家族史1 年龄
	private String familysecond;  //家族史2
	private String familysecond_cancertype;  //家族史2 肿瘤类型
	private String familysecond_confirmtime;  //家族史2 确认时间
	private String familysecond_age;  //家族史2 年龄
	private String senddate; //送检日期  ？寄样日期
	private String libraryname; //销售渠道
	private String fastcode;   //销售区域
	private String saleremail;   //销售员邮箱
	private String supportemail;   //技术支持邮箱
	private String manageremail;   //大区经理邮箱
	private String pmemail;   //运营群邮箱
	private String pathologynum;   //病理编号
	private String customedesc;	//送检单位
	private String sampleremark;
	private String firsttreatment;	//治疗史1
	private String secondtreatment;	//治疗史2
	private String thirdtreatment;	//治疗史3
	private String outpatient;	//门诊/住院号
	private String specimenno;	//病理编号 或者 银丰样本编号（银丰基因科技有限公司）
	private String recordercode;	//创建人编码
	private String mailingaddress;	//报告邮寄地址 / 病理诊断
	private String patientphone;	//患者电话
	private String receivertelephone;	//接收电话
	private String customertype;	//客户类别

	/**
	 * 是否为健康人群，否为健康人群
	 */
	private String patientinfoisacancer;

	public String getLaboratoryname() {
		return laboratoryname;
	}

	public void setLaboratoryname(String laboratoryname) {
		this.laboratoryname = laboratoryname;
	}

	/**
	 * 实验室信息
	 */
	private String laboratoryname;

	public String getPatientinfoisacancer() {
		return patientinfoisacancer;
	}

	public void setPatientinfoisacancer(String patientinfoisacancer) {
		this.patientinfoisacancer = patientinfoisacancer;
	}

	public String getCustomedesc() {
		return customedesc;
	}
	public void setCustomedesc(String customedesc) {
		this.customedesc = customedesc;
	}	
	public String getPathologynum() {
		return pathologynum;
	}
	public void setPathologynum(String pathologynum) {
		this.pathologynum = pathologynum;
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
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getShsampletype() {
		return shsampletype;
	}
	public void setShsampletype(String shsampletype) {
		this.shsampletype = shsampletype;
	}
	public String getSrsampletype() {
		return srsampletype;
	}
	public void setSrsampletype(String srsampletype) {
		this.srsampletype = srsampletype;
	}
	public String getSampleunit() {
		return sampleunit;
	}
	public void setSampleunit(String sampleunit) {
		this.sampleunit = sampleunit;
	}
	public String getOperatedate() {
		return operatedate;
	}
	public void setOperatedate(String operatedate) {
		this.operatedate = operatedate;
	}
	public String getFamilyhistory() {
		return familyhistory;
	}
	public void setFamilyhistory(String familyhistory) {
		this.familyhistory = familyhistory;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSubBarcode() {
		return subBarcode;
	}
	public void setSubBarcode(String subBarcode) {
		this.subBarcode = subBarcode;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public String getErpsalername() {
		return erpsalername;
	}
	public void setErpsalername(String erpsalername) {
		this.erpsalername = erpsalername;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getEnterdate() {
		return enterdate;
	}
	public void setEnterdate(String enterdate) {
		this.enterdate = enterdate;
	}
	public String getGetspecdate() {
		return getspecdate;
	}
	public void setGetspecdate(String getspecdate) {
		this.getspecdate = getspecdate;
	}
	public String getReportreceiver() {
		return reportreceiver;
	}
	public void setReportreceiver(String reportreceiver) {
		this.reportreceiver = reportreceiver;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getClinicalremark() {
		return clinicalremark;
	}
	public void setClinicalremark(String clinicalremark) {
		this.clinicalremark = clinicalremark;
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
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	public String getCollectdate() {
		return collectdate;
	}
	public void setCollectdate(String collectdate) {
		this.collectdate = collectdate;
	}
	public String getSamplenum() {
		return samplenum;
	}
	public void setSamplenum(String samplenum) {
		this.samplenum = samplenum;
	}
	public String getSpecimennum() {
		return specimennum;
	}
	public void setSpecimennum(String specimennum) {
		this.specimennum = specimennum;
	}

	public String getSampletime() {
		return sampletime;
	}

	public void setSampletime(String sampletime) {
		this.sampletime = sampletime;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSampletype() {
		return sampletype;
	}
	public void setSampletype(String sampletype) {
		this.sampletype = sampletype;
	}
	public String getClinicalstages() {
		return clinicalstages;
	}
	public void setClinicalstages(String clinicalstages) {
		this.clinicalstages = clinicalstages;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getLocationname() {
		return locationname;
	}
	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}
	public String getErptestname() {
		return erptestname;
	}
	public void setErptestname(String erptestname) {
		this.erptestname = erptestname;
	}
	public String getSamplesource() {
		return samplesource;
	}
	public void setSamplesource(String samplesource) {
		this.samplesource = samplesource;
	}
	public String getFromorgan() {
		return fromorgan;
	}
	public void setFromorgan(String fromorgan) {
		this.fromorgan = fromorgan;
	}
	public String getGenetype() {
		return genetype;
	}
	public void setGenetype(String genetype) {
		this.genetype = genetype;
	}
	public String getGeneresult() {
		return generesult;
	}
	public void setGeneresult(String generesult) {
		this.generesult = generesult;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getFamilyfirst_cancertype() {
		return familyfirst_cancertype;
	}
	public void setFamilyfirst_cancertype(String familyfirst_cancertype) {
		this.familyfirst_cancertype = familyfirst_cancertype;
	}
	public String getFamilyfirst_confirmtime() {
		return familyfirst_confirmtime;
	}
	public void setFamilyfirst_confirmtime(String familyfirst_confirmtime) {
		this.familyfirst_confirmtime = familyfirst_confirmtime;
	}
	public String getFamilyfirst_age() {
		return familyfirst_age;
	}
	public void setFamilyfirst_age(String familyfirst_age) {
		this.familyfirst_age = familyfirst_age;
	}
	public String getFamilysecond() {
		return familysecond;
	}
	public void setFamilysecond(String familysecond) {
		this.familysecond = familysecond;
	}
	public String getFamilysecond_cancertype() {
		return familysecond_cancertype;
	}
	public void setFamilysecond_cancertype(String familysecond_cancertype) {
		this.familysecond_cancertype = familysecond_cancertype;
	}
	public String getFamilysecond_confirmtime() {
		return familysecond_confirmtime;
	}
	public void setFamilysecond_confirmtime(String familysecond_confirmtime) {
		this.familysecond_confirmtime = familysecond_confirmtime;
	}
	public String getFamilysecond_age() {
		return familysecond_age;
	}
	public void setFamilysecond_age(String familysecond_age) {
		this.familysecond_age = familysecond_age;
	}
	public String getSenddate() {
		return senddate;
	}
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	public String getLibraryname() {
		return libraryname;
	}
	public void setLibraryname(String libraryname) {
		this.libraryname = libraryname;
	}
	public String getFastcode() {
		return fastcode;
	}
	public void setFastcode(String fastcode) {
		this.fastcode = fastcode;
	}

	public String getSampleremark() {
		return sampleremark;
	}

	public void setSampleremark(String sampleremark) {
		this.sampleremark = sampleremark;
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

	public String getOutpatient() {
		return outpatient;
	}

	public void setOutpatient(String outpatient) {
		this.outpatient = outpatient;
	}

	public String getSpecimenno() {
		return specimenno;
	}

	public void setSpecimenno(String specimenno) {
		this.specimenno = specimenno;
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

	public String getPatientphone() {
		return patientphone;
	}

	public void setPatientphone(String patientphone) {
		this.patientphone = patientphone;
	}

	public String getReceivertelephone() {
		return receivertelephone;
	}

	public void setReceivertelephone(String receivertelephone) {
		this.receivertelephone = receivertelephone;
	}

	public String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	@Override
	public String toString() {
		return "SpecimenHead{" +
				"shsampletype='" + shsampletype + '\'' +
				", srsampletype='" + srsampletype + '\'' +
				", samplenum='" + samplenum + '\'' +
				", sampleunit='" + sampleunit + '\'' +
				", operatedate='" + operatedate + '\'' +
				", familyhistory='" + familyhistory + '\'' +
				", idnum='" + idnum + '\'' +
				", cancertype='" + cancertype + '\'' +
				", pathologicaltype='" + pathologicaltype + '\'' +
				", barcode='" + barcode + '\'' +
				", subBarcode='" + subBarcode + '\'' +
				", patientname='" + patientname + '\'' +
				", erpsalername='" + erpsalername + '\'' +
				", sex='" + sex + '\'' +
				", age='" + age + '\'' +
				", enterdate='" + enterdate + '\'' +
				", getspecdate='" + getspecdate + '\'' +
				", reportreceiver='" + reportreceiver + '\'' +
				", birthday='" + birthday + '\'' +
				", clinicalremark='" + clinicalremark + '\'' +
				", room='" + room + '\'' +
				", bed='" + bed + '\'' +
				", customername='" + customername + '\'' +
				", emailaddress='" + emailaddress + '\'' +
				", collectdate='" + collectdate + '\'' +
				", specimennum='" + specimennum + '\'' +
				", unit='" + unit + '\'' +
				", sampletype='" + sampletype + '\'' +
				", clinicalstages='" + clinicalstages + '\'' +
				", doctorname='" + doctorname + '\'' +
				", locationname='" + locationname + '\'' +
				", erptestname='" + erptestname + '\'' +
				", samplesource='" + samplesource + '\'' +
				", fromorgan='" + fromorgan + '\'' +
				", genetype='" + genetype + '\'' +
				", generesult='" + generesult + '\'' +
				", birthplace='" + birthplace + '\'' +
				", familyfirst='" + familyfirst + '\'' +
				", familyfirst_cancertype='" + familyfirst_cancertype + '\'' +
				", familyfirst_confirmtime='" + familyfirst_confirmtime + '\'' +
				", familyfirst_age='" + familyfirst_age + '\'' +
				", familysecond='" + familysecond + '\'' +
				", familysecond_cancertype='" + familysecond_cancertype + '\'' +
				", familysecond_confirmtime='" + familysecond_confirmtime + '\'' +
				", familysecond_age='" + familysecond_age + '\'' +
				", senddate='" + senddate + '\'' +
				", libraryname='" + libraryname + '\'' +
				", fastcode='" + fastcode + '\'' +
				", saleremail='" + saleremail + '\'' +
				", supportemail='" + supportemail + '\'' +
				", manageremail='" + manageremail + '\'' +
				", pmemail='" + pmemail + '\'' +
				", pathologynum='" + pathologynum + '\'' +
				", customedesc='" + customedesc + '\'' +
				", sampleremark='" + sampleremark + '\'' +
				", firsttreatment='" + firsttreatment + '\'' +
				", secondtreatment='" + secondtreatment + '\'' +
				", thirdtreatment='" + thirdtreatment + '\'' +
				", outpatient='" + outpatient + '\'' +
				", specimenno='" + specimenno + '\'' +
				", recordercode='" + recordercode + '\'' +
				", mailingaddress='" + mailingaddress + '\'' +
				", patientphone='" + patientphone + '\'' +
				", receivertelephone='" + receivertelephone + '\'' +
				", customertype='" + customertype + '\'' +
				'}';
	}
}