package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.DetectionResult;

public interface PanelGeneDao {

	List<DetectionResult> getDetectionResultList(Integer product_id, Integer report_id);

	String getTagByGene(String gene);

}
