package com.novo.report.utils;

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
	public static boolean isExon19Deletion(String variant) {
		if (variant == null || variant.trim().isEmpty()) {
			return false;
		}
		// Check for EGFR
		/*if (!variant.contains("EGFR")) {
			return false;
		}*/

		// Check for NM_005228 (EGFR transcript)
		/*if (!variant.contains("NM_005228")) {
			return false;
		}*/

		// Check for exon19
		if (!variant.contains("exon19")) {
			return false;
		}

		// Check for del or delins or dup
		if (!(variant.contains("del") || variant.contains("delins") || variant.contains("dup"))) {
			return false;
		}

		return true;
	}


}
