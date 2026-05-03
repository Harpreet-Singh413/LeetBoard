import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowRight, Loader2 } from 'lucide-react';
import api from '../Api/axios';

export default function OnboardingPage() {
  const [username, setUsername] = useState('');
  const [course, setCourse] = useState('');
  const [semester, setSemester] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // At the top of OnboardingPage.jsx (or in your router)
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    if (token) {
      localStorage.setItem('token', token);
      // Clean the token from the URL
      window.history.replaceState({}, '', '/onboarding');
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!username.trim()) return;

    setLoading(true);
    setError('');

    try {
      await api.post('/auth/onboarding', { leetUsername: username, course: course, semester: semester });
      navigate('/dashboard');
    } catch (err) {
      setError('Failed to link LeetCode account. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="onboarding-page">
      <div className="onboarding-card">
        <div style={{ marginBottom: '1.5rem' }}>
          <img src="/favicon.svg" alt="LeetBoard Logo" width="64" height="64" />
        </div>
        <h2>Welcome to LeetBoard</h2>
        <p className="subtitle">Link your LeetCode account to get started.</p>

        <form onSubmit={handleSubmit} className="onboarding-form">
          <div className="input-group">
            <label htmlFor="leetUsername">LeetCode Username</label>
            <input
              type="text"
              id="leetUsername"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="e.g. neetcode"
              required
              className="glass-input"
            />
            <label htmlFor="course">Course</label>
            <input
              type="text"
              id="course"
              value={course}
              onChange={(e) => setCourse(e.target.value)}
              placeholder="e.g. BCA"
              required
              className="glass-input"
            />
            <label htmlFor="semester">Semester</label>
            <input
              type="text"
              id="semester"
              value={semester}
              onChange={(e) => setSemester(e.target.value)}
              placeholder="e.g. 1"
              required
              className="glass-input"
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <button
            type="submit"
            className="btn-primary"
            disabled={loading}
          >
            {loading ? <Loader2 className="spinner" /> : <span>Complete Setup</span>}
            {!loading && <ArrowRight size={18} />}
          </button>
        </form>
      </div>
    </div>
  );
}
