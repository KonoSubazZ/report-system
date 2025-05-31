package com.novo.report.dao.two;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ChemoDao {

    List<Map<String, String>> getChemoDBData(@Param("chr") String chr, @Param("position") String position);

}
