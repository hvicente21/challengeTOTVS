const API_PREFIXES = ['/meetings', '/transcricoes', '/resultados', '/alertas', '/palavras-chave'];

export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.status = status;
  }
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options
  });
  const raw = await response.text();
  let body = raw;
  try { body = raw ? JSON.parse(raw) : null; } catch { /* backend may return a plain message */ }
  if (!response.ok) {
    throw new ApiError(typeof body === 'string' ? body : 'Não foi possível concluir a operação.', response.status);
  }
  return body;
}

export const api = {
  getDashboardData: () => Promise.all(API_PREFIXES.map((path) => request(path))).then(([meetings, transcricoes, resultados, alertas, palavras]) => ({ meetings, transcricoes, resultados, alertas, palavras })),
  getMeeting: (id) => request(`/meetings/${id}`),
  createMeeting: (payload) => request('/meetings', { method: 'POST', body: JSON.stringify(payload) }),
  updateMeeting: (id, payload) => request(`/meetings/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  createTranscricao: (payload) => request('/transcricoes', { method: 'POST', body: JSON.stringify(payload) }),
  updateTranscricao: (id, payload) => request(`/transcricoes/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  analyzeTranscricao: (id) => request(`/transcricoes/${id}/analisar`, { method: 'POST' })
};
