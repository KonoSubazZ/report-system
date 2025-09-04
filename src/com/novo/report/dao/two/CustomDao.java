package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

public interface CustomDao {
	Map<String, Object> getCstoneTipInfoByKeyword(String disease);
	Map<String, Object> getCstoneTipInfoByDisease(String disease);
}
