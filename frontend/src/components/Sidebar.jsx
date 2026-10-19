export default function Sidebar({ page, onNavigate }) {
  const items = [
    ['dashboard', '▦', 'Visão geral'],
    ['meetings', '◫', 'Reuniões'],
    ['insights', '✦', 'Insights comerciais']
  ];
  return <aside className="sidebar">
    <button className="brand" onClick={() => onNavigate('dashboard')} aria-label="Ir para visão geral"><span>✦</span> Metaxy</button>
    <nav>{items.map(([key, icon, label]) => <button key={key} className={page === key ? 'nav-item active' : 'nav-item'} onClick={() => onNavigate(key)}><span>{icon}</span>{label}</button>)}</nav>
    <div className="sidebar-note"><strong>Inteligência conversacional</strong><small>Transforme reuniões em decisões.</small></div>
  </aside>;
}
