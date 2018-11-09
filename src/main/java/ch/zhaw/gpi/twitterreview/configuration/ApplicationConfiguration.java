package ch.zhaw.gpi.twitterreview.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/** 
 * Verschiedene Einstellungen für die Spring-Applikation 
 *  
 * @Configuration stellt sicher, dass Spring diese Klasse als Einstellungs- 
 * Klasse erkennt und damit beim Starten berücksichtigt 
 *  
 * @PropertySource("classpath:twitter.properties") stellt sicher, dass die Werte 
 * aus dieser Datei über @Value-Annotationen ausgelesen werden können 
 *  
 * @author Melek
 */

 // damit Klasse gelesen werden kann bevor Applikation gestartet wird
@Configuration
// Sicherstellen dass property file nur geladen wird. in der klammer: Angeben wo es sich befindet. classpath: alles innerhalb source main folder
@PropertySource("classpath:twitter.properties")
public class ApplicationConfiguration {
    
}
