package com.novo.report.service.impl;


import com.novo.report.dao.two.CustomDao;
import com.novo.report.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CustomServiceImpl implements CustomService {

    @Autowired
    private CustomDao customDao;

    @Override
    public Map<String, Object> getCstoneTipInfo(String disease) {

        List<Rule> rules = Arrays.asList(
                new Rule("小细胞肺", "小细胞肺癌"),
                new Rule("胃食管结合", "食管胃结合部癌"),
                new Rule("直肠", "结直肠癌"),
                new Rule("结肠", "结直肠癌"),
                new Rule("肺", "肺癌"),
                new Rule("胃肠道间质瘤", "胃肠道间质瘤"),
                new Rule("胃", "胃癌"),
                new Rule("食管", "食管癌"),
                new Rule("胃神经内分泌", "胃肠道神经内分泌肿瘤"),
                new Rule("肠神经内分泌", "胃肠道神经内分泌肿瘤"),
                new Rule("胃肠道神经内分泌", "胃肠道神经内分泌肿瘤"),
                new Rule("头颈", "头颈部癌"),
                new Rule("口", "头颈部癌"),
                new Rule("舌", "头颈部癌"),
                new Rule("腮", "头颈部癌"),
                new Rule("腮", "头颈部癌"),
                new Rule("喉", "头颈部癌")
        );

        // 遍历规则，匹配到第一个符合条件的就返回对应查询结果
        for (Rule rule : rules) {
            if (disease.contains(rule.keyword)) {
                return customDao.getCstoneTipInfoByDisease(rule.targetDisease);
            }
        }

        return customDao.getCstoneTipInfoByKeyword(disease);
    }

    private static class Rule {
        String keyword;      // 疾病名称中包含的关键词
        String targetDisease; // 匹配后要查询的目标疾病

        Rule(String keyword, String targetDisease) {
            this.keyword = keyword;
            this.targetDisease = targetDisease;
        }
    }
}
