package ee.ut.eventticketing.analytics.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "EventReport")
public class EventReport {

    @Id
    @Column(name = "report_id")
    private UUID id;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "total_checked_in")
    private long totalCheckedIn;

    @Column(name = "last_updated")
    private OffsetDateTime lastUpdated;

    public EventReport() {}

    public EventReport(UUID id, UUID eventId, long totalCheckedIn, OffsetDateTime lastUpdated) {
        this.id = id;
        this.eventId = eventId;
        this.totalCheckedIn = totalCheckedIn;
        this.lastUpdated = lastUpdated;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public long getTotalCheckedIn() { return totalCheckedIn; }
    public void setTotalCheckedIn(long totalCheckedIn) { this.totalCheckedIn = totalCheckedIn; }

    public OffsetDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(OffsetDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
