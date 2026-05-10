package ee.ut.eventticketing.analytics.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventReportResponse(
        UUID id,
        UUID eventId,
        long totalCheckedIn,
        OffsetDateTime lastUpdated
) {}
