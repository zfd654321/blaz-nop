package com.bl.nop.common.util;

import java.io.File;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.Base64.Encoder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SMTPSendMail {
	
	private static Logger log = LoggerFactory.getLogger(SMTPSendMail.class);

    public static void sendMail(String to,String Subject,String Text) {

        String host = "smtp.ym.163.com";
        String from = "blazalarm@blaz.com.cn";
        final String username = "blazalarm@blaz.com.cn";
        final String password = "blaz2018";
        Properties props = new Properties();
      
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.auth", "true");

        Session session=Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(Subject);
            message.setHeader("Content-Type", "text/html;charset=UTF-8");
            message.setText(Text);
            
            message.setSentDate(new Date());  
            message.saveChanges();  
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        log.info("发送完毕！");
    }
    
	public static void sendMail(String to,String Subject,String Text, String... files) {

        String host = "smtp.ym.163.com";
        String from = "blazalarm@blaz.com.cn";
        final String username = "blazalarm@blaz.com.cn";
        final String password = "blaz2018";
        Properties props = new Properties();
      
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.auth", "true");

        Session session=Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(Subject);
            message.setText(Text);
            
            Multipart multipart = new MimeMultipart();  
            BodyPart contentPart = new MimeBodyPart();  
            contentPart.setText(Text);  

            contentPart.setHeader("Content-Type", "text/html; charset=UTF-8");  
            multipart.addBodyPart(contentPart);  
            for (String file : files) {  
                File usFile = new File(file);  
                MimeBodyPart fileBody = new MimeBodyPart();  
                DataSource source = new FileDataSource(file);  
                fileBody.setDataHandler(new DataHandler(source));  
				Encoder enc = Base64.getEncoder();
                fileBody.setFileName("=?UTF-8?B?"  
                        + enc.encode(usFile.getName().getBytes()) + "?=");  
                multipart.addBodyPart(fileBody);  
            }  
            message.setContent(multipart);  
            message.setSentDate(new Date());  
            message.saveChanges();  
            Transport.send(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        log.info("发送完毕！");
    }
}
	
