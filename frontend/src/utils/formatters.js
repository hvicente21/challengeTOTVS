export const getId = (item, key) => item?.[key] ?? item?.id;

export function formatDate(value) {
  if (!value) return 'Data não informada';
  return new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(value));
}

export function formatDuration(seconds) {
  const total = Number(seconds || 0);
  const minutes = Math.round(total / 60);
  if (minutes < 60) return `${minutes} min`;
  return `${Math.floor(minutes / 60)}h ${minutes % 60}min`;
}

export function priorityLabel(churn) {
  const value = Number(churn || 0);
  if (value >= 70) return 'Prioridade alta';
  if (value >= 40) return 'Atenção necessária';
  return 'Situação estável';
}

export function sentimentTone(sentiment = '') {
  return ({ Negativo: 'danger', Positivo: 'success', Misto: 'warning', Neutro: 'neutral' })[sentiment] || 'neutral';
}
