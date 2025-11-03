package com.example.greenet;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSDecorador extends BaseDecorador {


    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    private static final String TWILIO_NUMBER = "+16203373277"; 

    public SMSDecorador(Notificacion notifier) {
        super(notifier);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void sendMessage(String message, String correo, long telefono) {
        super.sendMessage(message, correo, telefono);
        String numeroDestino = "+57"+telefono;
        String mensajeFinal ="Un usuario fue bloqueado por incumplir las normas revise su correo o wha para mas informacion";

        try {
            Message msg = Message.creator(new PhoneNumber(numeroDestino), new PhoneNumber(TWILIO_NUMBER),mensajeFinal).create();

            System.out.println(" SMS enviado correctamente a " + msg.getTo());
        } catch (Exception e) {
            System.out.println("️ Error al enviar el SMS: " + e.getMessage());
        }
    }
}

