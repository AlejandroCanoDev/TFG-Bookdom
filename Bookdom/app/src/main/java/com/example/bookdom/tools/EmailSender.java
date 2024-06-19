package com.example.bookdom.tools;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender extends AsyncTask<Void, Void, Void> {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USERNAME = "bookdominformation@gmail.com";
    private static final String SMTP_PASSWORD = "mhli nmcl klqp ihib";

    private String to;
    private String subject;
    private String body;

    public EmailSender(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            Log.e("EmailSender", "Error al enviar el correo", e);
        }
        return null;
    }
}
