export function Notice({ notice, onClose }) {
  if (!notice) return null;
  return <div className={`notice ${notice.type || 'success'}`} role="status"><span>{notice.type === 'error' ? '!' : '✓'}</span><p>{notice.message}</p><button onClick={onClose} aria-label="Fechar mensagem">×</button></div>;
}

export function EmptyState({ title, description, action }) {
  return <section className="empty-state"><span>◌</span><h3>{title}</h3><p>{description}</p>{action}</section>;
}
