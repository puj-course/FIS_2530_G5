package com.example.padron_decorador_modificado;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WhatsAppDecorador extends BaseDecorador {

    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    public WhatsAppDecorador(Notificacion notifier) {
        super(notifier);
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }



    @Override
    public void sendMessage(String message,String ind) {
        super.sendMessage(message,ind);
        message+="si se trata de alguna equivocacion omita este mensaje";
        Message msg = Message.creator(
                new PhoneNumber("whatsapp:+573150639689"),
                new PhoneNumber("whatsapp:+14155238886"),
                message
        ).create();

        System.out.println("✅ WhatsApp enviado a " + msg.getTo());
    }
}
