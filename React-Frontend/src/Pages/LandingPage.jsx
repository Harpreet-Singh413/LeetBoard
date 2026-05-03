import { ArrowRight, BarChart3, CheckCircle2, Code2, Trophy, UsersRound } from 'lucide-react';
import heroArt from '../assets/hero.png';

export default function LandingPage() {
  const handleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  return (
    <div className="landing-page">
      <div className="hero-section">
        <div className="hero-copy">
          <div className="hero-badge">
            <Code2 size={16} />
            <span>LeetCode progress, made visible</span>
          </div>
          <h1 className="hero-title">LeetBoard</h1>
          <p className="hero-subtitle">
            Track solved problems, compare difficulty mix, and see where you stand on a clean competitive leaderboard.
          </p>
          <div className="hero-actions">
            <button onClick={handleLogin} className="btn-primary btn-large">
              <span>Continue with Google</span>
              <ArrowRight size={20} />
            </button>
          </div>
        </div>

        <div className="hero-panel" aria-label="LeetBoard preview">
          <div className="preview-topbar">
            <div>
              <span className="eyebrow">Today</span>
              <strong>Contest Readiness</strong>
            </div>
            <img src={heroArt} alt="" className="hero-art" />
          </div>
          <div className="preview-score">
            <span>348</span>
            <small>total solved</small>
          </div>
          <div className="preview-bars">
            <div style={{ '--bar-color': '#22c55e', '--bar-width': '76%' }}><span>Easy</span></div>
            <div style={{ '--bar-color': '#f59e0b', '--bar-width': '58%' }}><span>Medium</span></div>
            <div style={{ '--bar-color': '#ef4444', '--bar-width': '31%' }}><span>Hard</span></div>
          </div>
          <div className="preview-footer">
            <span><Trophy size={16} /> Rank #12</span>
            <span><UsersRound size={16} /> 128 peers</span>
          </div>
        </div>
      </div>

      <div className="landing-strip">
        <div><BarChart3 size={20} /><span>Live stats</span></div>
        <div><Trophy size={20} /><span>Ranked leaderboard</span></div>
        <div><CheckCircle2 size={20} /><span>Difficulty breakdown</span></div>
      </div>
    </div>
  );
}
