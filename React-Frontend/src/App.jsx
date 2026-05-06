import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
import LandingPage from './Pages/LandingPage';
import OnboardingPage from './Pages/OnboardingPage';
import DashboardPage from './Pages/DashboardPage';
import LeaderboardPage from './Pages/LeaderboardPage';
import ProfilePage from './Pages/ProfilePage';
import Navbar from './Components/Navbar';
import './App.css';

// Route guard for authenticated users
function ProtectedRoute({ children }) {
  const token = localStorage.getItem('token');
  if (!token) {
    return <Navigate to="/" replace />;
  }
  return children;
}

// Redirect if already authenticated
function AuthRedirectRoute({ children }) {
  const token = localStorage.getItem('token');
  if (token) {
    return <Navigate to="/dashboard" replace />;
  }
  return children;
}

function App() {
  const location = useLocation();
  const [theme, setTheme] = useState(localStorage.getItem('theme') || 'light');

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prev) => (prev === 'light' ? 'dark' : 'light'));
  };

  // Extract token from URL if it exists (from OAuth redirect)
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    if (token) {
      localStorage.setItem('token', token);
      // Clean up the URL
      window.history.replaceState({}, document.title, location.pathname);
    }
  }, [location]);

  return (
    <div className="app-container">
      {location.pathname !== '/' && location.pathname !== '/onboarding' && (
        <Navbar theme={theme} toggleTheme={toggleTheme} />
      )}
      
      <main className="main-content">
        <Routes>
          <Route 
            path="/" 
            element={
              <AuthRedirectRoute>
                <LandingPage />
              </AuthRedirectRoute>
            } 
          />
          <Route path="/onboarding" element={<OnboardingPage />} />
          <Route 
            path="/dashboard" 
            element={
              <ProtectedRoute>
                <DashboardPage />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/leaderboard" 
            element={
              <ProtectedRoute>
                <LeaderboardPage />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/profile" 
            element={
              <ProtectedRoute>
                <ProfilePage />
              </ProtectedRoute>
            } 
          />
          {/* Catch all route */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
