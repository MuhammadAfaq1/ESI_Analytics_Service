import { useState, useEffect } from 'react';

type Summary = {
  eventId: string;
  totalCheckIns: number;
  uniqueAttendees: number;
};

type Report = {
  id: string;
  eventId: string;
  totalCheckedIn: number;
  lastUpdated: string;
};

type ApiError = {
  status?: number;
  message?: string;
  error?: string;
};

export default function App() {
  const [eventId, setEventId] = useState('aaaa1111-1111-1111-1111-111111111111');
  const [attendance, setAttendance] = useState<number | null>(null);
  const [summary, setSummary] = useState<Summary | null>(null);
  const [report, setReport] = useState<Report | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<ApiError | null>(null);

  async function fetchData() {
    if (!eventId) return;
    setLoading(true);
    setError(null);
    try {
      // Fetch Attendance from Gate
      const attRes = await fetch(`/events/${eventId}/attendance`);
      if (attRes.ok) {
        setAttendance(await attRes.json());
      } else {
        setAttendance(0);
      }

      // Fetch Summary from Gate
      const sumRes = await fetch(`/checkins/events/${eventId}/summary`);
      if (sumRes.ok) {
        setSummary(await sumRes.json());
      } else {
        setSummary({ eventId, totalCheckIns: 0, uniqueAttendees: 0 });
      }

      // Fetch Report from Analytics
      const repRes = await fetch(`/analytics/events/${eventId}`);
      if (repRes.ok) {
        setReport(await repRes.json());
      } else {
        setReport(null);
      }
      
    } catch (err) {
      console.error(err);
      setError({ message: 'Connection to services failed. Check if backends are running.' });
    } finally {
      setLoading(false);
    }
  }

  async function handleSync() {
    if (!eventId) return;
    setLoading(true);
    try {
      // Direct call to Analytics Service Sync endpoint.
      // The Analytics Service will then call the Check-In Service on its own.
      const syncRes = await fetch(`/analytics/events/${eventId}/sync`, {
        method: 'POST'
      });
      
      if (syncRes.ok) {
        const updatedReport = await syncRes.json();
        setReport(updatedReport);
        setAttendance(updatedReport.totalCheckedIn);
      }
    } catch (err) {
      setError({ message: 'Sync failed' });
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="dashboard">
      <header>
        <div>
          <h1>Event Insights</h1>
          <p className="subtitle">Real-time attendance analytics and synchronization</p>
        </div>
      </header>

      <section className="event-selector">
        <div style={{ flex: 1 }}>
          <label style={{ display: 'block', fontSize: '0.75rem', color: 'var(--text-muted)', marginBottom: '0.25rem' }}>ACTIVE EVENT ID</label>
          <input 
            value={eventId} 
            onChange={(e) => setEventId(e.target.value)}
            placeholder="Enter Event UUID..."
          />
        </div>
        <button onClick={fetchData} disabled={loading}>Refresh Data</button>
        <button onClick={handleSync} disabled={loading}>Sync with Gate</button>
      </section>

      {error && <div className="alert alert-error">{error.message}</div>}

      <div className="stats-grid">
        <div className="stat-card">
          <h3>Gate Attendance</h3>
          <div className="value">{attendance ?? '0'}</div>
          <div style={{ marginTop: '1rem', height: '8px', background: 'rgba(255,255,255,0.1)', borderRadius: '4px', overflow: 'hidden' }}>
             <div style={{ width: `${Math.min((attendance ?? 0) * 10, 100)}%`, height: '100%', background: 'var(--primary)', transition: 'width 0.5s ease-out' }}></div>
          </div>
          <p className="label">Live count from Gate Service</p>
        </div>
        <div className="stat-card" style={{ borderColor: 'var(--secondary)' }}>
          <h3 style={{ color: 'var(--secondary)' }}>Unique Attendees</h3>
          <div className="value">{summary?.uniqueAttendees ?? '0'}</div>
          <div style={{ marginTop: '1rem', height: '8px', background: 'rgba(255,255,255,0.1)', borderRadius: '4px', overflow: 'hidden' }}>
             <div style={{ width: `${Math.min((summary?.uniqueAttendees ?? 0) * 10, 100)}%`, height: '100%', background: 'var(--secondary)', transition: 'width 0.5s ease-out' }}></div>
          </div>
          <p className="label">Distinct guests checked in</p>
        </div>
        <div className="stat-card" style={{ borderColor: 'var(--accent)' }}>
          <h3 style={{ color: 'var(--accent)' }}>Validation Health</h3>
          <div className="value">
            {summary && summary.totalCheckIns > 0 
              ? Math.round((summary.uniqueAttendees / summary.totalCheckIns) * 100) 
              : 0}%
          </div>
          <div style={{ marginTop: '1rem', height: '8px', background: 'rgba(255,255,255,0.1)', borderRadius: '4px', overflow: 'hidden' }}>
             <div style={{ width: `${summary && summary.totalCheckIns > 0 ? (summary.uniqueAttendees / summary.totalCheckIns) * 100 : 0}%`, height: '100%', background: 'var(--accent)', transition: 'width 0.5s ease-out' }}></div>
          </div>
          <p className="label">Unique vs Total ratio</p>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem', marginBottom: '2rem' }}>
        <section className="report-section">
            <div className="report-header">
                <h2>Event Summary</h2>
            </div>
            <div className="report-data" style={{ gridTemplateColumns: '1fr 1fr' }}>
                <div className="report-item">
                    <span>TOTAL ATTEMPTS</span>
                    <strong>{summary?.totalCheckIns ?? 0}</strong>
                </div>
                <div className="report-item">
                    <span>UNIQUE GUESTS</span>
                    <strong>{summary?.uniqueAttendees ?? 0}</strong>
                </div>
                <div className="report-item" style={{ gridColumn: 'span 2' }}>
                    <span>DUPLICATE/REJECTED</span>
                    <strong>{(summary?.totalCheckIns ?? 0) - (summary?.uniqueAttendees ?? 0)}</strong>
                </div>
            </div>
        </section>

        <section className="report-section">
            <div className="report-header">
            <h2>Persistence Report</h2>
            {report ? (
                <span style={{ fontSize: '0.75rem', color: '#10b981', background: 'rgba(16, 185, 129, 0.1)', padding: '0.25rem 0.5rem', borderRadius: '1rem' }}>
                Synced
                </span>
            ) : (
                <span style={{ fontSize: '0.75rem', color: '#f59e0b', background: 'rgba(245, 158, 11, 0.1)', padding: '0.25rem 0.5rem', borderRadius: '1rem' }}>
                Out of Sync
                </span>
            )}
            </div>

            {report ? (
            <div className="report-data">
                <div className="report-item">
                <span>REPORT ID</span>
                <strong>{report.id.substring(0, 8)}...</strong>
                </div>
                <div className="report-item">
                <span>ARCHIVED COUNT</span>
                <strong>{report.totalCheckedIn}</strong>
                </div>
                <div className="report-item">
                <span>LAST SYNCED</span>
                <strong>{new Date(report.lastUpdated).toLocaleTimeString()}</strong>
                </div>
            </div>
            ) : (
            <div className="alert alert-info">
                No persistence report found. Click "Sync with Gate" to archive current data.
            </div>
            )}
        </section>
      </div>
    </div>
  );
}
