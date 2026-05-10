CREATE TABLE IF NOT EXISTS EventReport (
    report_id UUID PRIMARY KEY,
    event_id UUID,
    total_checked_in BIGINT,
    last_updated TIMESTAMP WITH TIME ZONE
);
