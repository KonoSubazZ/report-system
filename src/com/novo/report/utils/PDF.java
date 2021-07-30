package com.novo.report.utils;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class PDF {
    static final int wdFormatPDF = 17;// PDF 格式    
    @SuppressWarnings("static-access")
	public static void wordToPDF(){    
            
        System.out.println("启动Word...");      
        long start = System.currentTimeMillis();      
        ActiveXComponent app = null;  
        Dispatch doc = null;  
        try {      
            app = new ActiveXComponent("Word.Application");      
            app.setProperty("Visible", new Variant(false));  
            Dispatch docs = app.getProperty("Documents").toDispatch();    
            //String path = "C:/Users/DELL/Desktop";
            String sfileName = "C:/Users/DELL/Desktop/201706079800肠癌组织23基因报告711.doc";
            String toFileName = "C:/Users/DELL/Desktop/201706079800肠癌组织23基因报告711.pdf";
            System.out.println("打开文档..." + sfileName);  
            
            doc = Dispatch.call(docs,  "Open" , sfileName).toDispatch();  
            Dispatch activeDocument = app.getProperty("ActiveDocument").toDispatch();  
            /** 获取目录 */  
            Dispatch tablesOfContents = Dispatch.get(activeDocument, "TablesOfContents").toDispatch();  
            /** 获取第一个目录。若有多个目录，则传递对应的参数  */  
            Variant tablesOfContent = Dispatch.call(tablesOfContents, "Item", new Variant(1));  
            /** 更新目录，有两个方法：Update 更新域，UpdatePageNumbers 只更新页码 */  
            Dispatch toc = tablesOfContent.toDispatch();  
            toc.call(toc, "UpdatePageNumbers");  
            System.out.println("更新目录");  
      
            /** 另存为 */  
            Dispatch.call(Dispatch.call(app, "WordBasic").getDispatch(), "FileSaveAs", sfileName);  
            System.out.println("转换文档到PDF..." + toFileName);      
            File tofile = new File(toFileName);      
            if (tofile.exists()) {      
                tofile.delete();      
            }      
            Dispatch.call(doc,      
                          "SaveAs",      
                          toFileName, // FileName      
                          wdFormatPDF);      
            long end = System.currentTimeMillis();      
            System.out.println("转换完成..用时：" + (end - start) + "ms.");  
        } catch (Exception e) {      
            System.out.println("========Error:文档转换失败：" + e.getMessage());      
        } finally {  
            Dispatch.call(doc,"Close",false);  
            System.out.println("关闭文档");  
            if (app != null)      
                app.invoke("Quit", new Variant[] {});      
            }  
          //如果没有这句话,winword.exe进程将不会关闭  
           ComThread.Release();
           
    }
    
    public static void main(String[] args) {
    	PDF pdf = new PDF();
    	pdf.wordToPDF();
	}
}
