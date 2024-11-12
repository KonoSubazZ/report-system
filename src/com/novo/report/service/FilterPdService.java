package com.novo.report.service;

import com.novo.report.beans.FilterPd;

public interface FilterPdService {

    FilterPd getPDInfo(String subbarcode, String analysis_date, String product_name);

    void updatePd(FilterPd filterPd);
}
