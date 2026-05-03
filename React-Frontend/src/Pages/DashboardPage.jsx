import { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import { AlertTriangle, Award, CheckCircle2, Flame, Gauge, Loader2, Target, TrendingUp } from 'lucide-react';
import api from '../Api/axios';
import StatCard from '../Components/StatCard';

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const token = localStorage.getItem('token');
        if (!token) throw new Error("No token found");

        const decoded = jwtDecode(token);
        const userId = decoded.id;

        const response = await api.get(`/stats/${userId}`);
        setStats(response.data);
      } catch (err) {
        console.error(err);
        setError('Failed to load stats. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="loading-state">
        <Loader2 className="spinner" size={48} />
        <p>Loading your mastery...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-state glass-effect">
        <AlertTriangle size={48} className="text-danger" />
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div className="dashboard-page fade-in">
      <header className="dashboard-header">
        <div>
          <span className="eyebrow">Personal stats</span>
          <h1>Your Dashboard</h1>
          <p>Keep an eye on your solved count and difficulty balance.</p>
        </div>
        <div className="user-badge">
          <Award />
          <span>{stats?.leetcodeUsername || 'N/A'}</span>
        </div>
      </header>

      <div className="stats-grid">
        <StatCard
          title="Total Solved"
          value={(stats?.easyCount || 0) + (stats?.mediumCount || 0) + (stats?.hardCount || 0)}
          icon={<Flame />}
          color="#f97316"
        />
        <StatCard
          title="Easy Solved"
          value={stats?.easyCount || 0}
          icon={<CheckCircle2 />}
          color="#16a34a"
        />
        <StatCard
          title="Medium Solved"
          value={stats?.mediumCount || 0}
          icon={<Gauge />}
          color="#d97706"
        />
        <StatCard
          title="Hard Solved"
          value={stats?.hardCount || 0}
          icon={<Target />}
          color="#dc2626"
        />
      </div>

      <section className="dashboard-extra mt-8">
        <div>
          <span className="eyebrow">Momentum</span>
          <h3>Keep the solving loop warm</h3>
          <p>Consistency is what turns patterns into reflexes. Use this board as your quick daily check-in before opening the next problem.</p>
        </div>
        <div className="momentum-pill">
          <TrendingUp size={18} />
          <span>Ready for the next climb</span>
        </div>
      </section>
    </div>
  );
}
