import { useState, useEffect } from 'react';
import { User, Mail, GraduationCap, Calendar, CheckCircle2, Loader2, AlertCircle } from 'lucide-react';
import api from '../api/api';
import StatCard from '../Components/StatCard';

export default function ProfilePage() {
  const [profileData, setProfileData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await api.get('/stats/me');
        setProfileData(response.data);
      } catch (err) {
        console.error('Error fetching profile:', err);
        setError('Failed to load profile details. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) {
    return (
      <div className="loading-container">
        <Loader2 className="spinner" size={48} />
        <p>Loading profile...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <AlertCircle size={48} color="#ef4444" />
        <p>{error}</p>
        <button onClick={() => window.location.reload()} className="btn-primary" style={{ marginTop: '1rem' }}>
          Retry
        </button>
      </div>
    );
  }

  const { user, easyCount, mediumCount, hardCount, lastSync } = profileData;
  const totalSolved = easyCount + mediumCount + hardCount;

  return (
    <div className="profile-page animate-in">
      <header className="page-header">
        <h1>User Profile</h1>
        <p className="subtitle">View and manage your account details</p>
      </header>

      <div className="profile-grid">
        {/* Basic Details Card */}
        <div className="profile-card glass-card">
          <div className="profile-header">
            <div className="profile-avatar">
              {user.username.charAt(0).toUpperCase()}
            </div>
            <div className="profile-info">
              <h2>{user.username}</h2>
              <p className="leet-username">@{profileData.leetcodeUsername}</p>
            </div>
          </div>

          <div className="details-list">
            <div className="detail-item">
              <Mail size={18} />
              <div className="detail-content">
                <span className="detail-label">Email</span>
                <span className="detail-value">{user.email}</span>
              </div>
            </div>
            <div className="detail-item">
              <GraduationCap size={18} />
              <div className="detail-content">
                <span className="detail-label">Course</span>
                <span className="detail-value">{user.course || 'Not specified'}</span>
              </div>
            </div>
            <div className="detail-item">
              <Calendar size={18} />
              <div className="detail-content">
                <span className="detail-label">Semester</span>
                <span className="detail-value">Semester {user.semester || 'N/A'}</span>
              </div>
            </div>
            <div className="detail-item">
              <CheckCircle2 size={18} />
              <div className="detail-content">
                <span className="detail-label">Status</span>
                <span className="detail-value status-onboarded">
                  {user.onboarded ? 'Account Linked' : 'Pending Onboarding'}
                </span>
              </div>
            </div>
          </div>
        </div>

        {/* Stats Breakdown Section */}
        <div className="stats-section">
          <div className="stats-header">
            <h3>LeetCode Stats</h3>
            <span className="last-sync">
              Last synced: {new Date(lastSync).toLocaleString()}
            </span>
          </div>

          <div className="profile-stats-grid">
            <StatCard
              title="Easy"
              value={easyCount}
              icon={<CheckCircle2 size={20} />}
              color="#00b8a3"
            />
            <StatCard
              title="Medium"
              value={mediumCount}
              icon={<CheckCircle2 size={20} />}
              color="#ffc01e"
            />
            <StatCard
              title="Hard"
              value={hardCount}
              icon={<CheckCircle2 size={20} />}
              color="#ef4743"
            />
            <div className="total-stats-card glass-card">
              <div className="total-label">Total Solved</div>
              <div className="total-value">{totalSolved}</div>
              <div className="total-progress-bar">
                <div
                  className="progress-segment easy"
                  style={{ width: `${(easyCount / totalSolved) * 100}%` }}
                />
                <div
                  className="progress-segment medium"
                  style={{ width: `${(mediumCount / totalSolved) * 100}%` }}
                />
                <div
                  className="progress-segment hard"
                  style={{ width: `${(hardCount / totalSolved) * 100}%` }}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
