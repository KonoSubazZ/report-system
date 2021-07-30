package com.novo.report.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/** 
 * ftp上传下载工具类 
 * <p>Title: FtpUtil</p> 
 * <p>Description: </p> 
 * <p>Company: www.itcast.com</p>  
 * @author  入云龙 
 * @date    2015年7月29日下午8:11:51 
 * @version 1.0 
 */  
public class FtpUtil {
	  /**  
     * Description: 向FTP服务器上传文件  
     * @param host FTP服务器hostname  
     * @param port FTP服务器端口  
     * @param username FTP登录账号  
     * @param password FTP登录密码  
     * @param basePath FTP服务器基础目录 
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 输入流  
     * @return 成功返回true，否则返回false  
     */    
	 /**
     * Description: 从FTP服务器下载文件
     *
     *@paramurl
     *            FTP服务器hostname
     *@paramport
     *            FTP服务器端口
     *@paramusername
     *            FTP登录账号
     *@parampassword
     *            FTP登录密码
     *@paramremotePath
     *            FTP服务器上的相对路径
     *@paramfileName
     *            要下载的文件名
     *@paramlocalPath
     *            下载后保存到服务器的路径
     *@return
     */
    public static boolean downFile(String url,int port, String username,String password, String remotePath, String fileName,String path) {
          boolean success= false;
          FTPClient ftp= new FTPClient();
          int reply;
          try{
               ftp.connect(url,port);
               ftp.login(username,password);
               ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//文件类型为二进制文件
               reply= ftp.getReplyCode();
               if(!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    return success;
               }
               ftp.enterLocalPassiveMode();//本地模式
               ftp.changeWorkingDirectory(remotePath);
    		   File localFile= new File(path+fileName);
    		   OutputStream  is= new FileOutputStream(localFile);
    		   ftp.retrieveFile(fileName,is);
    		   is.close();
               /*FTPFile[]fs= ftp.listFiles();
               for(FTPFile ff: fs) {
                    if(ff.getName().equals(fileName)) {
                          File localFile= new File(path+ff.getName());
                          OutputStream  is= new FileOutputStream(localFile);
                          ftp.retrieveFile(ff.getName(),is);
                          is.close();
                    }
               }*/
               ftp.logout();
               success= true;
          }catch(SocketException e) {
               //TODOAuto-generated catch block
               e.printStackTrace();
          }catch(IOException e) {
               //TODOAuto-generated catch block
               e.printStackTrace();
          }finally{
               if(ftp.isConnected()) {
                    try{
                          ftp.disconnect();
                    }catch(IOException e) {
                          //TODOAuto-generated catch block
                          e.printStackTrace();
                    }
               }
          }
          return success;
    }
}
