package de.seism0saurus.loggingissue;

import net.logstash.logback.marker.LogstashMarker;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class LoggingIssueApplication {

    private static final Logger LOGGER = getLogger("customLogger");
    private static final String MESSAGE = "{  \"ordernumber\": \"Test1\",  \"shippingmethod\": \"homeDelivery\",  \"customer\": {    \"id\": \"1337\",    \"salutation\": \"Mr\",    \"name\": \"seism0saurus\",    \"birthdate\": \"1900-05-01\",    \"email\": \"test@seism0saurus.de\",    \"phone\": \"\"  },  \"billingAddress\": {    \"name\": \"seism0saurus\",    \"address\": \"Teststreet 5\",    \"address2\": \"\",    \"postcode\": \"90451\",    \"city\": \"Nürnberg\",    \"country\": \"Deutschland\",    \"countrycode\": \"DEU\"  },  \"shippingAddress\": {    \"name\": \"seism0saurus\",    \"address\": \"Teststreet 5\",    \"address2\": \"\",    \"postcode\": \"90451\",    \"city\": \"Nürnberg\",    \"country\": \"Deutschland\",    \"countrycode\": \"DEU\"  },  \"totalItems\": 1,  \"items\": [    {      \"position\": 53,      \"bundlenumber\": 53,      \"itemnumber\": \"900508\",      \"itemdescription\": \"Dinosaur Cookies\",      \"quantity\": 4,      \"saleType\": \"KL\",      \"saleItemType\": \"\",      \"grossAmount\": 40.0,      \"discounts\": [      ]    }  ]}";

    public static void main(String[] args) {
        SpringApplication.run(LoggingIssueApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testAppender() throws InterruptedException {

        LOGGER.warn("1 - Log without marker and message as parameter: {}", MESSAGE);

        LogstashMarker payloadMarker = Markers.append("payload", MESSAGE);
        LOGGER.warn(payloadMarker, "2 - Log with payload marker without parameter");
        LOGGER.warn(payloadMarker, "3 - Log with payload marker and message as parameter: {}", MESSAGE);

        LogstashMarker rawMarker = Markers.appendRaw("raw", MESSAGE);
        LOGGER.warn(rawMarker, "4 - Log with raw marker without parameter");

        //Needed, so the loggers can finish their job before the application is shut down.
        Thread.sleep(5000);
    }
}
