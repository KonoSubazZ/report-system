package com.novo.report.utils;

import java.io.File;

public class DeleteFileUtil {
	public static void deleteFiles(String path){
		File file = new File(path);
		//1级文件刪除
		if(!file.isDirectory()){
			file.delete();
		}else if(file.isDirectory()){
			//2级文件列表
			String []filelist = file.list();
			//获取新的二級路徑
			for(int j=0;j<filelist.length;j++){
				File filessFile= new File(path+"/"+filelist[j]);
				if(!filessFile.isDirectory()){
					filessFile.delete();
				}else if(filessFile.isDirectory()){
					//递归调用
					deleteFiles(path+"/"+filelist[j]);
				}
			}
			file.delete();
		}
	}
}
