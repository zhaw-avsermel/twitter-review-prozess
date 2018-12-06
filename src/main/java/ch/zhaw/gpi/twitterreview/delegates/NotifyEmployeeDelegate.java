package ch.zhaw.gpi.twitterreview.delegates;

import ch.zhaw.gpi.twitterreview.services.EmailService;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation des Send Tasks "Mitarbeiter benachrichtigen"
 * 
 * @author Melek
 */
@Named("notifyEmployeeAdapter")
public class NotifyEmployeeDelegate implements JavaDelegate {

    // Aus aufgabe 8.3.5: Verdrahten des Mailservers
    @Autowired
    private EmailService emailService;
    
    /**
     * Sendet eine Benachrichtigung per Mail
     * 
     * 1. Die Prozessvariable auslesen
     * 2. Die E-Mail-NAchricht zusammenstellen
     * (3. E-Mail in der Konsole ausgeben) --> wird in der Aufgabe 8.3.5 verändert: // 3. Mail über Mailservice versenden  
     * 
     * @param de            Objekt welches die Verknüpfung zur Process Engine und
     * aktuellen Execution enthält
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution de) throws Exception {
        // Prozessvariablen auslesen
        String email = (String) de.getVariable("email");
        String tweetContent = (String) de.getVariable("tweetContent");
        String checkResult = (String) de.getVariable("checkResult");
        String checkResultComment = (String) de.getVariable("checkResultComment");
        String mailMainPart = (String)de.getVariable("mailMainPart");
        
        // Die E-Mail-Nachricht zusammenbauen
        String mailHauptteil;
        if (mailMainPart != null){
                mailHauptteil = mailMainPart;
        } else if(checkResult.equals("rejected")){
            mailHauptteil = "Leider wurde diese Tweet-Anfrage abgelehnt mit " +
                    "folgender Begründung:\n" + checkResultComment;
        } else {
            mailHauptteil = "Dein Tweet wurde geposted. Herzlichen Dank für Deinen Beitrag.";
        }
        
        // Mail-Text zusammenbauen
        String mailBody = "Hallo Mitarbeiter\n\n" + "Du hast folgenden Text zum " +
                "Veröffentlichen als Tweet vorgeschlagen:\n" + tweetContent + "\n\n" +
                mailHauptteil + "\n\n" + "Deine Kommunikationsabteilung";
        
        /*
        --> Braucht es nicht mehr ---> wird mit Code aus der Aufgabe 8.3.5 ersetzt
        Mail in Konsole ausgeben
        System.out.println("########### BEGIN MAIL ##########################");
        System.out.println("############################### Mail-Empfänger: " + email);
        System.out.println(mailBody);
        System.out.println("########### END MAIL ############################");
**/
        // Durch verdrahten mit @autowired kann ich den emailServer hier nutzen
            // body: "Neuigkeiten zu Ihrer Tweet-Anfrage"
        emailService.sendSimpleMail(email, "Neuigkeiten zu Ihrer Tweet-Anfrage", mailBody);
    }
    
}
