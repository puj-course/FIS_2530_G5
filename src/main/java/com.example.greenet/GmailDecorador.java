package com.example.padron_decorador_modificado;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class GmailDecorador extends BaseDecorador {

    private String remitente = "greenetcorporation@gmail.com";
    private String clave = "ajlu fgwz euve aziz"; // no tu contraseña normal

    public GmailDecorador(Notificacion notifier) {
        super(notifier);
    }

    @Override
    public void sendMessage(String message,String iden, long telefono) {

        super.sendMessage(message,iden,telefono);
        message+=" si este no es su correo omita el mensaje ";
        enviarCorreo(iden, "Top secret", message);
    }

    private void enviarCorreo(String destino, String asunto, String cuerpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Correo enviado a " + destino);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

