package ch.zhaw.gpi.twitterreview.delegates;

import ch.zhaw.gpi.twitterreview.services.TwitterService;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

       
        
/**
 * Implementation des Service Tasks 'Tweet senden'
 *
 * @author Melek
 */
@Named("sendTweetAdapter")
public class SendTweetDelegate implements JavaDelegate { // meine Klasse SendTweetDelegate
    
    // Verdrahten von Twitter Service
@Autowired
private TwitterService twitterService;

    /**
     * Postet einen Tweet mit dem gewünschten Text
     *
     * @param execution Objekt, welches die Verknüpfung zur Process Engine und
     * zur aktuellen Execution enthält
     * @throws Exception
     */
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Zu postender Text aus Prozessvariable lesen
        String tweetContent = (String) execution.getVariable("tweetContent");

        // Dieser Text wird dem Twitter Service an die Methode updateStatus übergeben 
        twitterService.updateStatus(tweetContent);
    }
}
