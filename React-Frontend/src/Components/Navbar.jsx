import { NavLink, useNavigate } from 'react-router-dom';
import { BarChart3, Braces, LogOut, Trophy } from 'lucide-react';

export default function Navbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <span className="brand-mark">
          <Braces size={18} />
        </span>
        <span><span className="brand-accent">Leet</span>Board</span>
      </div>
      <div className="navbar-links">
        <NavLink to="/dashboard" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
          <BarChart3 size={18} />
          <span>Dashboard</span>
        </NavLink>
        <NavLink to="/leaderboard" className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}>
          <Trophy size={18} />
          <span>Leaderboard</span>
        </NavLink>
        <button onClick={handleLogout} className="btn-icon" aria-label="Log out" title="Log out">
          <LogOut size={18} />
        </button>
      </div>
    </nav>
  );
}
