package ch.zhaw.gpi.twitterreview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service-Klasse für die Kommunikation mit einem SMTP Server
 *
 * @Service stellt sicher, dass diese Klasse beim Starten der Applikation als
 * Bean generiert wird (wie bei @Component und @Bean), aber als Service
 * gekennzeichnet ist
 *
 * @author scep
 *
 * avsermel: In dieseer Klasse wird nun der Java-Mail server genutzt. Ich greife
 * auf ein Objekt, welces von spring beim Start automatisch generriert
 */
@Service
public class EmailService {

    // Um JavaMailsenden objekt nutzen zu können  mit Autowired verdrahten
    @Autowired
    private JavaMailSender javaMailSender;

    // Sender-Adresse aus application.properties auslesen
    @Value("${mail.senderaddress}")
    private String senderAddress;

    // Methode um Mail zu versenden
    
    /**
     * Sendet eine einfache Text-Mail
     *
     * @param to Empfänger
     * @param subject Betreff
     * @param body Mail-Text
     * @throws java.lang.Exception
     */
    public void sendSimpleMail(String to, String subject, String body) throws Exception {
        // Instanziert eine neue SimpleMail-Nachricht 
            // SimpleMailMessage ist ein spring framework--> wird anfangs leer gebildet
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // Legt Empfänger, Betreff und Mail-Text fest
            // Objekte welche oben in der Methode mitgegeben werden, werden hier abgefüllt
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(senderAddress);
        simpleMailMessage.setReplyTo(senderAddress);

        // Versucht, die Mail abzusenden  (wie bei tweet posten)
        try {
            // Mail versenden
                // Das was oben mit simpleMailMessage.setTo(to); etc. vorbereitet wurde, wird hier gesendet
            javaMailSender.send(simpleMailMessage);

            // WENN VERSAND ERFOLGREICH DANN:
            // In der Konsole mitteilen, dass die Mail versandt wurde für einfacheres Debugging 
            System.out.println("Mail erfolgreich versandt");
            
            // Mailexception (spring komponent) wird ausgelöst
        } catch (MailException me) {
             // WENN VERSAND NICHT ERFOLGREICH DANN:
           
            // Fehlermeldung ausgeben in Konsole 
            System.err.println(me.getLocalizedMessage());

            // Fehler weitergeben an aufrufende Methode 
            throw new Exception("Mail senden fehlgeschlagen", me);
        }
    }

}
