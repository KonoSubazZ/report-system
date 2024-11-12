package com.novo.report.dao.two;

import com.novo.report.beans.FilterQc;
import org.apache.ibatis.annotations.Param;

public interface FilterQcDao {

    void updateSampleFile(@Param("tumorcellcontent") String tumorcellcontent, @Param("DNA_total") String DNA_total, @Param("DNA_degradation") String DNA_degradation, @Param("outbound_quantity") String outbound_quantity, @Param("subbarcode") String subbarcode);

    void updateQc(FilterQc filterQc);

    void updateQcRna(FilterQc filterQc);

    void updateQcHrd(FilterQc filterQc);
}
