package ee.ut.eventticketing.analytics.repository;

import ee.ut.eventticketing.analytics.domain.EventReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventReportRepository extends JpaRepository<EventReport, UUID> {
    Optional<EventReport> findByEventId(UUID eventId);
}
