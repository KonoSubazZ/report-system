package com.novo.report.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.novo.report.beans.Json;

import javafx.util.Pair;
import net.sf.json.JSONArray;

public class TestSimple {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Pair<String, String>> listdrug = new ArrayList<Pair<String, String>>();
		Pair<String, String> pair0 = new Pair<>("基因说明","aa");
		Pair<String, String> pair1 = new Pair<>("信号通路说明","bb");
		Pair<String, String> pair2 = new Pair<>("位点说明","cc");
		Pair<String, String> pair3 = new Pair<>("突变频率","dd");
		Pair<String, String> pair4 = new Pair<>("NCCN指南","ee");
		Pair<String, String> pair5 = new Pair<>("耐药说明","ff");
		Pair<String, String> pair6 = new Pair<>("临床/临床前药物研究说明","gg");
		Pair<String, String> pair7 = new Pair<>("预后和诊断说明","hh");
		listdrug.add(pair0);
		listdrug.add(pair1);
		listdrug.add(pair2);
		listdrug.add(pair3);
		listdrug.add(pair4);
		listdrug.add(pair5);
		for (Pair<String, String> pair : listdrug) {
			System.out.println(pair.getKey());
			System.out.println(pair.getValue());
		}
		String string = JSONArray.fromObject(listdrug).toString();
		System.out.println(string);
		
		
		JSONArray array2 = JSONArray.fromObject(string);
		List<Json> list = (List<Json>) JSONArray.toCollection(array2, Json.class);
		for (Json json : list) {
			System.out.println(json.getKey());
			System.out.println(json.getValue());
		}
		
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		map.put("aa", true);
		map.put("bb", false);
		map.put("aa", false);
		Set<Entry<String, Boolean>> entrySet = map.entrySet();
		for (Entry<String, Boolean> entry : entrySet) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}

}
