package com.novo.report.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 邮件管理器 java 实现邮件的发送， 抄送及多附件
 * 
 * @version 1.0
 * @created at 2018年1月11日
 */
public class EmailUtil {

	public String username; // 服务邮箱(from邮箱)
	public String password; // 邮箱密码
	public String senderNick; // 发件人昵称
	public String auth;
	public String host; // 发件服务器
	public String port; // 发件端口号

	private Properties props; // 系统属性
	private Session session; // 邮件会话对象
	private MimeMessage mimeMsg; // MIME邮件对象
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	public EmailUtil(String mail) {
		Properties properties = System.getProperties();
		InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream(mail);
		try {
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		username = properties.getProperty("username");
		password = properties.getProperty("password");
		senderNick = properties.getProperty("senderNick");
		auth = properties.getProperty("auth");
		host = properties.getProperty("host");
		port = properties.getProperty("port");

		try {
			props = System.getProperties();
			props.put("mail.smtp.auth", auth);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("username", username);
			props.put("password", password);
			MailSSLSocketFactory factory = new MailSSLSocketFactory();
			factory.setTrustAllHosts(true);// 需要SSL加密 否则不能发送
			props.put("mail.smtp.ssl.enable", true);
			props.put("mail.smtp.ssl.socketFactory", factory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 建立会话
//		session = Session.getDefaultInstance(props); // 在同一个进程中Session.getDefaultInstance得到的是一个单例的Session对象，就是第一次getDefaultInstance得到的Session对象
		session = Session.getInstance(props);	// 每次切换账户得到的都是一个新的Session对象
		session.setDebug(false);
	}

	public synchronized static EmailUtil getInstance(String mail) {
		EmailUtil instance = new EmailUtil(mail);
		return instance;
	}

	/**
	 * 发送邮件
	 * 
	 * @param from
	 *            发件人
	 * @param to
	 *            收件人
	 * @param copyto
	 *            抄送
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 * @param fileList
	 *            附件列表
	 * @return
	 */
	public synchronized Map sendMail(String from, String[] to, String[] copyto, String subject, String content,
			String[] fileList) {
		Map map = new HashMap();
		boolean success = true;
		String errorMessage = "";
		List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
		// 20250107 测试系统增加特定邮箱
		if (ips.contains(ServerConfig.getServerTestIP())) {
			to = new String[]{"liushangzhi9168@novogene.com"};
			copyto = new String[]{"wangxueran7632@novogene.com", "tumor-bioinfo@novogene.com"};
			subject = "【测试邮件】";
			content = "【系统测试邮件】";
		}
		try {
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
			// 自定义发件人昵称
			String nick = "";
			try {
				nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 设置发件人
			mimeMsg.setFrom(new InternetAddress(from, nick));
			// 设置收件人
			if (to != null && to.length > 0) {
				String toListStr = getMailList(to);
				mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
			}
			// 设置抄送人
			if (copyto != null && copyto.length > 0) {
				String ccListStr = getMailList(copyto);
				mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr));
			}
			// 设置主题
			mimeMsg.setSubject(subject);
			// 设置正文
			BodyPart bp = new MimeBodyPart();
			bp.setContent(content, "text/html;charset=utf-8");
			mp.addBodyPart(bp);
			// 设置附件
			if (fileList != null && fileList.length > 0) {
				for (int i = 0; i < fileList.length; i++) {
					bp = new MimeBodyPart();
					FileDataSource fds = new FileDataSource(fileList[i]);
					bp.setDataHandler(new DataHandler(fds));
					bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
					mp.addBodyPart(bp);
				}
			}
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			// 发送邮件
			try {
				if (props.get("mail.smtp.auth").equals("true")) {
					Transport transport = session.getTransport("smtp");
					transport.connect((String) props.get("mail.smtp.host"), (String) props.get("username"),
							(String) props.get("password"));
					transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
					transport.close();
				} else {
					Transport.send(mimeMsg);
				}
				errorMessage = "邮件发送成功!";
				try {
					boolean imapReceiveMail = IMAPReceiveMail(subject);
					if(!imapReceiveMail) {
						errorMessage += "发件箱中没有该邮件，请确认！";
					}
				}catch(Exception e){
					errorMessage +="检查发件箱时网络连接失败，请确认！";
				}
			} catch (SendFailedException e) {
				e.printStackTrace();
				errorMessage = "邮件发送异常，请联系管理员查看！";
				success = false;
				Address[] invalid = e.getInvalidAddresses();
				if (invalid != null) {
					errorMessage = "邮件发送失败。";
					for (Address address : invalid) {
						errorMessage += address + ",";
					}
					errorMessage = errorMessage.substring(0,errorMessage.lastIndexOf(","));
					errorMessage += "邮箱存在错误，请即时反馈给运营组";
					success = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			errorMessage = "网络连接失败";
		}
		System.err.println(errorMessage);
		map.put("flag", success);
		map.put("errorMessage", errorMessage);
		return map;
	}

	public String getMailList(String[] mailArray) {
		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}
			}
		}
		return toList.toString();
	}
	
	public boolean IMAPReceiveMail(String subjectName) throws Exception {
		boolean flag = false;
		//连接会话信息
		Store store = session.getStore("imaps");
		store.connect("imap.exmail.qq.com", username, password);

		// 获得收件箱
        Folder folder = store.getFolder("Sent Messages");
        // 以读写模式打开收件箱
        folder.open(Folder.READ_ONLY);
        // 获得收件箱的邮件列表
        Message[] messages = folder.getMessages();
        for (int i = messages.length-1; i > messages.length-11; i--) {
        	if(i<0) {
        		break;
        	}
        	IMAPMessage msg = (IMAPMessage) messages[i];
            String subject = MimeUtility.decodeText(msg.getSubject());
            if(subject.indexOf(subjectName) != -1) {
            	System.out.println("邮件主题：[" + subject + "]");
            	Date received=messages[i].getReceivedDate();
                if(received!=null){
                    System.out.println("发送邮件时间："+received);
                }
                flag = true;
                break;
            }
		}
        folder.close(false);
		store.close();
		return flag;
	}

	public static void main(String[] args) throws Exception {
		/*String from = username;
		String[] to = { "759926902@qq.com" };
		String[] copyto = {};
		String subject = "请查收诺禾致源的检测报告，姓名：张三-123456";
		String content = "尊敬的客户：<br>您好！<br>请您查收附件的检测报告<br>祝好~";
		String[] fileList = new String[1];
		fileList[0] = "C:/Users/DELL/Desktop/自动化报告改进意见1.15.docx";
		String fileName = "张三-123456.docx";
		EmailUtil.getInstance().sendMail(from, to, copyto, subject, content, fileList);*/
		boolean imapReceiveMail = EmailUtil.getInstance("mail.properties").IMAPReceiveMail("请查收诺禾致源的检测报告，姓名：韩喜文-MKHS190074601-1A");
		System.err.println(imapReceiveMail);
	}
}