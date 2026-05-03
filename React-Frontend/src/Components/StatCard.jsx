export default function StatCard({ title, value, icon, color }) {
  return (
    <div className="stat-card" style={{ '--accent-color': color }}>
      <div className="stat-header">
        <div className="stat-icon" style={{ color: color }}>
          {icon}
        </div>
        <h3 className="stat-title">{title}</h3>
      </div>
      <div className="stat-value">{value}</div>
      <div className="stat-rule" />
    </div>
  );
}
