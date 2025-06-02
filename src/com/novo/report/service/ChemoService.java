package com.novo.report.service;


import java.util.List;
import java.util.Map;

public interface ChemoService {

    List<Map<String, String>> getChemoData(List<Map<String, String>> chemoVariantList, String chemoCancer);

}
