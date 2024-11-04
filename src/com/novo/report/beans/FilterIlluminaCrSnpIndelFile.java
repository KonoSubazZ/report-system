package com.novo.report.beans;

public class FilterIlluminaCrSnpIndelFile {
	private Integer file_id;
	private String chr;
	private String pos;
	private String gene;
	private String Transcript;
	private String Exon;
	private String cHGVS;
	private String pHGVS;
	private String Zygosity;
	private String ExonicFunc;
	private String c1000g2015aug_all;
	private String ExAC_EAS;
	private String avsnp150;
	private String SIFT_pred;
	private String Polyphen2_HDIV_pred;
	private String MutationTaster_pred;
	private String revel;
	private String gnomAD_genome_ALL;
	private String Interpro_domain;
	private String CLNSIG;
	private String OMIM_Phenotypes;
	private String HGMD_tag;
	private String HGMD_disease;
	private String HGMD_pmid;
	private String loaded_date;
	private Integer record_id;
	private String report;
	private String filtered_rationale;
	private Integer mapped_variant_id;

	public Integer getMapped_variant_id() {
		return mapped_variant_id;
	}
	public void setMapped_variant_id(Integer mapped_variant_id) {
		this.mapped_variant_id = mapped_variant_id;
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
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getTranscript() {
		return Transcript;
	}
	public void setTranscript(String transcript) {
		Transcript = transcript;
	}
	public String getExon() {
		return Exon;
	}
	public void setExon(String exon) {
		Exon = exon;
	}
	public String getcHGVS() {
		return cHGVS;
	}
	public void setcHGVS(String cHGVS) {
		this.cHGVS = cHGVS;
	}
	public String getpHGVS() {
		return pHGVS;
	}
	public void setpHGVS(String pHGVS) {
		this.pHGVS = pHGVS;
	}
	public String getZygosity() {
		return Zygosity;
	}
	public void setZygosity(String zygosity) {
		Zygosity = zygosity;
	}
	public String getExonicFunc() {
		return ExonicFunc;
	}
	public void setExonicFunc(String exonicFunc) {
		ExonicFunc = exonicFunc;
	}
	public String getC1000g2015aug_all() {
		return c1000g2015aug_all;
	}
	public void setC1000g2015aug_all(String c1000g2015aug_all) {
		this.c1000g2015aug_all = c1000g2015aug_all;
	}
	public String getExAC_EAS() {
		return ExAC_EAS;
	}
	public void setExAC_EAS(String exAC_EAS) {
		ExAC_EAS = exAC_EAS;
	}
	public String getAvsnp150() {
		return avsnp150;
	}
	public void setAvsnp150(String avsnp150) {
		this.avsnp150 = avsnp150;
	}
	public String getSIFT_pred() {
		return SIFT_pred;
	}
	public void setSIFT_pred(String sIFT_pred) {
		SIFT_pred = sIFT_pred;
	}
	public String getPolyphen2_HDIV_pred() {
		return Polyphen2_HDIV_pred;
	}
	public void setPolyphen2_HDIV_pred(String polyphen2_HDIV_pred) {
		Polyphen2_HDIV_pred = polyphen2_HDIV_pred;
	}
	public String getMutationTaster_pred() {
		return MutationTaster_pred;
	}
	public void setMutationTaster_pred(String mutationTaster_pred) {
		MutationTaster_pred = mutationTaster_pred;
	}

	public String getRevel() {
		return revel;
	}

	public void setRevel(String revel) {
		this.revel = revel;
	}

	public String getGnomAD_genome_ALL() {
		return gnomAD_genome_ALL;
	}

	public void setGnomAD_genome_ALL(String gnomAD_genome_ALL) {
		this.gnomAD_genome_ALL = gnomAD_genome_ALL;
	}

	public String getInterpro_domain() {
		return Interpro_domain;
	}

	public void setInterpro_domain(String interpro_domain) {
		Interpro_domain = interpro_domain;
	}

	public String getCLNSIG() {
		return CLNSIG;
	}

	public void setCLNSIG(String CLNSIG) {
		this.CLNSIG = CLNSIG;
	}

	public String getOMIM_Phenotypes() {
		return OMIM_Phenotypes;
	}

	public void setOMIM_Phenotypes(String OMIM_Phenotypes) {
		this.OMIM_Phenotypes = OMIM_Phenotypes;
	}

	public String getHGMD_tag() {
		return HGMD_tag;
	}

	public void setHGMD_tag(String HGMD_tag) {
		this.HGMD_tag = HGMD_tag;
	}

	public String getHGMD_disease() {
		return HGMD_disease;
	}

	public void setHGMD_disease(String HGMD_disease) {
		this.HGMD_disease = HGMD_disease;
	}

	public String getHGMD_pmid() {
		return HGMD_pmid;
	}

	public void setHGMD_pmid(String HGMD_pmid) {
		this.HGMD_pmid = HGMD_pmid;
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
}
