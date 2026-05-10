package ee.ut.eventticketing.analytics.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class CheckInClient {

    private final RestTemplate restTemplate;
    private final String checkInServiceUrl;

    public CheckInClient(RestTemplate restTemplate, 
                         @Value("${services.checkin.url:http://localhost:8086}") String checkInServiceUrl) {
        this.restTemplate = restTemplate;
        this.checkInServiceUrl = checkInServiceUrl;
    }

    /**
     * Calls the Check-In Service to get live attendance for an event.
     * This is a REAL backend-to-backend integration call.
     */
    public Long fetchLiveAttendance(UUID eventId) {
        String url = checkInServiceUrl + "/events/" + eventId + "/attendance";
        try {
            return restTemplate.getForObject(url, Long.class);
        } catch (Exception e) {
            // Fallback to 0 if the service is down or event not found
            return 0L;
        }
    }
}
