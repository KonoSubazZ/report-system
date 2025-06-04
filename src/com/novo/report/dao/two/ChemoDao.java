package com.novo.report.dao.two;


import com.novo.report.beans.ChemoVariant;
import com.novo.report.common.CommonQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ChemoDao {

    List<Map<String, String>> getChemoDBData(@Param("chr") String chr, @Param("position") String position);

    List<Map<String, String>> batchGetChemoDBData(@Param("chemoVariantList") List<ChemoVariant> chemoVariantList);
    List<ChemoVariant> getChemoVariantFileData(CommonQueryVO query);

}
