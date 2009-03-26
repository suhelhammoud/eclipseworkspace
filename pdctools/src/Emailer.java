
import org.apache.log4j.Logger;

import java.io.File;
import java.security.Security;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class Emailer 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Emailer.class);

	public synchronized void sendMail(String toEmail,String subject, String body, String sender,String fileAttachment) throws Exception 
	{	

		if(! new File(fileAttachment).exists()){
			logger.error("no file attechmet found"+ new File(fileAttachment).getAbsolutePath());
			return;
		}
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.gmail.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
		"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication()
			{ return new PasswordAuthentication("pdc.to.jpg@gmail.com","");	}
		});		

		////suhel
		// Define message
		MimeMessage message = new MimeMessage(session);

		message.setSender(new InternetAddress(sender));
		message.setSubject(subject);

		//message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		// create the message part 
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		//fill message
		messageBodyPart.setText(body);

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(fileAttachment);
		messageBodyPart.setDataHandler(	new DataHandler(source));
		messageBodyPart.setFileName(new File(fileAttachment).getName());
		multipart.addBodyPart(messageBodyPart);

		// Put parts in message
		message.setContent(multipart);

		////////////////
		//MimeMessage message = new MimeMessage(session);

		if (toEmail.indexOf(',') > 0) 
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
		else
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));


		Transport.send(message);

	}


	public static void main(String args[]) throws Exception
	{
		Emailer simpleMail = new Emailer();
		List<String> zipAttachment=Zipper.zipAndSplit("data/in", "data/zip", 1000000);
		System.out.println(zipAttachment);
		for (String attachment : zipAttachment) {
			simpleMail.sendMail("a", "a", "pdc.to.jpg@gmail.com", "eepgssh@gmail.com",attachment);
		}

	}
	public static boolean zipAndSend(String indir,String outdir,String toEmail,String subject,String body){
		Emailer simpleMail = new Emailer();
		List<String> zipAttachment=Zipper.zipAndSplit(indir, outdir, 9000000);
		if(zipAttachment.size()==0) return false;

		System.out.println(zipAttachment);
		for (String attachment : zipAttachment) {
			try {
				simpleMail.sendMail(toEmail,subject, body,"pdc.to.jpg@gmail.com" ,attachment);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
