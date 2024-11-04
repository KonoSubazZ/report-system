package com.novo.report.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TemplateUtil2 {
	public File stat_report(File file,String docxPath,String fileName) throws Exception {
		String dir = this.getClass().getResource("").getPath();
		File htmlFile = new File(fileName);
    	List<String> cmd = new ArrayList<String>();
    	cmd.add("python3");
    	cmd.add(String.format("%sgenerate_report.py", dir));
    	cmd.add(docxPath);
    	cmd.add(file.toPath().toString());
    	cmd.add(fileName);
    	String[] cmds = new String[cmd.size()];
    	cmd.toArray(cmds);
    	System.err.println(cmd.toString());
		try {
			Process pro = Runtime.getRuntime().exec(cmds);
			if (pro.isAlive()) {
				pro.waitFor();
				System.err.println("完成");
				/*final InputStream is1 = pro.getInputStream();
				new Thread(() -> {
					BufferedReader br = new BufferedReader(new InputStreamReader(is1));
					try{
						while(br.readLine() != null) ;
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}).start();
				InputStream is2 = pro.getErrorStream();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
				while(br2.readLine() != null){}
				pro.waitFor();
				System.err.println("完成");*/
			}
			/*String str = "";
			BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			while((str=(buffer.readLine()))!=null) {
				System.out.println(str);
			}*/
			if (pro.exitValue() == 0) {
				System.err.println("返回");
				return htmlFile;
			} else {
				InputStream es = pro.getErrorStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(es));
				String brs;
				String msg = "";
				while((brs = br.readLine())!=null) {
					msg += brs;
				}
				System.err.println(msg);
				return null;
			}
		} catch(IOException e) {
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
}
