import { useState, useEffect } from 'react';
import { Trophy, Medal, Loader2, AlertTriangle } from 'lucide-react';
import api from '../Api/axios';

export default function LeaderboardPage() {
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchLeaderboard = async () => {
      try {
        const response = await api.get('/stats/leaderboard');
        setLeaderboard(response.data || []);
      } catch (err) {
        console.error(err);
        setError('Failed to load leaderboard. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchLeaderboard();
  }, []);

  if (loading) {
    return (
      <div className="loading-state">
        <Loader2 className="spinner" size={48} />
        <p>Fetching global rankings...</p>
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
    <div className="leaderboard-page fade-in">
      <header className="page-header">
        <div className="header-icon" aria-hidden="true">
          <Trophy size={40} className="text-accent" />
        </div>
        <h1>Global Leaderboard</h1>
        <p>Scan the field, spot the leaders, and climb one problem at a time.</p>
      </header>

      <div className="leaderboard-container">
        <div className="table-responsive">
          <table className="leaderboard-table">
            <thead>
              <tr>
                <th>Rank</th>
                <th>Leetname</th>
                <th>Name</th>
                <th>Total Solved</th>
                <th className="hide-mobile">Easy</th>
                <th className="hide-mobile">Medium</th>
                <th className="hide-mobile">Hard</th>
                <th>Course</th>
                <th>Semester</th>
              </tr>
            </thead>
            <tbody>
              {leaderboard.map((user, index) => (
                <tr key={user.id} className={index < 3 ? `top-${index + 1}` : ''}>
                  <td>
                    <div className="rank-cell">
                      {index === 0 && <Medal className="gold-medal" size={20} />}
                      {index === 1 && <Medal className="silver-medal" size={20} />}
                      {index === 2 && <Medal className="bronze-medal" size={20} />}
                      {index > 2 && <span>#{index + 1}</span>}
                    </div>
                  </td>
                  <td>
                    <div className="user-cell">
                      <div className="avatar">{user.leetcodeUsername?.charAt(0).toUpperCase() || 'U'}</div>
                      <span className="username">{user.leetcodeUsername || 'Unknown'}</span>
                    </div>
                  </td>
                  <td>{user.user.username || 'Unknown'}</td>
                  <td className="font-bold text-accent">{(user.easyCount || 0) + (user.mediumCount || 0) + (user.hardCount || 0)}</td>
                  <td className="hide-mobile text-easy">{user.easyCount || 0}</td>
                  <td className="hide-mobile text-medium">{user.mediumCount || 0}</td>
                  <td className="hide-mobile text-hard">{user.hardCount || 0}</td>
                  <td>{user.user.course || 'Unknown'}</td>
                  <td>{user.user.semester || 'Unknown'}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {leaderboard.length === 0 && (
            <div className="empty-state">
              <p>No users found on the leaderboard yet.</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
