package com.novo.report.utils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 翻译突变说明.
 *
 * @author LinYuheng
 */
public class TranslateUtil {
    public String translate(String gene, String ori_variant, String mutFreq) {
        String str = ori_variant;
        String strNew = "";
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        try {
            if (str != null && str.contains("p.")) {
                if (str.contains("del") || str.contains("ins") || str.contains("delins")) {
                    String[] arr = str.split("\\s+");
                    String[] arrDelOrInsOrDelins = new String[2];
                    Matcher m = p.matcher(arr[0]);
                    String strNum1 = m.replaceAll("").trim();
                    //c.3739_3750delCAGCAGCAGCAA
                    if (str.contains("delins")) {
                        arrDelOrInsOrDelins[0] = arr[1];
                    } else if (str.contains("ins")) {
                        String[] delIns = arr[1].split("ins");
                        if (delIns.length > 1) {
                            arrDelOrInsOrDelins = delIns;
                        } else {
                            arrDelOrInsOrDelins[0] = delIns[0];
                            arrDelOrInsOrDelins[1] = "";
                        }
                    } else if (str.contains("del")) {
                        arrDelOrInsOrDelins = arr[1].split("del");
                    }
                    String[] arrDelsOne = arrDelOrInsOrDelins[0].split("_");
                    Matcher marrDelsOne = p.matcher(arrDelsOne[0]);
                    String strNum2 = marrDelsOne.replaceAll("").trim();
                    if (str.contains("delins")) {
                        if (arrDelsOne.length > 1) {
                            Matcher marrDelsThree = p.matcher(arrDelsOne[1]);
                            String strNum3 = marrDelsThree.replaceAll("").trim();
                            if (arrDelsOne[1].contains(">")) {
                                String[] varStr = arrDelsOne[1].split(">");
                                strNew = "位于" + strNum1 + "号外显子上的第" + strNum2 + "位到第" + strNum3 + "位核苷酸由" + varStr[0].replace(strNum3, "") + "突变为" + varStr[1] + "，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                            } else {
                                strNew = "位于" + strNum1 + "号外显子上的第" + strNum2 + "位到第" + strNum3 + "位核苷酸" + arrDelsOne[1].replace(strNum3, "") + "发生缺失，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                            }
                        } else {
                            String[] varStr = arrDelsOne[0].split(">");
                            strNew = "位于" + strNum1 + "号外显子上的第" + strNum2 + "位核苷酸由" + varStr[0].split(strNum2)[1] + "突变为" + varStr[1] + "，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                        }
                    } else if (str.contains("ins")) {
                        strNew = "位于" + strNum1 + "号外显子上的第" + strNum2 + "位和第" + arrDelsOne[1] + "位核苷酸之间插入碱基" + arrDelOrInsOrDelins[1] + "，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                    } else if (str.contains("del")) {
                        if (arrDelsOne.length == 1) {
                            strNew = "位于第" + strNum1 + "号外显子上的第" + strNum2 + "位核苷酸" + arrDelOrInsOrDelins[1] + "发生缺失，导致相应氨基酸序列发生变化，此突变的样本中的突变丰度为" + mutFreq + "。";
                        } else {
                            strNew = "位于" + strNum1 + "号外显子上的第" + strNum2 + "位到第" + arrDelsOne[1] + "位核苷酸" + arrDelOrInsOrDelins[1] + "发生缺失，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                        }
                    }
                } else if (str.contains(">") && !str.contains("_")) {
                    //Exon1 c.464T>G p.V155G／／exon11 c.C4540T p.R1514*
                    String[] arr = str.split("\\s+");
                    Matcher m = p.matcher(arr[0]);
                    String strNum1 = m.replaceAll("").trim();
                    Matcher mOtherOne = p.matcher(arr[1]);
                    String strNum2 = mOtherOne.replaceAll("").trim();
                    String[] varStr = arr[1].split(">");
                    varStr[0].replace("c." + strNum2, "");
                    strNew = "位于" + strNum1 + "号外显子的第" + strNum2 + "位核苷酸由" + varStr[0].replace("c." + strNum2, "") + "突变为" + varStr[1] + "，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                }
            } else {
                if (str.contains("Fusion")) {
                    String[] geneList = str.split("\\s+");
                    String[] genes = geneList[0].split("-");
                    strNew = "基因" + genes[0] + "和基因" + genes[1] + "发生融合。";
                } else if (str.contains("Amplification")) {
                    //EGFR 发生基因扩增，在样本中的扩增倍数为8.83
                    strNew = gene + " 发生基因扩增，在样本中的扩增倍数为" + mutFreq.replace("CN=", "") + "。";
                } else if (str.contains("Loss")) {
                    //EGFR 发生基因扩增，在样本中的扩增倍数为8.83
                    strNew = gene + " 发生基因缺失，在样本中的扩增倍数为" + mutFreq.replace("CN=", "") + "。";
                } else if (str.contains("exon") && str.contains("intron")) {
                    //exon4-intron4 c.372_375+11del（ins）CCCGTTGACTGGCAC	intron2-exon3 c.73-1_76GAAAG>AAAA
                    String strNum1 = "";
                    String strNum2 = "";
                    String strNum3 = "";
                    String strNum4 = "";
                    String strNum5 = "";
                    String[] arr = str.split("\\s+");
                    if (arr[0].contains("-exon")) {
                        strNum1 = arr[0].split("-exon")[1];
                    } else {
                        strNum1 = p.matcher(arr[0].split("-")[0]).replaceAll("").trim();
                    }
                    if (str.contains("del")) {
                        strNum2 = arr[1].split("_")[0].replaceAll("[^0-9+-]", "");
                        strNum3 = arr[1].split("_")[1].split("del")[0];
                        strNum4 = arr[1].split("del")[1];
                        strNew = "位于" + strNum1 + "号外显子的第" + strNum2 + "位到第" + strNum3 + "位核苷酸" + strNum4 + "发生缺失，此突变在样本中的突变丰度为" + mutFreq + "。";
                    } else if (str.contains("ins")) {
                        strNum2 = arr[1].split("_")[0].replaceAll("[^0-9+-]", "");
                        strNum3 = arr[1].split("_")[1].split("ins")[0];
                        strNum4 = arr[1].split("ins")[1];
                        strNew = "位于" + strNum1 + "号外显子的第" + strNum2 + "位到第" + strNum3 + "位核苷酸插入碱基" + strNum4 + "，此突变在样本中的突变丰度为" + mutFreq + "。";
                    } else if (str.contains(">")) {
                        strNum2 = arr[1].split("_")[0].replaceAll("[^0-9+-]", "");
                        strNum3 = arr[1].split("_")[1].replaceAll("[^0-9+-]", "");
                        strNum4 = arr[1].split("_")[1].split(">")[0].replaceAll("[^A-Z]", "");
                        strNum5 = arr[1].split(">")[1];
                        strNew = "位于" + strNum1 + "号外显子的第" + strNum2 + "位到第" + strNum3 + "位核苷酸由" + strNum4 + "突变为" + strNum5 + "，此突变在样本中的突变丰度为" + mutFreq + "。";
                    }
                } else if (str.contains("+") || str.contains("-")) {
                    //intron21 c.3153+1G>C
                    String[] arr = str.split("\\s+");
                    Matcher m = p.matcher(arr[0]);
                    String strNum1 = m.replaceAll("").trim();
                    String[] arrs = new String[2];
                    String strNum2 = "";
                    String strNum3 = "";
                    int strNum1_new = 0;
                    if (str.contains("+")) {
                        arrs = arr[1].split("\\+");
                    } else if (str.contains("-")) {
                        arrs = arr[1].split("\\-");
                        strNum1_new = Integer.valueOf(strNum1) + 1;
                    }
                    //intron13 c.2888-35_2888-20delTCTTTAACAAGCTCTT
                    //位于14号外显子之前的第2888-35位到第2888-20位核苷酸发生缺失，此突变在样本中的突变丰度为9.7%。注：del为缺失；ins为插入

                    if (str.contains("del") || str.contains("ins")) {
                        String[] strs = arr[1].split("_");
                        strNum2 = strs[0].replace("c.", "");
                        if (str.contains("del")) {
                            strNum3 = strs[1].split("del")[0];
                            strNew = "位于" + strNum1 + "号外显子之后的第" + strNum2 + "位到第" + strNum3 + "核苷酸发生缺失，此突变在样本中的突变丰度为" + mutFreq + "。";
                        } else if (str.contains("ins")) {
                            strNum3 = strs[1].split("ins")[0];
                            strNew = "位于" + String.valueOf(strNum1_new) + "号外显子之前的第" + strNum2 + "位到第" + strNum3 + "核苷酸发生插入，此突变在样本中的突变丰度为" + mutFreq + "。";
                        }
                    } else {
                        m = p.matcher(arrs[1]);
                        strNum2 = m.replaceAll("").trim();
                        m = p.matcher(arrs[1]);
                        strNum3 = m.replaceAll("").trim();
                        String[] vars = arrs[1].split(">");
                        if (str.contains("+")) {
                            strNew = "位于" + strNum1 + "号外显子之后的第" + strNum2 + "位核苷酸由" + vars[0].replace(strNum3, "") + "突变为" + vars[1] + "，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                        } else if (str.contains("-")) {
                            strNew = "位于" + String.valueOf(strNum1_new) + "号外显子之前的第" + strNum2 + "位核苷酸由" + vars[0].replace(strNum3, "") + "突变为" + vars[1] + "，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为" + mutFreq + "。";
                        }
                    }
                }
            }
        } catch (Exception e) {
            strNew = "";
            e.printStackTrace();
        }
        return strNew;
    }

    public String translate2(String gene, String mutation, String freq) {
        String dir = this.getClass().getResource("").getPath();
        String[] cmd = new String[]{"perl", String.format("%stranslate_hgvs.pl", dir), gene, mutation, freq};

        String msg = "";
        try {
            Process pro = Runtime.getRuntime().exec(cmd);
            InputStream ins = pro.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String brs;
            while ((brs = br.readLine()) != null) {
                msg += brs;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public File stat_report(File file) throws Exception {
        String dir = this.getClass().getResource("").getPath();
        File htmlFile = File.createTempFile("temp", ".xls");//创建临时文件
        String tempFileName = htmlFile.getCanonicalPath();
        List<String> cmd = new ArrayList<String>();
        cmd.add("python3");
        cmd.add(String.format("%sread_report.py", dir));
        cmd.add(tempFileName);
        cmd.add(file.toPath().toString());
        String[] cmds = new String[cmd.size()];
        cmd.toArray(cmds);
        try {
            Process pro = Runtime.getRuntime().exec(cmds);
            if (pro.isAlive()) {
                pro.waitFor();
            }
            if (pro.exitValue() == 0) {
                return htmlFile;
            } else {
                InputStream es = pro.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(es));
                String brs;
                String msg = "";
                while ((brs = br.readLine()) != null) {
                    msg += brs;
                }
                System.err.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String translateNCCN(Map nccn, List<Map> varList) {
        String nccnGene = nccn.get("gene").toString();
        String content = nccn.get("content").toString();
        boolean checkGene = false;
        for (Map map : varList) {
            String gene = map.get("gene").toString();
            if (nccnGene.equals(gene)) {
                checkGene = true;
            }
        }
        if (!checkGene) {
            return "未检出";
        }
        for (Map map : varList) {
            String gene = map.get("gene").toString();
            String ori_variant = map.get("ori_variant").toString();
            Set<String> numberSet = new HashSet<>();
            if (content.contains("外显子 ")) {
                numberSet = getNumber(content);
                for (String number : numberSet) {
                    if (ori_variant.contains("exon" + number + " ")) {
                        return "检出";
                    }
                }
            } else if (content.contains("密码子 ")) {
                numberSet = getNumber(content);
                String[] ori_variant_split = ori_variant.split(" ");
                if (ori_variant_split != null && ori_variant_split.length >= 3) {
                    String pHGVS = ori_variant_split[2];
                    Set<String> set = getNumber(pHGVS);
                    for (String s1 : numberSet) {
                        for (String s2 : set) {
                            if (s1.equals(s2)) {
                                return "检出";
                            }
                        }
                    }
                } else {
                    return "未检出";
                }
            } else if (content.contains("基因扩增")) {
                if (ori_variant.contains("Amplification")) {
                    return "检出";
                }
            } else if (content.contains("基因融合")) {
                if (ori_variant.contains("Fusion")) {
                    return "检出";
                }
            } else if (content.contains("基因扩增")) {
                return "检出";
            }
        }

        return "未检出";
    }

    public Set<String> getNumber(String str) {
        Set<String> result = new HashSet<>();
        for (String s : str.replaceAll("[^0-9]", ",").split(",")) {
            if (s.length() > 0)
                result.add(s);
        }
        return result;
    }

    public static void createJsonFile(File file, String json) {
        try {
            // 保证创建一个新文件
            System.err.println(file.toPath());
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(json);
            write.flush();
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
