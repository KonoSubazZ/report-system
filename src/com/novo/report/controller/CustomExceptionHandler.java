package com.novo.report.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(assignableTypes = GeneMarkerVwController.class)
public class CustomExceptionHandler {
   @ExceptionHandler(Exception.class)
   @ResponseBody
   public Map exceptionHandler(Exception e){
      e.printStackTrace();
      Map<String,Object> result = new HashMap<>();
      result.put("errorMsg", e.getMessage() == null ? "" : e.getMessage().toString());
      result.put("isError", true);
      return result;
   }
   
   // 格式化
   public String formatStackTrace(Throwable throwable) {  
	    if(throwable==null) return "";  
	    String rtn = throwable.getStackTrace().toString();  
	    try {  
	        Writer writer = new StringWriter();  
	        PrintWriter printWriter = new PrintWriter(writer);  
	        throwable.printStackTrace(printWriter);       
	        printWriter.flush();  
	        writer.flush();  
	        rtn = writer.toString();  
	        printWriter.close();              
	        writer.close();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } catch (Exception ex) {  
	    }  
	    return rtn;  
	}  

}
