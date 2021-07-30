package com.novo.report.beans;

public class FilterIlluminaSnpIndel {
	private Integer file_id;
	private String chr;
	private String start;
	private String end;
	private String ref;
	private String alt;
	private String hom_het;
	private String mutDepth;
	private String totalDepth;
	private String mutFreq;
	private String Func_knownGene;
	private String Gene_knownGene;
	private String ExonicFunc_knownGene;
	private String AAChange_knownGene;
	private String esp6500si_all;
	private String e1000g2012apr_all;  //与数据库中字段不一致
	private String dbSNP_rs;
	private String cosmic65;
	private String variant;
	private String ori_variant;
	private String report;
	private String filtered_rationale;
	private String loaded_date;
	private Integer record_id;
	private Integer mapped_variant_id;
	private String mapped_variant;
	
	public String getOri_variant() {
		return ori_variant;
	}
	public void setOri_variant(String ori_variant) {
		this.ori_variant = ori_variant;
	}
	public Integer getFile_id() {
		return file_id;
	}
	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}
	public String getChr() {
		return chr;
	}
	public void setChr(String chr) {
		this.chr = chr;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getHom_het() {
		return hom_het;
	}
	public void setHom_het(String hom_het) {
		this.hom_het = hom_het;
	}
	public String getMutDepth() {
		return mutDepth;
	}
	public void setMutDepth(String mutDepth) {
		this.mutDepth = mutDepth;
	}
	public String getTotalDepth() {
		return totalDepth;
	}
	public void setTotalDepth(String totalDepth) {
		this.totalDepth = totalDepth;
	}
	public String getMutFreq() {
		return mutFreq;
	}
	public void setMutFreq(String mutFreq) {
		this.mutFreq = mutFreq;
	}
	public String getFunc_knownGene() {
		return Func_knownGene;
	}
	public void setFunc_knownGene(String func_knownGene) {
		Func_knownGene = func_knownGene;
	}
	public String getGene_knownGene() {
		return Gene_knownGene;
	}
	public void setGene_knownGene(String gene_knownGene) {
		Gene_knownGene = gene_knownGene;
	}
	public String getExonicFunc_knownGene() {
		return ExonicFunc_knownGene;
	}
	public void setExonicFunc_knownGene(String exonicFunc_knownGene) {
		ExonicFunc_knownGene = exonicFunc_knownGene;
	}
	public String getAAChange_knownGene() {
		return AAChange_knownGene;
	}
	public void setAAChange_knownGene(String aAChange_knownGene) {
		AAChange_knownGene = aAChange_knownGene;
	}
	public String getEsp6500si_all() {
		return esp6500si_all;
	}
	public void setEsp6500si_all(String esp6500si_all) {
		this.esp6500si_all = esp6500si_all;
	}
	public String getE1000g2012apr_all() {
		return e1000g2012apr_all;
	}
	public void setE1000g2012apr_all(String e1000g2012apr_all) {
		this.e1000g2012apr_all = e1000g2012apr_all;
	}
	public String getDbSNP_rs() {
		return dbSNP_rs;
	}
	public void setDbSNP_rs(String dbSNP_rs) {
		this.dbSNP_rs = dbSNP_rs;
	}
	public String getCosmic65() {
		return cosmic65;
	}
	public void setCosmic65(String cosmic65) {
		this.cosmic65 = cosmic65;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getFiltered_rationale() {
		return filtered_rationale;
	}
	public void setFiltered_rationale(String filtered_rationale) {
		this.filtered_rationale = filtered_rationale;
	}
	public String getLoaded_date() {
		return loaded_date;
	}
	public void setLoaded_date(String loaded_date) {
		this.loaded_date = loaded_date;
	}
	public Integer getRecord_id() {
		return record_id;
	}
	public void setRecord_id(Integer record_id) {
		this.record_id = record_id;
	}
	public Integer getMapped_variant_id() {
		return mapped_variant_id;
	}
	public void setMapped_variant_id(Integer mapped_variant_id) {
		this.mapped_variant_id = mapped_variant_id;
	}
	public String getMapped_variant() {
		return mapped_variant;
	}
	public void setMapped_variant(String mapped_variant) {
		this.mapped_variant = mapped_variant;
	}
	
	
}
