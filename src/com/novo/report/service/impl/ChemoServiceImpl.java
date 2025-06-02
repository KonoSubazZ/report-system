package com.novo.report.service.impl;


import com.novo.report.dao.two.ChemoDao;
import com.novo.report.dao.two.VariantDao;
import com.novo.report.service.ChemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChemoServiceImpl implements ChemoService {

    @Autowired
    private ChemoDao chemoDao;


    @Override
    public List<Map<String, String>> getChemoData(List<Map<String, String>> chemoVariantList) {
        // 对等位基因进行排序去重AG/GA属于重复
        List<Map<String, String>> uniqueChemoVariants = deduplicateVariants(chemoVariantList);
        // 获取药物信息
        return Collections.emptyList();
    }

    /**
     * 对包含变异信息的Map列表进行去重
     * 根据chr、position和排序后的allele进行去重
     *
     * @param variantList 包含变异信息的Map列表
     * @return 去重后的列表
     */
    private List<Map<String, String>> deduplicateVariants(List<Map<String, String>> variantList) {
        // 使用Set来跟踪唯一的变异
        Set<String> uniqueKeys = new HashSet<>();
        List<Map<String, String>> result = new ArrayList<>();

        for (Map<String, String> variant : variantList) {
            // 获取关键信息
            String chr = variant.get("chr");
            String position = variant.get("position");
            String allele = variant.get("allele");

            // 对等位基因进行排序，使"AG"和"GA"变成相同的表示
            String sortedAllele = sortString(allele);

            // 创建唯一键
            String key = chr + "_" + position + "_" + sortedAllele;

            // 如果键不存在，则添加到结果中
            if (uniqueKeys.add(key)) {
                result.add(variant);
            }
        }

        return result;
    }

    /**
     * 对字符串进行排序
     */
    private String sortString(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

}
