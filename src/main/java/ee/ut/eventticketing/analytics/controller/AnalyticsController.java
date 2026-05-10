package ee.ut.eventticketing.analytics.controller;

import ee.ut.eventticketing.analytics.dto.EventReportResponse;
import ee.ut.eventticketing.analytics.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics/events")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    @Operation(summary = "Get all event reports", description = "Retrieves reports for all events")
    public List<EventReportResponse> getAllReports() {
        return analyticsService.getAllReports();
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get event report", description = "Retrieves the report for a specific event")
    public EventReportResponse getReportByEventId(@PathVariable UUID eventId) {
        return analyticsService.getReportByEventId(eventId);
    }

    @PostMapping("/{eventId}/attendance")
    @Operation(summary = "Update event attendance (Manual)", description = "Updates the event report attendance with provided value")
    public EventReportResponse updateEventAttendance(@PathVariable UUID eventId, @RequestBody Long attendanceCount) {
        return analyticsService.updateEventAttendance(eventId, attendanceCount);
    }

    @PostMapping("/{eventId}/sync")
    @Operation(summary = "Sync with Gate (Integrated)", description = "Analytics Service calls Check-In Service directly to sync data")
    public EventReportResponse syncWithGate(@PathVariable UUID eventId) {
        return analyticsService.syncWithGate(eventId);
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete event report", description = "Deletes the report for a specific event")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable UUID eventId) {
        analyticsService.deleteReport(eventId);
    }
}
