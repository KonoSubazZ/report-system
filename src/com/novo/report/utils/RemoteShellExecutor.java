package com.novo.report.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteShellExecutor {
     
     private Connection conn;
     /** 远程机器IP */
     private String ip;
     /** 用户名 */
     private String osUsername;
     /** 密码 */
     private String password;
     private String charset = Charset.defaultCharset().toString();

     private static final int TIME_OUT = 1000 * 5 * 60;

     /**
      * 构造函数
      * @param ip
      * @param usr
      * @param pasword
      */
     public RemoteShellExecutor(String ip, String usr, String pasword) {
          this.ip = ip;
         this.osUsername = usr;
         this.password = pasword;
     }


     /**
     * 登录
     * @return
     * @throws IOException
     */
     private boolean login() throws IOException {
         conn = new Connection(ip);
         conn.connect();
         return conn.authenticateWithPassword(osUsername, password);
     }

     /**
     * 执行脚本
     * 
     * @param cmds
     * @return
     * @throws Exception
     */
     public int exec(String cmds) throws Exception {
         InputStream stdOut = null;
         InputStream stdErr = null;
         String outStr = "";
         String outErr = "";
         int ret = -1;
         try {
         if (login()) {
        	 System.out.println("远程登录成功！");
        	 System.out.println("执行命令："+cmds);
             // Open a new {@link Session} on this connection
             Session session = conn.openSession();
             // Execute a command on the remote machine.
             session.execCommand(cmds);
             System.out.println("执行命令！");
             
             stdOut = new StreamGobbler(session.getStdout());
             outStr = processStream(stdOut, charset);
             
             stdErr = new StreamGobbler(session.getStderr());
             outErr = processStream(stdErr, charset);
             
             session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
             
             System.out.println("outStr=" + outStr);
             System.out.println("outErr=" + outErr);
             
             ret = session.getExitStatus();
         } else {
             throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
         }
         } finally {
             if (conn != null) {
                 conn.close();
             }
             IOUtils.closeQuietly(stdOut);
             IOUtils.closeQuietly(stdErr);
         }
         return ret;
     }

     /**
     * @param in
     * @param charset
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
     private String processStream(InputStream in, String charset) throws Exception {
         byte[] buf = new byte[1024];
         StringBuilder sb = new StringBuilder();
         while (in.read(buf) != -1) {
             sb.append(new String(buf, charset));
         }
         return sb.toString();
     }

    public static void main(String args[]) throws Exception {
    	 /*RemoteShellExecutor executor = new RemoteShellExecutor("172.25.82.3", "root", "123.com");
	        String pathUrl = session.getServletContext().getRealPath("/");
			String path="java -jar /usr/local/tomcat/webapps/novoreport/NovoLoader.jar daemon=no uploading=yes url="+"http://172.25.82.3:8080/Webservices/REST/"+" dir="+systemPropertyService.getPropertyValueByPropertyName("FILE_PATH")+  " dateFileter="+20171013+" &";
			
	        // 执行myTest.sh 参数为java Know dummy
	        try {
				System.out.println(executor.exec(path));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	           
    }
}