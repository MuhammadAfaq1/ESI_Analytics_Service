package ee.ut.eventticketing.analytics.config;

import ee.ut.eventticketing.analytics.domain.EventReport;
import ee.ut.eventticketing.analytics.repository.EventReportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    private final EventReportRepository eventReportRepository;

    public DataLoader(EventReportRepository eventReportRepository) {
        this.eventReportRepository = eventReportRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (eventReportRepository.count() == 0) {
            List<EventReport> reports = new ArrayList<>();

            // Event 1: Music Concert - high attendance
            reports.add(new EventReport(
                    UUID.randomUUID(),
                    UUID.fromString("aaaa1111-1111-1111-1111-111111111111"),
                    350,
                    OffsetDateTime.now().minusHours(2)
            ));

            // Event 2: Tech Conference - medium attendance
            reports.add(new EventReport(
                    UUID.randomUUID(),
                    UUID.fromString("bbbb2222-2222-2222-2222-222222222222"),
                    128,
                    OffsetDateTime.now().minusHours(5)
            ));

            // Event 3: Workshop - small attendance
            reports.add(new EventReport(
                    UUID.randomUUID(),
                    UUID.fromString("cccc3333-3333-3333-3333-333333333333"),
                    42,
                    OffsetDateTime.now().minusDays(1)
            ));

            // Event 4: Hackathon - recent sync
            reports.add(new EventReport(
                    UUID.randomUUID(),
                    UUID.fromString("dddd4444-4444-4444-4444-444444444444"),
                    85,
                    OffsetDateTime.now().minusMinutes(30)
            ));

            // Event 5: Networking Event - low attendance
            reports.add(new EventReport(
                    UUID.randomUUID(),
                    UUID.fromString("eeee5555-5555-5555-5555-555555555555"),
                    15,
                    OffsetDateTime.now().minusDays(3)
            ));

            eventReportRepository.saveAll(reports);

            System.out.println("Analytics database populated with 5 EventReport rows.");
        }
    }
}
