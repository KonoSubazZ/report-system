package com.novo.report.utils;

import java.util.*;

public class ImmuneAllUtil {

    /**
     * 针对于免疫 正/负/超进展 模块 检出的逻辑
     *
     * @param allMutation     位点 cr + snv/indel/cnv/fusion
     * @param medicalEvidence
     * @return
     */
    public static List<Map> immuneAll(List<Map> allMutation, List<Map> medicalEvidence) {
        // 设置初始数据框（都是未检出状态），后续根据检出替换数据框内容
        String[][] raw_lst = {{"1", "DDR基因突变", "ATM", "ATM", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "ATR", "ATR", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "BAP1", "BAP1", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "BLM", "BLM", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "BRCA1", "BRCA1", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "BRCA2", "BRCA2", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "BRIP1", "BRIP1", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "CHEK1", "CHEK1", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "CHEK2", "CHEK2", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "ERCC2", "ERCC2", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "ERCC3", "ERCC3", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "ERCC4", "ERCC4", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "ERCC5", " ERCC5", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "FANCA", "FANCA", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "FANCC", "FANCC", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "MRE11", "MRE11", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "NBN", "NBN", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "RAD50", "RAD50", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "RAD51", "RAD51", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "RAD51B", "RAD51B", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "RAD51D", "RAD51D", "未检测到相关基因突变", "/", "/", "/"}, {"1", "DDR基因突变", "RAD54L", "RAD54L", "未检测到相关基因突变", "/", "/", "/"}, {"1", "CD274(PDL1)扩增", "CD274(PDL1)", "CD274,PDL1", "未检测到相关基因扩增", "/", "/", "/"}, {"1", "KRAS突变", "KRAS", "KRAS", "未检测到相关基因突变", "/", "/", "/"}, {"1", "PBRM1突变", "PBRM1", "PBRM1", "未检测到相关基因突变", "/", "/", "/"}, {"1", "PDCD1LG2(PDL2)扩增", "PDCD1LG2(PDL2)", "PDCD1LG2,PDL2", "未检测到相关基因扩增", "/", "/", "/"}, {"1", "POLD1突变", "POLD1", "POLD1", "未检测到相关基因突变", "/", "/", "/"}, {"1", "POLE突变", "POLE", "POLE", "未检测到相关基因突变", "/", "/", "/"}, {"1", "TP53突变", "TP53", "TP53", "未检测到相关基因突变", "/", "/", "/"}, {"2", "ALK融合", "ALK", "ALK", "未检测到相关基因融合", "/", "/", "/"}, {"2", "B2M突变", "B2M", "B2M", "未检测到相关基因突变", "/", "/", "/"}, {"2", "CTNNB1突变", "CTNNB1", "CTNNB1", "未检测到相关基因突变", "/", "/", "/"}, {"2", "EGFR突变(EX19del/L858R)", "EGFR", "EGFR", "未检测到相关基因突变", "/", "/", "/"}, {"2", "JAK1突变", "JAK1", "JAK1", "未检测到相关基因突变", "/", "/", "/"}, {"2", "JAK2突变", "JAK2", "JAK2", "未检测到相关基因突变", "/", "/", "/"}, {"2", "KEAP1突变", "KEAP1", "KEAP1", "未检测到相关基因突变", "/", "/", "/"}, {"2", "PTEN突变", "PTEN", "PTEN", "未检测到相关基因突变", "/", "/", "/"}, {"2", "STK11突变", "STK11", "STK11", "未检测到相关基因突变", "/", "/", "/"}, {"3", "CCND1扩增", "CCND1", "CCND1", "未检测到相关基因扩增", "/", "/", "/"}, {"3", "FGF3扩增", "FGF3", "FGF3", "未检测到相关基因扩增", "/", "/", "/"}, {"3", "FGF4扩增", "FGF4", "FGF4", "未检测到相关基因扩增", "/", "/", "/"}, {"3", "FGF19扩增", "FGF19", "FGF19", "未检测到相关基因扩增", "/", "/", "/"}, {"3", "DNMT3A突变", "DNMT3A", "DNMT3A", "未检测到相关基因突变", "/", "/", "/"}, {"3", "EGFR扩增", "EGFR", "EGFR", "未检测到相关基因扩增", "/", "/", "/"}, {"3", "MDM2扩增", "MDM2", "MDM2", "未检测到相关基因扩增", "/", "/", "/"}, {"3", "MDM4扩增", "MDM4", "MDM4", "未检测到相关基因扩增", "/", "/", "/"}};
        List<Map> new_raw_lst = new ArrayList<>();
        for (String[] strings : raw_lst) {
            Map raw_map = new HashMap();
            raw_map.put("flag", strings[0]);
            raw_map.put("gene_type", strings[1]);
            raw_map.put("gene", strings[2]);
            raw_map.put("genelist", strings[3]);
            raw_map.put("variant", strings[4]);
            raw_map.put("mutFreq", strings[5]);
            raw_map.put("ExonicFunc", strings[6]);
            raw_map.put("varDesc", strings[7]);
            new_raw_lst.add(raw_map);
        }
        /**        处理成这种格式 raw_lst ==> new_raw_lst
         *         List<Map> new_raw_lst = {"1","DDR基因突变","ATM","ATM","未检测到相关基因突变","/","/","/"},
         *          {"1","DDR基因突变","ATR","ATR","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","BAP1","BAP1","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","BLM","BLM","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","BRCA1","BRCA1","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","BRCA2","BRCA2","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","BRIP1","BRIP1","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","CHEK1","CHEK1","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","CHEK2","CHEK2","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","ERCC2","ERCC2","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","ERCC3","ERCC3","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","ERCC4","ERCC4","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","ERCC5"," ERCC5","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","FANCA","FANCA","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","FANCC","FANCC","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","MRE11","MRE11","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","NBN","NBN","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","RAD50","RAD50","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","RAD51","RAD51","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","RAD51B","RAD51B","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","RAD51D","RAD51D","未检测到相关基因突变","/","/","/"},
         *        {"1","DDR基因突变","RAD54L","RAD54L","未检测到相关基因突变","/","/","/"},
         *        {"1","CD274(PDL1)扩增","CD274(PDL1)","CD274,PDL1","未检测到相关基因扩增","/","/","/"},
         *        {"1","KRAS突变","KRAS","KRAS","未检测到相关基因突变","/","/","/"},
         *        {"1","PBRM1突变","PBRM1","PBRM1","未检测到相关基因突变","/","/","/"},
         *        {"1","PDCD1LG2(PDL2)扩增","PDCD1LG2(PDL2)","PDCD1LG2,PDL2","未检测到相关基因扩增","/","/","/"},
         *        {"1","POLD1突变","POLD1","POLD1","未检测到相关基因突变","/","/","/"},
         *        {"1","POLE突变","POLE","POLE","未检测到相关基因突变","/","/","/"},
         *        {"1","TP53突变","TP53","TP53","未检测到相关基因突变","/","/","/"},
         *        {"2","ALK融合","ALK","ALK","未检测到相关基因融合","/","/","/"},
         *        {"2","B2M突变","B2M","B2M","未检测到相关基因突变","/","/","/"},
         *        {"2","CTNNB1突变","CTNNB1","CTNNB1","未检测到相关基因突变","/","/","/"},
         *        {"2","EGFR突变(EX19del/L858R)","EGFR","EGFR","未检测到相关基因突变","/","/","/"},
         *        {"2","JAK1突变","JAK1","JAK1","未检测到相关基因突变","/","/","/"},
         *        {"2","JAK2突变","JAK2","JAK2","未检测到相关基因突变","/","/","/"},
         *        {"2","KEAP1突变","KEAP1","KEAP1","未检测到相关基因突变","/","/","/"},
         *        {"2","PTEN突变","PTEN","PTEN","未检测到相关基因突变","/","/","/"},
         *        {"2","STK11突变","STK11","STK11","未检测到相关基因突变","/","/","/"},
         *        {"3","CCND1扩增","CCND1","CCND1","未检测到相关基因扩增","/","/","/"},
         *        {"3","FGF3扩增","FGF3","FGF3","未检测到相关基因扩增","/","/","/"},
         *        {"3","FGF4扩增","FGF4","FGF4","未检测到相关基因扩增","/","/","/"},
         *        {"3","FGF19扩增","FGF19","FGF19","未检测到相关基因扩增","/","/","/"},
         *        {"3","DNMT3A突变","DNMT3A","DNMT3A","未检测到相关基因突变","/","/","/"},
         *        {"3","EGFR扩增","EGFR","EGFR","未检测到相关基因扩增","/","/","/"},
         *        {"3","MDM2扩增","MDM2","MDM2","未检测到相关基因扩增","/","/","/"},
         *        {"3","MDM4扩增","MDM4","MDM4","未检测到相关基因扩增","/","/","/"};
         */
        //# 调整snv/indel/cnv/fusion/cr结果格式
        Map mut_gene = new HashMap();
        for (Map map : allMutation) {
            String gene = map.get("gene").toString();
            String mutFreq = map.get("mutFreq").toString();
            String ori_variant = map.get("ori_variant").toString().replaceFirst(" \\.$", "");
            // 调整突变频率 mutFreq 展示形式，扩增不做处理，点突变做百分比处理，融合mut Freq<100 加 %
            if (ori_variant.indexOf("Amplification") < 0 && ori_variant.indexOf("Loss") < 0 && !".".equals(mutFreq) && mutFreq.indexOf("合") < 0 && mutFreq.indexOf("-") < 0 && !"/".equals(mutFreq)) {
                if (ori_variant.indexOf("Fusion") != -1) {
                    if (mutFreq.indexOf(".") != -1 && Double.valueOf(mutFreq) < 100) {
                        mutFreq += "%";
                    }
                } else {
                    mutFreq += "%";
                }
            }
            String type = map.get("type").toString();
            // 暂时不检BRCA大片段缺失
            String ExonicFunc = (String) map.getOrDefault("ExonicFunc", "");
            if ("DEL".equals(ExonicFunc) || "DUP".equals(ExonicFunc)) {
                break;
            }

            /**把同基因的变异结果组合到一起，格式如下
             * Map<String,Set<String[]>> mut_gene = new HashMap();
             * mut_gene = {
             *     "ATM": [
             *         ["p.L858R", "45.6%", "突变"],
             *         ["exon19 del", "38.5%", "突变"]
             *     ],
             *     "EGFR": [
             *         ["Amplification", "150%", "扩增"]
             *     ]
             * }
             */
            if (ori_variant.equals("Amplification")) {
                String[] strings = {ori_variant, mutFreq, "扩增"};
                sameKeyCombination(mut_gene, gene, strings);
            } else if (ori_variant.equals("Loss")) {
                // TODO 免疫待增加 Loss
                String[] strings = {ori_variant, mutFreq, "缺失"};
                sameKeyCombination(mut_gene, gene, strings);
            } else if (ori_variant.indexOf("Fusion") != -1) {
                String[] strings = {ori_variant, mutFreq, "融合"};
                sameKeyCombination(mut_gene, gene, strings);
            } else if ("体系".equals(type)) {
                String[] strings = {ori_variant, mutFreq, "突变"};
                sameKeyCombination(mut_gene, gene, strings);
            } else if ("胚系".equals(type)) {
                String[] strings = {ori_variant, mutFreq, "突变"};
                sameKeyCombination(mut_gene, gene, strings);
            }
        }

        // 输出在免疫gene列表的结果
        List<Map> outfile = new ArrayList<>();
        // 根据有无检出决定是否替换 new_raw_lst 原有数据，同时处理是否添加新数据
        for (Map map : new_raw_lst) {
            String flag = map.get("flag").toString();
            String gene_type = map.get("gene_type").toString();
            String gene = map.get("gene").toString();
            // 是否添加新数据
            boolean b = true;
            List<String> genelist = new ArrayList<>(Arrays.asList(map.get("genelist").toString().split(",")));
            // mut_gene 位点
            Set<String> set = mut_gene.keySet();
            for (String s : set) {
                // genelist可能会有两个 {"1","PDCD1LG2(PDL2)扩增","PDCD1LG2(PDL2)","PDCD1LG2,PDL2","未检测到相关基因扩增","/","/","/"},
                if (genelist.contains(s)) {
                    Set<String[]> set1 = (Set<String[]>) mut_gene.get(s);
                    if (set1.isEmpty()) {
                        break;
                    }
                    if (gene_type.contains("突变")) {
                        for (String[] strings : set1) {
                            if (strings[2].contains("突变")) {
                                Map raw_map = new HashMap();
                                raw_map.put("flag", flag);
                                raw_map.put("gene", gene);
                                raw_map.put("variant", strings[0]);
                                raw_map.put("mutFreq", strings[1]);
                                raw_map.put("ExonicFunc", strings[2]);
                                for (Map map1 : medicalEvidence) {
                                    String gene_type1 = map1.get("gene_type").toString();
                                    String medical_evidence = map1.get("medical_evidence").toString();
                                    if (gene_type.equals(gene_type1)) {
                                        raw_map.put("varDesc", medical_evidence);
                                    }
                                }
                                if ("EGFR突变(EX19del/L858R)".equals(gene_type)) {
                                    String substring = "";
                                    if (strings[0].contains("p.")) {
                                        substring = strings[0].substring(strings[0].indexOf("p."));
                                    }
                                    if (strings[0].contains("p.L858R")) {
                                        outfile.add(raw_map);
                                        b = false;
                                    }
                                    // 增加19delins的判断
                                    if (strings[0].contains("exon19") && strings[0].contains("delins")) {
                                        String[] geneSplit = strings[0].split(" ");
                                        if (geneSplit.length >= 3) {
                                            String cHGVS = geneSplit[2];
                                            String[] delinsParts = cHGVS.split("delins");
                                            String delPart = delinsParts[0];
                                            String insSeq = delinsParts[1];

                                            // 2. 提取缺失位置（按 "." 分割，取最后一段）
                                            String[] dotParts = delPart.split("\\.");
                                            String delPos = dotParts[dotParts.length - 1];

                                            // 3. 按 "_" 分割起始和结束位置
                                            String[] posParts = delPos.split("_");

                                            // 4. 解析起始、结束位置，计算缺失长度
                                            int start = Integer.parseInt(posParts[0]);
                                            int end = Integer.parseInt(posParts[1]);
                                            int delCount = end - start + 1;

                                            // 5. 计算插入序列长度
                                            int insCount = insSeq.length();

                                            if (delCount > insCount) {
                                                outfile.add(raw_map);
                                                b = false;
                                            }
                                        }
                                    } else if (strings[0].contains("exon19") && strings[0].contains("del") && !strings[0].contains("delins")) {
                                        outfile.add(raw_map);
                                        b = false;
                                    }
                                } else {
                                    outfile.add(raw_map);
                                    b = false;
                                }
                            }
                        }
                    } else if (gene_type.contains("扩增")) {
                        for (String[] strings : set1) {
                            if (strings[2].contains("扩增")) {
                                Map raw_map = new HashMap();
                                raw_map.put("flag", flag);
                                raw_map.put("gene", gene);
                                raw_map.put("variant", strings[0]);
                                raw_map.put("mutFreq", strings[1]);
                                raw_map.put("ExonicFunc", strings[2]);
                                for (Map map1 : medicalEvidence) {
                                    String gene_type1 = map1.get("gene_type").toString();
                                    String medical_evidence = map1.get("medical_evidence").toString();
                                    if (gene_type.equals(gene_type1)) {
                                        raw_map.put("varDesc", medical_evidence);
                                    }
                                }
                                outfile.add(raw_map);
                                b = false;
                            }
                        }
                    } else if (gene_type.contains("融合")) {
                        for (String[] strings : set1) {
                            if (strings[2].contains("融合")) {
                                Map raw_map = new HashMap();
                                raw_map.put("flag", flag);
                                raw_map.put("gene", gene);
                                raw_map.put("variant", strings[0]);
                                raw_map.put("mutFreq", strings[1]);
                                raw_map.put("ExonicFunc", strings[2]);
                                for (Map map1 : medicalEvidence) {
                                    String gene_type1 = map1.get("gene_type").toString();
                                    String medical_evidence = map1.get("medical_evidence").toString();
                                    if (gene_type.equals(gene_type1)) {
                                        raw_map.put("varDesc", medical_evidence);
                                    }
                                }
                                outfile.add(raw_map);
                                b = false;
                            }
                        }
                    }
                }
            }
            if (b) {
                outfile.add(map);
            }
        }
        return outfile;
    }

    // 相同key元素组合
    private static void sameKeyCombination(Map<String, Object> map, String key, String[] value) {
        if (map.containsKey(key)) {
            Set<String[]> set = (Set<String[]>) map.get(key);
            set.add(value);
            map.put(key, set);
        } else {
            Set<String[]> set = new HashSet<>();
            set.add(value);
            map.put(key, set);
        }
    }
}
