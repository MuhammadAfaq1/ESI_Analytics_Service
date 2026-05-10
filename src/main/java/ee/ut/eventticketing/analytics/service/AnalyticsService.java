package ee.ut.eventticketing.analytics.service;

import ee.ut.eventticketing.analytics.domain.EventReport;
import ee.ut.eventticketing.analytics.dto.EventReportResponse;
import ee.ut.eventticketing.analytics.repository.EventReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final EventReportRepository repository;
    private final ee.ut.eventticketing.analytics.integration.CheckInClient checkInClient;

    public AnalyticsService(EventReportRepository repository, 
                            ee.ut.eventticketing.analytics.integration.CheckInClient checkInClient) {
        this.repository = repository;
        this.checkInClient = checkInClient;
    }

    /**
     * Integrated Sync: Calls Check-In Service directly and updates Analytics record.
     */
    public EventReportResponse syncWithGate(UUID eventId) {
        Long liveAttendance = checkInClient.fetchLiveAttendance(eventId);
        return updateEventAttendance(eventId, liveAttendance);
    }

    public List<EventReportResponse> getAllReports() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public EventReportResponse getReportByEventId(UUID eventId) {
        return repository.findByEventId(eventId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Report not found for event " + eventId));
    }

    public EventReportResponse updateEventAttendance(UUID eventId, Long attendanceCount) {
        if (attendanceCount == null) {
            attendanceCount = 0L;
        }

        EventReport report = repository.findByEventId(eventId)
                .orElse(new EventReport(UUID.randomUUID(), eventId, 0, OffsetDateTime.now()));
        
        report.setTotalCheckedIn(attendanceCount);
        report.setLastUpdated(OffsetDateTime.now());
        
        repository.save(report);
        return mapToDto(report);
    }
    
    public void deleteReport(UUID eventId) {
        EventReport report = repository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Report not found for event " + eventId));
        repository.delete(report);
    }

    private EventReportResponse mapToDto(EventReport report) {
        return new EventReportResponse(
                report.getId(),
                report.getEventId(),
                report.getTotalCheckedIn(),
                report.getLastUpdated()
        );
    }
}
