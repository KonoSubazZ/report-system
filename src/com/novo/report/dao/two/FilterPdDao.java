package com.novo.report.dao.two;

import com.novo.report.beans.FilterPd;
import org.apache.ibatis.annotations.Param;

public interface FilterPdDao {

    FilterPd getPDInfo(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    void updatePd(FilterPd filterPd);
}
