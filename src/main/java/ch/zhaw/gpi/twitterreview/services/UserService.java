package ch.zhaw.gpi.twitterreview.services;

import ch.zhaw.gpi.twitterreview.resources.User;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Sie heisst UserService, weil sie gegenüber den anderen Klassen (z.B.
 * JavaDelegate) als Service-Klasse auftritt. Selbst nutzt sie aber auch einen
 * UserService, aber per REST, ist also demgegenüber ein Rest-Client. Dass der
 * Rest-Service zufälligerweise 😉 auch UserService heisst, kann der Client
 * nicht wissen.
 *
 * @author Melek
 */
@Component
public class UserService {

    private final RestTemplate restTemplate;
    
    // um Variablen aus GetUserInformationDelegate überhaupt auslesen zu können
    // --> gibt mir irgendwas zurück, dass in der application.properties gesetzt ist, in unserem Fall "userservice.endpoint=http://localhost:8070/userapi/v1"
    @Value(value = "$(userservice.endpoint)")
    private String userserviceEndpoint;

    // Instanzierung des finals durch einen Konstruktor
    public UserService() {
        // Somit haben wir den Zugang zum RestTemplate Objekt
        restTemplate = new RestTemplate();
    }
    // Ziel Vorname und E-Mail von Benutzer zurückerhalten wenn der Username übergeben wird
    // RestTemplate gibt uns eben nur einen Json Objekt zurück. Dieser muss deshalb deserialisiert werden.

    /// Dafür gibt es 2 möglichkeiten
    // a. Wir parsen die JSON String um die einzelnen Elemente aus dem Json objekt herauszulesen (mühsam)
    // b. (eleganter, auch praktikabler für Abschlussarbeit) Wir lassen das RestTemplate die Arbeit für uns machen, indem JSON dies direkt in ein Objekt deserialisiert. Dafür muss ein Objekt angelegt werden. Wie: ich schaue was im json objekt zurückgegeben wird und baue mir entsprechend eine Klasse mit diesen Elementen. Wir wollen ja nur firstName und E-Mail. Deshalb wird unser neues Objekt auch nur diese beiden Elemente enhalten
    // --> neue Klasse User im unterordner resources
    public User getUser(String userName) {
        // Weil wir nicht sicher sein können, dass alles gut klappt, müssen wir so eine 404 Error Meldung geben können
        // dies funktioniert mit der try-catch-statement
        // try: Was passieren soll, wenn es funktioniert
        try {
            // getForObject: ich übergebe ihm einen url woner soll aufrufen und sage ihm welche klasse die antwort sein wird (als userklasse) damit wer weiss in welches Format bzw. Objekt er deserialisieren muss. 
            User user = restTemplate.getForObject(
                    // URL angeben mit gewünschten Pfadvariablen. Einen Statischen URL einzugeben "http://localhost:8070/userapi/v1/users/{userName}" ist eigentlich  nicht so gut und sollte daher geändert werden
                    // 1. application.properties wird weitere eigenschaft "userservice.endpoint=http://localhost:8070/userapi/v1" hinzugefügt
                    // 2. Oben @Value(value = "$(userservice.endpoint)") private String userserviceEndpoint; definieren
                    // 3. URL part "http://localhost:8070/userapi/v1/" löschen und userServiceEndpoint + "/users/{userName}" ersetzen
                    userserviceEndpoint + "/users/{userName}",
                    // Klassen-Name angeben
                    User.class,
                    // die Variable, welche in die Pfadvariable übergeben werden soll
                    userName);
            return user;
            
// catch: Was passieren soll, wenn es NICHT funktioniert --> Null soll zurückgegeben werden um entsprechend mit GetUserInformationDelegate reagieren zu können
        } catch (HttpClientErrorException httpClientErrorException) {
            // zuerst prüfen  httpClientErrorException
            if (httpClientErrorException.getStatusCode() == HttpStatus.NOT_FOUND){
                return null;
            } else{
                throw httpClientErrorException;
            }
        }

    }
}
