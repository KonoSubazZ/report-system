package com.novo.report.utils;

import java.util.List;
import java.util.Map;

public class VariantUtils {

    private VariantUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Checks if a variant is an EGFR exon 19 deletion (19del).
     * Expects HGVS format, e.g., "NM_005228.5 exon19 c.2239_2255delinsCAACA p.L747_S752delinsQH".
     * Returns true if:
     * - Variant involves NM_005228 (EGFR transcript).
     * - Contains "exon19" (case-sensitive).
     * - Contains "del" or "delins" (case-sensitive).
     *
     * @param variant the variant string
     * @return true if 19del, false otherwise
     */
    public static boolean isExon19Deletion(String gene, String variant) {
        if (variant == null || variant.trim().isEmpty()) {
            return false;
        }
        // Check for EGFR
		if (!gene.contains("EGFR")) {
			return false;
		}

        // Check for NM_005228 (EGFR transcript)
		/*if (!variant.contains("NM_005228")) {
			return false;
		}*/

        // Check for exon19
        if (!variant.contains("exon19")) {
            return false;
        }

        // Check for del or delins or dup
        if (!(variant.contains("del") || variant.contains("delins"))) {
            return false;
        }

        return true;
    }
   /* public void getParentMutId(Map mutation) {
        String gene = mutation.get("gene") == null ? "" : mutation.get("gene").toString();
        String variant = mutation.get("variant") == null ? "" : mutation.get("variant").toString();

        // 父级突变的变异信息，例 Exon11 Mutation， 可能有多个
        List<String> parentVariant = analysisReportDao.getParentVariant(gene, variant);

        // 没有查询到父级突变 根据variant类型判断
        if (parentVariant.isEmpty() && (variant.indexOf("fs") > -1 || variant.indexOf("*") > -1 || variant.indexOf("+") > -1 || variant.indexOf("-") > -1) && !(variant.indexOf("Fusion") > -1)) {
            Integer mut_id = analysisReportDao.getMutationId(gene, "Inactive Mutation");
            if (mut_id != null) {
                parentVariant.add("Inactive Mutation");
            }
        }
        mutation.put("parent_variant", parentVariant);
    }*/


}
