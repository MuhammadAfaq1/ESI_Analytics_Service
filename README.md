# Reporting and Analytics Service

## Overview
This service provides historical analytics and persistent reports for event attendance. It synchronizes with the Check-In service to archive live gate data.

## Key Endpoints
- `GET /analytics/events`: List all event reports.
- `GET /analytics/events/{eventId}`: Get the persistent report for a specific event.
- `POST /analytics/events/{eventId}/sync`: **Integrated Sync** - Calls the Check-In service directly to pull live data and archive it in the analytics database.
- `DELETE /analytics/events/{eventId}`: Remove a report.

## Integration
- **Backend-to-Backend**: Calls `attendee-checkin-service:8086` directly for synchronization.
- **API Gateway**: Exposed at `/api/analytics/**`.

## Database
Uses PostgreSQL in Docker (via `analytics-db`) and H2 for local development.